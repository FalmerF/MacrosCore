package net.smb.Macros.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.smb.Macros.CodeParser;
import net.smb.Macros.Localisation;
import net.smb.Macros.gui.elements.GuiButton;
import net.smb.Macros.gui.elements.GuiColorSelect;
import net.smb.Macros.gui.elements.GuiDropdown;
import net.smb.Macros.gui.elements.GuiField;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.gui.elements.GuiText;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.RenderUtil;

public class WidgetIndicator extends Widget {
	private Color color = new Color(Color.color_4, 1.0F);
	private String value = "0", minValue = "0", maxValue = "100";
	private int positioning = 0;

	public WidgetIndicator(String name) {
		super(name);
		displayString = "Text";
		
		this.editPanel = new GuiPanel(-1, 0, 0, 150, 220);
		editPanel.canMove = true;
		editPanel.shadow = true;
		
		GuiText text = new GuiText(75, 10, 1.0F, Localisation.getString("menu.guiedit.editing") + this.widgetName);
		text.centered = true;
		editPanel.addGuiElement(text);
		
		editPanel.addGuiElement(new GuiButton(16, 15, 200, 40, 15, Localisation.getString("menu.save")));
		editPanel.addGuiElement(new GuiButton(17, 95, 200, 40, 15, Localisation.getString("menu.cancel")));
		
		editPanel.addGuiElement(new GuiText(10, 35, 1.0F, Localisation.getString("widget.value")));
		GuiField field = new GuiField(60, 33, 80, 1.0F, value, Localisation.getString("field.description.value"));
		field.height = 16;
		editPanel.addGuiElement(field);
		
		editPanel.addGuiElement(new GuiText(10, 55, 1.0F, Localisation.getString("widget.minvalue")));
		field = new GuiField(60, 53, 80, 1.0F, minValue, Localisation.getString("field.description.value"));
		field.height = 16;
		editPanel.addGuiElement(field);
		
		editPanel.addGuiElement(new GuiText(10, 75, 1.0F, Localisation.getString("widget.maxvalue")));
		field = new GuiField(60, 73, 80, 1.0F, maxValue, Localisation.getString("field.description.value"));
		field.height = 16;
		editPanel.addGuiElement(field);
		
		editPanel.addGuiElement(new GuiText(10, 147, 1.0F, Localisation.getString("widget.positioning")));
		List<String> elements = new ArrayList<String>();
		elements.add(Localisation.getString("posIndicator1"));
		elements.add(Localisation.getString("posIndicator2"));
		elements.add(Localisation.getString("posIndicator3"));
		elements.add(Localisation.getString("posIndicator4"));
		editPanel.addGuiElement(new GuiDropdown(-1, 60, 145, 80, 15, elements, positioning));
		
		editPanel.addGuiElement(new GuiText(10, 105, 1.0F, Localisation.getString("widget.backgroundcolor")));
		editPanel.addGuiElement(new GuiColorSelect(-1, 120, 105, 10, backgroundColor));
		
		editPanel.addGuiElement(new GuiText(10, 125, 1.0F, Localisation.getString("widget.color")));
		editPanel.addGuiElement(new GuiColorSelect(-1, 120, 125, 10, color));
	}

	public void setParams() {
		propeties.put("posX", String.valueOf(posX));
		propeties.put("posY", String.valueOf(posY));
		propeties.put("width", String.valueOf(width));
		propeties.put("height", String.valueOf(height));
		
		minValue = ((GuiField)editPanel.guiElements.get(6)).text;
		propeties.put("minvalue", minValue);
		
		maxValue = ((GuiField)editPanel.guiElements.get(8)).text;
		propeties.put("maxvalue", maxValue);
		
		value = ((GuiField)editPanel.guiElements.get(4)).text;
		propeties.put("value", value);
		
		propeties.put("backgroundcolor", backgroundColor.getHexColor());
		
		propeties.put("color", color.getHexColor());
		
		positioning = ((GuiDropdown)editPanel.guiElements.get(10)).selectedElement;
		propeties.put("positioning", String.valueOf(positioning));
	}
	
	public void updateParams() {
		try {
			this.posX = Integer.parseInt(propeties.get("posX"));
			this.posY = Integer.parseInt(propeties.get("posY"));
			this.width = Integer.parseInt(propeties.get("width"));
			this.height = Integer.parseInt(propeties.get("height"));
			
			value = propeties.get("value");
			((GuiField)editPanel.guiElements.get(4)).text = String.valueOf(value);
			
			minValue = propeties.get("minvalue");
			((GuiField)editPanel.guiElements.get(6)).text = String.valueOf(minValue);
			
			maxValue = propeties.get("maxvalue");
			((GuiField)editPanel.guiElements.get(8)).text = String.valueOf(maxValue);
			
			this.backgroundColor.setHexColor(propeties.get("backgroundcolor"));
			((GuiColorSelect)editPanel.guiElements.get(12)).setColor();
			
			this.color.setHexColor(propeties.get("color"));
			((GuiColorSelect)editPanel.guiElements.get(14)).setColor();
			
			this.positioning = Integer.parseInt(propeties.get("positioning"));
			((GuiDropdown)editPanel.guiElements.get(10)).selectedElement = positioning;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			super.draw(mc, positionX, positionY);
			
			int val = CodeParser.globalParser.getInt(value);
			int intMinVal = CodeParser.globalParser.getInt(minValue);
			int intMaxVal = CodeParser.globalParser.getInt(maxValue);
			
			val = Math.max(Math.min(val, intMaxVal), intMinVal);
			
			if(positioning == 0) {
				RenderUtil.setColor(color);
				RenderUtil.drawRect(this.posX+2, this.posY+2, Math.max(this.posX-2+(int)(this.width*((float)(val-intMinVal)/(intMaxVal-intMinVal))), this.posX+2), this.posY-2+this.height);
			}
			else if(positioning == 1) {
				RenderUtil.setColor(color);
				RenderUtil.drawRect(Math.min(this.posX+2+this.width-(int)(this.width*((float)(val-intMinVal)/(intMaxVal-intMinVal))), this.posX-2+this.width), this.posY+2, this.posX-2+this.width, this.posY-2+this.height);
			}
			else if(positioning == 2) {
				RenderUtil.setColor(color);
				RenderUtil.drawRect(this.posX+2, this.posY+2, this.posX-2+this.width, Math.max(this.posY-2+(int)(this.height*((float)(val-intMinVal)/(intMaxVal-intMinVal))), this.posY+2));
			}
			else if(positioning == 3) {
				RenderUtil.setColor(color);
				RenderUtil.drawRect(this.posX+2, Math.min(this.posY+2+this.height-(int)(this.height*((float)(val-intMinVal)/(intMaxVal-intMinVal))), this.posY-2+this.height), this.posX-2+this.width, this.posY-2+this.height);
			}
		}
	}
}
