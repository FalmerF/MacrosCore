package net.smb.Macros.actions;

import net.smb.Macros.CodeParser;

public class ActionStop extends ActionBase {

	public ActionStop() {
		super("stop");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			String parserName = parser.getString(args[0]);
			for(CodeParser codeParser : CodeParser.activeParsers) {
				if(codeParser.parserName.equals(parserName)) codeParser.setError();
			}
		}
		else {
			parser.setError();
		}
		return null;
	}
}
