package net.smb.Macros.gui.screens;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.smb.Macros.Localisation;
import net.smb.Macros.MacroModCore;
import net.smb.Macros.MacrosSettings;
import net.smb.Macros.gui.elements.GuiButtonWithIcon;
import net.smb.Macros.gui.elements.GuiElement;
import net.smb.Macros.gui.elements.GuiField;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.gui.elements.GuiText;
import net.smb.Macros.util.Color;

public class GuiScreenEvents extends GuiScreenElement {
	public List<GuiElement> guiElements = new ArrayList<GuiElement>();
	public GuiButtonWithIcon selectedKey, nextSelected;
	public GuiPanel editPanel;
	public GuiField field;
	public GuiScreenMenu currentScreen;
	
	public GuiPanel infoPanel;
	
	GuiScreenEvents(int posX, int posY) {
		super(-1, posX, posY, 0, 0);
		
		currentScreen = (GuiScreenMenu)Minecraft.getMinecraft().currentScreen;
		
		List<String> events = MacroModCore.getEventsList();
		int maxEventsX = (currentScreen.width-this.posX-170)/110;
		int maxEventsY = (currentScreen.height-this.posY-75)/20;
		for(int i = 0; i < events.size(); i++) {
			guiElements.add(new GuiButtonWithIcon(-1, posX+10+(i%maxEventsX)*110, posY+10+(i/maxEventsX)*20, 100, 17, 2, events.get(i)));
		}
		
		editPanel = new GuiPanel(-1, 5, currentScreen.height-60, currentScreen.width-10, 55);
		editPanel.addGuiElement(new GuiText(5, 5, 1.0F, ""));
		field = new GuiField(5, 30, currentScreen.width-50, 1F, "", Localisation.getString("field.description.macros"));
		field.code = true;
		editPanel.addGuiElement(field);
		GuiButtonWithIcon button = new GuiButtonWithIcon(19, currentScreen.width-35, 30, 20, 0);
		button.iconColor = Color.color_9;
		editPanel.addGuiElement(button);
		editPanel.addGuiElement(new GuiButtonWithIcon(2, currentScreen.width-35, 5, 20, 1));
		editPanel.setVisible(false);
		
		infoPanel = new GuiPanel(-1, currentScreen.width-150, posY, 140, 230);
		infoPanel.addGuiElement(new GuiText(10, 10, 1.3F, Localisation.getString("menu.event.guide.title")));
		infoPanel.addGuiElement(new GuiText(10, 25, 1.0F, Localisation.getString("menu.event.guide")));
		
		guiElements.add(infoPanel);
	}
	
	public void update() {
		editPanel.update();
		for(GuiElement element : guiElements) element.update();
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			for(GuiElement element : guiElements) element.draw(mc, positionX, positionY);
			
			editPanel.draw(mc, positionX, positionY);
		}
	}
	
	public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
		if(this.visible && id == 0) {
			if(editPanel.clicked(id, posX, posY, gui)) return true;
			else if(CheckPressElements(guiElements, posX, posY)) return true;
    		else return false;
		} else return false;
	}
	
	public boolean CheckPressElements(List<GuiElement> elements, int posX, int posY) {
		try {
			for(GuiElement element : elements) {
	    		if(element.clicked(0, posX, posY, null)) {
	    			if(element != selectedKey) {
		    			if(selectedKey != null) { 
		    				GuiField field = (GuiField)editPanel.guiElements.get(1);
		    				selectedKey.selected = false;
		    				if(!field.saved) {
				    			nextSelected = (GuiButtonWithIcon) element;
				    			editPanel.setVisible(false);
				    			currentScreen.saveOpen(10);
		    				}
		    				else setSelecetdKey((GuiButtonWithIcon) element);
		    			}
		    			else setSelecetdKey((GuiButtonWithIcon) element);
		    			return true;
	    			}
	    		}
	    	}
		} catch(Exception e) {}
		return false;
	}
	
	public void setSelecetdKey(GuiButtonWithIcon element) {
		if(element == null) {
			if(selectedKey != null) selectedKey.selected = false;
			selectedKey = null;
			field.setSelected(false);
			editPanel.setVisible(false);
			((GuiText)infoPanel.guiElements.get(0)).SetText(Localisation.getString("menu.event.guide.title"));
			((GuiText)infoPanel.guiElements.get(1)).SetText(Localisation.getString("menu.event.guide"));
		}
		else {
			selectedKey = (GuiButtonWithIcon) element;
			((GuiText)editPanel.guiElements.get(0)).SetText(Localisation.getString("keybind.edit.event") + " " + selectedKey.displayString);
			field.text = MacrosSettings.getString("event_" + selectedKey.displayString);
			field.setSelected(true);
			currentScreen.selected = field;
			editPanel.setVisible(true);
			((GuiText)infoPanel.guiElements.get(0)).SetText(selectedKey.displayString);
			((GuiText)infoPanel.guiElements.get(1)).SetText(MacroModCore.getEventDescription(selectedKey.displayString));
		}
	}
	
	public void setSelected(boolean selected) {
    	this.selected = selected;
    	if(!selected) editPanel.setVisible(false);
    }
	
	public void keyTyped(char key, int keyId) {
		editPanel.keyTyped(key, keyId);
		for(GuiElement element : guiElements) element.keyTyped(key, keyId);
	}
	
	public void saveMacros() {
		MacrosSettings.setParam("event_" + selectedKey.displayString, field.text);
		field.saved = true;
	}
	
	public void saveClose(boolean answer, int actionId) {
		if(answer) saveMacros();
		else field.saved = true;
		
		if(actionId == 10) {
			selectedKey.selected = false;
			selectedKey = null;
			setSelecetdKey(nextSelected);
		}
		else if(actionId == 1) {
			setSelecetdKey(null);
		}
	}
	
	public boolean closeMenu() {
		if(editPanel.visible && !field.saved) {
			currentScreen.saveOpen(0);
			return false;
		}
		else return true;
	}
	public boolean switchScreen() {
		if(editPanel.visible && !field.saved) {
			currentScreen.saveOpen(1);
			return false;
		}
		else {
			setSelecetdKey(null);
			return true;
		}
	}
}
