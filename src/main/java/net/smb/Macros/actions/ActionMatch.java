package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionMatch extends ActionBase {

	public ActionMatch() {
		super("match");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 1) {
			String text = parser.getString(args[0]);
			String regular = parser.getString(args[1]);
			return text.matches(regular);
		}
		return false;
	}
}
