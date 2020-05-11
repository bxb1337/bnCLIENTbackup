package net.AzureWare.mod.mods.COMBAT;

import java.awt.Container;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.utils.PlayerInfo;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S14PacketEntity;

public class AntiBot extends Mod {
	private static Value mode = new Value("AntiBot", "Mode", 0);
	private TimeHelper timer = new TimeHelper();
	private static List<EntityPlayer> invalid = new ArrayList<EntityPlayer>();
	private static List<EntityPlayer> whitelist = new ArrayList<EntityPlayer>();
	private static List<EntityPlayer> hurtTimeCheck = new ArrayList<EntityPlayer>();

	private static List<PlayerInfo> playerList = new ArrayList();
	public Value<Boolean> remove = new Value<Boolean>("AntiBot_Remove", true);
	public static Value<Double> hurttime = new Value<Double>("AntiBot_HTimeCheckTime", 10000.0, 5000.0, 20000.0, 100.0);
	public static Value<Boolean> touchedGround = new Value<Boolean>("AntiBot_Ground", true);
	public static Value<Boolean> toolCheck = new Value<Boolean>("AntiBot_ToolCheck", true);
	public AntiBot() {
		super("AntiBot", Category.COMBAT);
		mode.addValue("Hypixel");
		mode.addValue("Name");

		mode.addValue("Mineplex");
		mode.addValue("HuaYuTing");
	}

	public static ArrayList bots = new ArrayList();
	private static ArrayList playersInGame = new ArrayList();

	private static List<EntityPlayer> removed = new ArrayList<>();
	public TimeHelper lastRemoved = new TimeHelper();
	public TimeHelper timer2 = new TimeHelper();
	public TimeHelper timer3 = new TimeHelper();

	public static List<EntityPlayer> getTabPlayerList() {
		return new GuiPlayerTabOverlay(Minecraft.getMinecraft(), Minecraft.getMinecraft().ingameGUI).getPlayerList();
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {

		if (mode.isCurrentMode("Hypixel")) {

			for (EntityPlayer entity : mc.theWorld.playerEntities) {
				if (entity != mc.thePlayer && entity != null && entity instanceof EntityLivingBase) {
					if (entity != mc.thePlayer && entity instanceof EntityPlayer) {
						EntityPlayer playerentity = (EntityPlayer) entity;
						if (!isInTablist(playerentity)
								&& !entity.getDisplayName().getFormattedText().toLowerCase().contains("[npc")
								&& entity.getDisplayName().getFormattedText().startsWith("\u00a7")) {
							mc.theWorld.removeEntity(playerentity);
						}
					}
				}
			}

			if (remove.getValueState()) {
				if (!removed.isEmpty()) {
					if (lastRemoved.isDelayComplete(1000)) {
						PlayerUtil.tellPlayer("\247b[AzureWare]\247Removed" + removed.size() + "Bots");
						lastRemoved.reset();
						removed.clear();
					}
				}
			}
			if (!hurtTimeCheck.isEmpty() && timer3.isDelayComplete(hurttime.getValueState().longValue())) {
				hurtTimeCheck.clear();
				timer3.reset();
			}
			if (!whitelist.isEmpty() && timer2.isDelayComplete(3000)) {
				whitelist.clear();
				timer2.reset();
			}

			if (!invalid.isEmpty() && timer.isDelayComplete(1000)) {
				invalid.clear();
				timer.reset();
			}

			for (Object o : mc.theWorld.getLoadedEntityList()) {
				if (o instanceof EntityPlayer) {
					EntityPlayer ent = (EntityPlayer) o;
					if (ent != mc.thePlayer && !invalid.contains(ent)) {
						String formated = ent.getDisplayName().getFormattedText();
						String custom = ent.getCustomNameTag();
						String name = ent.getName();

						if (ent.isInvisible() && !formated.startsWith("¡ìc") && formated.endsWith("¡ìr")
								&& custom.equals(name)) {
							double diffX = Math.abs(ent.posX - mc.thePlayer.posX);
							double diffY = Math.abs(ent.posY - mc.thePlayer.posY);
							double diffZ = Math.abs(ent.posZ - mc.thePlayer.posZ);
							double diffH = Math.sqrt(diffX * diffX + diffZ * diffZ);
							if (diffY < 13 && diffY > 10 && diffH < 3) {
								List<EntityPlayer> list = getTabPlayerList();
								if (!list.contains(ent)) {
									if (remove.getValueState()) {
										lastRemoved.reset();
										removed.add(ent);
										mc.theWorld.removeEntity(ent);
									}
									invalid.add(ent);
								}

							}

						}
						// SHOP BEDWARS
						if (!formated.startsWith("¡ì") && formated.endsWith("¡ìr")) {
							invalid.add(ent);
						}

						// TABCHECK
						if (!isInTablist(ent)) {
							invalid.add(ent);
						}

						//HURTTIMEPROTECT
						if (ent.hurtTime > 0) {
							this.hurtTimeCheck.add(ent);
						}

						//WhiteListAdd
						if (hurtTimeCheck.contains(ent) && !whitelist.contains(ent)) {
							whitelist.add(ent);
						}
						
						// HELDITEMCHECKER
						if (ent.getHeldItem() != null) {
							whitelist.add(ent);
						}

						if (ent.getHeldItem() == null && !whitelist.contains(ent) && AntiBot.toolCheck.getValueState()) {
							invalid.add(ent);
						}

						if (ent.isInvisible()) {
							// BOT INVISIBLES IN GAME
							if (!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("¡ìc¡ìc")
									&& name.contains("¡ìc")) {
								if (remove.getValueState()) {
									lastRemoved.reset();
									removed.add(ent);
									mc.theWorld.removeEntity(ent);
								}
								invalid.add(ent);
							}
						}
						// WATCHDOG BOT
						if (!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("¡ìc")
								&& custom.toLowerCase().contains("¡ìr")) {
							if (remove.getValueState()) {
								lastRemoved.reset();
								removed.add(ent);
								mc.theWorld.removeEntity(ent);
							}
							invalid.add(ent);
						}

						// BOT LOBBY
						if (formated.contains("¡ì8[NPC]")) {
							invalid.add(ent);
						}
						if (!formated.contains("¡ìc") && !custom.equalsIgnoreCase("")) {

							invalid.add(ent);
						}
					}
				}
			}

			this.setDisplayName("Hypixel");
		}

		if (mode.isCurrentMode("Name")) {
			this.setDisplayName("Debug");

			this.mc.theWorld.playerEntities.forEach((entity) -> {
				if (entity != this.mc.thePlayer && this.isBot(entity)) {
					invalid.add(entity);
				}

			});

		}
		if (mode.isCurrentMode("Mineplex"))
			this.setDisplayName("Mineplex");
		if (mode.isCurrentMode("HuaYuTing"))
			this.setDisplayName("HuaYuTing");
	}

	public static List<Integer> onAirInvalid = new ArrayList<Integer>();

	public void onEnable() {
		this.onAirInvalid.clear();
		super.onEnable();
	}

	public void onDisable() {
		this.onAirInvalid.clear();
		super.onDisable();
	}

	@EventTarget
	public void onReceivePacket(EventPacket event) {
		if (event.getPacket() instanceof S14PacketEntity) {
			S14PacketEntity packet = (S14PacketEntity) event.getPacket();
			Entity entity;
			if ((entity = packet.getEntity(mc.theWorld)) instanceof EntityPlayer) {
				if (!packet.getOnGround() && !this.onAirInvalid.contains(entity.getEntityId())) {
					this.onAirInvalid.add(entity.getEntityId());
				}
			}
		}
	}

	public static boolean isInTablist(EntityPlayer player) {
		if (Minecraft.getMinecraft().isSingleplayer()) {
			return true;
		}
		for (Object o : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
			NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) o;
			if (playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isBot(Entity e) {

		if (!ModManager.getModByName("AntiBot").isEnabled())
			return false;
		if (!(e instanceof EntityPlayer) || !ModManager.getModByName("AntiBot").isEnabled())
			return false;
		EntityPlayer player = (EntityPlayer) e;

		if (mode.isCurrentMode("Hypixel")) {
			return invalid.contains(player) || !onAirInvalid.contains(player.getEntityId());
		}
		if (mode.isCurrentMode("Name")) {
			if (mc.thePlayer.ticksExisted < 200) {
				mc.theWorld.playerEntities.forEach((entityPlayer) -> {
					if (!entityPlayer.isInvisible()) {
						playersInGame.add(entityPlayer);
					}

				});
			}

			return !playersInGame.contains(e);
		}

		if (mode.isCurrentMode("Mineplex") && !Float.isNaN(player.getHealth()))
			return true;
		if (mode.isCurrentMode("HuaYuTing") && (e.getEntityBoundingBox().maxX - e.getEntityBoundingBox().minX) < 0.21)
			return true;

		return false;
	}


}
