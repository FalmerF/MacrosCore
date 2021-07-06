package net.smb.Macros.gui.elements;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.smb.Macros.Localisation;
import net.smb.Macros.gui.screens.GuiScreenHeader;
import net.smb.Macros.gui.screens.GuiScreenMenu;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.GuiUtil;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiPanel extends GuiElement {
	public List<GuiElement> guiElements = new ArrayList<GuiElement>();
	public boolean canMove, hover = false, shadow = false;
	public int minSize = 20;
	private boolean moving;
	private int relMouseX, relMouseY;
	private GuiScreenHeader currentScreen;
	
	public GuiPanel(int id, int posX, int posY, int width, int height) {
		super(id, posX, posY, width, height);
		GuiUtil.setPos(guiElements, posX, posY);
		currentScreen = (GuiScreenHeader)Minecraft.getMinecraft().currentScreen;
		shadow = false;
	}
	
	public void addGuiElement(GuiElement element) {
		guiElements.add(element);
		GuiUtil.setPos(guiElements, posX, posY);
	}
	
	public void update() {
		for(GuiElement element : guiElements) element.update();
	}
	
	public void limitPos() {
		if(currentScreen == null) currentScreen = (GuiScreenHeader)Minecraft.getMinecraft().currentScreen;
		this.posX = Math.max(Math.min(this.posX, currentScreen.width-this.width), 0);
		this.posY = Math.max(Math.min(this.posY, currentScreen.height-this.height), 0);
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			if(this.moving && this.canMove) {
				this.posX = positionX - relMouseX;
				this.posY = positionY - relMouseY;
				
				this.posX -= this.posX%5;
				this.posY -= this.posY%5;
				
				limitPos();
				GuiUtil.setPos(guiElements, posX, posY);
			}
			
			mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
			if(this.shadow) {
				int shadowX = this.posX - minSize/2;
				int shadowY = this.posY - minSize/2;
				int shadowWidth = this.width + minSize;
				int shadowHeight = this.height + minSize;
				RenderUtil.setColor(Color.color_2);
				
				
				RenderUtil.drawFromAtlas(shadowX, shadowY, 1, 5, minSize, minSize);
				RenderUtil.drawFromAtlas(shadowWidth+shadowX-minSize, shadowY, 3, 5, minSize, minSize);
				RenderUtil.drawFromAtlas(shadowX, shadowHeight+shadowY-minSize, 1, 7, minSize, minSize);
				RenderUtil.drawFromAtlas(shadowWidth+shadowX-minSize, shadowHeight+shadowY-minSize, 3, 7, minSize, minSize);
				
				RenderUtil.drawFromAtlas(shadowX+minSize, shadowY, 2, 5, shadowWidth-minSize*2, minSize);
				RenderUtil.drawFromAtlas(shadowWidth+shadowX-minSize, shadowY+minSize, 3, 6, minSize, shadowHeight-minSize*2);
				RenderUtil.drawFromAtlas(shadowX, shadowY+minSize, 1, 6, minSize, shadowHeight-minSize*2);
				RenderUtil.drawFromAtlas(shadowX+minSize, shadowHeight+shadowY-minSize, 2, 7, shadowWidth-minSize*2, minSize);
				
				RenderUtil.drawFromAtlas(shadowX+minSize, shadowY+minSize, 2, 6, shadowWidth-minSize*2, shadowHeight-minSize*2);
			}
			else {
				RenderUtil.setColor(Color.color_2);
				RenderUtil.drawFromAtlas(this.posX, this.posY, 1, 1, minSize, minSize);
				RenderUtil.drawFromAtlas(this.width+this.posX-minSize, this.posY, 3, 1, minSize, minSize);
				RenderUtil.drawFromAtlas(this.posX, this.height+this.posY-minSize, 1, 3, minSize, minSize);
				RenderUtil.drawFromAtlas(this.width+this.posX-minSize, this.height+this.posY-minSize, 3, 3, minSize, minSize);
				
				RenderUtil.drawFromAtlas(this.posX+minSize, this.posY, 2, 1, this.width-minSize*2, minSize);
				RenderUtil.drawFromAtlas(this.width+this.posX-minSize, this.posY+minSize, 3, 2, minSize, this.height-minSize*2);
				RenderUtil.drawFromAtlas(this.posX, this.posY+minSize, 1, 2, minSize, this.height-minSize*2);
				RenderUtil.drawFromAtlas(this.posX+minSize, this.height+this.posY-minSize, 2, 3, this.width-minSize*2, minSize);
				
				RenderUtil.drawFromAtlas(this.posX+minSize, this.posY+minSize, 2, 2, this.width-minSize*2, this.height-minSize*2);
			}
			for(GuiElement element : guiElements) element.draw(mc, positionX, positionY);
		}
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void keyTyped(char key, int keyId) {
		if(this.visible) for(GuiElement element : guiElements) element.keyTyped(key, keyId);
	}
	
	public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
		if(id == 0 && this.visible && CheckPressElements(guiElements, posX, posY, gui)) {
			if(hover) currentScreen.hoverPanel = this;
			return true;
		}
		else if(this.canMove && this.pressed(posX, posY)) {
			relMouseX = posX - this.posX;
			relMouseY = posY - this.posY;
			this.moving = true;
			if(gui != null) {
				gui.pressed = this;
				gui.selected = this;
			}
			if(hover) currentScreen.hoverPanel = this;
			return true;
		}
		else if(hover && this.pressed(posX, posY)) {
			currentScreen.hoverPanel = this;
			return true;
		}
    	else {
    		if(hover) {
    			currentScreen.hoverPanel = null;
    			this.visible = false;
    		}
    		return false;
    	}
	}
	
	public void released(int posX, int posY) {
    	this.pressed = false;
    	this.moving = false;
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
		if(this.visible) for(GuiElement element : guiElements) element.mouseScroll(scroll);
	}
}
