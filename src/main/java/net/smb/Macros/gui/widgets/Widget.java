package net.smb.Macros.gui.widgets;

import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.opengl.GL11;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.minecraft.client.Minecraft;
import net.smb.Macros.gui.elements.GuiElement;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.gui.screens.GuiScreenGuiEditor;
import net.smb.Macros.gui.screens.GuiScreenHeader;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class Widget extends GuiElement {
	public Map<String, String> propeties = new TreeMap<String, String>();
	protected String widgetName;
	public int minSize = 5, minSizeO = 1;
	public GuiPanel editPanel = null;
	public Color backgroundColor = new Color(Color.color_2, 1.0F);
	
	protected boolean moving, scaleX, scaleMX, scaleY, scaleMY;
	protected int relMouseX, relMouseY;
	protected int rPosX, rPosY;
	protected boolean editing = true;
	
	public Widget(String name) {
		super(-1, 150, 150, 70, 15);
		widgetName = name;
	}
	
	public void saveToXML(Document document, Element layout) {
		Element widgetNode = document.createElement("widget");
		widgetNode.setAttribute("name", widgetName);
		setParams();
		for(Map.Entry<String, String> prop : propeties.entrySet()) {
			Element propNode = document.createElement(prop.getKey());
			propNode.setTextContent(String.valueOf(prop.getValue()));
			widgetNode.appendChild(propNode);
		}
		layout.appendChild(widgetNode);
	}
	
	public void setParams() {
		propeties.put("posX", String.valueOf(posX));
		propeties.put("posY", String.valueOf(posY));
		propeties.put("width", String.valueOf(width));
		propeties.put("height", String.valueOf(height));
	}
	
	public void updateParams() {
		try {
			this.posX = Integer.parseInt(propeties.get("posX"));
			this.posY = Integer.parseInt(propeties.get("posY"));
			this.width = Integer.parseInt(propeties.get("width"));
			this.height = Integer.parseInt(propeties.get("height"));
		} catch(Exception e) {
		}
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			this.hovered = positionX >= this.posX && positionX < this.posX + this.width &&
					positionY >= this.posY && positionY < this.posY + this.height;
			if(editing) {
				if(this.moving) {
					this.posX = positionX - relMouseX;
					this.posY = positionY - relMouseY;
					
					this.posX -= this.posX%5;
					this.posY -= this.posY%5;
					
					this.posX = Math.max(Math.min(this.posX, mc.currentScreen.width-this.width), 0);
					this.posY = Math.max(Math.min(this.posY, mc.currentScreen.height-this.height), 0);
				}
				if(scaleX) {
					this.posX = positionX - relMouseX;
					this.posX -= this.posX%5;
					this.posX = Math.max(Math.min(this.posX, rPosX-10), 0);
					this.width = rPosX-this.posX;
				}
				else if(scaleMX) {
					int x = positionX + 5;
					x -= x%5;
					x = Math.max(Math.min(x, mc.currentScreen.width), this.posX+10);
					this.width = x-this.posX;
				}
				
				if(scaleY) {
					this.posY = positionY - relMouseY;
					this.posY -= this.posY%5;
					this.posY = Math.max(Math.min(this.posY, rPosY-10), 0);
					this.height = rPosY-this.posY;
				}
				else if(scaleMY) {
					int y = positionY + 5;
					y -= y%5;
					y = Math.max(Math.min(y, mc.currentScreen.height), this.posY+10);
					this.height = y-this.posY;
				}
				
				mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
				int outlineX = this.posX-1;
				int outlineY = this.posY-1;
				int outlineWidth = this.width+2;
				int outlineHeight = this.height+2;
				
				if(this.selected) {
					RenderUtil.setColor(Color.color_8);
					RenderUtil.drawRect(outlineX, outlineY, outlineX+outlineWidth, outlineY+1);
					RenderUtil.drawRect(outlineX, outlineY+outlineHeight, outlineX+outlineWidth, outlineY+outlineHeight-1);
					RenderUtil.drawRect(outlineX, outlineY, outlineX+1, outlineY+outlineHeight);
					RenderUtil.drawRect(outlineX+outlineWidth, outlineY, outlineX+outlineWidth-1, outlineY+outlineHeight);
				}
				
				if((positionX >= this.posX && positionX < this.posX + 4 && this.hovered) || scaleX) {
					RenderUtil.setColor(Color.color_15);
					RenderUtil.drawRect(outlineX, outlineY, outlineX+1, outlineY+outlineHeight);
	    		}
	    		else if((positionX <= this.posX + this.width && positionX > this.posX + this.width - 4 && this.hovered) || scaleMX) {
	    			RenderUtil.setColor(Color.color_15);
	    			RenderUtil.drawRect(outlineX+outlineWidth, outlineY, outlineX+outlineWidth-1, outlineY+outlineHeight);
	    		}
	    		
	    		if((positionY >= this.posY && positionY < this.posY + 4 && this.hovered) || scaleY) {
	    			RenderUtil.setColor(Color.color_15);
	    			RenderUtil.drawRect(outlineX, outlineY, outlineX+outlineWidth, outlineY+1);
	    		}
	    		else if((positionY <= this.posY + this.height && positionY > this.posY + this.height - 4 && this.hovered) || scaleMY) {
	    			RenderUtil.setColor(Color.color_15);
	    			RenderUtil.drawRect(outlineX, outlineY+outlineHeight, outlineX+outlineWidth, outlineY+outlineHeight-1);
	    		}
			}
			
			RenderUtil.setColor(backgroundColor);
			mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
			RenderUtil.drawRect(this.posX, this.posY, this.posX+this.width, this.posY+this.height);
			
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);
		}
		
		editing = true;
	}
	
	public void drawButton(Minecraft mc, int positionX, int positionY)
    {
		editing = false;
    	draw(mc, positionX, positionY);
    }
	
	public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
    	if(id == 0 && pressed(posX, posY)) {
    		if(gui != null) gui.actionPerfomed(this, id);
    		setSelected(true);
    		this.pressed = true;
    		
    		relMouseX = posX - this.posX;
			relMouseY = posY - this.posY;
    		
    		if(posX >= this.posX && posX < this.posX + 4) {
    			scaleX = true;
    		}
    		else if(posX <= this.posX + this.width && posX > this.posX + this.width - 4) {
    			scaleMX = true;
    		}
    		
    		if(posY >= this.posY && posY < this.posY + 4) {
    			scaleY = true;
    		}
    		else if(posY <= this.posY + this.height && posY > this.posY + this.height - 4) {
    			scaleMY = true;
    		}
    		
			if(!scaleX && !scaleMX && !scaleY && !scaleMY) this.moving = true;
			else {
				rPosX = this.posX + this.width;
				rPosY = this.posY + this.height;
			}
			
			
    		return true;
    	}
    	else if(id == 1 && pressed(posX, posY) && editPanel != null) {
    		((GuiScreenGuiEditor)Minecraft.getMinecraft().currentScreen).showWidgetPanel(this.editPanel, this);
    		return true;
    	}
    	else return false;
    }
	
	public void released(int posX, int posY) {
    	this.pressed = false;
    	this.moving = false;
    	scaleX = false;
    	scaleMX = false;
    	scaleY = false;
    	scaleMY = false;
    }
}
