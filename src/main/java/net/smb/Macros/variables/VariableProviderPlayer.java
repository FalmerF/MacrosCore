package net.smb.Macros.variables;

import java.util.HashMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.smb.Macros.util.Reflection;

public class VariableProviderPlayer extends VariableProviderBase {
	private String[] blockSides = new String[]{"B", "T", "N", "S", "W", "E"};
	
	@Override
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityClientPlayerMP player = mc.thePlayer;
		World world = mc.theWorld;
		
		if(player != null) {
			int posX = (int)Math.floor(player.posX);
			int posY = (int)Math.floor(player.posY);
			int posZ = (int)Math.floor(player.posZ);
			
			float pitch = player.rotationPitch%360.0f;
			while (pitch < 0) pitch += 360;
			
			float yaw = player.rotationYaw%360.0f;
			while (yaw < 0) yaw += 360;
			
			int gamemode = player.capabilities.isCreativeMode ? 1 : 0;
			if(!player.capabilities.allowEdit) gamemode = 2;
			
			String vehicle = "none";
            float vehicleHealth = 0.0f;
            if (player.ridingEntity != null) {
                vehicle = player.ridingEntity.getCommandSenderName();
                vehicleHealth = player.ridingEntity instanceof EntityLiving ? ((EntityLiving)player.ridingEntity).getHealth() : (player.ridingEntity instanceof EntityMinecart ? 40.0f - ((EntityMinecart)player.ridingEntity).getDamage() : (player.ridingEntity instanceof EntityBoat ? 40.0f - ((EntityBoat)player.ridingEntity).getDamageTaken() : 0.0f));
            }
            
            String direction = "S";
            if (yaw >= 45 && yaw < 135) {
            	direction = "W";
            }
            else if (yaw >= 135 && yaw < 225) {
            	direction = "N";
            }
            else if (yaw >= 225 && yaw < 315) {
            	direction = "E";
            }
            
            String biomeName = "?";
            if (mc.theWorld != null && mc.theWorld.blockExists(posX, posY, posZ)) {
                Chunk playerChunk = world.getChunkFromBlockCoords(posX, posZ);
                biomeName = playerChunk.getBiomeGenForWorldCoords((int)(posX & 15), (int)(posZ & 15), (WorldChunkManager)mc.theWorld.getWorldChunkManager()).biomeName;
            }
			
			setVar("player", player.getDisplayName());
			setVar("armour", player.getTotalArmorValue());
			setVar("health", player.getHealth());
			setVar("canFly", player.capabilities.allowFlying);
			setVar("flying", player.capabilities.isFlying);
			setVar("hunger", player.getFoodStats().getFoodLevel());
			setVar("isCreative", player.capabilities.isCreativeMode);
			setVar("gamemode", gamemode);
			setVar("level", player.experienceLevel);
			setVar("totalXP", player.experienceTotal);
			setVar("XP", player.experience*player.xpBarCap());
			setVar("posX", posX);
			setVar("posY", posY);
			setVar("posZ", posZ);
			setVar("light", world.getBlockLightValue(posX, posY, posZ));
			setVar("oxygen", player.getAir());
			setVar("slot", player.inventory.currentItem);
			setVar("saturation", player.getFoodStats().getSaturationLevel());
			setVar("yaw", yaw);
			setVar("pitch", pitch);
			setVar("vehicle", vehicle);
			setVar("vehicleHealth", vehicleHealth);
			setVar("direction", direction);
			setVar("dimension", getNameForDimension(player.dimension, player.worldObj));
			setVar("biome", biomeName);
			
			setItemInfo(player.inventory.armorItemInSlot(3), "helm");
			setItemInfo(player.inventory.armorItemInSlot(2), "chestplate");
			setItemInfo(player.inventory.armorItemInSlot(1), "leggings");
			setItemInfo(player.inventory.armorItemInSlot(0), "boots");
			
			ItemStack item = player.inventory.getCurrentItem();
			setItemInfo(item, "item");
			if(item != null) {
				setVar("itemStackSize", item.stackSize);
			}
			else setVar("itemStackSize", 0);
		}
		else {
			setVar("player", "Player");
			setVar("armour", 0);
			setVar("health", 20);
			setVar("canFly", false);
			setVar("flying", false);
			setVar("hunger", 20);
			setVar("isCreative", false);
			setVar("gamemode", 0);
			setVar("level", 0);
			setVar("totalXP", 0);
			setVar("XP", 0);
			setVar("posX", 0);
			setVar("posY", 0);
			setVar("posZ", 0);
			setVar("light", 0);
			setVar("oxygen", 0);
			setVar("slot", 0);
			setVar("saturation", 0);
			setVar("yaw", 0);
			setVar("pitch", 0);
			setVar("vehicle", "none");
			setVar("vehicleHealth", 0);
			setVar("direction", "?");
			setVar("dimension", "?");
			setVar("biome", "");
		}
		
		MovingObjectPosition objectHit = mc.objectMouseOver;
		if(player != null && objectHit != null && objectHit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			Block block = world.getBlock(objectHit.blockX, objectHit.blockY, objectHit.blockZ);
			TileEntity tile = world.getTileEntity(objectHit.blockX, objectHit.blockY, objectHit.blockZ);
			int side = objectHit.sideHit;
			
			setVar("hit", "block");
			setVar("hitName", block.getLocalizedName());
			setVar("hitId", Block.getIdFromBlock(block));
			setVar("hitMeta", world.getBlockMetadata(objectHit.blockX, objectHit.blockY, objectHit.blockZ));
			setVar("hitX", objectHit.blockX);
			setVar("hitY", objectHit.blockY);
			setVar("hitZ", objectHit.blockZ);
			setVar("hitSide", side > -1 && side < 7 ? blockSides[side] : "?");
			
			int blockDamage = 0;
			
			try {
				HashMap<Integer, DestroyBlockProgress> damagedBlocks = Reflection.getPrivateValue(RenderGlobal.class, mc.renderGlobal, "field_72738_E");
				if (damagedBlocks != null) {
	                for (DestroyBlockProgress damage : damagedBlocks.values()) {
	                    if (damage.getPartialBlockX() == objectHit.blockX && damage.getPartialBlockY() == objectHit.blockY && damage.getPartialBlockZ() == objectHit.blockZ)
	                    	blockDamage = Math.max(0, damage.getPartialBlockDamage());
	                }
	            }
			} catch(Exception e) {}
			setVar("hitProgress", blockDamage);
			
			if(tile != null && (tile.blockType == Blocks.standing_sign || tile.blockType == Blocks.wall_sign)) {
				TileEntitySign sign = (TileEntitySign) tile;
				setVar("signLine1", sign.signText[0]);
				setVar("signLine2", sign.signText[1]);
				setVar("signLine3", sign.signText[2]);
				setVar("signLine4", sign.signText[3]);
			}
			else {
				setVar("signLine1", "");
				setVar("signLine2", "");
				setVar("signLine3", "");
				setVar("signLine4", "");
			}
		}
		else if(player != null && objectHit != null && objectHit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && objectHit.entityHit != null) {
			 if (objectHit.entityHit instanceof EntityPlayer) {
	                EntityPlayer entityPlayer = (EntityPlayer)objectHit.entityHit;
	                setVar("hit", "PLAYER");
	                setVar("hitName", entityPlayer.getCommandSenderName());
	                setVar("hitId", entityPlayer.getEntityId());
	            } else {
	                String entityName = EntityList.getEntityString((Entity)objectHit.entityHit);
	                if (entityName == null) {
	                    entityName = objectHit.entityHit.getClass().getSimpleName();
	                }
	                setVar("hit", "ENTITY");
	                setVar("hitName", entityName);
	                setVar("hitId", EntityList.getEntityID((Entity)objectHit.entityHit));
	            }
			 setVar("hitMeta", 0);
			 setVar("hitX", 0);
			 setVar("hitY", 0);
			 setVar("hitZ", 0);
			 setVar("hitSide", "?");
		}
		 else {
			 setVar("hit", "none");
			 setVar("hitName", "none");
			 setVar("hitId", 0);
			 setVar("hitMeta", 0);
			 setVar("hitX", 0);
			 setVar("hitY", 0);
			 setVar("hitZ", 0);
			 setVar("hitSide", "?");
			 setVar("signLine1", "");
			 setVar("signLine2", "");
			 setVar("signLine3", "");
			 setVar("signLine4", "");
        }
	}
	
	public void setItemInfo(ItemStack item, String name) {
		if(item != null) {
			setVar(name + "Id", Item.getIdFromItem(item.getItem()));
			setVar(name + "Name", item.getDisplayName());
			setVar(name + "Code", item.getUnlocalizedName());
			setVar(name + "Meta", item.getItemDamage());
			setVar(name + "Durability", item.getMaxDamage() - item.getItemDamageForDisplay());
			setVar(name + "MaxDurability", item.getMaxDamage());
		}
		else {
			setVar(name + "Id", 0);
			setVar(name + "Name", "none");
			setVar(name + "Code", "none");
			setVar(name + "Meta", 0);
			setVar(name + "Durability", 0);
			setVar(name + "MaxDurability", 0);
		}
	}
	
	public String getNameForDimension(int dimension, World worldObj) {
        switch (dimension) {
            case -1: {
                return "nether";
            }
            case 0: {
                return "surface";
            }
            case 1: {
                return "end";
            }
        }
        return worldObj == null || worldObj.provider == null ? "?" : ("" + worldObj.provider.getDimensionName()).toUpperCase();
    }
}
