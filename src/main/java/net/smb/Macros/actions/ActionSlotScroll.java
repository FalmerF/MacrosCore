package net.smb.Macros.actions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.smb.Macros.CodeParser;

public class ActionSlotScroll extends ActionBase {

	public ActionSlotScroll() {
		super("slotScroll");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			int scroll = parser.getInt(args[0]);
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			if(player != null) {
				int newItem = (player.inventory.currentItem+scroll)%9;
				if(newItem < 0) newItem = Math.max(9+newItem, 0);
				player.inventory.currentItem = newItem;
			}
		}
		return null;
	}
}
