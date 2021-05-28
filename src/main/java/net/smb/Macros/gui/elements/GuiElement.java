package net.smb.Macros.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.smb.Macros.gui.screens.GuiScreenHeader;

public class GuiElement extends Gui {
	public int id;
    public int posX, relPosX;
    public int posY, relPosY;
	public int width;
	public int height;
    public boolean enabled;
    public boolean visible;
    public boolean hovered;
    public boolean pressed;
    public boolean selected;
    
    protected GuiElement(int id, int posX, int posY, int width, int height){
    	this.id = id;
    	this.posX = posX;
    	this.posY = posY;
    	this.relPosX = posX;
    	this.relPosY = posY;
    	this.width = width;
    	this.height = height;
    	this.enabled = true;
    	this.visible = true;
    }
    
    public int getHoverState(boolean hovered)
    {
        byte state = 1;

        if (!this.enabled)
        {
        	state = 0;
        }
        else if (hovered)
        {
        	state = 2;
        }

        return state;
    }
    
    public void draw(Minecraft mc, int positionX, int positionY) {}
    
    protected void dragged(Minecraft mc, int posX, int posY) {}
    
    public void released(int p_146118_1_, int p_146118_2_) {
    	this.pressed = false;
    }
    
    public boolean pressed(int posX, int posY)
    {
    	if(this.enabled && this.visible && posX >= this.posX && posY >= this.posY && posX < this.posX + this.width && posY < this.posY + this.height) {
    		return true;
    	}
        return false;
    }

    public boolean clicked(int id, int posX, int posY, GuiScreenHeader gui) {
    	if(pressed(posX, posY)) {
    		if(gui != null) gui.actionPerfomed(this, id);
    		setSelected(true);
    		this.pressed = true;
    		return true;
    	}
    	else return false;
    }
    public void mouseScroll(int scroll) {}
    
    public boolean isMouseOver()
    {
        return this.hovered;
    }
    
    public void setSelected(boolean selected) {
    	this.selected = selected;
    }
    
    public void keyTyped(char key, int keyId) {}
    
    public void playPressSound(SoundHandler handler)
    {
    	handler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
    }
    
    public void update() {}
}
