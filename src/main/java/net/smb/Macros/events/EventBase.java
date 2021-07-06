package net.smb.Macros.events;

import java.util.Map;

import net.smb.Macros.CodeParser;
import net.smb.Macros.MacrosSettings;

public class EventBase implements IEvent {
	@Override
	public void init() {}
	
	protected void execute(String event) {
		execute(event, null);
	}
	
	protected void execute(String event, Map<String, Object> vars) {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					CodeParser newParser = new CodeParser("[" + event + "]", null);
					if(vars != null) newParser.vars.putAll(vars);
					newParser.executeCode(MacrosSettings.getString("event_" + event));
				}
			}).start();
		} catch(Exception e) {e.printStackTrace();}
	}
}
