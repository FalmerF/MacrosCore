package net.smb.Macros.actions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.smb.Macros.CodeParser;

public class ActionSlot extends ActionBase {

	public ActionSlot() {
		super("slot");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			if(player != null) {
				player.inventory.currentItem = Math.max(Math.min(parser.getInt(args[0]), 8), 0);
			}
		}
		return null;
	}
}
