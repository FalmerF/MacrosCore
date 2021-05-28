package net.smb.Macros;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.smb.Macros.Actions.*;
import net.smb.Macros.gui.screens.GuiScreenMenu;

@Mod(modid = MacroModCore.MODID, version = MacroModCore.VERSION)
public class MacroModCore {
 	public static final String MODID = "Macros Core";
    public static final String VERSION = "1.0";
    
    public List<Integer> pressedKeys = new ArrayList<Integer>();
    
    String macros;
    int keyId;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	MacrosSettings.loadSettings();
        Localisation.loadLang();
        FMLCommonHandler.instance().bus().register(this);
        
        // Initialize Actions
        new ActionLog();
        new ActionPow();
        new ActionSend();
        new ActionIf();
        new ActionElseIf();
        new ActionElse();
        new ActionFor();
        new ActionWait();
        new ActionRun();
    }
    
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
    	if(Keyboard.isKeyDown(Keyboard.KEY_GRAVE) && Minecraft.getMinecraft().currentScreen == null) {
    		Minecraft.getMinecraft().displayGuiScreen(new GuiScreenMenu(Minecraft.getMinecraft()));
    	}
    	
    	
    	if(Keyboard.getEventKey() != 0 && Keyboard.getEventKeyState() && Minecraft.getMinecraft().currentScreen == null) {
    		keyId = Keyboard.getEventKey();
    		if(pressedKeys.indexOf(keyId) == -1) {
    			pressedKeys.add(keyId);
    			macros = MacrosSettings.getString("key_" + keyId);
				if(!macros.equals("")) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							CodeParser parser = new CodeParser("[" + keyId + "]", null);
							parser.executeCode(macros);
						}
					}).start();
				}
    		}
    	}
    	
    	Object[] pressedKeysCopy = pressedKeys.toArray();
    	for(Object key : pressedKeysCopy) {
    		if(!Keyboard.isKeyDown((Integer) key)) pressedKeys.remove((Object)key);
    	}
    }
    
    public static void registerAction(String actionName, ActionBase instance) {
    	CodeParser.addAction(actionName, instance);
    }
}
