package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionWait extends ActionBase {

	public ActionWait() {
		super("wait");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			try {
				Thread.sleep((long) (parser.getFloat(args[0])));
			} catch (Exception e) {}
		}
		return null;
	}
}
