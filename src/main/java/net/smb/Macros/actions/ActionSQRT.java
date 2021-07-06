package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionSQRT extends ActionBase {

	public ActionSQRT() {
		super("sqrt");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			return Math.sqrt(parser.getFloat(args[0]));
		}
		return 0;
	}
}
