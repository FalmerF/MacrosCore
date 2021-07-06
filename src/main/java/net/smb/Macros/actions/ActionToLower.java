package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionToLower extends ActionBase {

	public ActionToLower() {
		super("toLower");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			return parser.getString(args[0]).toLowerCase();
		}
		return "";
	}
}
