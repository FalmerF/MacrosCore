package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionContinue extends ActionBase {

	public ActionContinue() {
		super("continue");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		parser.continueParser();
		return null;
	}
}
