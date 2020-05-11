package net.AzureWare.mod.mods.WORLD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.Client;
import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.COMBAT.Criticals;
import net.AzureWare.mod.mods.COMBAT.Killaura;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class AntiBotDisPlay extends Mod {
	private long time = 0;
	private long lastTime;
	private int times;
	private TimeHelper timer = new TimeHelper();
	private TimeHelper timer3 = new TimeHelper();

	private TimeHelper timer2 = new TimeHelper();
	public static Value<Boolean> MotionR = new Value("DateEditer_MotionReciever", true);
    static ArrayList<Double> FloatList = new ArrayList<>();
	public double float1;

	public AntiBotDisPlay() {
		super("DateEditer", Category.WORLD);
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
			if (MotionR.getValueState()) {

				if (mc.thePlayer.ticksExisted % 1 == 0) {
					System.out.println(mc.thePlayer.posY - Math.round(mc.thePlayer.posY));
					float1 = mc.thePlayer.posY - Math.round(mc.thePlayer.posY);
					FloatList.add(float1);
					writeFloat(float1 + "");
		
				if (timer3.isDelayComplete(10)) {
					// System.out.println(mc.thePlayer.posY);
				}
				if (timer3.isDelayComplete(10000)) {
					timer3.reset();
				}
			}
		}
		
		if (timer.isDelayComplete(1000)) {
			this.setDisplayName(Killaura.target.getDisplayName().getUnformattedText().toLowerCase());

			if (timer2.isDelayComplete(10000)) {
				PlayerUtil.tellPlayer("\247b[AzureWare]\247c TicksExisted :" + mc.thePlayer.ticksExisted);
				PlayerUtil.tellPlayer("\247b[AzureWare]\247c BPS :" + (PlayerUtil.getSpeed() / 0.26));

				timer2.reset();

			}

			timer.reset();
		}

	}

    public static void writeFloat(String float1) {
        String fileDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/" + "AzureWare";
        File f = new File(fileDir + "/FloatList.txt");

        try {
           if(!f.exists()) {
              f.createNewFile();
           }
         //  byte[] b = fileContent.getBytes();

           PrintWriter pw = new PrintWriter(f);

           pw.write(FloatList.toString());
           pw.flush();
           pw.close();
        } catch (Exception var2) {
           var2.printStackTrace();
        }
     }
    
}
