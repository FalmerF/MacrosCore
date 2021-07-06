package net.smb.Macros.actions;

import net.minecraft.client.Minecraft;
import net.smb.Macros.CodeParser;

public class ActionCalcYawTo extends ActionBase {

	public ActionCalcYawTo() {
		super("calcYawTo");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 1) {
	        Minecraft mc = Minecraft.getMinecraft();
	        if (mc.thePlayer != null) {
	            float posX = parser.getFloat(args[0]) + 0.5f;
	            float posZ = parser.getFloat(args[1]) + 0.5f;
	            double deltaX = (double)posX - mc.thePlayer.posX;
	            double deltaZ = (double)posZ - mc.thePlayer.posZ;
	            float yaw;
	            for (yaw = (float) (Math.atan2(deltaZ, deltaX)*180.0/Math.PI-90); yaw < 0; yaw += 360) {
	            }
	            return yaw;
	        }
		}
		return null;
	}
}
