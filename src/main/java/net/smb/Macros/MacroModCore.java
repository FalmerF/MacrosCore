package net.smb.Macros;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Locale;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.smb.Macros.actions.ActionBase;
import net.smb.Macros.actions.ActionBreak;
import net.smb.Macros.actions.ActionCalcYawTo;
import net.smb.Macros.actions.ActionClearChat;
import net.smb.Macros.actions.ActionContinue;
import net.smb.Macros.actions.ActionDecode;
import net.smb.Macros.actions.ActionDo;
import net.smb.Macros.actions.ActionElse;
import net.smb.Macros.actions.ActionElseIf;
import net.smb.Macros.actions.ActionEncode;
import net.smb.Macros.actions.ActionFilter;
import net.smb.Macros.actions.ActionFor;
import net.smb.Macros.actions.ActionForeach;
import net.smb.Macros.actions.ActionGetID;
import net.smb.Macros.actions.ActionGetMatch;
import net.smb.Macros.actions.ActionGetSlot;
import net.smb.Macros.actions.ActionGetSlotItem;
import net.smb.Macros.actions.ActionGui;
import net.smb.Macros.actions.ActionIf;
import net.smb.Macros.actions.ActionLog;
import net.smb.Macros.actions.ActionMatch;
import net.smb.Macros.actions.ActionPick;
import net.smb.Macros.actions.ActionPopupMessage;
import net.smb.Macros.actions.ActionPow;
import net.smb.Macros.actions.ActionRandom;
import net.smb.Macros.actions.ActionReplace;
import net.smb.Macros.actions.ActionRun;
import net.smb.Macros.actions.ActionSQRT;
import net.smb.Macros.actions.ActionSend;
import net.smb.Macros.actions.ActionSetSlotItem;
import net.smb.Macros.actions.ActionSlot;
import net.smb.Macros.actions.ActionSlotClick;
import net.smb.Macros.actions.ActionSlotScroll;
import net.smb.Macros.actions.ActionStrip;
import net.smb.Macros.actions.ActionTime;
import net.smb.Macros.actions.ActionToLower;
import net.smb.Macros.actions.ActionToUpper;
import net.smb.Macros.actions.ActionWait;
import net.smb.Macros.actions.ActionWhile;
import net.smb.Macros.events.EventBase;
import net.smb.Macros.events.EventManagerClient;
import net.smb.Macros.events.EventManagerPlayer;
import net.smb.Macros.gui.Layout;
import net.smb.Macros.gui.LayoutManager;
import net.smb.Macros.gui.elements.GuiButtonWithIcon;
import net.smb.Macros.gui.screens.GuiScreenActiveMacros;
import net.smb.Macros.gui.screens.GuiScreenGuiEditor;
import net.smb.Macros.gui.screens.GuiScreenMenu;
import net.smb.Macros.gui.widgets.Widget;
import net.smb.Macros.util.Color;
import net.smb.Macros.util.Log;
import net.smb.Macros.util.Reflection;
import net.smb.Macros.util.RenderUtil;
import net.smb.Macros.variables.VariableProviderBase;
import net.smb.Macros.variables.VariableProviderPlayer;
import net.smb.Macros.variables.VariableProviderSettings;
import net.smb.Macros.variables.VariableProviderWorld;

@Mod(modid = MacroModCore.MODID, version = MacroModCore.VERSION)
public class MacroModCore {
 	public static final String MODID = "Macros Core";
    public static final String VERSION = "1.0";
    
    public static PacketHandler handler = null;
    
    private List<Integer> pressedKeys = new ArrayList<Integer>();
    private static Map<String, String> events = new HashMap<String, String>();
    private static List<EventBase> eventManagers = new ArrayList<EventBase>();
    private static List<VariableProviderBase> variableProviders = new ArrayList<VariableProviderBase>();
    private static List<Widget> currentWidgets = new ArrayList<Widget>();
    public static List<ActionBase> activeActions = new ArrayList<ActionBase>();
    private static KeyBinding openKey;
    
    String macros, mouseMacros, keyName;
    int keyId;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
    	Log.info("Initialising Macros Core");
    	MacrosSettings.loadSettings();
    	((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new Localisation());
        Localisation.loadLang();
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        CodeParser.globalParser = new CodeParser("global", null);
        
        new PacketHandler(Minecraft.getMinecraft().getNetHandler());
        
        // Initialize Actions
        Log.info("Initialising Actions");
        new ActionLog();
        new ActionPow();
        new ActionSend();
        new ActionIf();
        new ActionElseIf();
        new ActionElse();
        new ActionFor();
        new ActionWait();
        new ActionRun();
        new ActionFilter();
        new ActionCalcYawTo();
        new ActionEncode();
        new ActionDecode();
        new ActionGetID();
        new ActionToLower();
        new ActionToUpper();
        new ActionMatch();
        new ActionGetMatch();
        new ActionRandom();
        new ActionReplace();
        new ActionSQRT();
        new ActionStrip();
        new ActionTime();
        new ActionDo();
        new ActionBreak();
        new ActionForeach();
        new ActionContinue();
        new ActionWhile();
        new ActionClearChat();
        new ActionGetSlot();
        new ActionGetSlotItem();
        new ActionGui();
        new ActionSlotScroll();
        new ActionPick();
        new ActionPopupMessage();
        new ActionSetSlotItem();
        new ActionSlot();
        new ActionSlotClick();
        
        
        // Initialize Events
        Log.info("Initialising Events");
        registerEventManager(new EventManagerPlayer());
        registerEventManager(new EventManagerClient());
        
        Log.info("Registering Events");
        for(EventBase manager : eventManagers) {
        	FMLCommonHandler.instance().bus().register(manager);
        	MinecraftForge.EVENT_BUS.register(manager);
        }
        
        Log.info("Registering Variable Providers");
        registerVariableProvider(new VariableProviderPlayer());
        registerVariableProvider(new VariableProviderWorld());
        registerVariableProvider(new VariableProviderSettings());
        
        Log.info("Loading Layouts");
        LayoutManager.loadLayout();
        
        try {
	        Locale locale = Reflection.getPrivateValue(I18n.class, I18n.class, "field_135054_a");
	        Map<String, String> translation = Reflection.getPrivateValue(Locale.class, locale, "field_135032_a");
	        translation.put(Localisation.getString("key.categories.macroscore"), "key.categories.macroscore");
	        translation.put(Localisation.getString("key.macrosmenu"), "key.macrosmenu");
	        
	        openKey = new KeyBinding("key.macrosmenu", MacrosSettings.getInt("openKey"), "key.categories.macroscore");
	        
	        LinkedList<KeyBinding> keyBindings = new LinkedList<KeyBinding>();
	        keyBindings.addAll(Arrays.asList(mc.gameSettings.keyBindings));
	        keyBindings.add(openKey);
	        mc.gameSettings.keyBindings = keyBindings.toArray(new KeyBinding[0]);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onMouseInput(MouseEvent event) {
    	Minecraft mc = Minecraft.getMinecraft();
    	if(mc.currentScreen == null) {
			int mouseButton = -1;
			if(event.buttonstate) mouseButton = event.button;
			mouseMacros = "";
			keyName = "";
			
			if(mouseButton == 0) {
				mouseMacros = MacrosSettings.getString("key_1001");
				keyName = "LMouse";
			}
			else if(mouseButton == 1) {
				mouseMacros = MacrosSettings.getString("key_1002");
				keyName = "RMouse";
			}
			else if(mouseButton == 2) {
				mouseMacros = MacrosSettings.getString("key_1006");
				keyName = "MiddleMouse";
			}
			else if(mouseButton == 3) {
				mouseMacros = MacrosSettings.getString("key_1003");
				keyName = "Mouse3";
			}
			else if(mouseButton == 4) {
				mouseMacros = MacrosSettings.getString("key_1004");
				keyName = "Mouse4";
			}
			
			if(!mouseMacros.equals("")) {
				new Thread(new Runnable() {
					String threadMacros = mouseMacros;
					String threadkeyName = keyName;
					@Override
					public void run() {
						CodeParser parser = new CodeParser("[" + threadkeyName + "]", null);
						parser.executeCode(threadMacros);
					}
				}).start();
			}
    		
			mouseMacros = "";
			keyName = "";
			int mouseScroll = Math.max(Math.min(event.dwheel,1),-1);
			if(mouseScroll == -1) {
				mouseMacros = MacrosSettings.getString("key_1007");
				keyName = "MouseScrollDown";
			}
			else if(mouseScroll == 1) {
				mouseMacros = MacrosSettings.getString("key_1005");
				keyName = "MouseScrollUp";
			}
			
			if(!mouseMacros.equals("")) {
				new Thread(new Runnable() {
					String threadMacros = mouseMacros;
					String threadkeyName = keyName;
					@Override
					public void run() {
						CodeParser parser = new CodeParser("[" + threadkeyName + "]", null);
						parser.executeCode(threadMacros);
					}
				}).start();
			}
    	}
    }
    
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
    	Minecraft mc = Minecraft.getMinecraft();
    	if(Keyboard.isKeyDown(openKey.getKeyCode()) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && mc.currentScreen == null) {
    		mc.displayGuiScreen(new GuiScreenActiveMacros(Minecraft.getMinecraft()));
    	}
    	else if(Keyboard.isKeyDown(openKey.getKeyCode()) && mc.currentScreen == null) {
    		mc.displayGuiScreen(new GuiScreenMenu(Minecraft.getMinecraft()));
    	}
    	
    	
    	if(Keyboard.getEventKey() != 0 && Keyboard.getEventKeyState() && mc.currentScreen == null) {
    		keyId = Keyboard.getEventKey();
    		if(pressedKeys.indexOf(keyId) == -1) {
    			pressedKeys.add(keyId);
    			macros = MacrosSettings.getString("key_" + keyId);
				if(!macros.equals("")) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							CodeParser parser = new CodeParser("[" + Keyboard.getKeyName(keyId) + "]", null);
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
    	
    	if(event.phase == TickEvent.Phase.END) {
    		for(ActionBase action : activeActions) action.onTick();
    	}
    }
    
    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event)
    {
    	if(event.phase == TickEvent.Phase.END) {
    		if(MacrosSettings.getBool("debug")) {
    			Minecraft mc = Minecraft.getMinecraft();
        		RenderUtil.zLevel = 201;
        		ScaledResolution var2 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        		int elementsInColumn = (var2.getScaledHeight()-10)/10;
        		int i = 0;
        		Object[] vars = CodeParser.globalVars.entrySet().toArray();
        		for(Object paramObj : vars) {
        			Entry<String, Object> param = (Entry<String, Object>) paramObj;
        			RenderUtil.drawString("\u00A76" + param.getKey() + "\u00A7r: " + param.getValue(), 10+100*(i/elementsInColumn), 10+10*(i%elementsInColumn), Color.color_4, false, 1.0F, false);
        			i++;
        		}
        		RenderUtil.zLevel = 0;
        	}
    	}
    }
    
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event)
    {
    	Minecraft mc = Minecraft.getMinecraft();
    	if(mc.currentScreen == null && currentWidgets.size() > 0) {
            for(Widget widget : currentWidgets) widget.drawButton(mc,0,0);
    	}
    }
    
    @SubscribeEvent
    public void onGuiScreenDraw(GuiScreenEvent.DrawScreenEvent event)
    {
    	Minecraft mc = Minecraft.getMinecraft();
    	if(MacrosSettings.getBool("slotid") && mc.currentScreen instanceof GuiContainer) {
    		try {
	    		GuiContainer container = (GuiContainer) mc.currentScreen;
	    		Slot slot = (Slot) GuiContainer.class.getDeclaredMethod("func_146975_c", int.class, int.class).invoke(container, event.mouseX, event.mouseY);
	    		String desc = "\u00A76Slot " + slot.getSlotIndex();
	    		int descWidth = RenderUtil.fontRenderer.getStringWidth(desc);
	    		
	    		int dPosX = 0;
				int dPosY = 0;
				if(slot.getStack() != null) {
					dPosX = Math.max(Math.min(event.mouseX+8, mc.currentScreen.width-descWidth), 0);
					dPosY = Math.max(Math.min(event.mouseY-30, mc.currentScreen.height-10), 0);
				}
				else {
		    		dPosX = Math.max(Math.min(event.mouseX+8, mc.currentScreen.width-descWidth), 0);
					dPosY = Math.max(Math.min(event.mouseY-10, mc.currentScreen.height-10), 0);
				}
				RenderUtil.zLevel = 200;
				RenderUtil.setColor(Color.color_3, 0.7F);
				RenderUtil.drawRect(dPosX, dPosY, dPosX+descWidth+2, dPosY+12);
				RenderUtil.drawString(desc, dPosX+2, dPosY+2, Color.color_4, false);
				RenderUtil.zLevel = 0;
    		} catch(Exception e) {}
    	}
    }
    
    @SubscribeEvent
    public void onGuiScreenClick(GuiScreenEvent.ActionPerformedEvent event)
    {
    	Minecraft mc = Minecraft.getMinecraft();
    	if(mc.currentScreen instanceof GuiChat && event.button instanceof GuiButtonWithIcon && ((GuiButtonWithIcon)event.button).id == 10) {
    		Layout layout = LayoutManager.bindedLayouts.get("inchat");
    		if(layout != null) mc.displayGuiScreen(new GuiScreenGuiEditor(mc, layout, mc.currentScreen));
    	}
    }
    
    public static void onShowGui(GuiScreen gui) {
    	if(gui != null) {
    		if(gui instanceof GuiChat) {
	    		Layout layout = LayoutManager.bindedLayouts.get("inchat");
	    		if(layout != null) {
	    			try {
	    				List list = Reflection.getPrivateValue(GuiScreen.class, gui, "field_146292_n");
	    				list.addAll(layout.widgets);
	    			} catch(Exception e) {e.printStackTrace();}
	    		}
	    		if(!MacrosSettings.getBool("chatbutton")) {
		    		try {
		    			Minecraft mc = Minecraft.getMinecraft();
		    			GuiChat chat = (GuiChat) gui;
		    			//GuiTextField field = Reflection.getPrivateValue(GuiChat.class, chat, "field_146415_a");
		    			List list = Reflection.getPrivateValue(GuiScreen.class, gui, "field_146292_n");
	    				list.add(new GuiButtonWithIcon(10, chat.width-15, chat.height-14, 10, 1));
		    		}
		    		catch(Exception e) {
		    			e.printStackTrace();
		    		}
	    		}
    		}
    		else if(gui instanceof GuiInventory || gui instanceof GuiContainerCreative) {
    			Layout layout = LayoutManager.bindedLayouts.get("ininventory");
	    		if(layout != null) {
	    			try {
	    				List list = Reflection.getPrivateValue(GuiScreen.class, gui, "field_146292_n");
	    				list.addAll(layout.widgets);
	    			} catch(Exception e) {e.printStackTrace();}
	    		}
    		}
    	}
    	else {
    		Layout layout = LayoutManager.bindedLayouts.get("ingame");
    		if(layout != null) {
    			try {
    				currentWidgets = layout.widgets;
    			} catch(Exception e) {e.printStackTrace();}
    		}
    	}
    }
    
    public static void registerAction(String actionName, ActionBase instance) {
    	CodeParser.addAction(actionName, instance);
    }
    
    public static void registerEvent(String eventName, String description) {
    	if(!hasEvent(eventName)) {
    		events.put(eventName, description);
    		Log.info("- " + eventName);
    	}
    	else Log.info("Event  " + eventName + " already exists");
    }
    
    public static void registerEventManager(EventBase manager) {
    	eventManagers.add(manager);
    	manager.init();
    }
    
    public static boolean hasEvent(String eventName) {
    	for(Map.Entry<String, String> event : events.entrySet()) if(event.getKey().equals(eventName)) return true;
    	return false;
    }
    
    public static List<String> getEventsList(){
    	List<String> eventsList = new ArrayList<String>();
    	for(Map.Entry<String, String> event : events.entrySet()) eventsList.add(event.getKey());
    	return eventsList;
    }
    
    public static String getEventDescription(String key) {
    	return events.get(key);
    }
    
    public static void saveControls() {
    	MacrosSettings.setParam("openKey", openKey.getKeyCode());
    }
    
    public static void registerVariableProvider(VariableProviderBase provider) {
    	FMLCommonHandler.instance().bus().register(provider);
    	MinecraftForge.EVENT_BUS.register(provider);
    	variableProviders.add(provider);
    	provider.init();
    }
}
