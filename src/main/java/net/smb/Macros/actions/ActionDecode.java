package net.smb.Macros.actions;

import org.bouncycastle.util.encoders.Base64;

import net.smb.Macros.CodeParser;

public class ActionDecode extends ActionBase {

	public ActionDecode() {
		super("decode");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			String decoded = new String(Base64.decode(parser.getString(args[0]).getBytes()));
			return decoded;
		}
		return null;
	}
}
