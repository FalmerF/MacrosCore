package net.smb.Macros.gui.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.smb.Macros.gui.screens.GuiScreenHeader;
import net.smb.Macros.gui.screens.GuiScreenMenu;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiButtonWithIcon extends GuiElement {
	public int iconIndex, type;
	public Color iconColor = Color.color_4;
	public String displayString;
	public boolean item = false;
	public boolean doubleClick = false;
	private boolean firstClick = false;
	private int clickTimer = 0;
	
	public GuiScreenMenu currentScreen;
	
	public int iconScale = 12;
	
	public GuiButtonWithIcon(int id, int posX, int posY, int scale, int iconIndex) {
		super(id, posX, posY, scale, scale);
		this.iconIndex = iconIndex;
		this.type = 1;
		try {
			currentScreen = (GuiScreenMenu)Minecraft.getMinecraft().currentScreen;
		} catch(Exception e) {}
	}
	
	public GuiButtonWithIcon(int id, int posX, int posY, int width, int height, int iconIndex, String displayString) {
		super(id, posX, posY, width, height);
		this.iconIndex = iconIndex;
		this.type = 2;
		this.displayString = displayString;
		try {
			currentScreen = (GuiScreenMenu)Minecraft.getMinecraft().currentScreen;
		} catch(Exception e) {}
	}
	
	public GuiButtonWithIcon(int id, int posX, int posY, int width, int height, int iconIndex, String displayString, boolean item, boolean doubleClick) {
		super(id, posX, posY, width, height);
		this.iconIndex = iconIndex;
		this.type = 2;
		this.displayString = displayString;
		this.item = item;
		this.doubleClick = doubleClick;
		try {
			currentScreen = (GuiScreenMenu)Minecraft.getMinecraft().currentScreen;
		} catch(Exception e) {}
	}
	
	public void update() {
		if(this.doubleClick && this.firstClick && this.clickTimer > 0) {
			clickTimer--;
		}
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			if(RenderUtil.scissor) {
				this.hovered = positionX >= this.posX && positionX < this.posX + this.width &&
							positionY >= this.posY && positionY < this.posY + this.height && 
							positionX >= RenderUtil.sX && positionX < RenderUtil.sX+RenderUtil.sW && 
							positionY >= RenderUtil.sY && positionY < RenderUtil.sY+RenderUtil.sH;
			}
			else {
				this.hovered = positionX >= this.posX && positionX < this.posX + this.width &&
						positionY >= this.posY && positionY < this.posY + this.height;
			}

			if(this.type == 1) {
				mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
				RenderUtil.setColor(Color.color_7);
				RenderUtil.drawFromAtlas(this.posX, this.posY, 11, 1, this.width, this.height);
				
				mc.getTextureManager().bindTexture(ResourceLocations.iconsAtlas);
				if(!this.hovered) RenderUtil.setColor(iconColor, 0.7F);
				else RenderUtil.setColor(iconColor);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
		    	Gui.func_152125_a(this.posX+(this.height-iconScale)/2, this.posY+(this.height-iconScale)/2, iconIndex%5, iconIndex/5, 1, 1, iconScale, iconScale, 5, 5);
		    	GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_BLEND);
			}
			else if(this.type == 2) {

				if(!this.hovered && !this.selected) RenderUtil.setColor(Color.color_1);
				else RenderUtil.setColor(Color.color_5);
				
				mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
				RenderUtil.drawFromAtlas(this.posX, this.posY, 5, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.width-this.height, this.posY, 9, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.height, this.posY, 7, 1, this.width-this.height*2, this.height);
				
				mc.getTextureManager().bindTexture(ResourceLocations.iconsAtlas);
				if(!this.hovered) RenderUtil.setColor(iconColor, 0.7F);
				else RenderUtil.setColor(iconColor);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
		    	Gui.func_152125_a(this.posX+4, this.posY+4, iconIndex%5, iconIndex/5, 1, 1, this.height-8, this.height-8, 5, 5);
		    	GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_BLEND);
				
				String displayText = displayString;
				
				if(RenderUtil.fontRenderer.getStringWidth(displayText) > this.width-this.height-7) {
					displayText = RenderUtil.fontRenderer.trimStringToWidth(displayText+"...", this.width-this.height-7) + "...";
				}
				
				RenderUtil.drawString(displayText, this.posX+this.height, this.posY+this.height/2-RenderUtil.fontRenderer.FONT_HEIGHT/2, Color.color_4, false, 1.0F, false);
			}
		}
	}
	
 	public boolean pressed(int positionX, int positionY)
    {
    	if(this.enabled && this.visible) {
    		if(RenderUtil.scissor && positionX >= this.posX && positionX < this.posX + this.width &&
					positionY >= this.posY && positionY < this.posY + this.height && 
					positionX >= RenderUtil.sX && positionX < RenderUtil.sX+RenderUtil.sW && 
					positionY >= RenderUtil.sY && positionY < RenderUtil.sY+RenderUtil.sH) {
    			return true;
    		}
			else if(!RenderUtil.scissor && positionX >= this.posX && positionX < this.posX + this.width &&
					positionY >= this.posY && positionY < this.posY + this.height) {
				return true;
			}
			else return false;
    	}
        return false;
    }
 	
 	 public void released(int positionX, int positionY) {
 		if(this.item) {
 			if(!pressed(positionX, positionY)) currentScreen.mouseClicked(positionX, positionY, 0);
 			currentScreen.selectedItem = null;
 		}
     }
 	
 	public void setSelected(boolean selected) {}
 	
 	public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
    	if(pressed(posX, posY)) {
    		if(gui != null) {
    			if(this.doubleClick) {
    				if(this.firstClick && this.clickTimer > 0) {
    					this.firstClick = false;
    					gui.doubleActionPerfomed(this, id);
    				}
    				else {
    					clickTimer = 10;
    					this.firstClick = true;
    				}
    			}
    			gui.actionPerfomed(this, id);
    			gui.pressed = this;
    			if(this.item) currentScreen.setSelectedItem(this, displayString);
    		}
    		this.pressed = true;
    		return true;
    	}
    	else return false;
 	}
 	
 	public boolean mousePressed(Minecraft mc, int posX, int posY)
    {
        return this.enabled && this.visible && posX >= this.posX && posX < this.posX + this.width &&
        		posY >= this.posY && posY < this.posY + this.height;
    }
}
