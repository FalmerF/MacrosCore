package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionToUpper extends ActionBase {

	public ActionToUpper() {
		super("toUpper");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			return parser.getString(args[0]).toUpperCase();
		}
		return "";
	}
}
