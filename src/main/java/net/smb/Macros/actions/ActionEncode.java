package net.smb.Macros.actions;

import org.bouncycastle.util.encoders.Base64;

import net.smb.Macros.CodeParser;

public class ActionEncode extends ActionBase {

	public ActionEncode() {
		super("encode");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			String encoded = new String(Base64.encode(parser.getString(args[0]).getBytes()));
			return encoded;
		}
		return null;
	}
}
