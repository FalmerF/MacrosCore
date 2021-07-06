package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;
import net.smb.Macros.ParserError;
import net.smb.Macros.util.Log;

public class ActionElseIf extends ActionBase {
	public boolean lastResult = false;
	
	public ActionElseIf() {
		super("elseif");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if((parser.lastAction instanceof ActionIf && !((ActionIf)parser.lastAction).lastResult) || (parser.lastAction instanceof ActionElseIf && !((ActionElseIf)parser.lastAction).lastResult)) {
			if(!arg.equals("") && parser.getBool(arg)) {
				lastResult = true;
				CodeParser newParser = new CodeParser(parser.parserName + " elseif", parser);
				newParser.alwaysCode = true;
				newParser.executeCode(code);
			}
			else lastResult = false;
		}
		else {
			lastResult = true;
			if(!(parser.lastAction instanceof ActionIf) && !(parser.lastAction instanceof ActionElseIf)) ParserError.syntax(parser, "elseif");
		}
		return null;
	}
}
