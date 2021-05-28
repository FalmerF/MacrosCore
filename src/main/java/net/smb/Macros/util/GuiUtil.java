package net.smb.Macros.util;

import java.util.List;

import net.smb.Macros.gui.elements.GuiEditor;
import net.smb.Macros.gui.elements.GuiElement;
import net.smb.Macros.gui.elements.GuiScrollView;

public class GuiUtil {
	public static void setPos(List<GuiElement> elements, int posX, int posY) {
		for(GuiElement element : elements) {
			setPos(element, posX, posY);
		}
	}
	
	public static void setPos(GuiElement element, int posX, int posY) {
		element.posX = element.relPosX + posX;
		element.posY = element.relPosY + posY;
		if(element instanceof GuiScrollView) {
			GuiScrollView scrollView = (GuiScrollView)element;
			setPos(scrollView.slider, scrollView.posX, scrollView.posY);
		}
		else if(element instanceof GuiEditor) {
			((GuiEditor)element).setPos();
		}
	}
	
	public static float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
    }
}
