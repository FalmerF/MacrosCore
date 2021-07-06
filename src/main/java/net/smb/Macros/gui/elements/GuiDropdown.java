package net.smb.Macros.gui.elements;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.smb.Macros.gui.screens.GuiScreenHeader;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiDropdown extends GuiElement {
	private List<String> elements;
	public int selectedElement;
	
	private int elementPanelHeight = 0;
	
	public GuiDropdown(int id, int posX, int posY, int width, int height, List<String> elements, int selectedElement) {
		super(id, posX, posY, width, height);
		this.elements = elements;
		this.selectedElement = selectedElement;
		elementPanelHeight = elements.size()*10;
	}
	
	public void addElement(String element) {
		elements.add(element);
		elementPanelHeight = elements.size()*10;
	}
	
	public void draw(Minecraft mc, int posX, int posY) {
		if(this.visible) {
			mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
			RenderUtil.setColor(Color.color_7);
			RenderUtil.drawFromAtlas(this.posX, this.posY, 5, 1, this.height, this.height);
			RenderUtil.drawFromAtlas(this.posX+this.width-this.height, this.posY, 9, 1, this.height, this.height);
			RenderUtil.drawFromAtlas(this.posX+this.height, this.posY, 7, 1, this.width-this.height*2, this.height);
			
			RenderUtil.drawString(elements.get(selectedElement), this.posX+2, this.posY+3);
			
			if(this.selected) {
				RenderUtil.zLevel = 10;
				RenderUtil.setColor(Color.color_7);
				RenderUtil.drawRect(this.posX, this.posY+this.height+2, this.posX+this.width, this.posY+this.height+2+elementPanelHeight);
				
				if(posX >= this.posX && posX < this.posX+this.width && posY >= this.posY+this.height+2 && posY < this.posY+this.height+2+elementPanelHeight) {
					 int hoveredElement = (posY-this.posY-this.height-2)/10;
					 RenderUtil.setColor(Color.color_10);
					 RenderUtil.drawRect(this.posX, this.posY+this.height+2+10*hoveredElement, this.posX+this.width, this.posY+this.height+12+10*hoveredElement);
				 }
				
				for(int i = 0; i < elements.size(); i++) {
					RenderUtil.drawString(elements.get(i), this.posX+2, this.posY+this.height+4+10*i);
				}
				RenderUtil.zLevel = 0;
			}
		}
	}
	
	 public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
    	if(pressed(posX, posY)) {
    		if(gui != null) {
    			gui.selected = this;
    		}
    		this.pressed = true;
    		this.setSelected(true);
    		return true;
    	}
    	else if(this.selected && pressToElement(posX, posY)) {
    		this.setSelected(false);
    		return true;
    	}
    	else {
    		this.setSelected(false);
    		return false;
    	}
    }
	 
	 public boolean pressToElement(int posX, int posY) {
		 if(posX >= this.posX && posX < this.posX+this.width && posY >= this.posY+this.height+2 && posY < this.posY+this.height+2+elementPanelHeight) {
			 selectedElement = (posY-this.posY-this.height-2)/10;
			 ((GuiScreenHeader)Minecraft.getMinecraft().currentScreen).actionPerfomed(this, 0);
			 return true;
		 }
		 return false;
	 }
	 
	 public void setSelected(boolean selected, int posX, int posY) {
	    	if(this.selected) {
	    		boolean hovered = posX >= this.posX && posX < this.posX+this.width && posY >= this.posY+this.height+2 && posY < this.posY+this.height+2+elementPanelHeight;
	    		if(!selected && !hovered) this.selected = false;
	    		else this.selected = true;
	    	}
	    	else this.selected = selected;
	    }
}
