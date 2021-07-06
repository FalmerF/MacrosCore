package net.smb.Macros.util;

import org.apache.logging.log4j.core.impl.Log4jContextFactory;

public class Color {
	public static Color color_1 = new Color(0.18F, 0.22F, 0.33F); // Buttons
	public static Color color_2 = new Color(0.12F, 0.16F, 0.20F); // Panels
	public static Color color_3 = new Color(0F, 0F, 0F); // Black
	public static Color color_4 = new Color(0.88F, 0.88F, 0.88F); // White
	public static Color color_5 = new Color(0.42F, 0.49F, 0.67F); // Buttons Hover
	public static Color color_6 = new Color(0.8F, 0.8F, 0.8F); // Gray
	public static Color color_7 = new Color(0.04F, 0.05F, 0.06F); // Pochti Black
	public static Color color_8 = new Color(0.77F, 0.78F, 0.78F); // Zapasnoy
	public static Color color_9 = new Color(0.21F, 0.64F, 0.09F); // Green
	public static Color color_10 = new Color(0.09F, 0.1F, 0.11F); // Svetlee chem Pochti Black
	public static Color color_11 = new Color(0F, 0F, 1.0F, 1.0F); // Select
	public static Color color_12 = new Color(0.09F, 0.10F, 0.11F); // Ne Pochti Black
	public static Color color_13 = new Color(1.0F, 0.33F, 0.33F); // &c
	public static Color color_14 = new Color(0.55F, 0.55F, 0.55F); // &8
	public static Color color_15 = new Color(1.0F, 1.0F, 0.33F); // &e
	public static Color color_16 = new Color(1.0F, 1.0F, 1.0F); // WIIITE

	public float colorR = 0, colorG = 0, colorB = 0, colorA = 1.0F;
	
	public Color(float colorR, float colorG, float colorB) {
		this.colorR = colorR;
		this.colorG = colorG;
		this.colorB = colorB;
		this.colorA = 1;
	}
	
	public Color(float colorR, float colorG, float colorB, float colorA) {
		this.colorR = colorR;
		this.colorG = colorG;
		this.colorB = colorB;
		this.colorA = colorA;
	}
	
	public Color(Color color, float colorA) {
		this.colorR = color.colorR;
		this.colorG = color.colorG;
		this.colorB = color.colorB;
		this.colorA = colorA;
	}
	
	public Color(String hexColor) {
		setHexColor(hexColor);
	}
	
	public String getHexColor() {
		String hexColor = "";
		String s;
		s = Integer.toHexString((int)(this.colorR*255.0f));
		if(s.length() == 1) s = "0"+s;
		hexColor += s;
		
		s =  Integer.toHexString((int)(this.colorG*255.0f));
		if(s.length() == 1) s = "0"+s;
		hexColor += s;
		
		s =  Integer.toHexString((int)(this.colorB*255.0f));
		if(s.length() == 1) s = "0"+s;
		hexColor += s;
		
		s =  Integer.toHexString((int)(this.colorA*255.0f));
		if(s.length() == 1) s = "0"+s;
		hexColor += s;
		return hexColor;
	}
	
	public void setHexColor(String hexColor) {
		char[] chars = hexColor.toCharArray();
		this.colorR = Integer.parseInt(chars[0]+""+chars[1], 16)/255.0F;
		this.colorG = Integer.parseInt(chars[2]+""+chars[3], 16)/255.0F;
		this.colorB = Integer.parseInt(chars[4]+""+chars[5], 16)/255.0F;
		if(chars.length >= 8) this.colorA = Integer.parseInt(chars[6]+""+chars[7], 16)/255.0F;
		else this.colorA = 1.0f;
	}
}
