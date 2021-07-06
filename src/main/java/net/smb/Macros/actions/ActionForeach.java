package net.smb.Macros.actions;

import java.util.Map;
import java.util.TreeMap;

import net.smb.Macros.CodeParser;
import net.smb.Macros.ParserError;

public class ActionForeach extends ActionBase {

	public ActionForeach() {
		super("foreach");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			Object val = parser.getVar(args[0]);
			if(val != null) {
				if(val instanceof TreeMap) {
					TreeMap<Integer, Object> array = (TreeMap) val;
					for(Map.Entry<Integer, Object> param : array.entrySet()) {
						if(param.getKey() != -1) {
							if(parser.getError()) break;
							CodeParser forParser = new CodeParser(parser.parserName + " foreach", parser);
							forParser.alwaysCode = true;
							forParser.vars.put("element", param.getValue());
							forParser.executeCode(code);
							if(forParser.getBreak()) break;
							try {
								Thread.sleep((long) (1));
							} catch (Exception e) {}
						}
					}
				}
				else ParserError.notArray(parser, args[0]);
			}
			else ParserError.varDoesntExist(parser, args[0]);
		}
		return null;
	}
}
