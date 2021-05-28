package net.smb.Macros;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.ChatComponentText;

public class ParserError {
	public static void invalidVarName(CodeParser parser, String varName) {
		logMessage(parser.parserName + ": " + Localisation.getString("parser.error.varname") + " \"" + varName + "\".");
		parser.setError();
	}
	
	public static void varAlreadyExists(CodeParser parser, String varName) {
		logMessage(parser.parserName + ": " + Localisation.getString("parser.error.varexists") + " \"" + varName + "\" " + Localisation.getString("parser.error.varexists2"));
		parser.setError();
	}
	
	public static void cantParse(CodeParser parser, String value) {
		logMessage(parser.parserName + ": " + Localisation.getString("parser.error.cantparse") + " \"" + value + "\".");
		parser.setError();
	}
	
	public static void methodDoesntExist(CodeParser parser, String method) {
		logMessage(parser.parserName + ": " + Localisation.getString("parser.error.methodexists") + " \"" + method + "\" " + Localisation.getString("parser.error.methodexists2"));
		parser.setError();
	}
	
	public static void varDoesntExist(CodeParser parser, String varName) {
		logMessage(parser.parserName + ": " + Localisation.getString("parser.error.varexists") + " \"" + varName + "\" " + Localisation.getString("parser.error.methodexists2"));
		parser.setError();
	}
	
	public static void syntax(CodeParser parser, String command) {
		logMessage(parser.parserName + ": " + Localisation.getString("parser.error.syntax") + " \"" + command + "\".");
		parser.setError();
	}
	
	public static void expected(CodeParser parser, String command) {
		logMessage(parser.parserName + ": " + Localisation.getString("parser.error.expected") + " \"" + command + "\".");
		parser.setError();
	}
	
	public static void customError(CodeParser parser, String text) {
		logMessage(parser.parserName + ": " + text);
		parser.setError();
	}
	
	public static void logMessage(String message) {
		GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		chat.printChatMessage(new ChatComponentText(Localisation.getString("paragraph") + "c" + message));
	}
}
