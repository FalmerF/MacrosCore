package net.smb.Macros.actions;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.smb.Macros.CodeParser;

public class ActionGetID extends ActionBase {

	public ActionGetID() {
		super("getID");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 2) {
			int posX = parser.getInt(args[0]);
			int posY = parser.getInt(args[1]);
			int posZ = parser.getInt(args[2]);
			World world = Minecraft.getMinecraft().theWorld;
			if(world != null) {
				Block block = world.getBlock(posX, posY, posZ);
				if(block != null) {
					String id = String.valueOf(Block.getIdFromBlock(block));
					int meta = world.getBlockMetadata(posX, posY, posZ);
					if(meta != 0) id += ":" + meta;
					return id;
				}
				else return "0";
			}
		}
		return null;
	}
}
