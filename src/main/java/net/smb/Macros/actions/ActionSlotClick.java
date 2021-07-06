package net.smb.Macros.actions;

import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.smb.Macros.CodeParser;
import net.smb.Macros.util.Reflection;

public class ActionSlotClick extends ActionBase {

	public ActionSlotClick() {
		super("slotClick");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityClientPlayerMP player = mc.thePlayer;
			if(player != null) {
				int slotId = parser.getInt(args[0]);
				int button = 0;
				boolean shift = false;
				if(args.length > 1) button = parser.getInt(args[1]);
				if(args.length > 2) shift = parser.getBool(args[2]);
				
				if(mc.currentScreen != null && mc.currentScreen instanceof GuiContainer) {
					try {
						GuiContainer gui = (GuiContainer) mc.currentScreen;
						Container container = gui.inventorySlots;
						Slot slot = container.getSlot(slotId);
						Method method = GuiContainer.class.getDeclaredMethod("func_146984_a", Slot.class, int.class, int.class, int.class);
						method.setAccessible(true);
						method.invoke(gui, slot, slotId, button, shift ? 1 : 0);
					} catch(Exception e) {e.printStackTrace();}
				}
			}
		}
		return null;
	}
}
