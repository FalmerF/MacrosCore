package net.smb.Macros.variables;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.smb.Macros.CodeParser;

public class VariableProviderBase implements IVariableProvider {

	@Override
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		
	}

	@Override
	public void init() {
	}
	
	@Override
	public void setVar(String key, Object value) {
		CodeParser.globalVars.put(key, value);
	}
}
