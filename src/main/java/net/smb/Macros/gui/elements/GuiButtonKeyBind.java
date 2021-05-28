package net.smb.Macros.gui.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.smb.Macros.MacrosSettings;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiButtonKeyBind extends GuiElement {
	public String displayString;
	public boolean hasCode;
	
	public GuiButtonKeyBind(int id, int posX, int posY, int width, int height, String displayString) {
		super(id, posX, posY, width, height);
		this.displayString = displayString;
		checkHasCode();
	}
	
	public void checkHasCode() {
		if(!MacrosSettings.getString("key_" + this.id).equals("")) hasCode = true;
		else hasCode = false;
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			this.hovered = positionX >= this.posX && positionY >= this.posY && positionX < this.posX + this.width && positionY < this.posY + this.height;
			
			if(!this.hovered && !this.selected) RenderUtil.setColor(Color.color_1);
			else RenderUtil.setColor(Color.color_5);
			
			mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
			if(this.height == this.width) RenderUtil.drawFromAtlas(this.posX, this.posY, 11, 1, this.width, this.height);
			else if(this.width < this.height) {
				GL11.glPushMatrix();
				GL11.glRotated(0, 1, 0, 0);
				RenderUtil.drawFromAtlas(this.posX, this.posY, 5, 1, this.width, this.width);
				RenderUtil.drawFromAtlas(this.posX, this.posY+this.height-this.width, 9, 1, this.width, this.width);
				RenderUtil.drawFromAtlas(this.posX, this.posY+this.width, 7, 1, this.width, this.height-this.width*2);
				GL11.glPopMatrix();
			}
			else if(this.width < this.height*2) {
				RenderUtil.drawFromAtlas(this.posX, this.posY, 5, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.width-this.height, this.posY, 9, 1, this.height, this.height);
			}
			else {
				RenderUtil.drawFromAtlas(this.posX, this.posY, 5, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.width-this.height, this.posY, 9, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.height, this.posY, 7, 1, this.width-this.height*2, this.height);
			}
			
			Color color = Color.color_4;
			if(hasCode) color = Color.color_13;
			
			if(RenderUtil.fontRenderer.getStringWidth(displayString) >= this.width) {
				RenderUtil.drawString(displayString, this.posX+this.width/2, this.posY+this.height/2, color, false, 0.75F, true);
			}
			
			else RenderUtil.drawString(displayString, this.posX+this.width/2, this.posY+this.height/2, color, false, 1.0F, true);
		}
	}
}
