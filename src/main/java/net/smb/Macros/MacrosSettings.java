package net.smb.Macros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;

public class MacrosSettings {
	public static File macrosDir = new File(Minecraft.getMinecraft().mcDataDir, "MacrosCore");
	public static File config = new File(macrosDir, "macros.config");
	private static Map<String, String> params = new TreeMap<String, String>();
	
	public static void loadSettings() {
		try {
			if(!config.exists()) {
				macrosDir.mkdir();
				config.createNewFile();
				params.put("openKey", "41");
			}
			else {
		    	params.clear();
		    	
		    	FileReader Fr;
	 			Fr = new FileReader(config);
	             final BufferedReader Br = new BufferedReader(Fr);
	             String line = "";
	             while ((line = Br.readLine()) != null) {
	            	 if(!line.startsWith("//")) {
	            		 String[] key = line.split("=", 2);
	            		 if(key.length == 2) params.put(key[0], key[1]);
	            	 }
	             }
	             Br.close();
			}
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static void saveSettings() {
		try {
			if(!config.exists()) {
				macrosDir.mkdir();
				config.createNewFile();
			}
			
			FileWriter fw2 = new FileWriter(config);
            final BufferedWriter bw2 = new BufferedWriter(fw2);
            String settings = "// Macros Core Config\n";
            
            boolean keysComment = false;
            
            for(Map.Entry<String, String> param : params.entrySet()) {
            	if(param.getKey().startsWith("key_") && !keysComment) {
            		keysComment = true;
            		settings += "// Key Binds\n";
            	}
            	settings += param.getKey() + "=" + param.getValue() + "\n\n";
            }
            
			bw2.write(settings);
			bw2.close();
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static String getString(String key) {
		String value = params.get(key);
		if(value == null) return "";
		else return value;
	}
	
	public static int getInt(String key) {
		String value = params.get(key);
		if(value == null) return 0;
		else { 
			try {
				return Integer.parseInt(value);
			} catch(Exception e) {
				return 0;
			}
		}
	}
	
	public static float getFloat(String key) {
		String value = params.get(key);
		if(value == null) return 0;
		else { 
			try {
				return Float.parseFloat(value);
			} catch(Exception e) {
				return 0;
			}
		}
	}
	
	public static boolean getBool(String key) {
		String value = params.get(key);
		if(value == null) return false;
		else { 
			try {
				return Boolean.parseBoolean(value);
			} catch(Exception e) {
				return false;
			}
		}
	}
	
	public static void setParam(String key, Object value) {
		params.put(key, String.valueOf(value));
		saveSettings();
	}
}
