package net.smb.Macros.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.smb.Macros.CodeParser;
import net.smb.Macros.Localisation;
import net.smb.Macros.MacrosSettings;
import net.smb.Macros.gui.elements.GuiButton;
import net.smb.Macros.gui.elements.GuiCheck;
import net.smb.Macros.gui.elements.GuiColorSelect;
import net.smb.Macros.gui.elements.GuiDropdown;
import net.smb.Macros.gui.elements.GuiField;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.gui.elements.GuiText;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class WidgetText extends Widget {
	public String displayString = "";
	public Color textColor = new Color(Color.color_4, 1.0F);
	public int positioning = 0;
	
	private GuiText textElement;

	public WidgetText(String name) {
		super(name);
		displayString = "Text";
		
		this.editPanel = new GuiPanel(-1, 0, 0, 150, 160);
		editPanel.canMove = true;
		editPanel.shadow = true;
		
		GuiText text = new GuiText(75, 10, 1.0F, Localisation.getString("menu.guiedit.editing") + this.widgetName);
		text.centered = true;
		editPanel.addGuiElement(text);
		
		editPanel.addGuiElement(new GuiButton(16, 15, 135, 40, 15, Localisation.getString("menu.save")));
		editPanel.addGuiElement(new GuiButton(17, 95, 135, 40, 15, Localisation.getString("menu.cancel")));
		
		editPanel.addGuiElement(new GuiText(10, 35, 1.0F, Localisation.getString("widget.text")));
		GuiField field = new GuiField(40, 33, 100, 1.0F, displayString, Localisation.getString("field.description.text"));
		field.height = 16;
		editPanel.addGuiElement(field);
		
		editPanel.addGuiElement(new GuiText(10, 102, 1.0F, Localisation.getString("widget.positioning")));
		List<String> elements = new ArrayList<String>();
		elements.add(Localisation.getString("pos1"));
		elements.add(Localisation.getString("pos2"));
		elements.add(Localisation.getString("pos3"));
		elements.add(Localisation.getString("pos4"));
		elements.add(Localisation.getString("pos5"));
		elements.add(Localisation.getString("pos6"));
		elements.add(Localisation.getString("pos7"));
		elements.add(Localisation.getString("pos8"));
		elements.add(Localisation.getString("pos9"));
		editPanel.addGuiElement(new GuiDropdown(-1, 60, 100, 80, 15, elements, positioning));
		
		editPanel.addGuiElement(new GuiText(10, 60, 1.0F, Localisation.getString("widget.backgroundcolor")));
		editPanel.addGuiElement(new GuiColorSelect(-1, 120, 60, 10, backgroundColor));
		
		editPanel.addGuiElement(new GuiText(10, 80, 1.0F, Localisation.getString("widget.textcolor")));
		editPanel.addGuiElement(new GuiColorSelect(-1, 120, 80, 10, textColor));
		
		textElement = new GuiText(0, 0, 1.0F, displayString);
		textElement.color = textColor;
	}

	public void setParams() {
		propeties.put("posX", String.valueOf(posX));
		propeties.put("posY", String.valueOf(posY));
		propeties.put("width", String.valueOf(width));
		propeties.put("height", String.valueOf(height));
		
		displayString = ((GuiField)editPanel.guiElements.get(4)).text;
		propeties.put("text", String.valueOf(displayString));
		
		//backgroundColor = ((GuiColorSelect)editPanel.guiElements.get(8)).color;
		propeties.put("backgroundcolor", backgroundColor.getHexColor());
		
		//textColor = ((GuiColorSelect)editPanel.guiElements.get(10)).color;
		propeties.put("textcolor", textColor.getHexColor());
		
		positioning = ((GuiDropdown)editPanel.guiElements.get(6)).selectedElement;
		propeties.put("positioning", String.valueOf(positioning));
	}
	
	public void updateParams() {
		try {
			this.posX = Integer.parseInt(propeties.get("posX"));
			this.posY = Integer.parseInt(propeties.get("posY"));
			this.width = Integer.parseInt(propeties.get("width"));
			this.height = Integer.parseInt(propeties.get("height"));
			this.displayString = propeties.get("text");
			((GuiField)editPanel.guiElements.get(4)).text = displayString;
			
			this.backgroundColor.setHexColor(propeties.get("backgroundcolor"));
			((GuiColorSelect)editPanel.guiElements.get(8)).setColor();
			
			this.textColor.setHexColor(propeties.get("textcolor"));
			((GuiColorSelect)editPanel.guiElements.get(10)).setColor();
			
			this.positioning = Integer.parseInt(propeties.get("positioning"));
			((GuiDropdown)editPanel.guiElements.get(6)).selectedElement = positioning;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			super.draw(mc, positionX, positionY);
			
			if(textElement != null) {
				textElement.positionedRight = false;
				textElement.centered = false;
				textElement.SetText(convertAmpCodes(CodeParser.globalParser.getString("\"" + displayString + "\"")));
				if(positioning == 0) {
					textElement.setPos(this.posX+2, this.posY+2);
				}
				else if(positioning == 1) {
					textElement.setPos(this.posX+2, this.posY+this.height/2-RenderUtil.fontRenderer.FONT_HEIGHT/2);
				}
				else if(positioning == 2) {
					textElement.setPos(this.posX+2, this.posY+this.height-RenderUtil.fontRenderer.FONT_HEIGHT);
				}
				
				else if(positioning == 3) {
					textElement.centered = true;
					textElement.setPos(this.posX+this.width/2, this.posY+2);
				}
				else if(positioning == 4) {
					textElement.centered = true;
					textElement.setPos(this.posX+this.width/2, this.posY+this.height/2-RenderUtil.fontRenderer.FONT_HEIGHT/2);
				}
				else if(positioning == 5) {
					textElement.centered = true;
					textElement.setPos(this.posX+this.width/2, this.posY+this.height-RenderUtil.fontRenderer.FONT_HEIGHT);
				}
				
				else if(positioning == 6) {
					textElement.positionedRight = true;
					textElement.setPos(this.posX+this.width-1, this.posY+2);
				}
				else if(positioning == 7) {
					textElement.positionedRight = true;
					textElement.setPos(this.posX+this.width-1, this.posY+this.height/2-RenderUtil.fontRenderer.FONT_HEIGHT/2);
				}
				else if(positioning == 8) {
					textElement.positionedRight = true;
					textElement.setPos(this.posX+this.width-1, this.posY+this.height-RenderUtil.fontRenderer.FONT_HEIGHT);
				}
				textElement.draw(mc, positionX, positionY);
			}
		}
	}
	
	public static String convertAmpCodes(String text) {
        return text.replaceAll("(?<!&)&([0-9a-fklmnor])", "\u00A7$1").replaceAll("&&", "&").replaceAll("\\\\n", "\n");
    }
}
