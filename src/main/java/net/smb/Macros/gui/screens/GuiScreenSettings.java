package net.smb.Macros.gui.screens;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.smb.Macros.Localisation;
import net.smb.Macros.MacrosSettings;
import net.smb.Macros.gui.elements.GuiCheck;
import net.smb.Macros.gui.elements.GuiDropdown;
import net.smb.Macros.gui.elements.GuiElement;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.gui.elements.GuiText;
import net.smb.Macros.util.Log;

public class GuiScreenSettings extends GuiScreenElement {
	public List<GuiElement> guiElements = new ArrayList<GuiElement>();
	public GuiPanel paramsPanel;
	public GuiScreenMenu currentScreen;
	public GuiDropdown languageDropdown;
	public GuiCheck hintsCheck, slotCheck, chatCheck, debugCheck;
	
	GuiScreenSettings(int posX, int posY) {
		super(-1, posX, posY, 0, 0);
		
		currentScreen = (GuiScreenMenu)Minecraft.getMinecraft().currentScreen;
		
		paramsPanel = new GuiPanel(-1, posX, posY, currentScreen.width-10, currentScreen.height-this.posY-5);
		paramsPanel.addGuiElement(new GuiText(20, 20, 1.0F, Localisation.getString("menu.settings.language")));
		List<String> elements = new ArrayList<String>();
		elements.add("Auto");
		elements.add("English");
		elements.add("Russian");
		languageDropdown = new GuiDropdown(25, 60, 17, 80, 15, elements, MacrosSettings.getInt("language"));
		paramsPanel.addGuiElement(languageDropdown);
		
		paramsPanel.addGuiElement(new GuiText(35, 50, 1.0F, Localisation.getString("menu.settings.codehints"), Localisation.getString("menu.settings.codehints.description")));
		hintsCheck = new GuiCheck(26, 20, 50, 10, MacrosSettings.getBool("codehints"));
		paramsPanel.addGuiElement(hintsCheck);
		
		paramsPanel.addGuiElement(new GuiText(35, 80, 1.0F, Localisation.getString("menu.settings.slotid"), Localisation.getString("menu.settings.slotid.description")));
		slotCheck = new GuiCheck(27, 20, 80, 10, MacrosSettings.getBool("slotid"));
		paramsPanel.addGuiElement(slotCheck);
		
		paramsPanel.addGuiElement(new GuiText(35, 110, 1.0F, Localisation.getString("menu.settings.chatbutton"), Localisation.getString("menu.settings.chatbutton.description")));
		chatCheck = new GuiCheck(28, 20, 110, 10, MacrosSettings.getBool("chatbutton"));
		paramsPanel.addGuiElement(chatCheck);
		
		paramsPanel.addGuiElement(new GuiText(35, 140, 1.0F, Localisation.getString("menu.settings.debug"), Localisation.getString("menu.settings.debug.description")));
		debugCheck = new GuiCheck(29, 20, 140, 10, MacrosSettings.getBool("debug"));
		paramsPanel.addGuiElement(debugCheck);
		
		guiElements.add(paramsPanel);
	}
	
	public void update() {
		for(GuiElement element : guiElements) element.update();
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			for(GuiElement element : guiElements) element.draw(mc, positionX, positionY);
		}
	}
	
	public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
		if(id == 0 && this.visible && CheckPressElements(guiElements, posX, posY, gui)) return true;
    	else return false;
	}
	
	public boolean CheckPressElements(List<GuiElement> elements, int posX, int posY, GuiScreenHeader gui) {
		try {
			for(GuiElement element : elements) {
	    		if(element.clicked(0, posX, posY, gui)) {
	    			return true;
	    		}
	    	}
		} catch(Exception e) {}
		return false;
	}
	
	public void saveParam(int id) {
		if(id == 25) {
			MacrosSettings.setParam("language", languageDropdown.selectedElement);
			Localisation.loadLang();
		}
		else if(id == 26) {
			MacrosSettings.setParam("codehints", hintsCheck.value);
		}
		else if(id == 27) {
			MacrosSettings.setParam("slotid", slotCheck.value);
		}
		else if(id == 28) {
			MacrosSettings.setParam("chatbutton", chatCheck.value);
		}
		else if(id == 29) {
			MacrosSettings.setParam("debug", debugCheck.value);
		}
	}
	
	public void mouseScroll(int scroll) {
		for(GuiElement element : guiElements) element.mouseScroll(scroll);
	}
	
	public void keyTyped(char key, int keyId) {
		for(GuiElement element : guiElements) element.keyTyped(key, keyId);
	}
	
	public boolean closeMenu() {
		return true;
	}
	public boolean switchScreen() {
		return true;
	}
}
