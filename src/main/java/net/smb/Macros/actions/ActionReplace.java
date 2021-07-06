package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionReplace extends ActionBase {

	public ActionReplace() {
		super("replace");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 2) {
			String text = parser.getString(args[0]);
			String pattern = parser.getString(args[1]);
			String replace = parser.getString(args[2]);
			return text.replaceAll(pattern, replace);
		}
		return "";
	}
}
