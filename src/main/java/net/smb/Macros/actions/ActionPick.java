package net.smb.Macros.actions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.smb.Macros.CodeParser;

public class ActionPick extends ActionBase {

	public ActionPick() {
		super("pick");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			try {
				String id = parser.getString(args[0]);
				InventoryPlayer inventory = Minecraft.getMinecraft().thePlayer.inventory;
				
				for(int i = 0; i < inventory.getHotbarSize(); i++) {
					ItemStack item = inventory.getStackInSlot(i);
					if(item != null) {
						String itemId = String.valueOf(Item.getIdFromItem(item.getItem()));
						if(item.getItemDamage() != 0) itemId += ":" + item.getItemDamage();
						if(itemId.equals(id)) {
							inventory.currentItem = i;
							return true;
						}
					}
				}
			} catch(Exception e) {}
		}
		return false;
	}
}
