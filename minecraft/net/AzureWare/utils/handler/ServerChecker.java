package net.AzureWare.utils.handler;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;

public class ServerChecker {
	   public static boolean isInPit;
	   public static ServerResult currentServer;

	   public static void checkifPit() {
	      Minecraft var0 = Minecraft.getMinecraft();
	      if(var0.getCurrentServerData() != null) {
	         String var1 = var0.getCurrentServerData().serverIP.toLowerCase();
	         if((var1.toLowerCase().endsWith("hypixel.net") || var1.toLowerCase().endsWith("hypixel.net:25565")) && var0.theWorld.loadedEntityList.stream().anyMatch(ServerChecker::isBot)) {
	            currentServer = ServerResult.HYPIXEL;
	         } else if(!var1.toLowerCase().endsWith("cubecraft.net") && !var1.toLowerCase().endsWith("cubecraft.net:25565")) {
	            if(!var1.toLowerCase().endsWith("mineplex.com") && !var1.toLowerCase().endsWith("mineplex.com:25565")) {
	               currentServer = ServerResult.OTHER;
	            } else {
	               currentServer = ServerResult.MINEPLEX;
	            }
	         } else {
	            currentServer = ServerResult.CUBECRAFT;
	         }
	      }

	      isInPit = false;
	      if(var0.theWorld != null) {
	         Iterator var3 = var0.theWorld.getScoreboard().getScoreObjectives().iterator();

	         while(var3.hasNext()) {
	            ScoreObjective var2 = (ScoreObjective)var3.next();
	            if(var2.getName().equals("Pit")) {
	               isInPit = true;
	               break;
	            }
	         }
	      }

	   }

	   private static boolean isBot(Entity var0) {
	      return var0 instanceof EntityPlayer && var0.getDisplayName().getFormattedText().startsWith("\u00a7") && var0.getCustomNameTag().equals("");
	   }

	   
} 



