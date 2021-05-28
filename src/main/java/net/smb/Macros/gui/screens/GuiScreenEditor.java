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
import net.smb.Macros.gui.elements.GuiButton;
import net.smb.Macros.gui.elements.GuiButtonWithIcon;
import net.smb.Macros.gui.elements.GuiEditor;
import net.smb.Macros.gui.elements.GuiElement;
import net.smb.Macros.gui.elements.GuiField;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.gui.elements.GuiScrollView;
import net.smb.Macros.gui.elements.GuiText;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.GuiUtil;
import net.smb.Macros.util.Log;

public class GuiScreenEditor extends GuiScreenElement {
	public List<GuiElement> guiElements = new ArrayList<GuiElement>();
	public GuiField fileNameField, createFileField;
	public GuiButtonWithIcon selectedFileButton, deleteButton, saveButton, tempButton;
	public GuiScrollView scrollView;
	public GuiPanel createFilePanel, filesPanel, editorPanel;
	public GuiEditor editor;
	public GuiText saveText;
	public GuiScreenMenu currentScreen;
	
	private boolean colorAnim;
	private float saveColorA = 0.0F;
	
	GuiScreenEditor(int posX, int posY) {
		super(-1, posX, posY, 0, 0);
		
		currentScreen = (GuiScreenMenu)Minecraft.getMinecraft().currentScreen;
		
		filesPanel = new GuiPanel(-1, posX, posY, 154, 299);
		scrollView = new GuiScrollView(-1, 2, 30, 150, 267);
		scrollView.elementHeight = 20;
		updateFiles();
		
		filesPanel.addGuiElement(scrollView);
		filesPanel.addGuiElement(new GuiButtonWithIcon(13, 5, 5, 20, 3));
		fileNameField = new GuiField(28, 5, filesPanel.width-56, 1.0F, "", Localisation.getString("field.description.file"));
		fileNameField.id = 11;
		fileNameField.visible = false;
		filesPanel.addGuiElement(fileNameField);
		deleteButton = new GuiButtonWithIcon(12, filesPanel.width-25, 5, 20, 4);
		deleteButton.visible = false;
		filesPanel.addGuiElement(deleteButton);
		saveButton = new GuiButtonWithIcon(16, filesPanel.width-25, filesPanel.height-25, 20, 3);
		saveButton.visible = false;
		filesPanel.addGuiElement(saveButton);
		guiElements.add(filesPanel);
		
		createFilePanel = new GuiPanel(-1, currentScreen.width/2-100, currentScreen.height/2-40, 200, 80);
		GuiText text = new GuiText(100, 10, 1.0F, Localisation.getString("menu.editor.createfile"));
		text.centered = true;
		createFilePanel.addGuiElement(text);
		createFileField = new GuiField(10, 30, 180, 1.0F, "", Localisation.getString("field.description.file"));
		createFilePanel.addGuiElement(createFileField);
		createFilePanel.addGuiElement(new GuiButton(15, 10, 60, 70, 15, Localisation.getString("menu.create")));
		createFilePanel.addGuiElement(new GuiButton(14, 120, 60, 70, 15, Localisation.getString("menu.cancel")));
		createFilePanel.setVisible(false);
		guiElements.add(createFilePanel);
		
		editorPanel = new GuiPanel(-1, posX+159, posY, currentScreen.width-169, 299);
		editor = new GuiEditor(2, 2, editorPanel.width-4, editorPanel.height-4, "", Localisation.getString("field.description.code"));
		editor.setEnabled(false);
		editorPanel.addGuiElement(editor);
		guiElements.add(editorPanel);
		
		saveText = new GuiText(posX+5, currentScreen.height-25, 1.5F, Localisation.getString("field.saved"));
		guiElements.add(saveText);
	}
	
	public void updateFiles() {
		scrollView.guiElements.clear();
		if(MacrosSettings.macrosDir.exists()) {
			for(File file : MacrosSettings.macrosDir.listFiles()) {
				if(file.getName().endsWith(".txt")) {
					String name = file.getName().substring(0, file.getName().length()-4);
					scrollView.addGuiElement(new GuiButtonWithIcon(10, 5, scrollView.guiElements.size()*17+2, scrollView.width-15, 15, 2, name));
				}
			}
		}
	}
	
	public void update() {
		if(colorAnim && saveColorA < 1.0F) {
			saveColorA += 0.14f;
		}
		else if(colorAnim && saveColorA >= 1.0F) {
			colorAnim = false;
		}
		else if(!colorAnim && saveColorA > 0) saveColorA -= 0.04f;
		
		saveText.color = new Color(Color.color_4, saveColorA);
		
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
		if(keyId == Keyboard.KEY_S && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) saveFile();
		else for(GuiElement element : guiElements) element.keyTyped(key, keyId);
	}
	
	public void selectFile(GuiButtonWithIcon button) {
		if(!editor.saved) {
			currentScreen.saveOpen(10);
			tempButton = button;
		}
		else {
			if(selectedFileButton != null) selectedFileButton.selected = false;
			selectedFileButton = button;
			
			if(selectedFileButton == null) {
				fileNameField.visible = false;
				deleteButton.visible = false;
				editor.setEnabled(false);
				scrollView.setHeight(267);
				saveButton.visible = false;
				editor.setText("");
			}
			else {
				fileNameField.text = selectedFileButton.displayString;
				fileNameField.visible = true;
				deleteButton.visible = true;
				selectedFileButton.selected = true;
				
				FileReader Fr;
				File f = new File(MacrosSettings.macrosDir, selectedFileButton.displayString + ".txt");
				editor.setText("");
				if(f.exists()) {
					try {
			 			 Fr = new FileReader(f);
			             final BufferedReader Br = new BufferedReader(Fr);
			             String line = "";
			             String text = "";
			             while ((line = Br.readLine()) != null) {
			            	 text += line + (char)10;
			             }
			             editor.setText(text);
			             Br.close();
					} catch(Exception e) {}
				}
				editor.setEnabled(true);
				
				scrollView.setHeight(237);
				saveButton.visible = true;
			}
		}	
	}
	
	public void renameFile() {
		if(!selectedFileButton.displayString.equals(fileNameField.text)) {
			File f = new File(MacrosSettings.macrosDir, selectedFileButton.displayString + ".txt");
			File newF = new File(MacrosSettings.macrosDir, fileNameField.text + ".txt");
			if(newF.exists()) {
				for(int i = 1;;i++) {
					newF = new File(MacrosSettings.macrosDir, fileNameField.text + "_" + i + ".txt");
					if(!newF.exists()) break;
				}
			}
			if(MacrosSettings.macrosDir.exists()) MacrosSettings.macrosDir.mkdir();
			f.renameTo(newF);
			selectedFileButton.displayString = newF.getName().substring(0, newF.getName().length()-4);
			fileNameField.text = selectedFileButton.displayString;
		}
	}
	
	public void deleteFile() {
		File f = new File(MacrosSettings.macrosDir, selectedFileButton.displayString + ".txt");
		f.delete();
		updateFiles();
		selectFile(null);
	}
	
	public void createFilePart1() {
		if(!editor.saved) {
			currentScreen.saveOpen(11);
		}
		else {
			createFileField.text = "";
			filesPanel.setVisible(false);
			editorPanel.setVisible(false);
			createFilePanel.setVisible(true);
		}	
	}
	
	public void createFilePart2() {
		if(!createFileField.text.equals("")) {
			File f = new File(MacrosSettings.macrosDir, createFileField.text + ".txt");
			try {
				if(f.exists()) {
					for(int i = 1;;i++) {
						f = new File(MacrosSettings.macrosDir, createFileField.text + "_" + i + ".txt");
						if(!f.exists()) break;
					}
				}
				if(MacrosSettings.macrosDir.exists()) MacrosSettings.macrosDir.mkdir();
				f.createNewFile();
			} catch(Exception e) {}
			
			String name = f.getName().substring(0, f.getName().length()-4);
			GuiButtonWithIcon button = new GuiButtonWithIcon(10, 5, scrollView.guiElements.size()*17+2, scrollView.width-15, 15, 2, name);
			scrollView.addGuiElement(button);
			scrollView.slider.steps = Math.max(scrollView.guiElements.size()-15, 1);
			selectFile(button);
			filesPanel.setVisible(true);
			editorPanel.setVisible(true);
			createFilePanel.setVisible(false);
		}
	}
	
	public void cancelCreateFile() {
		filesPanel.setVisible(true);
		editorPanel.setVisible(true);
		createFilePanel.setVisible(false);
	}
	
	public void saveFile() {
		try {
			File f = new File(MacrosSettings.macrosDir, selectedFileButton.displayString + ".txt");
			if(!f.exists()) f.createNewFile();
			FileWriter fw2 = new FileWriter(f);
	        final BufferedWriter bw2 = new BufferedWriter(fw2);
	        
			bw2.write(editor.getText());
			bw2.close();
			colorAnim = true;
			editor.saved = true;
		} catch(Exception e) {}
	}
	
	public void saveClose(boolean answer, int actionId) {
		if(answer) saveFile();
		else editor.saved = true;
		
		if(actionId == 10) {
			selectFile(tempButton);
		}
		else if(actionId == 11) {
			cancelCreateFile();
		}
	}
	
	public boolean closeMenu() {
		if(!editor.saved) {
			currentScreen.saveOpen(0);
			return false;
		}
		else return true;
	}
	public boolean switchScreen() {
		if(!editor.saved) {
			currentScreen.saveOpen(1);
			return false;
		}
		else {
			selectFile(null);
			return true;
		}
	}
}
