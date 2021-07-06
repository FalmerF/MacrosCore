package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionStrip extends ActionBase {

	public ActionStrip() {
		super("strip");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			return parser.getString(args[0]).replaceAll("(?<!&)\u00A7([0-9a-fklmnor])", "").replaceAll("(?<!&)&([0-9a-fklmnor])", "");
		}
		return null;
	}
}
