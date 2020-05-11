package net.AzureWare.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import net.AzureWare.Client;
import net.AzureWare.mod.mods.PLAYER.Spammer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class Kuai8Tools {
	static String fileDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/" + Client.CLIENT_NAME;
	
	public static String getCommandline() {
		String command = "wmic process where \"name like 'javaw.exe'\" get Commandline";
        Runtime runtime = Runtime.getRuntime();
        StringBuffer b = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec(command).getInputStream()));
            String line = null;
            while((line=br.readLine()) != null) {
                if(line.contains("mcagent.jar")) {
                	return line;
                }
            }
            System.out.println(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
	}
	
	public static String getUUID() {
		String commandLine = getCommandline().replaceAll(" ", "");
		return getSubString(commandLine, "--uuid", "--accessToken");
	}
	
	public static Boolean isKuai8MCRunning() {
		if(getCommandline() == null) {
			return false;
		}
		return true;
	}
	
	public static String getToken() {
		String commandLine = getCommandline().replaceAll(" ", "");
		return getSubString(commandLine, "--accessToken", "--userProperties");
	}
	
	public static String getIP() {
		String commandLine = getCommandline().replaceAll(" ", "");
		return getSubString(commandLine, "--server", "--port") + ":" + commandLine.substring(commandLine.length() - 5);
	}
	
	public static String getUsername() {
		String commandLine = getCommandline().replaceAll(" ", "");
		return getSubString(commandLine, "--username", "--version");
	}
	
	public static Session getKuai8Session() {
		saveSession();
		Session Kuai8Session = new Session(getUsername(), getUUID(), getToken(), "mojang");
		return Kuai8Session;
	}
	
	public static void saveSession() {
	    File f = new File(fileDir + "/Kuai8Session.txt");
	      try {
		         if(!f.exists()) {
		            f.createNewFile();
		         }

		         PrintWriter pw = new PrintWriter(f);
		         pw.print(getUsername() + "|" + getUUID() + "|" + getToken());
		         pw.close();
		      } catch (Exception var2) {
		         var2.printStackTrace();
		      }
	}
	
	@SuppressWarnings("resource")
	public static Session getLastKuai8Session(){
        try {
            File f = new File(fileDir + "/Kuai8Session.txt");
            if(!f.exists()) {
               f.createNewFile();
            } else {
               BufferedReader br = new BufferedReader(new FileReader(f));
               String line;
               while((line = br.readLine()) != null) {
                  try {
                     String text = String.valueOf(line);
                     return new Session(text.split("\\|")[0], text.split("\\|")[1], text.split("\\|")[2], "mojang");
                  } catch (Exception var4) {
                     var4.printStackTrace();
                  }
               }
            }
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
        return null;
	}
	
	public static void killKuai8MC() {
		try {
			Runtime.getRuntime().exec("taskkill /f /fi \"WINDOWTITLE eq Minecraft 1.8.8\"");
			//Remove there if you want to kill Kuai8MCBox.
			//Runtime.getRuntime().exec("taskkill /f /im K8MC.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static String getSubString(String text, String left, String right) {
		String result = "";
		int zLen;
		if (left == null || left.isEmpty()) {
			zLen = 0;
		} else {
			zLen = text.indexOf(left);
			if (zLen > -1) {
				zLen += left.length();
			} else {
				zLen = 0;
			}
		}
		int yLen = text.indexOf(right, zLen);
		if (yLen < 0 || right == null || right.isEmpty()) {
			yLen = text.length();
		}
		result = text.substring(zLen, yLen);
		return result;
	}
}
