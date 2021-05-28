package net.smb.Macros.gui.elements;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.smb.Macros.Localisation;
import net.smb.Macros.gui.screens.GuiScreenHeader;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.GuiUtil;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiPanel extends GuiElement {
	public List<GuiElement> guiElements = new ArrayList<GuiElement>();
	
	public GuiPanel(int id, int posX, int posY, int width, int height) {
		super(id, posX, posY, width, height);
		GuiUtil.setPos(guiElements, posX, posY);
	}
	
	public void addGuiElement(GuiElement element) {
		guiElements.add(element);
		GuiUtil.setPos(guiElements, posX, posY);
	}
	
	public void update() {
		for(GuiElement element : guiElements) element.update();
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			RenderUtil.setColor(Color.color_2);
			RenderUtil.drawFromAtlas(this.posX, this.posY, 1, 1);
			RenderUtil.drawFromAtlas(this.width+this.posX-20, this.posY, 3, 1);
			RenderUtil.drawFromAtlas(this.posX, this.height+this.posY-20, 1, 3);
			RenderUtil.drawFromAtlas(this.width+this.posX-20, this.height+this.posY-20, 3, 3);
			
			RenderUtil.drawFromAtlas(this.posX+20, this.posY, 2, 1, this.width-40, 20);
			RenderUtil.drawFromAtlas(this.width+this.posX-20, this.posY+20, 3, 2, 20, this.height-40);
			RenderUtil.drawFromAtlas(this.posX, this.posY+20, 1, 2, 20, this.height-40);
			RenderUtil.drawFromAtlas(this.posX+20, this.height+this.posY-20, 2, 3, this.width-40, 20);
			
			RenderUtil.drawFromAtlas(this.posX+20, this.posY+20, 2, 2, this.width-40, this.height-40);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			
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
		if(id == 0 && this.visible && CheckPressElements(guiElements, posX, posY, gui)) return true;
    	else return false;
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
