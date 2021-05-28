package net.smb.Macros.Actions;

import net.smb.Macros.CodeParser;
import net.smb.Macros.ParserError;

public class ActionFor extends ActionBase {
	public boolean lastResult = false;
	
	public ActionFor() {
		super("for");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length == 3 && !args[1].equals("") && !code.equals("")) {
			CodeParser newParser = new CodeParser(parser.parserName, parser);
			if(!args[0].equals("")) {
				newParser.executeCode(args[0] + ";");
			}
			if(!args[2].equals("")) {
				for(;newParser.getBool(args[1]); newParser.executeCode(args[2] + ";")) {
					if(parser.getError()) break;
					CodeParser forParser = new CodeParser(newParser.parserName + " for", newParser);
					forParser.executeCode(code);
					try {
						Thread.sleep((long) (1));
					} catch (Exception e) {}
				}
			}
			else {
				for(;newParser.getBool(args[1]);) {
					if(parser.getError()) break;
					CodeParser forParser = new CodeParser(newParser.parserName + " for", newParser);
					forParser.executeCode(code);
					try {
						Thread.sleep((long) (1));
					} catch (Exception e) {}
				}
			}
		}
		else ParserError.syntax(parser, "for");
		return null;
	}
}
