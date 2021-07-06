package net.smb.Macros.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.smb.Macros.MacroModCore;
import net.smb.Macros.PacketHandler;
import net.smb.Macros.util.Log;

public class EventManagerPlayer extends EventBase {
	public void init() {
		MacroModCore.registerEvent("onJoinGame", "");
		MacroModCore.registerEvent("onPlayerJoined", "");
		MacroModCore.registerEvent("onPlayerLeaved", "");
		MacroModCore.registerEvent("onHealthChange", "");
		MacroModCore.registerEvent("onFoodChange", "");
		MacroModCore.registerEvent("onOxygenChange", "");
		MacroModCore.registerEvent("onXpChange", "");
		MacroModCore.registerEvent("onLevelChange", "");
		MacroModCore.registerEvent("onArmourChange", "");
		MacroModCore.registerEvent("onArmourDurabilityChange", "");
		MacroModCore.registerEvent("onItemDurabilityChange", "");
		MacroModCore.registerEvent("onInventorySlotChange", "");
		MacroModCore.registerEvent("onModeChange", "");
		MacroModCore.registerEvent("onDeath", "");
		MacroModCore.registerEvent("onRespawn", "");
		MacroModCore.registerEvent("onWeatherChange", "");
	}
	
	List<String> oldPlayerNames = null;
	private boolean inGame;
	
	private float playerHealth = 0;
	private int playerFood = 0;
	private int oxygen = 0;
	private float playerXp = 0;
	private int xplayerXpLevel = 0;
	private int playerArmour = 0;
	private float rainStrength = 0;
	
	private ItemStack lastItemStack = null;
	private ItemStack lastHelm = null;
	private ItemStack lastChestPlate = null;
	private ItemStack lastLeggings = null;
	private ItemStack lastBoots = null;
	
	private int itemDurability = 0;
	
	private int lastInventorySlot = -1;
	
	private boolean inCreative = false;
	
	@SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
		try {
		Minecraft mc = Minecraft.getMinecraft();
		EntityClientPlayerMP thePlayer = mc.thePlayer;
		if(thePlayer!= null && !inGame) {
			inGame = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						execute("onJoinGame");
						if(MacroModCore.handler == null) MacroModCore.handler = new PacketHandler(Minecraft.getMinecraft().getNetHandler());
					} catch(Exception e) {}
				}
			}).start();
			MacroModCore.onShowGui(null);
			
			playerHealth = thePlayer.getHealth();
			playerFood = thePlayer.getFoodStats().getFoodLevel();
			oxygen = thePlayer.getAir();
			playerXp = thePlayer.experience;
			xplayerXpLevel = thePlayer.experienceLevel;
			playerArmour = thePlayer.getTotalArmorValue();
			rainStrength = mc.theWorld.getRainStrength(0.0F);
			lastInventorySlot = thePlayer.inventory.currentItem;
			inCreative = mc.playerController.isInCreativeMode();
			
			lastItemStack = thePlayer.inventory.getCurrentItem();
			if(lastItemStack != null) itemDurability = lastItemStack.getItemDamageForDisplay();
			else itemDurability = 0;
			
			lastHelm = thePlayer.inventory.armorItemInSlot(3);
			lastChestPlate = thePlayer.inventory.armorItemInSlot(2);
			lastLeggings = thePlayer.inventory.armorItemInSlot(1);
			lastBoots = thePlayer.inventory.armorItemInSlot(0);
		}
		else if(thePlayer == null && inGame) {
			inGame = false;
			oldPlayerNames = null;
		}
		
		if(thePlayer != null) {
			if(playerHealth != thePlayer.getHealth()) {
				playerHealth = thePlayer.getHealth();
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("health", playerHealth);
				execute("onHealthChange", vars);
			}
			if(playerFood != thePlayer.getFoodStats().getFoodLevel()) {
				playerFood = thePlayer.getFoodStats().getFoodLevel();
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("food", playerFood);
				execute("onFoodChange", vars);
			}
			if(oxygen != thePlayer.getAir()) {
				oxygen = thePlayer.getAir();
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("oxygen", oxygen);
				execute("onOxygenChange", vars);
			}
			if(xplayerXpLevel != thePlayer.experienceLevel) {
				xplayerXpLevel = thePlayer.experienceLevel;
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("level", xplayerXpLevel);
				execute("onLevelChange", vars);
			}
			if(playerXp != thePlayer.experience) {
				playerXp = thePlayer.experience;
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("xp", playerXp);
				execute("onXpChange", vars);
			}
			if(playerArmour != thePlayer.getTotalArmorValue()) {
				playerArmour = thePlayer.getTotalArmorValue();
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("armour", playerArmour);
				execute("onArmourChange", vars);
			}
			if(rainStrength != mc.theWorld.getRainStrength(0.0F)) {
				rainStrength = mc.theWorld.getRainStrength(0.0F);
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("rain", rainStrength);
				execute("onWeatherChange", vars);
			}
			if(lastInventorySlot != thePlayer.inventory.currentItem) {
				lastInventorySlot = thePlayer.inventory.currentItem;
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("slot", lastInventorySlot);
				execute("onInventorySlotChange", vars);
			}
			if(inCreative != mc.playerController.isInCreativeMode()) {
				inCreative = mc.playerController.isInCreativeMode();
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("isCreative", inCreative);
				execute("onModeChange", vars);
			}
			
			if(lastItemStack != thePlayer.inventory.getCurrentItem()) {
				lastItemStack = thePlayer.inventory.getCurrentItem();
				if(lastItemStack != null) itemDurability = lastItemStack.getItemDamageForDisplay();
				else itemDurability = 0;
			}
			if(lastHelm != thePlayer.inventory.armorItemInSlot(3)) {
				lastHelm = thePlayer.inventory.armorItemInSlot(3);
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("durability", lastHelm.getItemDamageForDisplay());
				vars.put("item", "helm");
				execute("onArmourDurabilityChange", vars);
			}
			if(lastChestPlate != thePlayer.inventory.armorItemInSlot(2)) {
				lastChestPlate = thePlayer.inventory.armorItemInSlot(2);
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("durability", lastChestPlate.getItemDamageForDisplay());
				vars.put("item", "chestplate");
				execute("onArmourDurabilityChange", vars);
			}
			if(lastLeggings != thePlayer.inventory.armorItemInSlot(1)) {
				lastLeggings = thePlayer.inventory.armorItemInSlot(1);
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("durability", lastLeggings.getItemDamageForDisplay());
				vars.put("item", "leggings");
				execute("onArmourDurabilityChange", vars);
			}
			if(lastBoots != thePlayer.inventory.armorItemInSlot(0)) {
				lastBoots = thePlayer.inventory.armorItemInSlot(0);
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("durability", lastBoots.getItemDamageForDisplay());
				vars.put("item", "boots");
				execute("onArmourDurabilityChange", vars);
			}
			
			if(lastItemStack != null && itemDurability != lastItemStack.getItemDamageForDisplay()) {
				itemDurability = lastItemStack.getItemDamageForDisplay();
				Map<String, Object> vars = new TreeMap<String, Object>();
				vars.put("durability", itemDurability);
				execute("onItemDurabilityChange", vars);
			}
		}
		
		main:
		if (!Minecraft.getMinecraft().isGamePaused() && Minecraft.getMinecraft().thePlayer != null) {
			ArrayList<GuiPlayerInfo> playerList = (ArrayList<GuiPlayerInfo>) thePlayer.sendQueue.playerInfoList;
			List<String> playerNames = new ArrayList<String>();
			for(GuiPlayerInfo player: playerList) playerNames.add(player.name);
			if(playerNames.size() != 0 && oldPlayerNames == null) {
				oldPlayerNames = playerNames;
				break main;
			}
			else if(oldPlayerNames == null) break main;
			for(String playerInfo : playerNames) {
				if(oldPlayerNames.indexOf(playerInfo) == -1) {
					Map<String, Object> vars = new TreeMap<String, Object>();
					vars.put("player", playerInfo);
					execute("onPlayerJoined", vars);
				}
			}
			for(String playerInfo : oldPlayerNames) {
				if(playerNames.indexOf(playerInfo) == -1) {
					Map<String, Object> vars = new TreeMap<String, Object>();
					vars.put("player", playerInfo);
					execute("onPlayerLeaved", vars);
				}
			}
			oldPlayerNames = playerNames;
		}
		}catch(Exception e) {}
    }
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if(event.player.getDisplayName().equals(Minecraft.getMinecraft().thePlayer.getDisplayName()))
			execute("onRespawn");
	}
	
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if(event.entityLiving instanceof EntityPlayerMP && ((EntityPlayerMP)event.entityLiving).getDisplayName().equals(Minecraft.getMinecraft().thePlayer.getDisplayName()))
			execute("onDeath");
	}
}
