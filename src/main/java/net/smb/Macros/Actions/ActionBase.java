package net.smb.Macros.Actions;

import net.smb.Macros.CodeParser;
import net.smb.Macros.MacroModCore;

public class ActionBase implements IAction {

	public ActionBase(String name) {
		MacroModCore.registerAction(name, this);
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		return null;
	}
}
