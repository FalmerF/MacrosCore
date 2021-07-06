package net.smb.Macros.events;

import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.smb.Macros.CodeParser;
import net.smb.Macros.MacroModCore;
import net.smb.Macros.MacrosSettings;
import net.smb.Macros.util.Log;

public class EventManagerClient extends EventBase {
	public void init() {
		MacroModCore.registerEvent("onShowGui", "Test desc");
		MacroModCore.registerEvent("onChat", "");
		MacroModCore.registerEvent("onSendChatMessage", "");
		MacroModCore.registerEvent("chatFilter", "");
		MacroModCore.registerEvent("onKeyTyped", "");
		MacroModCore.registerEvent("onMouseClicked", "");
		MacroModCore.registerEvent("onSound", "");
		MacroModCore.registerEvent("onFilterableChat", "");
		MacroModCore.registerEvent("onTick", "");
	}
	
	private GuiScreen lastScreen = null;
	
	@SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
		try {
			Minecraft mc = Minecraft.getMinecraft();
			EntityClientPlayerMP thePlayer = mc.thePlayer;
			if(thePlayer!= null) {
				if(lastScreen != mc.currentScreen) {
					if(lastScreen instanceof GuiControls) {
						MacroModCore.saveControls();
					}
					lastScreen = mc.currentScreen;
					if(lastScreen != null) {
						Map<String, Object> vars = new TreeMap<String, Object>();
						vars.put("screen", lastScreen.getClass().getSimpleName());
						execute("onShowGui", vars);
					}	
					MacroModCore.onShowGui(lastScreen);
				}
			}
		}catch(Exception e) {}
		if(event.phase == TickEvent.Phase.END) {
			execute("onTick", null);
		}
    }
	
	@SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event)
    {
		Map<String, Object> vars = new TreeMap<String, Object>();
		vars.put("message", event.message.getFormattedText());
		vars.put("cleared_message", event.message.getUnformattedText());
		execute("onChat", vars);
		
		CodeParser newParser = new CodeParser("[chatFilter]", null);
		newParser.vars.putAll(vars);
		boolean cancel = !newParser.executeCode(MacrosSettings.getString("event_chatFilter"));
		event.setCanceled(cancel);
		if(cancel) execute("onFilterableChat", vars);
    }
	
	public static boolean onSendChatMessage(String message) {
		Map<String, Object> vars = new TreeMap<String, Object>();
		vars.put("message", message);
		CodeParser newParser = new CodeParser("[onSendChatMessage]", null);
		newParser.vars.putAll(vars);
		return newParser.executeCode(MacrosSettings.getString("event_onSendChatMessage"));
	}
	
	@SubscribeEvent
    public void onKeyInput(KeyInputEvent event)
    {
		if (Keyboard.getEventKeyState())
        {
			Map<String, Object> vars = new TreeMap<String, Object>();
			vars.put("key", Keyboard.getKeyName(Keyboard.getEventKey()));
			execute("onKeyTyped", vars);
        }
    }
	
	@SubscribeEvent
    public void onMouseInput(MouseInputEvent event)
    {
		if (Mouse.getEventButtonState())
        {
			Map<String, Object> vars = new TreeMap<String, Object>();
			vars.put("mouse", Mouse.getEventButton());
			execute("onMouseClicked", vars);
        }
    }
	
	@SubscribeEvent
    public void onPlaySoundAtEntity(PlaySoundAtEntityEvent event)
    {
		Map<String, Object> vars = new TreeMap<String, Object>();
		vars.put("sound", event.name);
		execute("onSound", vars);
    }
}
