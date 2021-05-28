package net.smb.Macros;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.smb.Macros.Actions.ActionBase;
import net.smb.Macros.util.Log;

public class CodeParser {
	public String parserName;
	public CodeParser parentParser;
	public static Map<String, Object> globalVars = new TreeMap<String, Object>();
	private static Map<String, ActionBase> actions = new TreeMap<String, ActionBase>();
	
	public Map<String, Object> vars = new TreeMap<String, Object>();
	
	public static Pattern intPattern = Pattern.compile("^([0-9]+)$");
	public static Pattern floatPattern = Pattern.compile("^([0-9.]+)$");
	public static Pattern stringPattern = Pattern.compile("\"(.*)\"$");
	public static Pattern boolPattern = Pattern.compile("^(true)|(false)$");
	
	private String runningCode;
	
	private boolean error;
	
	private CodeParser instance;
	
	public ActionBase lastAction;
	
	public CodeParser(String name, CodeParser parentParser){
		instance = this;
		this.parserName = name;
		this.parentParser = parentParser;
	}
	
	public void executeCode(String code) {
		runningCode = code;
		String command = "";
		int bracketsCount = 0;
		boolean quotes = false;
		char lastSymbol = ' ';
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
			
			if(error) {
				if(parentParser != null) parentParser.error = true;
				break main;
			}
		}
		if(!error && !command.equals("")) ParserError.expected(instance, command);
	}
	
	public void setError() {
		this.error = true;
		if(this.parentParser != null) this.parentParser.setError();
	}
	
	public boolean getError() {
		return error;
	}
	
	public void runCommand(String command, boolean executeCode) {
		String[] args = command.split(" ");
		if(Pattern.compile("^((int)|(float)|(string)|(bool))\\s([a-zA-Z_0-9])").matcher(command).find()) {
			if(hasVar(args[1])) {
				ParserError.varAlreadyExists(instance, args[1]);
			}
			else if(intPattern.matcher(args[1].toLowerCase()).find()) {
				ParserError.invalidVarName(instance, args[1]);
			}
			if(args[0].equals("int")) {
				if(args.length > 2) {
					String[] value = command.split("=", 2);
					vars.put(args[1], getInt(value[1]));
				}
				else vars.put(args[1], 0);
			}
			else if(args[0].equals("float")) {
				if(args.length > 2) {
					String[] value = command.split("=", 2);
					vars.put(args[1], getFloat(value[1]));
				}
				else vars.put(args[1], 0);
			}
			else if(args[0].equals("string")) {
				if(args.length > 2) {
					String[] value = command.split("=", 2);
					vars.put(args[1], getString(value[1]));
				}
				else vars.put(args[1], "");
			}
			if(args[0].equals("bool")) {
				if(args.length > 2) {
					String[] value = command.split("=", 2);
					vars.put(args[1], getBool(value[1]));
				}
				else vars.put(args[1], false);
			}
		}
		else if(Pattern.compile("^([a-zA-Z_0-9]+)\\s*=(.+)$").matcher(command).find()) {
			Matcher matcher = Pattern.compile("^([a-zA-Z_0-9]+)\\s*=(.+)$").matcher(command);
			matcher.find();
			String varName = matcher.group(1);
			String value = matcher.group(2);
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
			}
			else ParserError.varDoesntExist(instance, varName);
		}
		else if(Pattern.compile("^([a-zA-Z_0-9]+)\\(([^{}]*)\\)\\s*(\\{(.*)\\})*$").matcher(command).find()){
			runAction(command, true);
		}
		else ParserError.syntax(instance, command);
	}
	
	public int getInt(String value) {
		try {
			value = replaceSpaces(value);
			value = replaceActions(value);
			if(Pattern.compile("^([0-9]+)$").matcher(value).find()) {
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
					else if(val.matches("^([a-zA-Z_0-9])+$")) {
						newValues[i] = String.valueOf(getVar(val));
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
			if(Pattern.compile("^([0-9.]+)$").matcher(value).find()) {
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
				Map<String, Object> allVars = getAllVars(true);
				for(Entry<String, Object> var : allVars.entrySet()) {
					value = value.replaceAll("%" + var.getKey() + "%", String.valueOf(var.getValue()));
				}
				value = value.replaceAll("\\\\\\\"", "\"");
				return value;
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
			Object returnValue = action.execute(this, value, values2, matcher.group(4));
			if(executeCode) lastAction = action;
			return returnValue;
		}
		ParserError.methodDoesntExist(instance, matcher.group(1));
		return null;
	}
	
	public Object getVar(String key) {
		key = key.toLowerCase();
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
		for(char c : code.toCharArray()) {
			if((" +-=/*{}(),;\"".indexOf(c) != -1 || c == 10)) {
				if(c == '"' && lastSymbol != '\\' && inText) {
					coloredCode += word + "\"" + p + "f";
					word = "";
					inText = false;
					continue;
				}
				else if(!inText) {
					if(word.equals("int") || word.equals("float") || word.equals("string") || word.equals("bool")) {
						coloredCode += p+"9" + word;
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
			
			lastSymbol = c;
		}
		
		if(inText && !word.equals("")) {
			coloredCode += word;
		}
		return coloredCode;
	}
}
