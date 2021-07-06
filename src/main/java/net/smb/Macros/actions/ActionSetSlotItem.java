package net.smb.Macros.actions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.smb.Macros.CodeParser;

public class ActionSetSlotItem extends ActionBase {

	public ActionSetSlotItem() {
		super("setSlotItem");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 1) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityClientPlayerMP player = mc.thePlayer;
			if(mc.playerController.isInCreativeMode()) {
				String[] splitId = parser.getString(args[0]).split(":", 2);
				int slot = parser.getInt(args[1]);
				int stackSize = 1;
				if(args.length > 2) stackSize = parser.getInt(args[2]);
				int id = 0;
				int meta = 0;
				if(splitId.length > 0) id = Integer.parseInt(splitId[0]);
				if(splitId.length > 1) meta = Integer.parseInt(splitId[1]);
				
				ItemStack item = new ItemStack(Item.getItemById(id), stackSize, meta);
				stackSize = Math.max(Math.min(stackSize, item.getMaxStackSize()), 0);
				
				CreativeCrafting craft = new CreativeCrafting(mc);
				
				player.inventoryContainer.addCraftingToCrafters(craft);
				player.inventory.setInventorySlotContents(slot, item);
				player.inventoryContainer.detectAndSendChanges();
				player.inventoryContainer.removeCraftingFromCrafters((ICrafting)craft);
			}
		}
		return null;
	}
}
