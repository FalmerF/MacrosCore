package net.smb.Macros.gui.screens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.smb.Macros.Localisation;
import net.smb.Macros.MacrosSettings;
import net.smb.Macros.gui.Layout;
import net.smb.Macros.gui.LayoutManager;
import net.smb.Macros.gui.elements.GuiButton;
import net.smb.Macros.gui.elements.GuiButtonWithIcon;
import net.smb.Macros.gui.elements.GuiEditor;
import net.smb.Macros.gui.elements.GuiElement;
import net.smb.Macros.gui.elements.GuiField;
import net.smb.Macros.gui.elements.GuiItemField;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.gui.elements.GuiScrollView;
import net.smb.Macros.gui.elements.GuiText;
import net.smb.Macros.util.Color;

public class GuiScreenGuiSelect extends GuiScreenElement {
	public List<GuiElement> guiElements = new ArrayList<GuiElement>();
	public GuiField fileNameField, createFileField;
	public GuiButtonWithIcon selectedFileButton, deleteButton, tempButton;
	public GuiScrollView scrollView;
	public GuiPanel createFilePanel, filesPanel, editorPanel;
	public GuiScreenMenu currentScreen;
	
	private boolean colorAnim;
	private float saveColorA = 0.0F;
	
	GuiScreenGuiSelect(int posX, int posY) {
		super(-1, posX, posY, 0, 0);
		
		currentScreen = (GuiScreenMenu)Minecraft.getMinecraft().currentScreen;
		
		filesPanel = new GuiPanel(-1, posX, posY, 154, currentScreen.height-this.posY-5);
		scrollView = new GuiScrollView(-1, 2, 30, 150, filesPanel.height-32);
		scrollView.elementHeight = 20;
		updateFiles();
		
		filesPanel.addGuiElement(scrollView);
		filesPanel.addGuiElement(new GuiButtonWithIcon(20, 5, 5, 20, 3));
		fileNameField = new GuiField(28, 5, filesPanel.width-56, 1.0F, "", Localisation.getString("field.description.name"));
		fileNameField.id = 25;
		fileNameField.visible = false;
		filesPanel.addGuiElement(fileNameField);
		deleteButton = new GuiButtonWithIcon(21, filesPanel.width-25, 5, 20, 4);
		deleteButton.visible = false;
		filesPanel.addGuiElement(deleteButton);
		guiElements.add(filesPanel);
		
		createFilePanel = new GuiPanel(-1, currentScreen.width/2-100, currentScreen.height/2-40, 200, 80);
		GuiText text = new GuiText(100, 10, 1.0F, Localisation.getString("menu.editor.createfile"));
		text.centered = true;
		createFilePanel.addGuiElement(text);
		createFileField = new GuiField(10, 30, 180, 1.0F, "", Localisation.getString("field.description.file"));
		createFilePanel.addGuiElement(createFileField);
		createFilePanel.addGuiElement(new GuiButton(23, 10, 60, 70, 15, Localisation.getString("menu.create")));
		createFilePanel.addGuiElement(new GuiButton(24, 120, 60, 70, 15, Localisation.getString("menu.cancel")));
		createFilePanel.setVisible(false);
		guiElements.add(createFilePanel);
		
		editorPanel = new GuiPanel(-1, posX+159, posY, currentScreen.width-169, currentScreen.height-this.posY-5);
		text = new GuiText(10, 10, 1.0F, Localisation.getString("menu.guiselect.guide"));
		text.width = currentScreen.width-179;
		editorPanel.addGuiElement(text);
		
		editorPanel.addGuiElement(new GuiText(15, 60, 1.0F, Localisation.getString("menu.guiselect.slot")));
		editorPanel.addGuiElement(new GuiText(70, 60, 1.0F, Localisation.getString("menu.guiselect.layout")));
		
		editorPanel.addGuiElement(new GuiText(15, 80, 1.0F, "ingame"));
		editorPanel.addGuiElement(new GuiText(15, 110, 1.0F, "inchat"));
		editorPanel.addGuiElement(new GuiText(15, 140, 1.0F, "ininventory"));
		
		String ingame = LayoutManager.bindedLayouts.get("ingame") == null ? "none" : LayoutManager.bindedLayouts.get("ingame").getLayoutName();
		String inchat = LayoutManager.bindedLayouts.get("inchat") == null ? "none" : LayoutManager.bindedLayouts.get("inchat").getLayoutName();
		String ininventory = LayoutManager.bindedLayouts.get("ininventory") == null ? "none" : LayoutManager.bindedLayouts.get("ininventory").getLayoutName();
		
		editorPanel.addGuiElement(new GuiItemField(70, 75, 100, ingame, "ingame", 0));
		editorPanel.addGuiElement(new GuiItemField(70, 105, 100, inchat, "inchat", 0));
		editorPanel.addGuiElement(new GuiItemField(70, 135, 100, ininventory, "ininventory", 0));
		
		guiElements.add(editorPanel);
	}
	
	public void updateFiles() {
		scrollView.guiElements.clear();
		for(Layout layout : LayoutManager.layouts) {
			scrollView.addGuiElement(new GuiButtonWithIcon(22, 5, scrollView.guiElements.size()*17+2, scrollView.width-15, 15, 2, layout.getLayoutName(), true, true));
		}
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
	
	public void mouseScroll(int scroll) {
		for(GuiElement element : guiElements) element.mouseScroll(scroll);
	}
	
	public void keyTyped(char key, int keyId) {
		for(GuiElement element : guiElements) element.keyTyped(key, keyId);
	}
	
	public void selectFile(GuiButtonWithIcon button) {
		if(selectedFileButton != null) selectedFileButton.selected = false;
		selectedFileButton = button;
		
		if(selectedFileButton == null) {
			fileNameField.visible = false;
			deleteButton.visible = false;
		}
		else {
			fileNameField.text = selectedFileButton.displayString;
			fileNameField.visible = true;
			deleteButton.visible = true;
			selectedFileButton.selected = true;
		}
	}
	
	public void renameGui() {
		if(fileNameField.text.equals("")) {
			fileNameField.text = selectedFileButton.displayString;
		}
		else {
			if(!selectedFileButton.displayString.equals(fileNameField.text) && !fileNameField.text.equals("")) {
				String name = LayoutManager.renameLayout(selectedFileButton.displayString, fileNameField.text);
				LayoutManager.saveToXML();
				selectedFileButton.displayString = name;
				fileNameField.text = name;
			}
		}
	}
	
	public void deleteFile() {
		LayoutManager.layouts.remove(LayoutManager.getlayout(selectedFileButton.displayString));
		LayoutManager.saveToXML();
		updateFiles();
		selectFile(null);
	}
	
	public void createGuiPart1() {
		createFileField.text = "";
		filesPanel.setVisible(false);
		editorPanel.setVisible(false);
		createFilePanel.setVisible(true);
	}
	
	public void createGuiPart2() {
		if(!createFileField.text.equals("")) {
			String name = LayoutManager.createLayout(createFileField.text);
			LayoutManager.saveToXML();
			GuiButtonWithIcon button = new GuiButtonWithIcon(22, 5, scrollView.guiElements.size()*17+2, scrollView.width-15, 15, 2, name, true, true);
			scrollView.addGuiElement(button);
			scrollView.slider.steps = Math.max(scrollView.guiElements.size()-15, 1);
			selectFile(button);
			filesPanel.setVisible(true);
			editorPanel.setVisible(true);
			createFilePanel.setVisible(false);
		}
	}
	
	public void cancelCreateGui() {
		filesPanel.setVisible(true);
		editorPanel.setVisible(true);
		createFilePanel.setVisible(false);
	}
	
	public void openLayoutEdit(GuiButtonWithIcon element) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiScreenGuiEditor(Minecraft.getMinecraft(), LayoutManager.getlayout(element.displayString), currentScreen));
	}
	
	public boolean closeMenu() {
		return true;
	}
	public boolean switchScreen() {
		selectFile(null);
		return true;
	}
}
