package net.smb.Macros.actions;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.smb.Macros.CodeParser;

public class ActionTime extends ActionBase {

	public ActionTime() {
		super("time");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			String formatPattern = parser.getString(args[0]);
			try {
				SimpleDateFormat format = new SimpleDateFormat(formatPattern);
				return format.format(new Date());
			} catch(Exception e) {
				return "Incorrect date format";
			}
		}
		return "";
	}
}
