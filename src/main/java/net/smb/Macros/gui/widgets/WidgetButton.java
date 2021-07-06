package net.smb.Macros.gui.widgets;

import java.util.Map;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.smb.Macros.CodeParser;
import net.smb.Macros.Localisation;
import net.smb.Macros.MacrosSettings;
import net.smb.Macros.gui.elements.GuiButton;
import net.smb.Macros.gui.elements.GuiCheck;
import net.smb.Macros.gui.elements.GuiColorSelect;
import net.smb.Macros.gui.elements.GuiField;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.gui.elements.GuiText;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class WidgetButton extends Widget {
	public String displayString = "";
	public boolean close;
	public Color textColor = new Color(Color.color_4, 1.0F);

	public WidgetButton(String name) {
		super(name);
		displayString = "Button";
		
		this.editPanel = new GuiPanel(-1, 0, 0, 150, 200);
		editPanel.canMove = true;
		editPanel.shadow = true;
		
		GuiText text = new GuiText(75, 10, 1.0F, Localisation.getString("menu.guiedit.editing") + this.widgetName);
		text.centered = true;
		editPanel.addGuiElement(text);
		
		editPanel.addGuiElement(new GuiButton(16, 15, 175, 40, 15, Localisation.getString("menu.save")));
		editPanel.addGuiElement(new GuiButton(17, 95, 175, 40, 15, Localisation.getString("menu.cancel")));
		
		editPanel.addGuiElement(new GuiText(10, 35, 1.0F, Localisation.getString("widget.text")));
		GuiField field = new GuiField(40, 33, 100, 1.0F, displayString, Localisation.getString("field.description.text"));
		field.height = 16;
		editPanel.addGuiElement(field);
		
		editPanel.addGuiElement(new GuiText(25, 60, 1.0F, Localisation.getString("widget.dontclose")));
		editPanel.addGuiElement(new GuiCheck(-1, 10, 59, 10, close));
		
		editPanel.addGuiElement(new GuiText(10, 90, 1.0F, Localisation.getString("widget.backgroundcolor")));
		editPanel.addGuiElement(new GuiColorSelect(-1, 120, 90, 10, backgroundColor));
		
		editPanel.addGuiElement(new GuiText(10, 110, 1.0F, Localisation.getString("widget.textcolor")));
		editPanel.addGuiElement(new GuiColorSelect(-1, 120, 110, 10, textColor));
		
		editPanel.addGuiElement(new GuiText(10, 135, 1.0F, Localisation.getString("widget.macros")));
		field = new GuiField(10, 150, 130, 1.0F, "", Localisation.getString("field.description.macros"));
		field.height = 16;
		field.code = true;
		editPanel.addGuiElement(field);
	}

	public void setParams() {
		propeties.put("posX", String.valueOf(posX));
		propeties.put("posY", String.valueOf(posY));
		propeties.put("width", String.valueOf(width));
		propeties.put("height", String.valueOf(height));
		
		displayString = ((GuiField)editPanel.guiElements.get(4)).text;
		propeties.put("text", String.valueOf(displayString));
		
		close = ((GuiCheck)editPanel.guiElements.get(6)).value;
		propeties.put("close", String.valueOf(close));
		
		//backgroundColor = ((GuiColorSelect)editPanel.guiElements.get(8)).color;
		propeties.put("backgroundcolor", backgroundColor.getHexColor());
		
		//textColor = ((GuiColorSelect)editPanel.guiElements.get(10)).color;
		propeties.put("textcolor", textColor.getHexColor());
		
		propeties.put("macros", ((GuiField)editPanel.guiElements.get(12)).text);
	}
	
	public void updateParams() {
		try {
			this.posX = Integer.parseInt(propeties.get("posX"));
			this.posY = Integer.parseInt(propeties.get("posY"));
			this.width = Integer.parseInt(propeties.get("width"));
			this.height = Integer.parseInt(propeties.get("height"));
			this.displayString = propeties.get("text");
			((GuiField)editPanel.guiElements.get(4)).text = displayString;
			
			this.close = Boolean.parseBoolean(propeties.get("close"));
			((GuiCheck)editPanel.guiElements.get(6)).value = close;
			
			this.backgroundColor.setHexColor(propeties.get("backgroundcolor"));
			((GuiColorSelect)editPanel.guiElements.get(8)).setColor();
			
			this.textColor.setHexColor(propeties.get("textcolor"));
			((GuiColorSelect)editPanel.guiElements.get(10)).setColor();
			
			((GuiField)editPanel.guiElements.get(12)).text = propeties.get("macros");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			boolean editing = this.editing;
			super.draw(mc, positionX, positionY);
			
			RenderUtil.drawString(convertAmpCodes(CodeParser.globalParser.getString("\"" + displayString + "\"")), this.posX + this.width/2, this.posY + this.height/2, textColor, false, 1.0F, true);
		
			if(!editing && this.hovered) {
				mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
				int outlineX = this.posX-1;
				int outlineY = this.posY-1;
				int outlineWidth = this.width+2;
				int outlineHeight = this.height+2;
				
				RenderUtil.setColor(Color.color_8);
				RenderUtil.drawRect(outlineX, outlineY, outlineX+outlineWidth, outlineY+1);
				RenderUtil.drawRect(outlineX, outlineY+outlineHeight, outlineX+outlineWidth, outlineY+outlineHeight-1);
				RenderUtil.drawRect(outlineX, outlineY, outlineX+1, outlineY+outlineHeight);
				RenderUtil.drawRect(outlineX+outlineWidth, outlineY, outlineX+outlineWidth-1, outlineY+outlineHeight);
			}
		}
	}
	
	public static String convertAmpCodes(final String text) {
        return text.replaceAll("(?<!&)&([0-9a-fklmnor])", "\u00A7$1").replaceAll("&&", "&");
    }
	
	public boolean mousePressed(Minecraft mc, int posX, int posY) {
		if(this.pressed(posX, posY)) {
			try {
				new Thread(new Runnable() {
					@Override
					public void run() {
						CodeParser newParser = new CodeParser("[Button]", null);
						newParser.executeCode(propeties.get("macros"));
					}
				}).start();
			} catch(Exception e) {e.printStackTrace();}
			if(!close) mc.displayGuiScreen((GuiScreen)null);
			return true;
		}
		return false;
	}
}
