package net.smb.Macros.gui.widgets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.smb.Macros.Localisation;
import net.smb.Macros.gui.elements.GuiButton;
import net.smb.Macros.gui.elements.GuiColorSelect;
import net.smb.Macros.gui.elements.GuiField;
import net.smb.Macros.gui.elements.GuiPanel;
import net.smb.Macros.gui.elements.GuiText;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.RenderUtil;

public class WidgetImage extends Widget {
	public Color imageColor = new Color(Color.color_4, 1.0F);
	public int positioning = 0;
	
	private String image = "";
	private ResourceLocation imageResource;
	private ItemStack itemStack;
	private Pattern itemMeta = Pattern.compile("^([0-9]*?)[: ]([0-9]+)$");
	private RenderItem renderItem = new RenderItem();

	public WidgetImage(String name) {
		super(name);
		this.editPanel = new GuiPanel(-1, 0, 0, 150, 130);
		editPanel.canMove = true;
		editPanel.shadow = true;
		
		GuiText text = new GuiText(75, 10, 1.0F, Localisation.getString("menu.guiedit.editing") + this.widgetName);
		text.centered = true;
		editPanel.addGuiElement(text);
		
		editPanel.addGuiElement(new GuiButton(16, 15, 105, 40, 15, Localisation.getString("menu.save")));
		editPanel.addGuiElement(new GuiButton(17, 95, 105, 40, 15, Localisation.getString("menu.cancel")));
		
		editPanel.addGuiElement(new GuiText(10, 35, 1.0F, Localisation.getString("widget.image")));
		GuiField field = new GuiField(40, 33, 100, 1.0F, image, Localisation.getString("field.description.image"));
		field.height = 16;
		editPanel.addGuiElement(field);
		
		editPanel.addGuiElement(new GuiText(10, 60, 1.0F, Localisation.getString("widget.backgroundcolor")));
		editPanel.addGuiElement(new GuiColorSelect(-1, 120, 60, 10, backgroundColor));
		
		editPanel.addGuiElement(new GuiText(10, 80, 1.0F, Localisation.getString("widget.textcolor")));
		editPanel.addGuiElement(new GuiColorSelect(-1, 120, 80, 10, imageColor));
	}

	public void setParams() {
		propeties.put("posX", String.valueOf(posX));
		propeties.put("posY", String.valueOf(posY));
		propeties.put("width", String.valueOf(width));
		propeties.put("height", String.valueOf(height));
		
		image = ((GuiField)editPanel.guiElements.get(4)).text;
		setImage();
		propeties.put("image", String.valueOf(image));
		
		propeties.put("backgroundcolor", backgroundColor.getHexColor());
		
		propeties.put("color", imageColor.getHexColor());
	}
	
	public void updateParams() {
		try {
			this.posX = Integer.parseInt(propeties.get("posX"));
			this.posY = Integer.parseInt(propeties.get("posY"));
			this.width = Integer.parseInt(propeties.get("width"));
			this.height = Integer.parseInt(propeties.get("height"));
			image = propeties.get("image");
			setImage();
			((GuiField)editPanel.guiElements.get(4)).text = image;
			
			this.backgroundColor.setHexColor(propeties.get("backgroundcolor"));
			((GuiColorSelect)editPanel.guiElements.get(6)).setColor();
			
			this.imageColor.setHexColor(propeties.get("color"));
			((GuiColorSelect)editPanel.guiElements.get(8)).setColor();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			super.draw(mc, positionX, positionY);
			
			if(itemStack != null) {
				RenderUtil.setColor(imageColor);
				GL11.glPushMatrix();
				GL11.glTranslatef(this.posX, this.posY, 0);
				GL11.glScalef(this.width/16.0f, this.height/16.0f, 0);
				renderItem.zLevel = 0;
				renderItem.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), itemStack, 0, 0);
				GL11.glPopMatrix();
				GL11.glBlendFunc(770, 771);
				GL11.glDepthFunc(515);
				GL11.glDisable(GL11.GL_LIGHTING);
			}
			else if(imageResource != null) {
				mc.getTextureManager().bindTexture(imageResource);
				RenderUtil.setColor(imageColor);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				Gui.func_152125_a(this.posX, this.posY, 1, 1, 1, 1, this.width, this.height, 1, 1);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_BLEND);
			}
		}
	}
	
	public static String convertAmpCodes(final String text) {
        return text.replaceAll("(?<!&)&([0-9a-fklmnor])", "\u00A7$1").replaceAll("&&", "&");
    }
	
	public void setImage() {
		itemStack = null;
		imageResource = null;
		try {
			if(image.equals("")) {
				return;
			}
			
			if(image.endsWith(".png")) {
				imageResource = new ResourceLocation(image);
				return;
			}
			
			int meta = 0;
			int itemId = 0;
			Matcher itemMeta = this.itemMeta.matcher(image);
			if(itemMeta.matches()) {
				itemId = Integer.parseInt(itemMeta.group(1));
				meta = Integer.parseInt(itemMeta.group(2));
			}
			else itemId = Integer.parseInt(image);
			
			Item item = Item.getItemById(itemId);
			if(item == null) {
				return;
			}
			itemStack = new ItemStack(item, 1, meta > -1 ? meta : 0);
		} catch(Exception e) {
		}
	}
}
