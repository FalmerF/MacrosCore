package net.smb.Macros.gui.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.smb.Macros.gui.screens.GuiScreenHeader;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiColorSelect extends GuiElement {
	public Color color, hsbColor;
	private int editingParam = 0;
	
	private int panelWidth = 170, panelHeight = 110, minSize = 20;
	private int panelX = 0;
	private int panelY = 0;
	
	private GuiField fieldR, fieldG, fieldB, fieldA;
	private GuiField selectedField;
	
	public GuiColorSelect(int id, int posX, int posY, int scale, Color color) {
		super(id, posX, posY, scale, scale);
		this.color = color;
		float[] hsb = java.awt.Color.RGBtoHSB((int)(color.colorR*255.0f), (int)(color.colorG*255.0f), (int)(color.colorB*255.0f), null);
		this.hsbColor = new Color(hsb[0], hsb[1], hsb[2]);
		
		fieldR = new GuiField(0, 0, 25, 1.0F, String.valueOf((int)(this.color.colorR*255.0F)), "R");
		fieldG = new GuiField(0, 0, 25, 1.0F, String.valueOf((int)(this.color.colorG*255.0F)), "G");
		fieldB = new GuiField(0, 0, 25, 1.0F, String.valueOf((int)(this.color.colorB*255.0F)), "B");
		fieldA = new GuiField(0, 0, 25, 1.0F, String.valueOf((int)(this.color.colorA*255.0F)), "A");
		
		fieldR.numbers = true;
		fieldG.numbers = true;
		fieldB.numbers = true;
		fieldA.numbers = true;
		
		fieldR.height = 10;
		fieldG.height = 10;
		fieldB.height = 10;
		fieldA.height = 10;
	}
	
	public void setColor() {
		float[] hsb = java.awt.Color.RGBtoHSB((int)(color.colorR*255.0f), (int)(color.colorG*255.0f), (int)(color.colorB*255.0f), null);
		this.hsbColor = new Color(hsb[0], hsb[1], hsb[2]);
	}
	
	public void update() {
		if(selectedField != null) selectedField.update();
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			// Element render
			
			this.hovered = positionX >= this.posX && positionX < this.posX + this.width && positionY >= this.posY && positionY < this.posY + this.height;
			mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
			if(this.hovered) RenderUtil.setColor(Color.color_8);
			else RenderUtil.setColor(Color.color_4);
			
			RenderUtil.drawFromAtlas(this.posX-1, this.posY-1, 11, 1, this.width+2, this.height+2);

			RenderUtil.setColor(this.color);
			RenderUtil.drawRect(this.posX, this.posY, this.posX+this.width, this.posY+this.height);
			
			// Panel Render
			if(this.selected) {
				panelX = this.posX+20;
				panelY = this.posY-40;
				
				mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
				RenderUtil.setColor(Color.color_10);
				RenderUtil.drawFromAtlas(panelX, panelY, 1, 1, minSize, minSize);
				RenderUtil.drawFromAtlas(panelWidth+panelX-minSize, panelY, 3, 1, minSize, minSize);
				RenderUtil.drawFromAtlas(panelX, panelHeight+panelY-minSize, 1, 3, minSize, minSize);
				RenderUtil.drawFromAtlas(panelWidth+panelX-minSize, panelHeight+panelY-minSize, 3, 3, minSize, minSize);
				
				RenderUtil.drawFromAtlas(panelX+minSize, panelY, 2, 1, panelWidth-minSize*2, minSize);
				RenderUtil.drawFromAtlas(panelWidth+panelX-minSize, panelY+minSize, 3, 2, minSize, panelHeight-minSize*2);
				RenderUtil.drawFromAtlas(panelX, panelY+minSize, 1, 2, minSize, panelHeight-minSize*2);
				RenderUtil.drawFromAtlas(panelX+minSize, panelHeight+panelY-minSize, 2, 3, panelWidth-minSize*2, minSize);
				
				RenderUtil.drawFromAtlas(panelX+minSize, panelY+minSize, 2, 2, panelWidth-minSize*2, panelHeight-minSize*2);
				
				int brightness = java.awt.Color.HSBtoRGB(hsbColor.colorR, hsbColor.colorG, 1.0f);
				
				mc.getTextureManager().bindTexture(ResourceLocations.colors);
				RenderUtil.setColor(Color.color_16);
				Gui.func_152125_a(panelX+5, panelY+5, 1, 1, 1, 1, 100, 100, 1, 1);
				RenderUtil.drawGradientRect(panelX+110, panelY+5, panelX+120, panelY+105, brightness, -16777216);
				RenderUtil.drawGradientRect(panelX+125, panelY+5, panelX+135, panelY+105, -1, -16777216);
				
				RenderUtil.setColor(color);
				RenderUtil.drawRect(panelX+155, panelY+95, panelX+165, panelY+105);
				
				RenderUtil.setColor(Color.color_11);
				GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
		        GL11.glLogicOp(GL11.GL_OR_REVERSE);
				RenderUtil.drawRect((int)(panelX+4+100*hsbColor.colorR), (int)(panelY+104-100*hsbColor.colorG), (int)(panelX+6+100*hsbColor.colorR), (int)(panelY+106-100*hsbColor.colorG));
				RenderUtil.drawRect(panelX+110, (int)(panelY+104-100*hsbColor.colorB), panelX+120, (int)(panelY+106-100*hsbColor.colorB));
				RenderUtil.drawRect(panelX+125, (int)(panelY+104-100*color.colorA), panelX+135, (int)(panelY+106-100*color.colorA));
				GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
				
				fieldR.draw(mc, positionX, positionY);
				fieldG.draw(mc, positionX, positionY);
				fieldB.draw(mc, positionX, positionY);
				fieldA.draw(mc, positionX, positionY);
				
				if(this.pressed) dragged(mc, positionX, positionY);
			}
		}
	}
	
	protected void dragged(Minecraft mc, int posX, int posY) {
		if(this.editingParam == 1) {
			hsbColor.colorR = Math.min(Math.max(posX-this.panelX-5, 0.0f), 100.0f)/100.0f;
			hsbColor.colorG = (100.0f - Math.min(Math.max(posY - panelY - 5, 0.0f), 100.0f)) / 100.0f;
			updateColor();
		}
		else if(this.editingParam == 2) {
			hsbColor.colorB = (100.0f -  Math.min(Math.max(posY - panelY - 5, 0.0f), 100.0f)) / 100.0f;
			updateColor();
		}
		else if(this.editingParam == 3) {
			color.colorA = (100.0f -  Math.min(Math.max(posY - panelY - 5, 0.0f), 100.0f)) / 100.0f;
			updateColor();
		}
	}
	
	public void updateColor() {
		int rgb = java.awt.Color.HSBtoRGB(hsbColor.colorR, hsbColor.colorG, hsbColor.colorB);
		color.colorR = (rgb >> 16 & 0xFF)/255.0f;
		color.colorG = (rgb >> 8 & 0xFF)/255.0f;
		color.colorB = (rgb & 0xFF)/255.0f;
		
		
		fieldR.text = String.valueOf((int)(this.color.colorR*255.0F));
		fieldG.text = String.valueOf((int)(this.color.colorG*255.0F));
		fieldB.text = String.valueOf((int)(this.color.colorB*255.0F));
		fieldA.text = String.valueOf((int)(this.color.colorA*255.0F));
	}
	
	public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
		if(selectedField != null) {
			selectedField.setSelected(false);
			selectedField = null;
		}
    	if(pressed(posX, posY) || (this.selected && isPanelClicked(posX, posY))) {
    		if(gui != null) {
    			gui.actionPerfomed(this, id);
    			gui.selected = this;
    			gui.pressed = this;
    		}
    		this.setSelected(true);
    		this.pressed = true;
    		
    		fieldR.setPos(this.posX+160, this.posY-35);
    		fieldG.setPos(this.posX+160, this.posY-20);
    		fieldB.setPos(this.posX+160, this.posY-5);
    		fieldA.setPos(this.posX+160, this.posY+10);
    		return true;
    	}
    	else return false;
	}
	
	public void released(int posX, int posY) {
    	this.pressed = false;
    	this.editingParam = 0;
    	if(selectedField != null) selectedField.released(posX, posY);
    }
	
	public void setSelected(boolean selected, int posX, int posY) {
    	if(this.selected) {
    		boolean hovered = posX >= this.posX+20 && posX < this.posX+190 && posY >= this.posY-40 && posY < this.posY+70;
    		if(!selected && !hovered) this.selected = false;
    		else this.selected = true;
    	}
    	else this.selected = selected;
    }
	
	public boolean isPanelClicked(int posX, int posY) {
		boolean hovered = posX >= this.posX+20 && posX < this.posX+190 && posY >= this.posY-40 && posY < this.posY+70;
		if(hovered) {
			if(posX >= this.posX+25 && posX < this.posX+125 && posY >= this.posY-35 && posY < this.posY+60) this.editingParam = 1;
			else if(posX >= this.posX+130 && posX < this.posX+140 && posY >= this.posY-35 && posY < this.posY+60) this.editingParam = 2;
			else if(posX >= this.posX+145 && posX < this.posX+155 && posY >= this.posY-35 && posY < this.posY+60) this.editingParam = 3;
			else if(fieldR.clicked(0, posX, posY, null)) selectedField = fieldR;
			else if(fieldG.clicked(0, posX, posY, null)) selectedField = fieldG;
			else if(fieldB.clicked(0, posX, posY, null)) selectedField = fieldB;
			else if(fieldA.clicked(0, posX, posY, null)) selectedField = fieldA;
		}
		return hovered;
	}
	
	public void keyTyped(char key, int keyId) {
		if(selectedField != null) {
			selectedField.keyTyped(key, keyId);
			
			color.colorR = Integer.parseInt("0"+fieldR.text)/255.0F;
			color.colorG = Integer.parseInt("0"+fieldG.text)/255.0F;
			color.colorB = Integer.parseInt("0"+fieldB.text)/255.0F;
			color.colorA = Integer.parseInt("0"+fieldA.text)/255.0F;
			setColor();
		}
	}
}
