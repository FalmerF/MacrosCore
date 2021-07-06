package net.smb.Macros.actions;

import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.smb.Macros.CodeParser;
import net.smb.Macros.util.Log;

public class ActionGetMatch extends ActionBase {

	public ActionGetMatch() {
		super("getMatch");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 1) {
			String text = parser.getString(args[0]);
			String regular = parser.getString(args[1]);
			TreeMap<Integer, String> array = new TreeMap<Integer, String>();
			array.put(-1, "string");
			try {
				Matcher matcher = Pattern.compile(regular).matcher(text);
				matcher.matches();
				for(int i = 0; i < matcher.groupCount(); i++) {
					array.put(i, matcher.group(i));
				}
				return array;
			} catch(Exception e) {
				return array;
			}
		}
		return null;
	}
}
