package net.smb.Macros.gui.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.RenderUtil;

public class GuiText extends GuiElement {
	private String text;
	private String[] lines;
	private float scale = 1.0F;
	private boolean colorCodes = false;
	private boolean positionedRight;
	public boolean centered;
	public boolean shadow;
	public Color color;
	
	public GuiText(int posX, int posY, float scale, String text){
		super(-1, posX, posY, 0, 0);
		this.text = text;
		this.scale = scale;
		this.shadow = false;
		this.color = Color.color_4;
		UpdateLines();
	}
	
	public void UpdateLines() {
		String newText = this.text;
		if(this.width != 0) newText = this.getMultilineString();
		if(colorCodes) this.lines = newText.split("(\\n)+");
		else this.lines = newText.split("\\n");
		this.height = (int) (lines.length * (10 * this.scale));
	}
	
	public String getMultilineString() {
    	String[] words = this.text.replaceAll("\\\\n", " \n").split(" ");
    	String line = "", lastLine = "";
    	int fieldWidth = this.width;
    	for(String word : words) {
    		int newLineWidth = (int)(Minecraft.getMinecraft().fontRenderer.getStringWidth(lastLine + " " + word)*this.scale);
    		if(newLineWidth > fieldWidth || word.startsWith("\n") || word.endsWith("\n")) {
    			if(!line.equals("")) line += "\n";
    			line += word;
    			lastLine = word;
    		}
    		else {
    			if(line.equals("")) {
    				line += word;
	    			lastLine = word;
    			}
    			else {
	    			line += " " + word;
	    			lastLine += " " + word;
    			}
    		}
    	}
    	
    	return line;
    }
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			FontRenderer fontRender = mc.fontRenderer;
			int startTextPosX = this.posX;
			int startTextPosY = this.posY;
			
			for(int i = 0; i < lines.length; i++) {
				int textPosX = startTextPosX;
				if(this.positionedRight) {
					textPosX = startTextPosX - (int)(fontRender.getStringWidth(lines[i])*this.scale);
				}
				else if(centered) {
					textPosX = startTextPosX - (int)(fontRender.getStringWidth(lines[i])*this.scale)/2;
				}
	        	GL11.glPushMatrix();
	            int textPosY = (int)(startTextPosY+((10*this.scale)*i));
	            GL11.glTranslatef(textPosX, textPosY, 0.0F);
	            GL11.glScalef(scale, scale, scale);
	            RenderUtil.drawString(lines[i], 0, 0, color, shadow);
	        	GL11.glPopMatrix();
	        }
		}
	}
	
	public void SetText(String text) {
		this.text = text;
		this.UpdateLines();
	}
	
	public void setColorFormating(boolean b) {
		this.colorCodes = b;
		UpdateLines();
	}
	
	public void setScale(float scale) {
		this.scale = scale;
		this.UpdateLines();
	}
}
