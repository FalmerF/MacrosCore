package net.smb.Macros.variables;

import java.text.SimpleDateFormat;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

public class VariableProviderWorld extends VariableProviderBase {
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityClientPlayerMP player = mc.thePlayer;
		World world = mc.theWorld;
		
		if(world != null) {
			long totalWorldTime = world.getTotalWorldTime();
            int totalWorldTicks = (int)(totalWorldTime % 24000L);
            long worldTime = world.getWorldTime();
            int worldTicks = (int)(worldTime % 24000L);
            int dayTicks = (int)((worldTime + 6000L) % 24000L);
            int dayHour = dayTicks / 1000;
            int dayMinute = (int)((double)(dayTicks % 1000) * 0.06);
            
			setVar("totalTicks", totalWorldTicks);
			setVar("ticks", worldTicks);
			setVar("dayTicks", dayTicks);
			setVar("dayTime", String.format("%02d:%02d", dayHour, dayMinute));
			setVar("seed", world.getSeed());
			setVar("rain", (int)(world.getRainStrength(0.0f) * 100.0f));
			setVar("difficulty", world.difficultySetting);
			setVar("chunkUpdates", WorldRenderer.chunksUpdated);
			
			if(mc.func_147104_D() != null) {
				setVar("server", mc.func_147104_D().serverName);
				setVar("serverMOTD", mc.func_147104_D().serverMOTD);
				
				if (player != null && player.sendQueue != null) {
		            int maxPlayers = player.sendQueue.currentServerMaxPlayers;
		            setVar("onlinePlayers", player.sendQueue.playerInfoList.size());
		            setVar("maxPlayers", maxPlayers);
		        } else {
		        	setVar("onlinePlayers", 0);
		        	setVar("maxPlayers", 0);
		        }
			}
			else {
				setVar("server", mc.getIntegratedServer().getWorldName());
				setVar("serverMOTD", "none");
				setVar("onlinePlayers", 0);
	        	setVar("maxPlayers", 0);
			}
		}
		else {
			setVar("totalTicks", 0);
			setVar("ticks", 0);
			setVar("dayTicks", 0);
			setVar("dayTime", "00:00");
			setVar("seed", "0");
			setVar("rain", 0);
			setVar("server", "none");
			setVar("serverMOTD", "none");
			setVar("onlinePlayers", 0);
        	setVar("maxPlayers", 0);
        	setVar("difficulty", "none");
        }
		
		setVar("dateTime", dateTimeFormatter.format(System.currentTimeMillis()));
		setVar("date", dateFormatter.format(System.currentTimeMillis()));
		setVar("time", timeFormatter.format(System.currentTimeMillis()));
	}
}
