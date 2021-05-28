package net.smb.Macros.gui.screens;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.smb.Macros.Localisation;
import net.smb.Macros.MacrosSettings;
import net.smb.Macros.gui.elements.GuiButton;
import net.smb.Macros.gui.elements.GuiButtonWithIcon;
import net.smb.Macros.gui.elements.GuiElement;
import net.smb.Macros.gui.elements.GuiField;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.gui.elements.GuiText;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiScreenMenu extends GuiScreenHeader {
	private GuiScreenKeyBinding screenKeyBinding;
	private GuiScreenEditor screenEditor;
	
	public GuiScreenElement selectedCategory, tempCategory;
	public GuiButton buttonOfSelectedCategory, tempButtonOfCategory;
	
	public GuiPanel savePanel;
	
	private int actionId = 0;
	
	public GuiScreenMenu(Minecraft mc) {
		super(mc);
	}
	
	public void initGui()
    {
		super.initGui();
		
		screenKeyBinding = new GuiScreenKeyBinding(15, 55);
		selectedCategory = screenKeyBinding;
		this.guiElements.add(screenKeyBinding);
		screenEditor = new GuiScreenEditor(5, 55);
		screenEditor.visible = false;
		this.guiElements.add(screenEditor);
		
		buttonOfSelectedCategory = new GuiButton(5, 5, 30, 100, 15, Localisation.getString("menu.category.keybind"), 4);
		buttonOfSelectedCategory.selected = true;
		this.guiElements.add(buttonOfSelectedCategory);
		this.guiElements.add(new GuiButton(6, 105, 30, 100, 15, Localisation.getString("menu.category.events"), 2));
		this.guiElements.add(new GuiButton(7, 205, 30, 100, 15, Localisation.getString("menu.category.editor"), 2));
		this.guiElements.add(new GuiButton(8, 305, 30, 100, 15, Localisation.getString("menu.category.gui"), 3));
		this.guiElements.add(new GuiButton(9, this.width-105, 30, 100, 15, Localisation.getString("menu.category.settings")));
		
		savePanel = new GuiPanel(-1, this.width/2-100, this.height/2-25, 200, 50);
		GuiText text2 = new GuiText(100, 10, 1.0F, Localisation.getString("menu.keybinding.save"));
		text2.centered = true;
		savePanel.addGuiElement(text2);
		savePanel.addGuiElement(new GuiButton(17, 10, 30, 70, 15, Localisation.getString("menu.yes")));
		savePanel.addGuiElement(new GuiButton(18, 120, 30, 70, 15, Localisation.getString("menu.no")));
		savePanel.setVisible(false);
		guiElements.add(savePanel);
    }
	
	public void actionPerfomed(GuiElement element, int mouseButton) {
		String macrosText = "";
		if(mouseButton == 0) {
			switch (element.id)
	        {
				case 1:
					GuiPanel editPanel = screenKeyBinding.editPanel;
					macrosText = ((GuiField)editPanel.guiElements.get(1)).text;
					MacrosSettings.setParam("key_" + screenKeyBinding.selectedKey.id, macrosText);
					editPanel.setVisible(false);
					screenKeyBinding.selectedKey.checkHasCode();
					screenKeyBinding.selectedKey.selected = false;
					screenKeyBinding.selectedKey = null;
					break;
				case 2:
					setCategory((GuiScreenElement) screenEditor, (GuiButton) getElementById(7));
					break;
				case 5:
					setCategory((GuiScreenElement) screenKeyBinding, (GuiButton) element);
					break;
				case 7:
					setCategory((GuiScreenElement) screenEditor, (GuiButton) element);
					break;
				case 8:
					break;
				case 9:
					break;
				case 10:
					screenEditor.selectFile((GuiButtonWithIcon) element);
					break;
				case 12:
					screenEditor.deleteFile();
					break;
				case 13:
					screenEditor.createFilePart1();
					break;
				case 14:
					screenEditor.cancelCreateFile();
					break;
				case 15:
					screenEditor.createFilePart2();
					break;
				case 16:
					screenEditor.saveFile();
					break;
				case 17:
					this.selectedCategory.saveClose(true, this.actionId);
					saveClose();
					break;
				case 18:
					this.selectedCategory.saveClose(false, this.actionId);
					saveClose();
					break;
			}
		}
    }
	
	public void actionReleased(GuiElement element) {
		switch (element.id)
        {
			case 11:
				screenEditor.renameFile();
				break;
        }
	}
	
	public void setCategory(GuiScreenElement category, GuiButton button) {
		if(this.selectedCategory.switchScreen()) {
			if(this.selectedCategory != null) this.selectedCategory.visible = false;
			if(this.buttonOfSelectedCategory != null) this.buttonOfSelectedCategory.selected = false;
			
			category.visible = true;
			this.selectedCategory = category;
			if(button != null) button.selected = true;
			this.buttonOfSelectedCategory = button;
		}
		else {
			tempCategory = category;
			tempButtonOfCategory = button;
		}
	}
	
	public void saveOpen(int actionId) {
		this.actionId = actionId;
		this.savePanel.setVisible(true);
	}
	
	public void saveClose() {
		this.savePanel.setVisible(false);
		if(this.actionId == 0) {
			mc.displayGuiScreen((GuiScreen)null);
		}
		else if(this.actionId == 1) {
			setCategory(tempCategory, tempButtonOfCategory);
		}
	}
	
	protected void keyTyped(char key, int keyId)
    {
		for(GuiElement element : guiElements) element.keyTyped(key, keyId);
		if (keyId == 1)
        {
			if(this.selectedCategory.closeMenu()) {
				mc.displayGuiScreen((GuiScreen)null);
			}
        }
    }
	
	public GuiElement getElementById(int id) {
		for(GuiElement element : guiElements) if(element.id == id) return element;
		return null;
	}
	
	public void updateScreen() {
		super.updateScreen();
		for(GuiElement element : guiElements) element.update();
	}
	
	public void drawScreen(int positionX, int positionY, float partialTicks)
    {
		super.drawScreen(positionX, positionY, partialTicks);
		
		this.mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);

		RenderUtil.setColor(Color.color_3, 0.4F);
		RenderUtil.drawFromAtlas(5, 30, 1, 1);
		RenderUtil.drawFromAtlas(this.width-25, 30, 3, 1);
		RenderUtil.drawFromAtlas(5, this.height-25, 1, 3);
		RenderUtil.drawFromAtlas(this.width-25, this.height-25, 3, 3);
		
		RenderUtil.drawFromAtlas(25, 30, 2, 1, this.width-50, 20);
		RenderUtil.drawFromAtlas(this.width-25, 50, 3, 2, 20, this.height-75);
		RenderUtil.drawFromAtlas(5, 50, 1, 2, 20, this.height-75);
		RenderUtil.drawFromAtlas(25, this.height-25, 2, 3, this.width-50, 20);
		
		RenderUtil.drawFromAtlas(25, 50, 2, 2, this.width-50, this.height-75);
		
		for(GuiElement element : guiElements) element.draw(mc, positionX, positionY);
    }
	
	public boolean CheckPressElements(List<GuiElement> elements, int posX, int posY, int mouseButton) {
		try {
			if(savePanel.visible) {
				if(savePanel.clicked(mouseButton, posX, posY, this)) return true;
				else return false;
			}
			else {
				for(GuiElement element : elements) {
					if(mouseButton == 0 && element.clicked(mouseButton, posX, posY, this)) {
						if(!(element instanceof GuiScreenKeyBinding) && !(element instanceof GuiScreenEditor) && !(element instanceof GuiPanel)) {
							selected = element;
							pressed = element;
						}
		    			return true;
		    		}
		    	}
			}
		} catch(Exception e) {}
		return false;
	}
}
