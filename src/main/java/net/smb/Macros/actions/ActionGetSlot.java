package net.smb.Macros.actions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.smb.Macros.CodeParser;

public class ActionGetSlot extends ActionBase {

	public ActionGetSlot() {
		super("getSlot");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			String searchItemId = parser.getString(args[0]);
			int startSearch = 0;
			if(args.length > 1) startSearch = parser.getInt(args[1]);
			InventoryPlayer inventory = Minecraft.getMinecraft().thePlayer.inventory;
			for(int i = startSearch; i < inventory.getHotbarSize(); i++) {
				ItemStack item = inventory.getStackInSlot(i);
				if(item != null) {
					String itemId = String.valueOf(Item.getIdFromItem(item.getItem()));
					if(item.getItemDamage() != 0) itemId += ":" + item.getItemDamage();
					if(itemId.equals(searchItemId)) return i;
				}
			}
		}
		return -1;
	}
}
