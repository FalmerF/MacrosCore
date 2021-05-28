package net.smb.Macros.util;

import java.util.Properties;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class RenderUtil {
	public static StandartFontRenderer fontRenderer;
	
	public static boolean scissor;
	public static int sX, sY, sW, sH;
	
	public static void setColor(Color col) {
		setColor(col, col.colorA);
	}
	
	public static void setColor(Color col, float alpha) {
		GL11.glColor4f(col.colorR, col.colorG, col.colorB, alpha);
	}
	
    public static void drawFromAtlas(int posX, int posY, int texturePosX, int texturePosY) {
    	drawFromAtlas(posX, posY, texturePosX, texturePosY, 20, 20);
    }
    
    public static void drawFromAtlas(int posX, int posY, int texturePosX, int texturePosY, int width, int height) {
    	GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
    	Gui.func_152125_a(posX, posY, texturePosX, texturePosY, 1, 1, width, height, 20, 20);
    	GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
    }
    
    public static void drawString(String text, int posX, int posY, Color color) {
    	drawString(text, posX, posY, color, true, 1, false);
    }
    
    public static void drawString(String text, int posX, int posY, boolean shadow) {
    	drawString(text, posX, posY, Color.color_4, shadow, 1, false);
    }
    
    public static void drawString(String text, int posX, int posY) {
    	drawString(text, posX, posY, Color.color_4, true, 1, false);
    }
    
    public static void drawString(String text, int posX, int posY, float scale) {
    	drawString(text, posX, posY, Color.color_4, true, scale, false);
    }
    
    public static void drawString(String text, int posX, int posY, boolean shadow, float scale) {
    	drawString(text, posX, posY, Color.color_4, shadow, scale, false);
    }
    
    public static void drawString(String text, int posX, int posY, Color color, float scale) {
    	drawString(text, posX, posY, color, true, scale, false);
    }
    
    public static void drawString(String text, int posX, int posY, Color color, boolean shadow) {
    	drawString(text, posX, posY, color, shadow, 1, false);
    }
    
    public static void drawString(String text, int posX, int posY, boolean shadow, boolean centered) {
    	drawString(text, posX, posY, Color.color_4, shadow, 1, centered);
    }
    
    public static void drawString(String text, int posX, int posY, boolean shadow, boolean centered, float scale) {
    	drawString(text, posX, posY, Color.color_4, shadow, scale, centered);
    }
    
    public static void drawString(String text, int posX, int posY, Color color, boolean shadow, float scale, boolean centered) {
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glPushMatrix();
    	if(centered) {
    		posX -= fontRenderer.getStringWidth(text)*scale/2;
    		posY -= (fontRenderer.FONT_HEIGHT*scale/2)-1;
    	}
    	GL11.glTranslatef(posX, posY, 0);
    	GL11.glScalef(scale, scale, scale);
    	if(shadow) fontRenderer.drawStringWithShadow(text, 0, 0, color);
    	else fontRenderer.drawString(text, 0, 0, color);
    	GL11.glPopMatrix();
    	GL11.glDisable(GL11.GL_BLEND);
    }
    
    static {
    	fontRenderer = new StandartFontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), true);
    }
    
    public static void drawRect(int p_73734_0_, int p_73734_1_, int p_73734_2_, int p_73734_3_)
    {
        int var5;

        if (p_73734_0_ < p_73734_2_)
        {
            var5 = p_73734_0_;
            p_73734_0_ = p_73734_2_;
            p_73734_2_ = var5;
        }

        if (p_73734_1_ < p_73734_3_)
        {
            var5 = p_73734_1_;
            p_73734_1_ = p_73734_3_;
            p_73734_3_ = var5;
        }

        Tessellator var9 = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        var9.startDrawingQuads();
        var9.addVertex((double)p_73734_0_, (double)p_73734_3_, 0.0D);
        var9.addVertex((double)p_73734_2_, (double)p_73734_3_, 0.0D);
        var9.addVertex((double)p_73734_2_, (double)p_73734_1_, 0.0D);
        var9.addVertex((double)p_73734_0_, (double)p_73734_1_, 0.0D);
        var9.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public static void glScissor(int x, int y, int width, int height){
    	sX = x;
    	sY = y;
    	sW = width;
    	sH = height;
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int scale = resolution.getScaleFactor();

        int scissorWidth = width * scale;
        int scissorHeight = height * scale;
        int scissorX = x * scale;
        int scissorY = mc.displayHeight - scissorHeight - (y * scale);

        scissor = true;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }
    
    public static void glScissorDisable() {
    	scissor = false;
    	GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
