package net.smb.Macros.gui.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.smb.Macros.gui.screens.GuiScreenHeader;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiCheck extends GuiElement {
	public boolean value;
	
	public GuiCheck(int id, int posX, int posY, int scale, boolean value) {
		super(id, posX, posY, scale, scale);
		this.value = value;
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			this.hovered = positionX >= this.posX && positionX < this.posX + this.width && positionY >= this.posY && positionY < this.posY + this.height;
			mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
			if(this.hovered) {
				RenderUtil.setColor(Color.color_8);
				RenderUtil.drawFromAtlas(this.posX-1, this.posY-1, 11, 1, this.width+2, this.height+2);
			}

			RenderUtil.setColor(Color.color_10);
			RenderUtil.drawFromAtlas(this.posX, this.posY, 11, 1, this.width, this.height);
			
			if(this.value) {
				mc.getTextureManager().bindTexture(ResourceLocations.iconsAtlas);
				RenderUtil.setColor(Color.color_9);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				Gui.func_152125_a(this.posX, this.posY, 0, 0, 1, 1, this.width, this.height, 5, 5);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_BLEND);
			}
		}
	}
	
	public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
    	if(pressed(posX, posY)) {
    		this.value = !this.value;
    		if(gui != null) gui.actionPerfomed(this, id);
    		this.pressed = true;
    		return true;
    	}
    	else return false;
	}
}
