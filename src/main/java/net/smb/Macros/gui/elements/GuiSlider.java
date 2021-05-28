package net.smb.Macros.gui.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.smb.Macros.gui.screens.GuiScreenHeader;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiSlider extends GuiElement {
	public float value = 0.0F;
	public int steps = 1;
	public boolean horizontal;
	
	public GuiSlider(int id, int posX, int posY, int width, int height, float value, int steps) {
		super(id, posX, posY, width, height);
		this.value = value;
		this.steps = steps;
		this.horizontal = false;
	}
	
	public GuiSlider(int id, int posX, int posY, int width, int height, float value, int steps, boolean horizontal) {
		super(id, posX, posY, width, height);
		this.value = value;
		this.steps = steps;
		this.horizontal = horizontal;
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			this.hovered = positionX >= this.posX && positionY >= this.posY && positionX < this.posX + this.width && positionY < this.posY + this.height;
			
			if(this.horizontal) {
				RenderUtil.setColor(Color.color_7);
				mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
				RenderUtil.drawFromAtlas(this.posX, this.posY, 5, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.width-this.height, this.posY, 9, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.height, this.posY, 7, 1, this.width-this.height*2, this.height);
				
				if(!this.hovered && !this.pressed) RenderUtil.setColor(Color.color_5);
				else RenderUtil.setColor(Color.color_8);
				if(pressed) {
					value = Math.max(Math.min((float)((positionX-this.posX)/(this.width-13.0F)), 1.0F), 0.0F);
					value = ((int)(value/(1.0F/steps)))*(1.0F/steps);
				}
				int handlePosX = (int)(this.posX+(this.width-25)*value);
				RenderUtil.drawFromAtlas(handlePosX, this.posY, 5, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(handlePosX+25-this.height, this.posY, 9, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(handlePosX+this.height, this.posY, 7, 1, 25-this.height*2, this.height);
			} 
			else {
				GL11.glPushMatrix();
				GL11.glRotated(0, 1, 0, 0);
				RenderUtil.setColor(Color.color_7);
				mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
				RenderUtil.drawFromAtlas(this.posX, this.posY, 5, 1, this.width, this.width);
				RenderUtil.drawFromAtlas(this.posX, this.posY+this.height-this.width, 9, 1, this.width, this.width);
				RenderUtil.drawFromAtlas(this.posX, this.posY+this.width, 7, 1, this.width, this.height-this.width*2);
				
				if(!this.hovered && !this.pressed) RenderUtil.setColor(Color.color_5);
				else RenderUtil.setColor(Color.color_8);
				if(pressed) {
					value = Math.max(Math.min((float)((positionY-this.posY)/(this.height-13.0F)), 1.0F), 0.0F);
					value = ((int)(value/(1.0F/steps)))*(1.0F/steps);
				}
				int handlePosY = (int)(this.posY+(this.height-25)*value);
				RenderUtil.drawFromAtlas(this.posX, handlePosY, 5, 1, this.width, this.width);
				RenderUtil.drawFromAtlas(this.posX, handlePosY+25-this.width, 9, 1, this.width, this.width);
				RenderUtil.drawFromAtlas(this.posX, handlePosY+this.width, 7, 1, this.width, 25-this.width*2);
				GL11.glPopMatrix();
			}
		}
	}
	
	 public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
    	if(pressed(posX, posY)) {
    		if(gui != null) { 
    			gui.actionPerfomed(this, id);
    			gui.pressed = this;
    		}
    		this.pressed = true;
    		return true;
    	}
    	else return false;
    }
	 
	 public void addStep(int multiplier) {
		 value = Math.max(Math.min((float)(value+((1.0F/steps)*multiplier)), 1.0F), 0.0F);
	 }
}
