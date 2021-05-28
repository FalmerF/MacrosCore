package net.smb.Macros.gui.screens;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.smb.Macros.Localisation;
import net.smb.Macros.MacrosSettings;
import net.smb.Macros.gui.elements.*;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiScreenKeyBinding extends GuiScreenElement {
	public List<GuiElement> guiElements = new ArrayList<GuiElement>();
	public GuiButtonKeyBind selectedKey, nextSelected;
	public GuiPanel editPanel;
	public GuiField field;
	public GuiScreenMenu currentScreen;
	
	GuiScreenKeyBinding(int posX, int posY) {
		super(-1, posX, posY, 0, 0);
		
		currentScreen = (GuiScreenMenu)Minecraft.getMinecraft().currentScreen;
		
		guiElements.add(new GuiButtonKeyBind(1, posX, posY, 26, 16, "Esc"));
		guiElements.add(new GuiButtonKeyBind(59, posX+56, posY, 26, 16, "F1"));
		guiElements.add(new GuiButtonKeyBind(60, posX+84, posY, 26, 16, "F2"));
		guiElements.add(new GuiButtonKeyBind(61, posX+112, posY, 26, 16, "F3"));
		guiElements.add(new GuiButtonKeyBind(62, posX+140, posY, 26, 16, "F4"));
		
		guiElements.add(new GuiButtonKeyBind(63, posX+178, posY, 26, 16, "F5"));
		guiElements.add(new GuiButtonKeyBind(64, posX+206, posY, 26, 16, "F6"));
		guiElements.add(new GuiButtonKeyBind(65, posX+234, posY, 26, 16, "F7"));
		guiElements.add(new GuiButtonKeyBind(66, posX+262, posY, 26, 16, "F8"));
		
		guiElements.add(new GuiButtonKeyBind(67, posX+300, posY, 26, 16, "F9"));
		guiElements.add(new GuiButtonKeyBind(68, posX+328, posY, 26, 16, "F10"));
		guiElements.add(new GuiButtonKeyBind(87, posX+356, posY, 26, 16, "F11"));
		guiElements.add(new GuiButtonKeyBind(88, posX+384, posY, 26, 16, "F12"));
		
		guiElements.add(new GuiButtonKeyBind(183, posX+422, posY, 26, 16, "SysRq"));
		guiElements.add(new GuiButtonKeyBind(70, posX+450, posY, 26, 16, "Scroll"));
		guiElements.add(new GuiButtonKeyBind(197, posX+478, posY, 26, 16, "Pause"));
		
		guiElements.add(new GuiButtonKeyBind(41, posX, posY+26, 26, 16, "`"));
		guiElements.add(new GuiButtonKeyBind(2, posX+28, posY+26, 26, 16, "1"));
		guiElements.add(new GuiButtonKeyBind(3, posX+56, posY+26, 26, 16, "2"));
		guiElements.add(new GuiButtonKeyBind(4, posX+84, posY+26, 26, 16, "3"));
		guiElements.add(new GuiButtonKeyBind(5, posX+112, posY+26, 26, 16, "4"));
		guiElements.add(new GuiButtonKeyBind(6, posX+140, posY+26, 26, 16, "5"));
		guiElements.add(new GuiButtonKeyBind(7, posX+168, posY+26, 26, 16, "6"));
		guiElements.add(new GuiButtonKeyBind(8, posX+196, posY+26, 26, 16, "7"));
		guiElements.add(new GuiButtonKeyBind(9, posX+224, posY+26, 26, 16, "8"));
		guiElements.add(new GuiButtonKeyBind(10, posX+252, posY+26, 26, 16, "9"));
		guiElements.add(new GuiButtonKeyBind(11, posX+280, posY+26, 26, 16, "0"));
		guiElements.add(new GuiButtonKeyBind(12, posX+308, posY+26, 26, 16, "-"));
		guiElements.add(new GuiButtonKeyBind(13, posX+336, posY+26, 26, 16, "+"));
		guiElements.add(new GuiButtonKeyBind(14, posX+364, posY+26, 48, 16, "\u2B05"));
		
		guiElements.add(new GuiButtonKeyBind(210, posX+422, posY+26, 26, 16, "Ins"));
		guiElements.add(new GuiButtonKeyBind(199, posX+450, posY+26, 26, 16, "Home"));
		guiElements.add(new GuiButtonKeyBind(201, posX+478, posY+26, 26, 16, "PgUp"));
		
		guiElements.add(new GuiButtonKeyBind(15, posX, posY+45, 40, 16, "Tab"));
		guiElements.add(new GuiButtonKeyBind(16, posX+42, posY+45, 26, 16, "Q"));
		guiElements.add(new GuiButtonKeyBind(17, posX+70, posY+45, 26, 16, "W"));
		guiElements.add(new GuiButtonKeyBind(18, posX+98, posY+45, 26, 16, "E"));
		guiElements.add(new GuiButtonKeyBind(19, posX+126, posY+45, 26, 16, "R"));
		guiElements.add(new GuiButtonKeyBind(20, posX+154, posY+45, 26, 16, "T"));
		guiElements.add(new GuiButtonKeyBind(21, posX+182, posY+45, 26, 16, "Y"));
		guiElements.add(new GuiButtonKeyBind(22, posX+210, posY+45, 26, 16, "U"));
		guiElements.add(new GuiButtonKeyBind(23, posX+238, posY+45, 26, 16, "I"));
		guiElements.add(new GuiButtonKeyBind(24, posX+266, posY+45, 26, 16, "O"));
		guiElements.add(new GuiButtonKeyBind(25, posX+294, posY+45, 26, 16, "P"));
		guiElements.add(new GuiButtonKeyBind(26, posX+322, posY+45, 26, 16, "["));
		guiElements.add(new GuiButtonKeyBind(27, posX+350, posY+45, 26, 16, "]"));
		guiElements.add(new GuiButtonKeyBind(43, posX+378, posY+45, 34, 16, "\\"));
		
		guiElements.add(new GuiButtonKeyBind(211, posX+422, posY+45, 26, 16, "Del"));
		guiElements.add(new GuiButtonKeyBind(207, posX+450, posY+45, 26, 16, "End"));
		guiElements.add(new GuiButtonKeyBind(209, posX+478, posY+45, 26, 16, "PgDn"));
		
		guiElements.add(new GuiButtonKeyBind(58, posX, posY+64, 45, 16, "Caps Lock"));
		guiElements.add(new GuiButtonKeyBind(30, posX+47, posY+64, 26, 16, "A"));
		guiElements.add(new GuiButtonKeyBind(31, posX+75, posY+64, 26, 16, "S"));
		guiElements.add(new GuiButtonKeyBind(32, posX+103, posY+64, 26, 16, "D"));
		guiElements.add(new GuiButtonKeyBind(33, posX+131, posY+64, 26, 16, "F"));
		guiElements.add(new GuiButtonKeyBind(34, posX+159, posY+64, 26, 16, "G"));
		guiElements.add(new GuiButtonKeyBind(35, posX+187, posY+64, 26, 16, "H"));
		guiElements.add(new GuiButtonKeyBind(36, posX+215, posY+64, 26, 16, "J"));
		guiElements.add(new GuiButtonKeyBind(37, posX+243, posY+64, 26, 16, "K"));
		guiElements.add(new GuiButtonKeyBind(38, posX+271, posY+64, 26, 16, "L"));
		guiElements.add(new GuiButtonKeyBind(39, posX+299, posY+64, 26, 16, ";"));
		guiElements.add(new GuiButtonKeyBind(40, posX+327, posY+64, 26, 16, "'"));
		guiElements.add(new GuiButtonKeyBind(58, posX+355, posY+64, 57, 16, "Enter"));
		
		guiElements.add(new GuiButtonKeyBind(42, posX, posY+83, 55, 16, "Shift"));
		guiElements.add(new GuiButtonKeyBind(44, posX+57, posY+83, 26, 16, "Z"));
		guiElements.add(new GuiButtonKeyBind(45, posX+85, posY+83, 26, 16, "X"));
		guiElements.add(new GuiButtonKeyBind(46, posX+113, posY+83, 26, 16, "C"));
		guiElements.add(new GuiButtonKeyBind(47, posX+141, posY+83, 26, 16, "V"));
		guiElements.add(new GuiButtonKeyBind(48, posX+169, posY+83, 26, 16, "B"));
		guiElements.add(new GuiButtonKeyBind(49, posX+197, posY+83, 26, 16, "N"));
		guiElements.add(new GuiButtonKeyBind(50, posX+225, posY+83, 26, 16, "M"));
		guiElements.add(new GuiButtonKeyBind(51, posX+253, posY+83, 26, 16, ","));
		guiElements.add(new GuiButtonKeyBind(52, posX+281, posY+83, 26, 16, "."));
		guiElements.add(new GuiButtonKeyBind(53, posX+309, posY+83, 26, 16, "/"));
		guiElements.add(new GuiButtonKeyBind(54, posX+337, posY+83, 75, 16, "RShift"));
		
		guiElements.add(new GuiButtonKeyBind(29, posX, posY+102, 37, 16, "Ctrl"));
		guiElements.add(new GuiButtonKeyBind(219, posX+39, posY+102, 26, 16, "Meta"));
		guiElements.add(new GuiButtonKeyBind(56, posX+67, posY+102, 37, 16, "Alt"));
		guiElements.add(new GuiButtonKeyBind(57, posX+106, posY+102, 162, 16, "Space"));
		guiElements.add(new GuiButtonKeyBind(184, posX+270, posY+102, 34, 16, "RAlt"));
		guiElements.add(new GuiButtonKeyBind(220, posX+306, posY+102, 34, 16, "RMeta"));
		guiElements.add(new GuiButtonKeyBind(184, posX+342, posY+102, 34, 16, "Menu"));
		guiElements.add(new GuiButtonKeyBind(157, posX+378, posY+102, 34, 16, "RCtrl"));
		
		guiElements.add(new GuiButtonKeyBind(200, posX+450, posY+83, 26, 16, "\u2B06"));
		
		guiElements.add(new GuiButtonKeyBind(203, posX+422, posY+102, 26, 16, "\u2B05"));
		guiElements.add(new GuiButtonKeyBind(208, posX+450, posY+102, 26, 16, "\u2B07"));
		guiElements.add(new GuiButtonKeyBind(205, posX+478, posY+102, 26, 16, "\u2B0C"));
		
		guiElements.add(new GuiButtonKeyBind(69, posX+394, posY+125, 26, 16, "Num"));
		guiElements.add(new GuiButtonKeyBind(181, posX+422, posY+125, 26, 16, "/"));
		guiElements.add(new GuiButtonKeyBind(55, posX+450, posY+125, 26, 16, "*"));
		guiElements.add(new GuiButtonKeyBind(74, posX+478, posY+125, 26, 16, "-"));
		
		guiElements.add(new GuiButtonKeyBind(71, posX+394, posY+144, 26, 16, "7"));
		guiElements.add(new GuiButtonKeyBind(72, posX+422, posY+144, 26, 16, "8"));
		guiElements.add(new GuiButtonKeyBind(73, posX+450, posY+144, 26, 16, "9"));
		guiElements.add(new GuiButtonKeyBind(78, posX+478, posY+144, 26, 35, "+"));
		
		guiElements.add(new GuiButtonKeyBind(75, posX+394, posY+163, 26, 16, "4"));
		guiElements.add(new GuiButtonKeyBind(76, posX+422, posY+163, 26, 16, "5"));
		guiElements.add(new GuiButtonKeyBind(77, posX+450, posY+163, 26, 16, "6"));
		
		guiElements.add(new GuiButtonKeyBind(79, posX+394, posY+182, 26, 16, "1"));
		guiElements.add(new GuiButtonKeyBind(80, posX+422, posY+182, 26, 16, "2"));
		guiElements.add(new GuiButtonKeyBind(81, posX+450, posY+182, 26, 16, "3"));
		guiElements.add(new GuiButtonKeyBind(156, posX+478, posY+182, 26, 35, "Ent"));
		
		guiElements.add(new GuiButtonKeyBind(82, posX+394, posY+201, 54, 16, "0"));
		guiElements.add(new GuiButtonKeyBind(83, posX+450, posY+201, 26, 16, "."));
		
		guiElements.add(new GuiButtonKeyBind(1001, posX, posY+128, 54, 16, "LMouse"));
		guiElements.add(new GuiButtonKeyBind(1004, posX, posY+175, 54, 16, "Mouse4"));
		guiElements.add(new GuiButtonKeyBind(1003, posX, posY+198, 54, 16, "Mouse3"));
		
		guiElements.add(new GuiButtonKeyBind(1002, posX+190, posY+128, 54, 16, "RMouse"));
		
		guiElements.add(new GuiButtonKeyBind(1005, posX+190, posY+182, 54, 16, "Up"));
		guiElements.add(new GuiButtonKeyBind(1006, posX+190, posY+200, 54, 16, "Middle"));
		guiElements.add(new GuiButtonKeyBind(1007, posX+190, posY+218, 54, 16, "Down"));
		
		editPanel = new GuiPanel(-1, 5, currentScreen.height-60, currentScreen.width-10, 55);
		editPanel.addGuiElement(new GuiText(5, 5, 1.0F, ""));
		field = new GuiField(5, 30, currentScreen.width-50, 1F, "", Localisation.getString("field.description.macros"));
		field.code = true;
		editPanel.addGuiElement(field);
		GuiButtonWithIcon button = new GuiButtonWithIcon(1, currentScreen.width-35, 30, 20, 0);
		button.iconColor = Color.color_9;
		editPanel.addGuiElement(button);
		editPanel.addGuiElement(new GuiButtonWithIcon(2, currentScreen.width-35, 5, 20, 1));
		editPanel.setVisible(false);
		
		GuiPanel panel = new GuiPanel(-1, 530, posY, 140, 230);
		panel.addGuiElement(new GuiText(10, 10, 1.3F, Localisation.getString("menu.keybinding.guide.title")));
		panel.addGuiElement(new GuiText(10, 20, 1.0F, Localisation.getString("menu.keybinding.guide")));
		
		guiElements.add(panel);
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			mc.getTextureManager().bindTexture(ResourceLocations.mouseImg);
			RenderUtil.setColor(Color.color_5);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			Gui.func_152125_a(this.posX+40, this.posY+135, 1, 1, 1, 1, 160, 130, 1, 1);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			
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
		    				String macro = MacrosSettings.getString("key_" + selectedKey.id);
		    				selectedKey.selected = false;
		    				if(!field.saved) {
				    			nextSelected = (GuiButtonKeyBind) element;
				    			editPanel.setVisible(false);
				    			currentScreen.saveOpen(10);
		    				}
		    				else setSelecetdKey((GuiButtonKeyBind) element);
		    			}
		    			else setSelecetdKey((GuiButtonKeyBind) element);
		    			return true;
	    			}
	    		}
	    	}
		} catch(Exception e) {}
		return false;
	}
	
	public void setSelecetdKey(GuiButtonKeyBind element) {
		if(element == null) {
			if(selectedKey != null) selectedKey.selected = false;
			selectedKey = null;
			field.setSelected(false);
			editPanel.setVisible(false);
		}
		else {
			selectedKey = (GuiButtonKeyBind) element;
			((GuiText)editPanel.guiElements.get(0)).SetText(Localisation.getString("keybind.edit.key") + "[" + selectedKey.displayString + "]");
			field.text = MacrosSettings.getString("key_" + selectedKey.id);
			field.setSelected(true);
			currentScreen.selected = field;
			editPanel.setVisible(true);
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
		MacrosSettings.setParam("key_" + selectedKey.id, field.text);
		selectedKey.checkHasCode();
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
