package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionFilter extends ActionBase {

	public ActionFilter() {
		super("filter");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0 && !args[0].equals("")) {
			parser.returnVar = !parser.getBool(args[0]);
		}
		else parser.returnVar = false;
		return null;
	}
}
