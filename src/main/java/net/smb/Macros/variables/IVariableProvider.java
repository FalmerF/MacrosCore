package net.smb.Macros.variables;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public abstract interface IVariableProvider {
	@SubscribeEvent
    public void onClientTick(ClientTickEvent event);
	
	public void init();
	
	public void setVar(String key, Object value);
}
