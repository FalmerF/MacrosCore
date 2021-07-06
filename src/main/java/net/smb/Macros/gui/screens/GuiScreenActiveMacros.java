package net.smb.Macros.gui.screens;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.smb.Macros.CodeParser;
import net.smb.Macros.gui.elements.GuiButtonWithIcon;
import net.smb.Macros.gui.elements.GuiElement;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.RenderUtil;

public class GuiScreenActiveMacros extends GuiScreenHeader {
	public GuiScreenActiveMacros(Minecraft mc) {
		super(mc);
	}
	
	public void initGui()
    {
		super.initGui();
		for(int i = 0; i < 100; i++) {
			GuiButtonWithIcon button = new GuiButtonWithIcon(i, 0, 0, 10, 4);
			button.visible = false;
			button.iconScale = 6;
			guiElements.add(button);
		}
		
    }
	
	public void actionPerfomed(GuiElement element, int mouseButton) {
		if(mouseButton == 0) {
			if(element.id < CodeParser.activeParsers.size()) {
				CodeParser parser = CodeParser.activeParsers.get(element.id);
				parser.setError();
			}
		}
    }
	
	protected void keyTyped(char key, int keyId)
    {
		for(GuiElement element : guiElements) element.keyTyped(key, keyId);
		if (keyId == 1)
        {
			mc.displayGuiScreen((GuiScreen)null);
        }
    }
	
	public GuiElement getElementById(int id) {
		for(GuiElement element : guiElements) if(element.id == id) return element;
		return null;
	}
	
	public void updateScreen() {
		super.updateScreen();
		for(GuiElement element : guiElements) element.update();
	}
	
	public void drawScreen(int positionX, int positionY, float partialTicks)
    {
		int elementsInColumn = (this.height-10)/10;
		long currentMillis = System.currentTimeMillis();
		int i = 0;
		for(; i < CodeParser.activeParsers.size(); i++) {
			if(i == 100) break;
			CodeParser parser = CodeParser.activeParsers.get(i);
			long activeTime = currentMillis-parser.start;
			String time = (int)(activeTime/60000) + ":" + (int)(activeTime/1000) + "." + (int)(activeTime%1000);
			RenderUtil.drawString("\u00A76" + parser.parserName + "\u00A7r: " + time, 13+70*(i/elementsInColumn), 10+10*(i%elementsInColumn), Color.color_4, false, 1.0F, false);
			GuiElement button = guiElements.get(i);
			button.setPos(70*(i/elementsInColumn), 10+10*(i%elementsInColumn));
			button.visible = true;
			button.draw(mc, positionX, positionY);
		}
		for(;i < 100; i++) guiElements.get(i).visible = false;
    }
	
	public boolean CheckPressElements(List<GuiElement> elements, int posX, int posY, int mouseButton) {
		try {
			for(GuiElement element : elements) {
				if(mouseButton == 0 && element.clicked(mouseButton, posX, posY, this)) {
	    			return true;
	    		}
	    	}
		} catch(Exception e) {}
		return false;
	}
}
