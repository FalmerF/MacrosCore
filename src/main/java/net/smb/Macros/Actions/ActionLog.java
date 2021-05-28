package net.smb.Macros.Actions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.ChatComponentText;
import net.smb.Macros.CodeParser;

public class ActionLog extends ActionBase {

	public ActionLog() {
		super("log");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
			String message = String.valueOf(parser.getString(args[0]));
			message = message.replaceAll("(?<!&)&([0-9a-fklmnor])", "\u00A7$1").replaceAll("&&", "&").replaceAll("\\\\n", "\n");
			chat.printChatMessage(new ChatComponentText(message));
		}
		return null;
	}
}
