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
	public static float zLevel = 0.0F;
	
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
    	GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		drawTexture(posX, posY, texturePosX, texturePosY, 1, 1, width, height, 20, 20);
    	GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
    }
    
    public static void drawTexture(int posX, int posY, float u, float v, int var1, int var2, int width, int height, float var3, float var4)
    {
        float var10 = 1.0F / var3;
        float var11 = 1.0F / var4;
        Tessellator var12 = Tessellator.instance;
        var12.startDrawingQuads();
        var12.addVertexWithUV((double)posX, (double)(posY + height), zLevel, (double)(u * var10), (double)((v + (float)var2) * var11));
        var12.addVertexWithUV((double)(posX + width), (double)(posY + height), zLevel, (double)((u + (float)var1) * var10), (double)((v + (float)var2) * var11));
        var12.addVertexWithUV((double)(posX + width), (double)posY, zLevel, (double)((u + (float)var1) * var10), (double)(v * var11));
        var12.addVertexWithUV((double)posX, (double)posY, zLevel, (double)(u * var10), (double)(v * var11));
        var12.draw();
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
    
    public static void drawRect(int pos1, int pos2, int pos3, int pos4)
    {
        int var5;

        if (pos1 < pos3)
        {
            var5 = pos1;
            pos1 = pos3;
            pos3 = var5;
        }

        if (pos2 < pos4)
        {
            var5 = pos2;
            pos2 = pos4;
            pos4 = var5;
        }

        Tessellator var9 = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        var9.startDrawingQuads();
        var9.addVertex((double)pos1, (double)pos4, zLevel);
        var9.addVertex((double)pos3, (double)pos4, zLevel);
        var9.addVertex((double)pos3, (double)pos2, zLevel);
        var9.addVertex((double)pos1, (double)pos2, zLevel);
        var9.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public static void drawGradientRect(int pos1, int pos2, int pos3, int pos4, int startColor, int endColor)
    {
        float var7 = (float)(startColor >> 24 & 255) / 255.0F;
        float var8 = (float)(startColor >> 16 & 255) / 255.0F;
        float var9 = (float)(startColor >> 8 & 255) / 255.0F;
        float var10 = (float)(startColor & 255) / 255.0F;
        float var11 = (float)(endColor >> 24 & 255) / 255.0F;
        float var12 = (float)(endColor >> 16 & 255) / 255.0F;
        float var13 = (float)(endColor >> 8 & 255) / 255.0F;
        float var14 = (float)(endColor & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        var15.setColorRGBA_F(var8, var9, var10, var7);
        var15.addVertex((double)pos3, (double)pos2, zLevel);
        var15.addVertex((double)pos1, (double)pos2, zLevel);
        var15.setColorRGBA_F(var12, var13, var14, var11);
        var15.addVertex((double)pos1, (double)pos4, zLevel);
        var15.addVertex((double)pos3, (double)pos4, zLevel);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
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
