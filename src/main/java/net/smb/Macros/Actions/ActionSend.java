package net.smb.Macros.actions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.smb.Macros.CodeParser;

public class ActionSend extends ActionBase {

	public ActionSend() {
		super("send");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			((EntityClientPlayerMP)Minecraft.getMinecraft().thePlayer).sendChatMessage(parser.getString(args[0]));
		}
		return null;
	}
}
