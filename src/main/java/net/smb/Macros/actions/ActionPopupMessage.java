package net.smb.Macros.actions;

import net.minecraft.client.Minecraft;
import net.smb.Macros.CodeParser;

public class ActionPopupMessage extends ActionBase {

	public ActionPopupMessage() {
		super("popupMessage");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			boolean animate = false;
			if(args.length > 1) animate = parser.getBool(args[1]);
			Minecraft.getMinecraft().ingameGUI.func_110326_a(parser.getString(args[0]), animate);
		}
		return null;
	}
}
