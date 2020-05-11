package net.AzureWare.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import net.AzureWare.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class TheAlten {
	static String fileDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/" + Client.CLIENT_NAME;
	
	public static void saveAPI(String Token) {
	    File f = new File(fileDir + "/TheAltenAPI.txt");
	      try {
		         if(!f.exists()) {
		            f.createNewFile();
		         }
		         PrintWriter pw = new PrintWriter(f);
		         pw.print(Token);
		         pw.close();
		      } catch (Exception var2) {
		         var2.printStackTrace();
		      }
	}
	
	@SuppressWarnings("resource")
	public static String getAPI(){
        try {
            File f = new File(fileDir + "/TheAltenAPI.txt");
            if(!f.exists()) {
               f.createNewFile();
            } else {
               BufferedReader br = new BufferedReader(new FileReader(f));
               String line;
               while((line = br.readLine()) != null) {
                  try {
                     String text = String.valueOf(line);
                     return line;
                  } catch (Exception var4) {
                     var4.printStackTrace();
                  }
               }
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	

	public static Alten getAlt(String Token) {
		try {
			String args[] = Client.sendGet("http://api.thealtening.com/v1/generate?token=" + Token, null).split("\"");
			return new Alten(args[3], args[7]);
		}catch (Throwable e){
			return new Alten("Failed", "Failed");
		}

	}
}
