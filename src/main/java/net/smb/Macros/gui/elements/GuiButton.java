package net.smb.Macros.gui.elements;

import net.minecraft.client.Minecraft;
import net.smb.Macros.gui.screens.GuiScreenHeader;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiButton extends GuiElement {
	public String displayString;
	public int renderType = 1;
	
	public GuiButton(int id, int posX, int posY, int width, int height, String displayString) {
		super(id, posX, posY, width, height);
		this.displayString = displayString;
	}
	
	public GuiButton(int id, int posX, int posY, int width, int height, String displayString, int renderType) {
		super(id, posX, posY, width, height);
		this.displayString = displayString;
		this.renderType = renderType;
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
			
			if(!this.hovered && !this.selected) RenderUtil.setColor(Color.color_1);
			else RenderUtil.setColor(Color.color_5);
			
			mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
			if(renderType == 1) {
				RenderUtil.drawFromAtlas(this.posX, this.posY, 5, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.width-this.height, this.posY, 9, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.height, this.posY, 7, 1, this.width-this.height*2, this.height);
			} else if(renderType == 2) {
				RenderUtil.drawFromAtlas(this.posX, this.posY, 7, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.width-this.height, this.posY, 7, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.height, this.posY, 7, 1, this.width-this.height*2, this.height);
			}
			else if(renderType == 3) {
				RenderUtil.drawFromAtlas(this.posX, this.posY, 7, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.width-this.height, this.posY, 9, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.height, this.posY, 7, 1, this.width-this.height*2, this.height);
			}
			else if(renderType == 4) {
				RenderUtil.drawFromAtlas(this.posX, this.posY, 5, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.width-this.height, this.posY, 7, 1, this.height, this.height);
				RenderUtil.drawFromAtlas(this.posX+this.height, this.posY, 7, 1, this.width-this.height*2, this.height);
			}
			
			RenderUtil.drawString(displayString, this.posX+this.width/2, this.posY+this.height/2, Color.color_4, false, 1.0F, true);
		}
	}
	
	 public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
	    	if(pressed(posX, posY)) {
	    		if(gui != null) gui.actionPerfomed(this, id);
	    		this.pressed = true;
	    		return true;
	    	}
	    	else return false;
    }
	 
 	public boolean pressed(int positionX, int positionY)
    {
    	if(this.enabled && this.visible) {
    		if(RenderUtil.scissor && positionX >= this.posX && positionX < this.posX + this.width &&
					positionY >= this.posY && positionY < this.posY + this.height && 
					positionX >= RenderUtil.sX && positionX < RenderUtil.sX+RenderUtil.sW && 
					positionY >= RenderUtil.sY && positionY < RenderUtil.sY+RenderUtil.sH) return true;
			else if(!RenderUtil.scissor && positionX >= this.posX && positionX < this.posX + this.width &&
					positionY >= this.posY && positionY < this.posY + this.height) return true;
    		return false;
    	}
        return false;
    }
	public void setSelected(boolean selected) {}
}
