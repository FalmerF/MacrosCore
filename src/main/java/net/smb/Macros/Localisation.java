package net.smb.Macros;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.ResourceLocation;
import net.smb.Macros.util.Log;

public class Localisation implements IResourceManagerReloadListener{
	public static Map<String, String> lang = new HashMap<String, String>();
	
	public static void loadLang() {
		ResourceLocation langFile = new ResourceLocation("macroscore", "lang/en_US.lang");
		int langNum = MacrosSettings.getInt("language");
		switch(langNum) {
			case 0:
				LanguageManager langManager = Minecraft.getMinecraft().getLanguageManager();
				String langCode = langManager.getCurrentLanguage().getLanguageCode();
				if(langCode.equals("ru_RU")) {
					langFile = new ResourceLocation("macroscore", "lang/ru_RU.lang");
				}
				else {
					langFile = new ResourceLocation("macroscore", "lang/en_US.lang");
				}
				break;
			case 1:
				langFile = new ResourceLocation("macroscore", "lang/en_US.lang");
				break;
			case 2:
				langFile = new ResourceLocation("macroscore", "lang/ru_RU.lang");
				break;
		}
		
		try {
			lang.clear();
	    	
 			InputStream var1 = Minecraft.getMinecraft().getResourceManager().getResource(langFile).getInputStream();
            final BufferedReader Br = new BufferedReader(new InputStreamReader(var1));
            String line = "";
            while ((line = Br.readLine()) != null) {
            	if(!line.startsWith("//") || !line.startsWith("#")) {
            		String[] key = line.split("=", 2);
           		 	if(key.length == 2) lang.put(key[0], key[1]);
           	 	}
            }
            Br.close();
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static String getString(String key) {
		String value = lang.get(key);
		if(value == null) return "";
		else return value;
	}

	@Override
	public void onResourceManagerReload(IResourceManager arg0) {
		loadLang();
	}
}
