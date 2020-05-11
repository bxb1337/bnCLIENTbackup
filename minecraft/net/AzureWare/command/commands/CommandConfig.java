package net.AzureWare.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sun.jna.platform.FileUtils;

import net.AzureWare.Client;
import net.AzureWare.command.Command;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.PLAYER.Spammer;
import net.AzureWare.mod.mods.WORLD.IRC;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class CommandConfig extends Command {
    public CommandConfig(String[] commands) {
        super(commands);
        this.setArgs("-Debug");
    }

    @Override
    public void onCmd(String[] args) {
    	if(args.length == 1) {
    		ClientUtil.sendClientMessage("You are playing " + Client.Server, Notification.Type.INFO);
    	}else if(args.length == 2) {
    		if(!get("http://hanabi.alphaantileak.cn:893/azureware/config/" + args[1].toLowerCase() + "/values.txt").equals("")) {
    			try {
    				writeValue(get("http://hanabi.alphaantileak.cn:893/azureware/config/" + args[1].toLowerCase() + "/values.txt"));
    				writeMods(get("http://hanabi.alphaantileak.cn:893/azureware/config/" + args[1].toLowerCase() + "/mods.txt"));
    				Client.getInstance().getFileUtil().loadMods();
    				Client.getInstance().getFileUtil().loadValues();
    			} catch (IOException e) {
    				ClientUtil.sendClientMessage("Write Config Failed!", Notification.Type.ERROR);
    				e.printStackTrace();
    			}
    		}else {
    			ClientUtil.sendClientMessage("We do not have this config!", Notification.Type.INFO);
    		}
    	}else {
    		ClientUtil.sendClientMessage(".config [server]", Notification.Type.INFO);
    	}
    	//writeValue(get("https://alphaantileak.cn/debug/preset/"+args[0]+"/values.txt"));

    }
    
    public static void writeValue(String value) {
        String fileDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/" + "AzureWare";
        File f = new File(fileDir + "/values.txt");

        try {
           if(!f.exists()) {
              f.createNewFile();
           }

           PrintWriter pw = new PrintWriter(f);
           pw.print(value);
           pw.close();
        } catch (Exception var2) {
           var2.printStackTrace();
        }
     }
    
    public static void writeMods(String mods) {
        String fileDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/" + "AzureWare";
        File f = new File(fileDir + "/mods.txt");

        try {
           if(!f.exists()) {
              f.createNewFile();
           }

           PrintWriter pw = new PrintWriter(f);
           pw.print(mods);
           pw.close();
        } catch (Exception var2) {
           var2.printStackTrace();
        }
     }
    
    public static String get(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setDoOutput(true);
            connection.setReadTimeout(99781);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppIeWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            for (String key : map.keySet()) { }
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
        } catch (Exception e) {
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            }
        }
  	return result;
    }
}
