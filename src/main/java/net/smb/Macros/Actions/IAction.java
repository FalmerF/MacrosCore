package net.smb.Macros.Actions;

import net.smb.Macros.CodeParser;

public abstract interface IAction {
	public Object execute(CodeParser parser, String arg, String[] args, String code);
}
