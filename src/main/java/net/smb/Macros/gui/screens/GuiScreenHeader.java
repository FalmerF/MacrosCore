package net.smb.Macros.gui.screens;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.smb.Macros.Localisation;
import net.smb.Macros.Interfaces.IGuiScreenHeader;
import net.smb.Macros.gui.elements.GuiElement;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class GuiScreenHeader extends GuiScreen implements IGuiScreenHeader {
	protected Minecraft mc;
	
	protected List<GuiElement> guiElements = new ArrayList<GuiElement>();
	public GuiElement selected;
	public GuiElement pressed;
	public GuiPanel hoverPanel;
	
	public GuiScreenHeader(Minecraft mc) {
		this.mc = mc;
	}
	
	public void initGui()
    {
		guiElements.clear();
		
		Keyboard.enableRepeatEvents(true);
        mc.setIngameNotInFocus();
    }
	
	public void updateScreen() {
		super.updateScreen();
	}
	
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		mc.setIngameFocus();
	}
	
	public void actionPerfomed(GuiElement element, int mouseButton) {}
	public void doubleActionPerfomed(GuiElement element, int mouseButton) {}
	public void actionReleased(GuiElement element) {}
	
	public void drawScreen(int posX, int posY, float partialTicks)
    {
		this.drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
		RenderUtil.setColor(Color.color_2);
		RenderUtil.drawFromAtlas(5, 5, 5, 1);
		RenderUtil.drawFromAtlas(25, 5, 7, 1, this.width-50, 20);
		RenderUtil.drawFromAtlas(this.width-25, 5, 9, 1);
		
		RenderUtil.drawString(Localisation.getString("menu.title"), 9, 10);
    }
	
	protected void keyTyped(char key, int keyId)
    {
		for(GuiElement element : guiElements) element.keyTyped(key, keyId);
		if (keyId == 1)
        {
        	mc.displayGuiScreen((GuiScreen)null);
        }
    }
	
	public void mouseClicked(int posX, int posY, int mouseButton)
    {
        if (mouseButton == 0)
        {
        	if(selected != null) {
        		selected.setSelected(false);
        		selected = null;
    		}
        	CheckPressElements(this.guiElements, posX, posY, mouseButton);
        }
    }
	
	public void mouseMovedOrUp(int posX, int posY, int mouseButton)
    {
		if (mouseButton == 0)
        {
			 if(pressed != null) {
				 pressed.released(posX, posY);
				 pressed = null;
			 }
        }
    }
	
	public boolean CheckPressElements(List<GuiElement> elements, int posX, int posY, int mouseButton) {
		try {
			for(GuiElement element : elements) {
				if(mouseButton == 0 && element.clicked(mouseButton, posX, posY, this)) {
					if(!(element instanceof GuiScreenKeyBinding) && !(element instanceof GuiScreenEditor) && !(element instanceof GuiPanel)) {
						selected = element;
						pressed = element;
					}
	    			return true;
	    		}
	    	}
		} catch(Exception e) {}
		return false;
	}
	
	public void handleInput()
    {
        if (Mouse.isCreated())
        {
            while (Mouse.next())
            {
                this.handleMouseInput();
            }
        }

        if (Keyboard.isCreated())
        {
            while (Keyboard.next())
            {
                this.handleKeyboardInput();
            }
        }
        super.handleInput();
    }
	
	public void handleMouseInput()
    {
        int var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int var3 = Mouse.getEventButton();
        int var4 = Math.max(Math.min(Mouse.getEventDWheel(),1),-1);

        if (var3 != -1)
        {
            this.mouseMovedOrUp(var1, var2, var3);
        }
        if (Mouse.getEventButtonState())
        {
            this.mouseClicked(var1, var2, var3);
        }
        
        
        if(var4 != 0) {
        	for(GuiElement element : guiElements) element.mouseScroll(var4);
        }
    }
    
    public void handleKeyboardInput()
    {
        if (Keyboard.getEventKeyState())
        {
            this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }

        this.mc.func_152348_aa();
    }

}
