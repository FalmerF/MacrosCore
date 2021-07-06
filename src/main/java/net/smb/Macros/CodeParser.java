package net.smb.Macros;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.smb.Macros.actions.ActionBase;
import net.smb.Macros.util.Log;

public class CodeParser {
	public String parserName;
	public CodeParser parentParser;
	public static Map<String, Object> globalVars = new TreeMap<String, Object>();
	private static Map<String, ActionBase> actions = new TreeMap<String, ActionBase>();
	public static List<CodeParser> activeParsers = new ArrayList<CodeParser>();
	public static CodeParser globalParser;
	
	public Map<String, Object> vars = new TreeMap<String, Object>();
	
	public static Pattern intPattern = Pattern.compile("^-?([0-9]+)$");
	public static Pattern floatPattern = Pattern.compile("^-?([0-9.]+)$");
	public static Pattern stringPattern = Pattern.compile("\"(.*)\"$");
	public static Pattern boolPattern = Pattern.compile("^(true)|(false)$");
	public static Pattern initialVarPattern = Pattern.compile("^(global\\s)?((int)|(float)|(string)|(bool))(\\[\\])?\\s([a-zA-Z_0-9]*)(\\s*=\\s*(.+))?$");
	public static Pattern varSetPattern = Pattern.compile("^([a-zA-Z_0-9]+)(\\[([0-9]+)\\])?\\s*=\\s*(.+)$");
	public static Pattern arrayPattern = Pattern.compile("^([a-zA-Z_0-9]+)(\\[([0-9]+)\\]){1}$");
	
	private String runningCode;
	
	private boolean error, breakParser = false, continueParser = false;
	public boolean returnVar = true, alwaysCode = false;
	
	private CodeParser instance;
	
	public ActionBase lastAction;
	
	public long start;
	
	public CodeParser(String name, CodeParser parentParser){
		instance = this;
		this.parserName = name;
		this.parentParser = parentParser;
	}
	
	public boolean executeCode(String code) {
		if((code.startsWith("$$") || alwaysCode) && !code.equals("")) {
			runningCode = code.replace("$$", "");
			String command = "";
			int bracketsCount = 0;
			boolean quotes = false;
			char lastSymbol = ' ';
			if(this.parentParser == null) activeParsers.add(this);
			start = System.currentTimeMillis();
			
			main:
			for(char c : runningCode.toCharArray()) {
				if(c == '"' && lastSymbol != '\\') quotes = !quotes;
				if(quotes) command += c;
				else {
					if(c == ';' && bracketsCount == 0) {
						runCommand(command, true);
						command = "";
						try {
							Thread.sleep((long) (1));
						} catch (Exception e) {}
					}
					else if(c == '{') {
						bracketsCount++;
						command += c;
					}
					else if(c == '}') {
						bracketsCount--;
						command += c;
					}
					else if(command.equals("") && c == ' ') continue;
					else command += c;
				}
				
				lastSymbol = c;
				
				if(error || parentError()) {
					if(parentParser != null) parentParser.error = true;
					break main;
				}
				else if(breakParser) break main;
				else if(continueParser) {
					continueParser = false;
					break main;
				}
			}
			if(!error && !command.equals("")) ParserError.expected(instance, command);
			if(parentParser != null) parentParser.returnVar = returnVar;
		}
		else if(!code.equals("")) {
			((EntityClientPlayerMP)Minecraft.getMinecraft().thePlayer).sendChatMessage(code);
		}
		activeParsers.remove(this);
		return returnVar;
	}
	
	public void setError() {
		this.error = true;
		activeParsers.remove(this);
		if(this.parentParser != null) this.parentParser.setError();
	}
	
	public boolean getError() {
		return error;
	}
	public boolean getBreak() {
		return breakParser;
	}
	
	public boolean breakParser() {
		if(this.parserName.endsWith("do") || this.parserName.endsWith("for") || this.parserName.endsWith("foreach") || this.parserName.endsWith("while")) {
			breakParser = true;
			return true;
		}
		else if(parentParser != null && parentParser.breakParser()) {
			breakParser = true;
			return true;
		}
		else return false;
	}
	
	public boolean continueParser() {
		if(this.parserName.endsWith("do") || this.parserName.endsWith("for") || this.parserName.endsWith("foreach") || this.parserName.endsWith("while")) {
			continueParser = true;
			return true;
		}
		else if(parentParser != null && parentParser.continueParser()) {
			continueParser = true;
			return true;
		}
		else return false;
	}
	
	public boolean parentError() {
		if(this.parentParser != null && this.parentParser.error) return true;
		else return false;
	}
	
	public void runCommand(String command, boolean executeCode) {
		Matcher matcherInitial = initialVarPattern.matcher(command);
		Matcher matcherVarSet = varSetPattern.matcher(command);
		try {
		//Just vars
		if(matcherInitial.matches()) {
			boolean global = matcherInitial.group(1) != null;
			String varType = matcherInitial.group(2);
			boolean isArray = matcherInitial.group(7) != null;
			String varName = matcherInitial.group(8);
			String value = matcherInitial.group(10);
			
			if(hasVar(varName) && !global) {
				ParserError.varAlreadyExists(instance, varName);
				return;
			}
			else if(intPattern.matcher(varName.toLowerCase()).find()) {
				ParserError.invalidVarName(instance, varName);
				return;
			}
			if(varType.equals("int")) {
				if(!isArray) {
					if(value != null && !value.equals("")) {
						addVar(varName, getInt(value), global);
					}
					else addVar(varName, 0, global);
				} else {
					if(value != null && !value.equals("")) {
						putArray(varName, value, Integer.class);
					}
					else {
						TreeMap<Integer, Integer> array = new TreeMap<Integer, Integer>();
						array.put(-1, 1);
						addVar(varName, array, global);
					}
				}
			}
			else if(varType.equals("float")) {
				if(!isArray) {
					if(value != null && !value.equals("")) {
						addVar(varName, getFloat(value), global);
					}
					else addVar(varName, 0, global);
				} else {
					if(value != null && !value.equals("")) {
						putArray(varName, value, Float.class);
					}
					else {
						TreeMap<Integer, Float> array = new TreeMap<Integer, Float>();
						array.put(-1, 1.0F);
						addVar(varName, array, global);
					}
				}
			}
			else if(varType.equals("string")) {
				if(!isArray) {
					if(value != null && !value.equals("")) {
						addVar(varName, getString(value), global);
					}
					else addVar(varName, "", global);
				} else {
					if(value != null && !value.equals("")) {
						putArray(varName, value, String.class);
					}
					else {
						TreeMap<Integer, String> array = new TreeMap<Integer, String>();
						array.put(-1, "string");
						addVar(varName, array, global);
					}
				}
			}
			else if(varType.equals("bool")) {
				if(!isArray) {
					if(value != null && !value.equals("")) {
						addVar(varName, getBool(value), global);
					}
					else addVar(varName, false, global);
				} else {
					if(value != null && !value.equals("")) {
						putArray(varName, value, Boolean.class);
					}
					else {
						TreeMap<Integer, Boolean> array = new TreeMap<Integer, Boolean>();
						array.put(-1, true);
						addVar(varName, array, global);
					}
				}
			}
		}
		else if(matcherVarSet.matches()) {
			String varName = matcherVarSet.group(1);
			int arrayNum = matcherVarSet.group(3) != null ? Integer.parseInt(matcherVarSet.group(3)) : -1;
			String value = matcherVarSet.group(4);
			if(hasVar(varName)) {
				Object varValue = getVar(varName);
				if(varValue instanceof Integer)
					setVar(varName, getInt(value));
				else if(varValue instanceof Float)
					setVar(varName, getFloat(value));
				else if(varValue instanceof String)
					setVar(varName, getString(value));
				else if(varValue instanceof Boolean)
					setVar(varName, getBool(value));
				else if(varValue instanceof TreeMap && arrayNum != -1) {
					putToArray(varName, value, arrayNum);
					
				}
			}
			else ParserError.varDoesntExist(instance, varName);
		}
		else if(Pattern.compile("^([a-zA-Z_0-9]+)\\(([^{}]*)\\)\\s*(\\{(.*)\\})*$").matcher(command).find()){
			runAction(command, true);
		}
		else ParserError.syntax(instance, command);
		} catch(Exception e) {
			ParserError.customError(this, "\"" + command + "\" - " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void putArray(String varName, String value, Class varClass) {
		Object val = null;
		if(value.matches("^([a-zA-Z_0-9]+)(\\[([0-9]+)\\])?$")) val = getVar(value);
		else if(value.matches("^([a-zA-Z_0-9]+)(\\((.+)\\))$")) val = runAction(value, true);
		
		if(!(val instanceof TreeMap)) {
			ParserError.notArray(instance, value);
			return;
		}
		
		TreeMap array = (TreeMap) val;
		if(!(array.get(-1).getClass() == varClass)) {
			ParserError.cantParse(instance, varName);
			return;
		}
		vars.put(varName, array.clone());
	}
	
	public void putToArray(String varName, String value, int arrayNum) {
		if(arrayNum < 0) {
			ParserError.outOfBounds(this, varName);
			return;
		}
		TreeMap array = (TreeMap) getVar(varName);
		Class arrayClass = array.get(-1).getClass();
		if(arrayClass == Integer.class) {
			array.put(arrayNum, getInt(value));
		}
		else if(arrayClass == Float.class) {
			array.put(arrayNum, getFloat(value));
		}
		else if(arrayClass == String.class) {
			array.put(arrayNum, getString(value));
		}
		else if(arrayClass == Boolean.class) {
			array.put(arrayNum, getBool(value));
		}
	}
	
	public int getInt(String value) {
		try {
			value = replaceSpaces(value);
			value = replaceActions(value);
			if(intPattern.matcher(value).find()) {
				return Integer.parseInt(value);
			}
			else if(Pattern.compile("\\(").matcher(value).find()) {
				String clearValue = "";
				String currentValue = "";
				boolean writeCurrentValue = false;
				int bracketsCount = 0;
				for(char c : value.toCharArray()) {
					if(writeCurrentValue && c == '(') {
						bracketsCount++;
					}
					
					if(!writeCurrentValue && c == '(') {
						writeCurrentValue = true;
					}
					else if(writeCurrentValue && c == ')' && bracketsCount == 0) {
						clearValue += getInt(currentValue);
						writeCurrentValue = false;
						currentValue = "";
					}
					else if(writeCurrentValue) {
						currentValue += c;
					}
					else {
						clearValue += c;
					}
					
					if(writeCurrentValue && c == ')' && bracketsCount > 0) {
						bracketsCount--;
					}
				}
				
				return getInt(clearValue);
			}
			else {
				String[] values = value.split("([+-/*%])");
				String[] newValues = values;
				for(int i = 0; i < values.length; i++) {
					String val = values[i];
					if(val.matches("^([0-9]+)$")) continue;
					else if(val.matches("^([a-zA-Z_0-9]+)$")) {
						newValues[i] = String.valueOf(getVar(val));
					}
					else if(val.matches("^([a-zA-Z_0-9]+)(\\[([0-9]+)\\]){1}$")) {
						Matcher matcher = arrayPattern.matcher(val);
						matcher.matches();
						String varName = matcher.group(1);
						int num = Integer.parseInt(matcher.group(3));
						if(num < 0) {
							ParserError.outOfBounds(this, varName);
							return 0;
						}
						TreeMap array = (TreeMap) getVar(varName);
						newValues[i] = String.valueOf(array.get(num));
					}
					else continue;
				}
				String[] symbols = value.split("([a-zA-Z_0-9]+)");
				int finalValue = Integer.parseInt(newValues[0]);
				try {
					for(int i = 0; i < symbols.length; i++) {
						if(symbols[i].equals("+")) finalValue += Integer.parseInt(newValues[i]);
						else if(symbols[i].equals("-")) finalValue -= Integer.parseInt(newValues[i]);
						else if(symbols[i].equals("/")) finalValue /= Integer.parseInt(newValues[i]);
						else if(symbols[i].equals("*")) finalValue *= Integer.parseInt(newValues[i]);
						else if(symbols[i].equals("%")) finalValue %= Integer.parseInt(newValues[i]);
					}
				} catch(Exception e) {
					ParserError.cantParse(instance, value);
					return 0;
				}
				return finalValue;
			}
		} catch(Exception e) {
			ParserError.cantParse(instance, value);
			return 0;
		}
	}
	
	public float getFloat(String value) {
		try {
			value = replaceSpaces(value);
			value = replaceActions(value);
			if(floatPattern.matcher(value).find()) {
				return Float.parseFloat(value);
			}
			else if(Pattern.compile("\\(").matcher(value).find()) {
				String clearValue = "";
				String currentValue = "";
				boolean writeCurrentValue = false;
				int bracketsCount = 0;
				for(char c : value.toCharArray()) {
					if(writeCurrentValue && c == '(') {
						bracketsCount++;
					}
					
					if(!writeCurrentValue && c == '(') {
						writeCurrentValue = true;
					}
					else if(writeCurrentValue && c == ')' && bracketsCount == 0) {
						clearValue += getFloat(currentValue);
						writeCurrentValue = false;
						currentValue = "";
					}
					else if(writeCurrentValue) {
						currentValue += c;
					}
					else {
						clearValue += c;
					}
					
					if(writeCurrentValue && c == ')' && bracketsCount > 0) {
						bracketsCount--;
					}
				}
				
				return getFloat(clearValue);
			}
			else {
				String[] values = value.split("([-/*%+])");
				String[] newValues = values;
				for(int i = 0; i < values.length; i++) {
					String val = values[i];
					if(val.matches("^([0-9.]+)$")) continue;
					else if(val.matches("^([a-zA-Z_0-9])+$")) {
						newValues[i] = String.valueOf(getVar(val));
					}
					else if(val.matches("^([a-zA-Z_0-9]+)(\\[([0-9]+)\\]){1}$")) {
						Matcher matcher = arrayPattern.matcher(val);
						matcher.matches();
						String varName = matcher.group(1);
						int num = Integer.parseInt(matcher.group(3));
						if(num < 0) {
							ParserError.outOfBounds(this, varName);
							return 0;
						}
						TreeMap array = (TreeMap) getVar(varName);
						newValues[i] = String.valueOf(array.get(num));
					}
					else continue;
				}
				String[] symbols = value.split("([a-zA-Z_0-9.]+)");
				float finalValue = Float.parseFloat(newValues[0]);
				try {
					for(int i = 0; i < symbols.length; i++) {
						if(symbols[i].equals("+")) finalValue += Float.parseFloat(newValues[i]);
						else if(symbols[i].equals("-")) finalValue -= Float.parseFloat(newValues[i]);
						else if(symbols[i].equals("/")) finalValue /= Float.parseFloat(newValues[i]);
						else if(symbols[i].equals("*")) finalValue *= Float.parseFloat(newValues[i]);
						else if(symbols[i].equals("%")) finalValue %= Float.parseFloat(newValues[i]);
					}
				} catch(Exception e) {
					ParserError.cantParse(instance, value);
					return 0;
				}
				return finalValue;
			}
		} catch(Exception e) {
			e.printStackTrace();
			ParserError.cantParse(instance, value);
			return 0;
		}
	}
	
	public String getString(String value) {
		try {
			Matcher matcher = stringPattern.matcher(value);
			if(matcher.find()) {
				value = matcher.group(1);
				String newValue = "", varName = "";
				boolean var = false;
				char lastChar = ' ';
				for(char c : value.toCharArray()) {
					if(c == '%' && lastChar != '\\') {
						if(var) {
							var = false;
							if(varName.matches("^([a-zA-Z_0-9]+)(\\[([0-9]+)\\]){1}$")) {
								matcher = arrayPattern.matcher(varName);
								matcher.matches();
								String varName2 = matcher.group(1);
								int num = Integer.parseInt(matcher.group(3));
								if(num < 0) {
									ParserError.outOfBounds(this, varName2);
									return "";
								}
								TreeMap array = (TreeMap) getVar(varName2);
								newValue += String.valueOf(array.get(num));
								varName = "";
							}
							else {
								newValue += getVar(varName);
								varName = "";
							}
						}
						else {
							var = true;
							varName = "";
						}
					}
					else if(var) varName += c;
					else newValue += c;
					
					lastChar = c;
				}
				if(!varName.equals("")) newValue += "%" + varName;
				
				return newValue;
			}
			else if(value.matches("^([a-zA-Z_0-9]+)(\\[([0-9]+)\\])?$")) {
				if(value.matches("^([a-zA-Z_0-9]+)(\\[([0-9]+)\\]){1}$")) {
					matcher = arrayPattern.matcher(value);
					matcher.matches();
					String varName2 = matcher.group(1);
					int num = Integer.parseInt(matcher.group(3));
					if(num < 0) {
						ParserError.outOfBounds(this, varName2);
						return "";
					}
					TreeMap array = (TreeMap) getVar(varName2);
					return String.valueOf(array.get(num));
				}
				else {
					return String.valueOf(getVar(value));
				}
			}
			else if(value.matches("^([a-zA-Z_0-9]+)(\\((.+)\\))$")) {
				return String.valueOf(runAction(value, true));
			}
			ParserError.cantParse(instance, value);
			return "";
		} catch(Exception e) {
			ParserError.cantParse(instance, value);
			return "";
		}
	}
	
	public boolean getBool(String value) {
		try {
			value = replaceSpaces(value);
			value = replacecConditionActions(value);
			if(value.equals("true") || value.equals("false")) {
				return Boolean.parseBoolean(value);
			}
			else if(Pattern.compile("\\(").matcher(value).find()) {
				String clearValue = "";
				String currentValue = "";
				boolean writeCurrentValue = false;
				int bracketsCount = 0;
				for(char c : value.toCharArray()) {
					if(writeCurrentValue && c == '(') {
						bracketsCount++;
					}
					
					if(!writeCurrentValue && c == '(') {
						writeCurrentValue = true;
					}
					else if(writeCurrentValue && c == ')' && bracketsCount == 0) {
						clearValue += getBool(currentValue);
						writeCurrentValue = false;
						currentValue = "";
					}
					else if(writeCurrentValue) {
						currentValue += c;
					}
					else {
						clearValue += c;
					}
					
					if(writeCurrentValue && c == ')' && bracketsCount > 0) {
						bracketsCount--;
					}
				}
				
				return getBool(clearValue);
			}
			else if(Pattern.compile("\\|").matcher(value).find()) {
				value = value.replaceAll("\\|\\|", "\\|");
				String[] values = value.split("\\|");
				for(String val : values) {
					if(getBool(val)) return true;
				}
				return false;
			}
			else if(Pattern.compile("&").matcher(value).find()) {
				value = value.replaceAll("&&", "&");
				String[] values = value.split("&");
				for(String val : values) {
					if(!getBool(val)) return false;
				}
				return true;
			}
			else {
				if(Pattern.compile("!=").matcher(value).find()) {
					String[] values = value.split("!=", 2);
					if(stringPattern.matcher(values[0]).find() || stringPattern.matcher(values[1]).find())
						return !getString(values[0]).equals(getString(values[1]));
					else if(floatPattern.matcher(values[0]).find() || floatPattern.matcher(values[1]).find())
						return getFloat(values[0])!=getFloat(values[1]);
					else
						return getBool(values[0])!=getBool(values[1]);
				}
				else if(Pattern.compile("=").matcher(value).find()) {
					value = value.replaceAll("==", "=");
					String[] values = value.split("=", 2);
					if(stringPattern.matcher(values[0]).find() || stringPattern.matcher(values[1]).find()) 
						return getString(values[0]).equals(getString(values[1]));
					else if(floatPattern.matcher(values[0]).find() || floatPattern.matcher(values[1]).find())
						return getFloat(values[0])==getFloat(values[1]);
					else
						return getBool(values[0])==getBool(values[1]);
				}
				else if(Pattern.compile(">").matcher(value).find()) {
					String[] values = value.split(">", 2);
					return getFloat(values[0])>getFloat(values[1]);
				}
				else if(Pattern.compile("<").matcher(value).find()) {
					String[] values = value.split("<", 2);
					return getFloat(values[0])<getFloat(values[1]);
				}
				else if(hasVar(value)) return (Boolean) getVar(value);
				else if(value.matches("^([a-zA-Z_0-9]+)(\\[([0-9]+)\\]){1}$")) {
					Matcher matcher = arrayPattern.matcher(value);
					matcher.matches();
					String varName = matcher.group(1);
					int num = Integer.parseInt(matcher.group(3));
					if(num < 0) {
						ParserError.outOfBounds(this, varName);
						return false;
					}
					TreeMap array = (TreeMap) getVar(varName);
					return (boolean) array.get(num);
				}
				
				ParserError.cantParse(instance, value);
				return false;
			}
		} catch(Exception e) {
			ParserError.cantParse(instance, value);
			return false;
		}
	}
	
	public static void addAction(String actionName, ActionBase instance) {
		actions.put(actionName, instance);
	}
	
	public Object runAction(String actionString) {
		return runAction(actionString, false);
	}
	
	public Object runAction(String actionString, boolean executeCode) {
		Matcher matcher = Pattern.compile("^([a-zA-Z_0-9]+)\\(([^{}]*)\\)\\s*(\\{(.*)\\})*$").matcher(actionString);
		matcher.find();
		if(actions.containsKey(matcher.group(1))) {
			ActionBase action = actions.get(matcher.group(1));
			List<String> values = new ArrayList<String>();
			String value = matcher.group(2);
			String val = "";
			int bracketsCount = 0;
			boolean quotes = false;
			char lastSymbol = ' ';
			for(char c : value.toCharArray()) {
				if(c == '"' && lastSymbol != '\\') quotes = !quotes;
				
				if(quotes) val += c;
				else {
					if(c == ',' && bracketsCount == 0) {
						values.add(val);
						val = "";
					}
					else if(c == '(') {
						bracketsCount++;
						val += c;
					}
					else if(c == ')') {
						bracketsCount--;
						val += c;
					}
					else val += c;
				}
				
				lastSymbol = c;
			}
			values.add(val);
			String[] values2 = new String[values.size()];
			for(int i = 0; i < values.size(); i++) {
				values2[i] = values.get(i);
			}
			MacroModCore.activeActions.add(action);
			Object returnValue = action.execute(this, value, values2, matcher.group(4));
			if(executeCode) lastAction = action;
			MacroModCore.activeActions.remove(action);
			return returnValue;
		}
		ParserError.methodDoesntExist(instance, matcher.group(1));
		return null;
	}
	
	public Object getVar(String key) {
		if(vars.containsKey(key)) return vars.get(key);
		else if(globalVars.containsKey(key)) return globalVars.get(key);
		else if(parentParser != null) return parentParser.getVar(key);
		else return null;
	}
	
	public Map<String, Object> getAllVars(boolean gVars) {
		Map<String, Object> allVars = new TreeMap<String, Object>();
		allVars.putAll(vars);
		if(gVars) allVars.putAll(globalVars);
		if(parentParser != null) allVars.putAll(parentParser.getAllVars(false));
		return allVars;
	}
	
	public boolean hasVar(String key) {
		key = key.toLowerCase();
		if(vars.containsKey(key)) return true;
		else if(globalVars.containsKey(key)) return true;
		else if(parentParser != null && parentParser.hasVar(key)) return true;
		else return false;
	}
	
	public void setVar(String key, Object value) {
		key = key.toLowerCase();
		if(vars.containsKey(key)) vars.put(key, value);
		else if(globalVars.containsKey(key)) globalVars.put(key, value);
		else if(parentParser != null && parentParser.hasVar(key)) parentParser.setVar(key, value);
		else ParserError.varDoesntExist(instance, key);
	}
	
	public void addVar(String key, Object value, boolean global) {
		if(global) globalVars.put(key, value);
		else vars.put(key, value);
	}
	
	public String replaceSpaces(String value) {
		String newValue = "";
		boolean stringText = false;
		for(char c : value.toCharArray()) {
			if(c == '"') {
				stringText = !stringText;
			}
			else if(c == ' ' && !stringText) continue;
			newValue += c;
		}
		return newValue;
	}
	
	public String replaceActions(String value) {
		String newValue = "";
		String currentWord = "";
		boolean method = false;
		int bracketsCount = 0;
		for(char c : value.toCharArray()) {
			if(!method && (c == '+' || c == '-' || c == '/' || c == '*' || c == '%')) {
				newValue += currentWord + c;
				currentWord = "";
			}
			else if(c == '(') {
				if(!method && currentWord.length()>0 && currentWord.toCharArray()[currentWord.length()-1] != '(') {
					method = true;
					currentWord += c;
				}
				else if(method) {
					bracketsCount++;
					currentWord += c;
				}
				else if(!method) {
					newValue += currentWord + c;
					currentWord = "";
				}
			}
			else if(c == ')') {
				if(method && bracketsCount > 0) {
					bracketsCount--;
					currentWord += c;
				}
				else if(method && bracketsCount == 0) {
					method = false;
					currentWord += c;
					currentWord = String.valueOf(runAction(currentWord));
					newValue += currentWord;
					currentWord = "";
				}
				else currentWord += c;
			}
			else currentWord += c;
		}
		newValue += currentWord;
		return newValue;
	}
	
	public String replacecConditionActions(String value) {
		value = value.replaceAll("\\|\\|", "\\|");
		value = value.replaceAll("&&", "&");
		value = value.replaceAll("==", "=");
		String newValue = "";
		String currentWord = "";
		boolean method = false;
		int bracketsCount = 0;
		char lastChar = ' ';
		for(char c : value.toCharArray()) {
			if(!method && (c == '|' || c == '&' || c == '>' || c == '<' || c == '=' || (c == '=' && lastChar == '!'))) {
				newValue += currentWord + c;
				currentWord = "";
			}
			else if(c == '(') {
				if(!method && currentWord.length()>0 && currentWord.toCharArray()[currentWord.length()-1] != '(') {
					method = true;
					currentWord += c;
				}
				else if(method) {
					bracketsCount++;
					currentWord += c;
				}
				else if(!method) {
					newValue += currentWord + c;
					currentWord = "";
				}
			}
			else if(c == ')') {
				if(method && bracketsCount > 0) {
					bracketsCount--;
					currentWord += c;
				}
				else if(method && bracketsCount == 0) {
					method = false;
					currentWord += c;
					currentWord = String.valueOf(runAction(currentWord));
					newValue += currentWord;
					currentWord = "";
				}
				else currentWord += c;
			}
			else currentWord += c;
			lastChar = c;
		}
		newValue += currentWord;
		return newValue;
	}
	
	public static String setColor(String code) {
		code = code + (char)10;
		String coloredCode = "";
		String word = "";
		char lastSymbol = ' ';
		char p = '\u00A7';
		boolean inText = false;
		boolean inTextVar = false;
		for(char c : code.toCharArray()) {
			if((" +-=/*{}(),;\"%".indexOf(c) != -1 || c == 10)) {
				if(c == '"' && lastSymbol != '\\' && inText) {
					coloredCode += word + "\"" + p + "f";
					word = "";
					inText = false;
					inTextVar = false;
					continue;
				}
				else if(c == '%' && lastSymbol != '\\' && inText) {
					if(inTextVar) {
						inTextVar = false;
						coloredCode += p+"6" + word + p+"7%";
						word = "";
					}
					else {
						inTextVar = true;
						coloredCode += p+"7" + word+"%";
						word = "";
					}
				}
				else if(!inText) {
					if(word.matches("^((int)|(float)|(string)|(bool))(\\[\\])?$")) {
						coloredCode += p+"9" + word;
						word = "";
					}
					else if(word.equals("global")) {
						coloredCode += p+"5" + word;
						word = "";
					}
					else if(c == '(' && actions.get(word) != null) {
						coloredCode += p+"6" + word;
						word = "";
					}
					else if(c == ')') {
						coloredCode += word + p+"6";
						word = "";
					}
					else if(Pattern.compile("^([0-9.]+)$").matcher(word).find() || Pattern.compile("^\"(.*)\"$").matcher(word).find() || Pattern.compile("^(true)|(false)$").matcher(word).find()) {
						coloredCode += p+"b" + word;
						word = "";
					}
					else {
						coloredCode += p+"f" + word;
						word = "";
					}
					
					word = "";
					
					if(c == '"' && lastSymbol != '\\' && !inText) {
						inText = true;
						coloredCode += p+"7" + c;
					}
					else {
						coloredCode += c;
					}
				}
				else word += c;
			}
			else word += c;
			
			if(coloredCode.equals("") && word.equals("$$")) {
				coloredCode += p + "5" + word;
				word = "";
			}
			
			lastSymbol = c;
		}
		
		if(inText && !word.equals("")) {
			coloredCode += word;
		}
		return coloredCode;
	}
	
	public static ActionBase getAction(String name) {
		return actions.get(name);
	}
}
