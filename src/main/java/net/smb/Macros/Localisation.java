package net.smb.Macros;

import java.util.HashMap;
import java.util.Map;

public class Localisation {
	public static Map<String, String> lang = new HashMap<String, String>();
	
	public static void loadLang() {
		lang.put("menu.title", "Macros Core");
		
		lang.put("menu.category.keybind", "Keys");
		lang.put("menu.category.events", "Events");
		lang.put("menu.category.gui", "Gui");
		lang.put("menu.category.editor", "Editor");
		lang.put("menu.category.settings", "Settings");
		
		lang.put("keybind.edit.key", "Editing a macro for ");
		
		lang.put("field.description.macros", "Macros...");
		lang.put("field.description.file", "File Name...");
		lang.put("field.description.code", "Code...");
		lang.put("field.saved", "Saved...");
		
		lang.put("menu.keybinding.save", "Do you want to save your work before leaving?");
		lang.put("menu.keybinding.guide.title", "Guide");
		lang.put("menu.keybinding.guide", "Guuuuuuuuuuuide");
		
		lang.put("menu.editor.createfile", "Create new File");
		
		lang.put("menu.yes", "Yes");
		lang.put("menu.no", "No");
		lang.put("menu.create", "Create");
		lang.put("menu.cancel", "Cancel");
		
		lang.put("paragraph", "\u00A7");
		
		lang.put("parser.error.varname", "Invalid variable name");
		lang.put("parser.error.varexists", "Variable");
		lang.put("parser.error.varexists2", "already exists.");
		lang.put("parser.error.cantparse", "Can't parse from");
		lang.put("parser.error.methodexists", "Action");
		lang.put("parser.error.methodexists2", "doesn't exist");
		lang.put("parser.error.syntax", "Syntax error");
		lang.put("parser.error.expected", "Expected ; in");
		lang.put("parser.error.file", "File");
	}
	
	public static String getString(String key) {
		String value = lang.get(key);
		if(value == null) return "";
		else return value;
	}
}
