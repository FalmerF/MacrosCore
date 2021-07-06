package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionIf extends ActionBase {
	public boolean lastResult = false;
	
	public ActionIf() {
		super("if");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(!arg.equals("") && parser.getBool(arg)) {
			lastResult = true;
			CodeParser newParser = new CodeParser(parser.parserName + " if", parser);
			newParser.alwaysCode = true;
			newParser.executeCode(code);
		}
		else lastResult = false;
		return null;
	}
}
