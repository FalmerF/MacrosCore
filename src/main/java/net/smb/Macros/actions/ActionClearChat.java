package net.smb.Macros.actions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.smb.Macros.CodeParser;

public class ActionClearChat extends ActionBase {

	public ActionClearChat() {
		super("clearChat");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		GuiNewChat newChat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		for(int i = 0; i < 1000; i++) {
			newChat.deleteChatLine(i);
        }
		return null;
	}
}
