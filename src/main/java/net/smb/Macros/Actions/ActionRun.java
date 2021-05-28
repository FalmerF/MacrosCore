package net.smb.Macros.Actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import net.smb.Macros.CodeParser;
import net.smb.Macros.Localisation;
import net.smb.Macros.MacrosSettings;
import net.smb.Macros.ParserError;

public class ActionRun extends ActionBase {

	public ActionRun() {
		super("run");
	}
	
	@Override
	public Object execute(CodeParser parser, String arg, String[] args, String code) {
		if(args.length > 0) {
			String fileName = parser.getString(args[0]);
			
			FileReader Fr;
			File f = new File(MacrosSettings.macrosDir, fileName + ".txt");
			if(f.exists()) {
				try {
		 			 Fr = new FileReader(f);
		             final BufferedReader Br = new BufferedReader(Fr);
		             String line = "";
		             String text = "";
		             while ((line = Br.readLine()) != null) {
		            	 text += line;
		             }
		             Br.close();
		             
		             if(!text.equals("")) {
		            	 CodeParser newParser = new CodeParser("[" + fileName + "]", null);
		            	 newParser.executeCode(text);
		             }
				} catch(Exception e) {}
			}
			else {
				ParserError.customError(parser, Localisation.getString("parser.error.file") + " \"" + fileName + "\" " + Localisation.getString("parser.error.methodexists2"));
			}
		}
		return null;
	}
}
