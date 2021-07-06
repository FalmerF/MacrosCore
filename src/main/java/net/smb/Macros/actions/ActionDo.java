package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionDo extends ActionBase {

	public ActionDo() {
		super("do");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0 && !code.equals("")) {
			int count = parser.getInt(args[0]);
			for(int i = 0; i < count; i++) {
				if(parser.getError()) break;
				CodeParser doParser = new CodeParser(parser.parserName + " do", parser);
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
