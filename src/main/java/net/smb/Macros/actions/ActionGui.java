package net.smb.Macros.actions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.smb.Macros.CodeParser;
import net.smb.Macros.gui.screens.GuiScreenMenu;

public class ActionGui extends ActionBase {
	private GuiScreen openGui;
	
	
	public ActionGui() {
		super("gui");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.thePlayer == null) return null;
		if(args.length > 0 && !args[0].equals("")) {
			String screen = parser.getString(args[0]);
			openGui = getGui(screen);
			while(openGui != null) {
				try {
					Thread.sleep((long) (100));
				} catch (Exception e) {}
			}
		}
		else mc.displayGuiScreen(null);
		return null;
	}
	
	@Override
	public void onTick() {
		if(openGui != null) {
			Minecraft.getMinecraft().displayGuiScreen(openGui);
			openGui = null;
		}
	}
	
	public GuiScreen getGui(String name) {
		if(name.equals("inventory")) {
			return new GuiInventory(Minecraft.getMinecraft().thePlayer);
		}
		else if(name.equals("menu")) {
			return new GuiIngameMenu();
		}
		else if(name.equals("options")) {
			return new GuiOptions(null, Minecraft.getMinecraft().gameSettings);
		}
		else if(name.equals("macrosmenu")) {
			return new GuiScreenMenu(Minecraft.getMinecraft());
		}
		return null;
	}
}
