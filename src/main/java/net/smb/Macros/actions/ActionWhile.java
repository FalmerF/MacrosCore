package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionWhile extends ActionBase {

	public ActionWhile() {
		super("while");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0 && !code.equals("")) {
			while(parser.getBool(args[0])) {
				if(parser.getError()) break;
				CodeParser doParser = new CodeParser(parser.parserName + " while", parser);
				doParser.alwaysCode = true;
				doParser.executeCode(code);
				if(doParser.getBreak()) break;
				try {
					Thread.sleep((long) (1));
				} catch (Exception e) {}
			}
		}
		return null;
	}
}
