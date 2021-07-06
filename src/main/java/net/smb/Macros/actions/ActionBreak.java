package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionBreak extends ActionBase {

	public ActionBreak() {
		super("break");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		parser.breakParser();
		return null;
	}
}
