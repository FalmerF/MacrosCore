package net.smb.Macros.actions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ChatComponentText;
import net.smb.Macros.CodeParser;
import net.smb.Macros.Localisation;

public class ActionKey extends ActionBase {

	public ActionKey() {
		super("key");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			int keyCode = getKey(parser.getString(args[0]));
			int type = 0;
			if(args.length > 1) {
				String pressType = parser.getString(args[1]);
				if(pressType.equals("press")) type = 0;
				else if(pressType.equals("up")) type = 1;
				else if(pressType.equals("down")) type = 2;
			}
			
			if(type == 0) {
				
			}
			else if(type == 1) {
				
			}
		}
		return null;
	}
	
	public static int getKey(String key) {
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		if(key.equals("forward")) {
			return settings.keyBindForward.getKeyCode();
		}
		else if(key.equals("back")) {
			return settings.keyBindBack.getKeyCode();
		}
		else if(key.equals("left")) {
			return settings.keyBindLeft.getKeyCode();
		}
		else if(key.equals("right")) {
			return settings.keyBindRight.getKeyCode();
		}
		else if(key.equals("sprint")) {
			return settings.keyBindSprint.getKeyCode();
		}
		else if(key.equals("jump")) {
			return settings.keyBindJump.getKeyCode();
		}
		else if(key.equals("sneak")) {
			return settings.keyBindSneak.getKeyCode();
		}
		else if(key.equals("playerlist")) {
			return settings.keyBindPlayerList.getKeyCode();
		}
		else if(key.equals("attack")) {
			return settings.keyBindAttack.getKeyCode();
		}
		else if(key.equals("drop")) {
			return settings.keyBindDrop.getKeyCode();
		}
		else if(key.equals("use")) {
			return settings.keyBindUseItem.getKeyCode();
		}
		else if(key.equals("chat")) {
			return settings.keyBindChat.getKeyCode();
		}
		else if(key.equals("inventory")) {
			return settings.keyBindInventory.getKeyCode();
		}
		else if(key.equals("pick")) {
			return settings.keyBindPickBlock.getKeyCode();
		}
		else if(key.equals("screenshot")) {
			return settings.keyBindScreenshot.getKeyCode();
		}
		else if(key.equals("smoothcamera")) {
			return settings.keyBindSmoothCamera.getKeyCode();
		}
		return 0;
	}
}
