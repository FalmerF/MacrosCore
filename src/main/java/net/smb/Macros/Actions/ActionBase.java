package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;
import net.smb.Macros.Localisation;
import net.smb.Macros.MacroModCore;

public class ActionBase implements IAction {
	public String description;
	private String name;
	
	public ActionBase(String name) {
		MacroModCore.registerAction(name, this);
		this.name = name;
		description = Localisation.getString("action.description." + name);
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void onTick() {
	}
}
