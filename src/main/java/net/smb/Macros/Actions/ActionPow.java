package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;
import net.smb.Macros.util.Log;

public class ActionPow extends ActionBase {

	public ActionPow() {
		super("pow");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 1) {
			float x = parser.getFloat(args[0]);
			float y = parser.getFloat(args[1]);
			return Math.pow(x, y);
		}
		return 0;
	}
}
