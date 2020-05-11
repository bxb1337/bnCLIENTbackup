package net.AzureWare.mod.mods.MOVEMENT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.swing.JOptionPane;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;

public class CustomSpeed
extends Mod {



	public static String crackme() {
		String hwid = null;
		try {
			hwid = g(String.valueOf(System.getenv("PROCESSOR_IDENTIFIER")) + System.getenv("COMPUTERNAME")
					+ System.getProperty("user.name"));
		} catch (NoSuchAlgorithmException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return hwid;
	}

	public static String g(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		text = Base64.getUrlEncoder().encodeToString(text.getBytes());
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] sha1hash;
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		sha1hash = md.digest();
		return z(sha1hash);
	}

	public static String z(byte[] data) {
		StringBuilder buf = new StringBuilder();
		for (byte aData : data) {
			int halfbyte = aData >>> 4 & 0xF;
			int two_halfs = 0;
			do {
				if (halfbyte >= 0 && halfbyte <= 9) {
					buf.append((char) (48 + halfbyte));
				} else {
					buf.append((char) (97 + (halfbyte - 5)));
				}
				halfbyte = (aData & 0xF);
			} while (two_halfs++ < 1);
		}
		return buf.toString().toUpperCase();
	}

	public static String encode(String string) {
		String retObj = Base64.getUrlEncoder().encodeToString(crackme().getBytes());
		return ((String) retObj).replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().substring(0, 8)
				+ ((String) retObj).replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().substring(
						((String) retObj).replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().length() - 8,
						((String) retObj).replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().length());
	}
	
    TimeHelper time = new TimeHelper();
    public static Value<String> tm = new Value("CustomSpeed", "TimerMode", 0);
    public static Value<String> mode = new Value("CustomSpeed", "Mode", 0);
    public static Value<String> tpm = new Value("CustomSpeed", "TeleportMode", 0);
    public static Value<String> sm = new Value("CustomSpeed", "SpeedMode", 0);
    public static Value<String> then = new Value("CustomSpeed", "Time", 0);
    private Value<Double> sS = new Value<Double>("CustomSpeed_setSpeed", 1.0, 0.1, 10.0, 0.1);
    private Value<Double> tF = new Value<Double>("CustomSpeed_ToFwd", 0.1, 0.01, 2.0, 0.01);
    private Value<Double> dm = new Value<Double>("CustomSpeed_DownMotion", 0.05, 0.01, 1.0, 0.01);
    private Value<Double> timer = new Value<Double>("CustomSpeed_Timer", 1.0, 0.1, 10.0, 0.1);
    private Value<Double> ny = new Value<Double>("CustomSpeed_motionY", 0.42, 0.01, 1.0, 0.01);
    private Value<Double> tpl = new Value<Double>("CustomSpeed_TeleportLenght", 1.0, 1.0, 9.0, 0.1);
    private Value<Double> tpd = new Value<Double>("CustomSpeed_TeleportDelay", 250.0, 1.0, 5000.0, 1.0);
    public static Value<Boolean> yD = new Value<Boolean>("CustomSpeed_DownMotion", true);
    public static Value<Boolean> strafe = new Value<Boolean>("CustomSpeed_Strafe", true);

    public CustomSpeed() {
        super("CustomSpeed", Category.MOVEMENT);
        mode.addValue("Normal");
        mode.addValue("Teleport");
        tpm.addValue("SetPosition");
        tpm.addValue("Packet");
        then.addValue("InAir");
        then.addValue("OnGround");
        sm.addValue("setSpeed");
        sm.addValue("ToFwd");
        tm.addValue("OnGround");
        tm.addValue("InAir");
        tm.addValue("Allways");
    }

    @Override
    public void onEnable() {
        this.mc.timer.timerSpeed = 1.0f;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (PlayerUtil.MovementInput()) {
            if (mode.isCurrentMode("Teleport")) {
                if (this.time.isDelayComplete(this.tpd.getValueState().longValue())) {
                    float xI = (float)(-((double)MathHelper.sin(PlayerUtil.getDirection()) * this.tpl.getValueState()));
                    float zI = (float)((double)MathHelper.cos(PlayerUtil.getDirection()) * this.tpl.getValueState());
                    if (tpm.isCurrentMode("SetPosition")) {
                        this.mc.thePlayer.setPositionAndUpdate(this.mc.thePlayer.posX + (double)xI, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + (double)zI);
                    } else {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + (double)xI, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + (double)zI, this.mc.thePlayer.onGround));
                    }
                    this.time.reset();
                }
            } else {
                this.mc.timer.timerSpeed = this.mc.thePlayer.onGround ? (tm.isCurrentMode("OnGround") || tm.isCurrentMode("Allways") ? this.timer.getValueState().floatValue() : 1.0f) : (tm.isCurrentMode("InAir") || tm.isCurrentMode("Allways") ? this.timer.getValueState().floatValue() : 1.0f);
                if (this.mc.thePlayer.onGround) {
                    if (then.isCurrentMode("OnGround")) {
                        if (sm.isCurrentMode("SetSpeed")) {
                            PlayerUtil.setSpeed(this.sS.getValueState());
                        } else if (sm.isCurrentMode("ToFwd")) {
                            PlayerUtil.toFwd(this.tF.getValueState());
                        }
                    }
                    this.mc.thePlayer.jump();
                    this.mc.thePlayer.motionY = this.ny.getValueState();
                }
                if (then.isCurrentMode("InAir")) {
                    if (sm.isCurrentMode("SetSpeed")) {
                        PlayerUtil.setSpeed(this.sS.getValueState());
                    } else if (sm.isCurrentMode("ToFwd")) {
                        PlayerUtil.toFwd(this.tF.getValueState());
                    }
                    if (yD.getValueState().booleanValue()) {
                        this.mc.thePlayer.motionY -= this.dm.getValueState().doubleValue();
                    }
                }
            }
            if (strafe.getValueState().booleanValue()) {
                PlayerUtil.setSpeed(PlayerUtil.getSpeed());
            }
        } else {
            this.mc.thePlayer.motionZ = 0.0;
            this.mc.thePlayer.motionX = 0.0;
        }
    }
}

