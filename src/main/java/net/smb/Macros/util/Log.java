package net.smb.Macros.util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class Log extends AbstractAppender {
	protected Log() {
		super("Internal Log Appender", (Filter)null, null);
        this.start();
	}

	private static Logger logger;
	
	public static void info(Object info) {
        logger.info(String.valueOf(info));
    }
	
	static {
		Log.logger = (Logger)LogManager.getLogger("MacrosCore");
		Log.logger.addAppender((Appender)new Log());
    }

	@Override
	public void append(LogEvent event) {
		
	}
	
	public static void chat(String text) {
		GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		chat.printChatMessage((IChatComponent) new ChatComponentText(text));
	}
}
