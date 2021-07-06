package net.smb.Macros.actions;

import java.util.Random;

import net.smb.Macros.CodeParser;

public class ActionRandom extends ActionBase {
	private static Random rand = new Random();

	public ActionRandom() {
		super("random");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 1) {
			int min = parser.getInt(args[0]);
            int max = parser.getInt(args[1]);
            if (max < min) {
                int swap = min;
                min = max;
                max = swap;
            }
            return rand.nextInt(max - min + 1) + min;
		}
		return 0;
	}
}
