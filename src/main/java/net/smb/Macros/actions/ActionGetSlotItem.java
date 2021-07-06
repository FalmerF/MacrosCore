package net.smb.Macros.actions;

import java.util.TreeMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.smb.Macros.CodeParser;

public class ActionGetSlotItem extends ActionBase {

	public ActionGetSlotItem() {
		super("getSlotItem");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			int slotId = parser.getInt(args[0]);
			InventoryPlayer inventory = Minecraft.getMinecraft().thePlayer.inventory;
			ItemStack item = inventory.getStackInSlot(slotId);
			TreeMap<Integer, String> array = new TreeMap<Integer, String>();
			array.put(-1, "string");
			if(item != null) {
				array.put(0, item.getDisplayName());
				array.put(1, String.valueOf(Item.getIdFromItem(item.getItem())));
				array.put(2, String.valueOf(item.getItemDamage()));
				array.put(3, String.valueOf(item.stackSize));
				array.put(4, String.valueOf(item.getMaxStackSize()));
			}
			else {
				array.put(0, "air");
				array.put(1, "0");
				array.put(2, "0");
				array.put(3, "0");
				array.put(4, "64");
			}
			return array;
		}
		return null;
	}
}
