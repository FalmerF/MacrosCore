package net.smb.Macros.Actions;

import net.smb.Macros.CodeParser;
import net.smb.Macros.ParserError;

public class ActionElse extends ActionBase {
	public ActionElse() {
		super("else");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if((parser.lastAction instanceof ActionIf && !((ActionIf)parser.lastAction).lastResult) || (parser.lastAction instanceof ActionElseIf && !((ActionElseIf)parser.lastAction).lastResult)) {
			CodeParser newParser = new CodeParser(parser.parserName, parser);
			newParser.executeCode(code);
		}
		else if(!(parser.lastAction instanceof ActionIf) && !(parser.lastAction instanceof ActionElseIf)) ParserError.syntax(parser, "else");
		return null;
	}
}
