package net.smb.Macros.variables;

import java.text.SimpleDateFormat;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.world.World;
import net.smb.Macros.util.Reflection;

public class VariableProviderSettings extends VariableProviderBase {
	private GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
	
	@Override
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityClientPlayerMP player = mc.thePlayer;
		World world = mc.theWorld;
		
		if(gameSettings != null) {
			setVar("fov", this.getOptionIntValue(GameSettings.Options.FOV, 70.0f, 110.0f));
			setVar("gamma", this.getOptionIntValue(GameSettings.Options.GAMMA, 0.0f, 100.0f));
			setVar("sensitivity", this.getOptionIntValue(GameSettings.Options.SENSITIVITY, 0.0f, 200.0f));
			setVar("music", this.getSoundLevel(SoundCategory.MUSIC, 0.0f, 100.0f));
			setVar("sound", this.getSoundLevel(SoundCategory.MASTER, 0.0f, 100.0f));
			setVar("recordVolume", this.getSoundLevel(SoundCategory.RECORDS, 0.0f, 100.0f));
			setVar("weatherVolume", this.getSoundLevel(SoundCategory.WEATHER, 0.0f, 100.0f));
			setVar("blockVolume", this.getSoundLevel(SoundCategory.BLOCKS, 0.0f, 100.0f));
			setVar("hostileVolume", this.getSoundLevel(SoundCategory.MOBS, 0.0f, 100.0f));
			setVar("neutralVolume", this.getSoundLevel(SoundCategory.ANIMALS, 0.0f, 100.0f));
			setVar("playerVolume", this.getSoundLevel(SoundCategory.PLAYERS, 0.0f, 100.0f));
			setVar("ambientVolume", this.getSoundLevel(SoundCategory.AMBIENT, 0.0f, 100.0f));
			try {
				setVar("FPS", Reflection.getPrivateValue(Minecraft.class, mc, "field_71470_ab"));
			} catch(Exception e) {setVar("FPS", 0);}
		}
	}
	
	protected int getOptionIntValue(GameSettings.Options option, float min, float max) {
        return (int)(min + this.gameSettings.getOptionFloatValue(option) * (max - min));
    }

    protected int getSoundLevel(SoundCategory category, float min, float max) {
        return (int)(min + this.gameSettings.getSoundLevel(category) * (max - min));
    }
}
