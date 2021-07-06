package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public abstract interface IAction {
	public Object execute(CodeParser parser, String arg, String[] args, String code);
	public String getName();
	public void onTick();
}
