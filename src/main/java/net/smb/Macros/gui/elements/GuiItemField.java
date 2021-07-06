package net.smb.Macros.gui.elements;

import net.minecraft.client.Minecraft;
import net.smb.Macros.gui.LayoutManager;
import net.smb.Macros.gui.screens.GuiScreenHeader;
import net.smb.Macros.gui.screens.GuiScreenMenu;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiItemField extends GuiElement {
	public String text;
	public String key;
	public int type;
	public GuiScreenMenu currentScreen;
	
	public GuiItemField(int posX, int posY, int width, String text, String key, int type){
		super(-1, posX, posY, width, 20);
		this.text = text;
		this.key = key;
		this.type = type;
		currentScreen = (GuiScreenMenu)Minecraft.getMinecraft().currentScreen;
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
		this.hovered = positionX >= this.posX && positionX < this.posX + this.width &&
				positionY >= this.posY && positionY < this.posY + this.height;
		if(hovered && currentScreen.selectedItem != null) RenderUtil.setColor(Color.color_10);
		else RenderUtil.setColor(Color.color_7);
		RenderUtil.drawFromAtlas(this.posX, this.posY, 5, 1, this.height, this.height);
		RenderUtil.drawFromAtlas(this.posX+this.height, this.posY, 7, 1, this.width-this.height*2, this.height);
		RenderUtil.drawFromAtlas(this.posX+this.width-this.height, this.posY, 9, 1, this.height, this.height);
		
		RenderUtil.setColor(Color.color_8);
		RenderUtil.drawFromAtlas(this.posX, this.posY, 5, 3, this.height, this.height);
		RenderUtil.drawFromAtlas(this.posX+this.height, this.posY, 7, 3, this.width-this.height*2, this.height);
		RenderUtil.drawFromAtlas(this.posX+this.width-this.height, this.posY, 9, 3, this.height, this.height);
		
		RenderUtil.drawString(text, this.posX+5, this.posY+5);
	}
	
	public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
    	if(pressed(posX, posY)) {
    		if(gui != null) gui.actionPerfomed(this, id);
    		setSelected(true);
    		this.pressed = true;
    		if(currentScreen.selectedItem != null) {
    			text = currentScreen.selectedName;
    			
    			if(this.type == 0) {
    				LayoutManager.bindedLayouts.put(key, LayoutManager.getlayout(text));
    				LayoutManager.saveToXML();
    			}
    		}
    		return true;
    	}
    	else return false;
    }
}
