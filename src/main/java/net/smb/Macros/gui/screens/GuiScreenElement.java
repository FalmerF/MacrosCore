package net.smb.Macros.gui.screens;

import net.smb.Macros.gui.elements.GuiElement;

public class GuiScreenElement extends GuiElement {

	protected GuiScreenElement(int id, int posX, int posY, int width, int height) {
		super(id, posX, posY, width, height);
	}

	public boolean closeMenu() {
		return true;
	}
	
	public boolean switchScreen() {
		return true;
	}
	
	public void saveClose(boolean answer, int actionId) {}
}
