package net.smb.Macros.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.smb.Macros.CodeParser;
import net.smb.Macros.Localisation;
import net.smb.Macros.gui.LayoutManager;
import net.smb.Macros.gui.elements.GuiButton;
import net.smb.Macros.gui.elements.GuiColorSelect;
import net.smb.Macros.gui.elements.GuiDropdown;
import net.smb.Macros.gui.elements.GuiField;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.gui.elements.GuiText;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.util.ResourceLocations;

public class WidgetSlider extends Widget {
	private Color color = new Color(Color.color_4, 1.0F);
	private String minValue = "0", maxValue = "100", steps = "0";
	private int value = 0;
	private int positioning = 0;
	
	int intMinVal = 0;
	int intMaxVal = 0;
	int intSteps = 0;

	public WidgetSlider(String name) {
		super(name);
		displayString = "Text";
		
		this.editPanel = new GuiPanel(-1, 0, 0, 150, 260);
		editPanel.canMove = true;
		editPanel.shadow = true;
		
		GuiText text = new GuiText(75, 10, 1.0F, Localisation.getString("menu.guiedit.editing") + this.widgetName);
		text.centered = true;
		editPanel.addGuiElement(text);
		
		editPanel.addGuiElement(new GuiButton(16, 15, 240, 40, 15, Localisation.getString("menu.save")));
		editPanel.addGuiElement(new GuiButton(17, 95, 240, 40, 15, Localisation.getString("menu.cancel")));
		
		editPanel.addGuiElement(new GuiText(10, 35, 1.0F, Localisation.getString("widget.value")));
		GuiField field = new GuiField(60, 33, 80, 1.0F, String.valueOf(value), Localisation.getString("field.description.value"));
		field.height = 16;
		field.numbers = true;
		editPanel.addGuiElement(field);
		
		editPanel.addGuiElement(new GuiText(10, 55, 1.0F, Localisation.getString("widget.minvalue")));
		field = new GuiField(60, 53, 80, 1.0F, String.valueOf(minValue), Localisation.getString("field.description.value"));
		field.height = 16;
		editPanel.addGuiElement(field);
		
		editPanel.addGuiElement(new GuiText(10, 75, 1.0F, Localisation.getString("widget.maxvalue")));
		field = new GuiField(60, 73, 80, 1.0F, String.valueOf(maxValue), Localisation.getString("field.description.value"));
		field.height = 16;
		editPanel.addGuiElement(field);
		
		editPanel.addGuiElement(new GuiText(10, 95, 1.0F, Localisation.getString("widget.steps")));
		field = new GuiField(60, 93, 80, 1.0F, String.valueOf(steps), Localisation.getString("field.description.value"));
		field.height = 16;
		editPanel.addGuiElement(field);
		
		editPanel.addGuiElement(new GuiText(10, 167, 1.0F, Localisation.getString("widget.positioning")));
		List<String> elements = new ArrayList<String>();
		elements.add(Localisation.getString("posH"));
		elements.add(Localisation.getString("posV"));
		editPanel.addGuiElement(new GuiDropdown(-1, 60, 165, 80, 15, elements, positioning));
		
		editPanel.addGuiElement(new GuiText(10, 125, 1.0F, Localisation.getString("widget.backgroundcolor")));
		editPanel.addGuiElement(new GuiColorSelect(-1, 120, 125, 10, backgroundColor));
		
		editPanel.addGuiElement(new GuiText(10, 145, 1.0F, Localisation.getString("widget.color")));
		editPanel.addGuiElement(new GuiColorSelect(-1, 120, 145, 10, color));
		
		editPanel.addGuiElement(new GuiText(10, 195, 1.0F, Localisation.getString("widget.macros")));
		field = new GuiField(10, 210, 130, 1.0F, "", Localisation.getString("field.description.macros"));
		field.height = 16;
		field.code = true;
		editPanel.addGuiElement(field);
	}

	public void setParams() {
		propeties.put("posX", String.valueOf(posX));
		propeties.put("posY", String.valueOf(posY));
		propeties.put("width", String.valueOf(width));
		propeties.put("height", String.valueOf(height));
		
		minValue = ((GuiField)editPanel.guiElements.get(6)).text;
		propeties.put("minvalue", String.valueOf(minValue));
		
		maxValue = ((GuiField)editPanel.guiElements.get(8)).text;
		propeties.put("maxvalue", String.valueOf(maxValue));
		
		value = Integer.parseInt(((GuiField)editPanel.guiElements.get(4)).text);
		propeties.put("value", String.valueOf(value));
		
		steps = ((GuiField)editPanel.guiElements.get(10)).text;
		propeties.put("steps", String.valueOf(steps));
		
		propeties.put("backgroundcolor", backgroundColor.getHexColor());
		
		propeties.put("color", color.getHexColor());
		
		positioning = ((GuiDropdown)editPanel.guiElements.get(12)).selectedElement;
		propeties.put("positioning", String.valueOf(positioning));
		
		propeties.put("macros", ((GuiField)editPanel.guiElements.get(18)).text);
	}
	
	public void updateParams() {
		try {
			this.posX = Integer.parseInt(propeties.get("posX"));
			this.posY = Integer.parseInt(propeties.get("posY"));
			this.width = Integer.parseInt(propeties.get("width"));
			this.height = Integer.parseInt(propeties.get("height"));
			
			value = Integer.parseInt(propeties.get("value"));
			((GuiField)editPanel.guiElements.get(4)).text = String.valueOf(value);
			
			minValue = propeties.get("minvalue");
			((GuiField)editPanel.guiElements.get(6)).text = minValue;
			
			maxValue = propeties.get("maxvalue");
			((GuiField)editPanel.guiElements.get(8)).text = maxValue;
			
			steps = propeties.get("steps");
			((GuiField)editPanel.guiElements.get(10)).text = steps;
			
			this.backgroundColor.setHexColor(propeties.get("backgroundcolor"));
			((GuiColorSelect)editPanel.guiElements.get(14)).setColor();
			
			this.color.setHexColor(propeties.get("color"));
			((GuiColorSelect)editPanel.guiElements.get(16)).setColor();
			
			this.positioning = Integer.parseInt(propeties.get("positioning"));
			((GuiDropdown)editPanel.guiElements.get(12)).selectedElement = positioning;
			
			((GuiField)editPanel.guiElements.get(18)).text = propeties.get("macros");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			boolean editing = this.editing;
			super.draw(mc, positionX, positionY);
			
			intMinVal = CodeParser.globalParser.getInt(minValue);
			intMaxVal = CodeParser.globalParser.getInt(maxValue);
			intSteps = CodeParser.globalParser.getInt(steps);
			
			value = Math.max(Math.min(value, intMaxVal), intMinVal);
			intSteps = Math.max(Math.min(intSteps, intMaxVal-intMinVal), 0);
			
			if(positioning == 0) {
				RenderUtil.setColor(color);
				RenderUtil.drawRect(this.posX, this.posY-1+this.height/2, this.posX+this.width, this.posY+1+this.height/2);
				
				GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
		        GL11.glLogicOp(GL11.GL_OR_REVERSE);
		        RenderUtil.setColor(Color.color_11);
				for(int i = 1; i <= intSteps; i++) {
					int stepDist = (int)(this.width*(i/(intSteps+1.0F)));
					RenderUtil.drawRect(this.posX+stepDist, this.posY-1+this.height/2, this.posX+1+stepDist, this.posY+1+this.height/2);
				}
				GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
				
				RenderUtil.setColor(color);
				mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
				int handlePos = (int)(this.posX+(float)(this.width*(value/(float)(intMaxVal-intMinVal))))-3;
				RenderUtil.drawFromAtlas(handlePos, this.posY-3+this.height/2, 11, 3, 7, 7);
			}
			else {
				RenderUtil.setColor(color);
				RenderUtil.drawRect(this.posX-1+this.width/2, this.posY, this.posX+1+this.width/2, this.posY+this.height);
				
				GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
		        GL11.glLogicOp(GL11.GL_OR_REVERSE);
		        RenderUtil.setColor(Color.color_11);
				for(int i = 1; i <= intSteps; i++) {
					int stepDist = (int)(this.height*(i/(intSteps+1.0F)));
					RenderUtil.drawRect(this.posX-1+this.width/2, this.posY+stepDist, this.posX+1+this.width/2, this.posY+1+stepDist);
				}
				GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
				
				RenderUtil.setColor(color);
				mc.getTextureManager().bindTexture(ResourceLocations.guiAtlas);
				int handlePos = (int)(this.posY+(float)(this.height*(value/(float)(intMaxVal-intMinVal))))-3;
				RenderUtil.drawFromAtlas(this.posX-3+this.width/2, handlePos, 11, 3, 7, 7);
			}
			if(!editing && this.pressed) {
				dragged(mc, positionX, positionY);
			}
		}
	}
	
	protected void dragged(Minecraft mc, int posX, int posY) {
		if(positioning == 0) {
			int stepDist = 0;
			if(intSteps > 0) stepDist = (int)(this.width/(intSteps+1.0F)/2)+1;
			this.value = (int)(Math.max(Math.min(posX-this.posX+1+stepDist, this.width), 0)/(float)this.width*(this.intMaxVal-this.intMinVal));
			limitValue();
		}
		else {
			int stepDist = 0;
			if(intSteps > 0) stepDist = (int)(this.height/(intSteps+1.0F)/2)+1;
			this.value = (int)(Math.max(Math.min(posY-this.posY+1+stepDist, this.height), 0)/(float)this.height*(this.intMaxVal-this.intMinVal));
			limitValue();
		}
	}
	
	void limitValue() {
		try {
			if(this.intSteps > 0) {
				int steps = this.intSteps+1;
				this.value -= this.value%((this.intMaxVal-this.intMinVal)/steps);
			}
		} catch(Exception e) {}
	}
	
	public static String convertAmpCodes(final String text) {
        return text.replaceAll("(?<!&)&([0-9a-fklmnor])", "\u00A7$1").replaceAll("&&", "&");
    }
	
	public void mouseReleased(int posX, int posY) {
		this.pressed = false;
		((GuiField)editPanel.guiElements.get(4)).text = String.valueOf(value);
		LayoutManager.saveToXML();
		
		try {
			Map<String, Object> vars = new TreeMap<String, Object>();
			vars.put("value", this.value);
			vars.put("minvalue", this.minValue);
			vars.put("maxvalue", this.maxValue);
			vars.put("steps", this.steps);
			new Thread(new Runnable() {
				@Override
				public void run() {
					CodeParser newParser = new CodeParser("[Slider]", null);
					newParser.vars.putAll(vars);
					newParser.executeCode(propeties.get("macros"));
				}
			}).start();
		} catch(Exception e) {e.printStackTrace();}
	}
    public boolean mousePressed(Minecraft mc, int posX, int posY)
    {
    	if(this.pressed(posX, posY)) {
    		this.pressed = true;
    		return true;
    	}
        return false;
    }
    
    public boolean pressed(int posX, int posY)
    {
    	if(this.enabled && this.visible && posX >= this.posX && posX < this.posX + this.width && posY >= this.posY && posY < this.posY + this.height) {
    		return true;
    	}
        return false;
    }
}
