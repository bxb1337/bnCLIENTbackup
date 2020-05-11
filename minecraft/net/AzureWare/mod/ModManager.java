package net.AzureWare.mod;

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import net.AzureWare.Client;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.mod.mods.RandomModule;
import net.AzureWare.mod.mods.COMBAT.*;
import net.AzureWare.mod.mods.MOVEMENT.*;
import net.AzureWare.mod.mods.PLAYER.*;
import net.AzureWare.mod.mods.RENDER.*;
import net.AzureWare.mod.mods.WORLD.*;
import net.AzureWare.utils.fontmanager.UnicodeFontRenderer;
import net.minecraft.Wrapper;
import net.minecraft.client.main.Main;

public class ModManager {
	private static ArrayList<Mod> modList = new ArrayList();
	public static ArrayList<Mod> sortedModList = new ArrayList();
	public static boolean needsort = true;
	private static ArrayList<Mod> enabledModList;

	public ModManager() {
		modList.add(new AntiAim());
		modList.add(new AimAssist());

		// COMBAT
		modList.add(new AntiBot());
		modList.add(new Velocity());
		modList.add(new Hitbox());
		modList.add(new KeepSprint());
		// modList.add(new BowAimbot());
		// modList.add(new Superknockback());

		modList.add(new Reach());
		modList.add(new WTap());
		modList.add(new Killaura());
		modList.add(new AutoSoup());
		modList.add(new Criticals());
		// modList.add(new Regen());
		modList.add(new AutoArmor());
		// modList.add(new TPAura());
		modList.add(new AutoSword());
		// modList.add(new RDBProject());
		modList.add(new AutoClicker());
		//

		// MOVEMENT
		modList.add(new Sprint());
		modList.add(new Speed());
		// modList.add(new Step());
		// modList.add(new DebugScaffold());
		modList.add(new Fly());
		// modList.add(new Fly_InJected());
		modList.add(new Jesus());
		modList.add(new Strafe());
		modList.add(new NoSlow());
		modList.add(new TargetStrafe());
		// modList.add(new AutoWalk());
		modList.add(new HighJump());
		modList.add(new Disabler());
		modList.add(new NoRotate());
		modList.add(new EntitySpeed());
		// modList.add(new CustomSpeed());
		// PLAYER
		modList.add(new NoCommand());
		modList.add(new Teams());
		modList.add(new InvMove());
		modList.add(new Spammer());
		modList.add(new AutoAbuse());
		modList.add(new HYTBypass());
		modList.add(new LongJump());
		modList.add(new NoFall());
		modList.add(new AimBot());

	//	if (Client.isDebugMode || Client.isTestUser)
	  //  modList.add(new Step());


		modList.add(new AutoRespawn());
		// modList.add(new Fucker());
		modList.add(new Crasher());
		modList.add(new ChestStealer());
		modList.add(new InvCleaner());
		modList.add(new FastUse());
		modList.add(new BanCheck());
		modList.add(new AutoTools());
		modList.add(new FastPlace());
		modList.add(new AntiObf());
		

		modList.add(new TeleportBedFucker());
		// modList.add(new FastUse());
		// modList.add(new HitCall());
	
		// RENDER
		modList.add(new Gui());
		modList.add(new HUD());
		modList.add(new Dab());
		modList.add(new ESP());
		modList.add(new Fullbright());
		modList.add(new PingSpoof());
		modList.add(new ItemESP());
		modList.add(new Nametag());
		modList.add(new BlockOverlay());
		modList.add(new Projectiles());
		modList.add(new HitAnimation());
		modList.add(new ItemPhysic());
		modList.add(new NoHurtcam());
		modList.add(new AntiDebuff());
		modList.add(new Particles());
		modList.add(new BlockEsp());

		// modList.add(new Radar());
		modList.add(new ChestESP());
//		modList.add(new EveryThingBlock());
		modList.add(new NameProtect());
		modList.add(new Freecam());
		// modList.add(new XRay());
		modList.add(new Chams());
		modList.add(new BigGod());
		modList.add(new Mod("ViewClip", Category.RENDER));

		// WORLD
		modList.add(new AntiBotDisPlay());

		modList.add(new Eagle());
		modList.add(new Scaffold2());
		modList.add(new IRC());
//		 modList.add(new Blink());
		modList.add(new AutoGG());
		modList.add(new SafeWalk());
		modList.add(new FastDig());
		modList.add(new AutoL());
		modList.add(new Timer());
		modList.add(new AntiFall());
		modList.add(new AutoParty());
		modList.add(new Phase());
		modList.add(new HideAndSeek());
		modList.add(new MurderFinder());
		modList.add(new PacketMotior());
		// modList.add(new AntiBots());

		// modList.add(new Teleport());

		// Dev Mode
		if (Client.isDebugMode) {
			modList.add(new Mod("CornnerSetting", Category.WORLD));
		}
		// for(int i = 0 ; i < 20 ; i++) modList.add(new RandomModule());

	}

	public static ArrayList<Mod> getModList() {
		return modList;
	}

	public static ArrayList<Mod> getEnabledModList() {
		ArrayList<Mod> enabledModList = new ArrayList();
		for (Mod m : modList) {
			if (m.isEnabled()) {
				enabledModList.add(m);
			}
		}
		return enabledModList;
	}

	static UnicodeFontRenderer fr = Client.getInstance().getFontManager().comfortaa17;

	public static ArrayList<Mod> getEnabledModListHUD() {
		if (needsort) {
			enabledModList = new ArrayList();
			for (Mod m : modList) {
				enabledModList.add(m);
			}
			// static UnicodeFontRenderer fr =
			// Client.getInstance().getFontManager().comfortaa17;

			if (HUD.font.isCurrentMode("Simpleton")) {
				fr = Client.getInstance().getFontManager().simpleton17;
			} else if (HUD.font.isCurrentMode("Payback")) {
				fr = Client.getInstance().getFontManager().payback18;
			} else if (HUD.font.isCurrentMode("VERDANA")) {
				fr = Client.getInstance().getFontManager().VERDANA18;
			} else if (HUD.font.isCurrentMode("ArialBold")) {
				fr = Client.getInstance().getFontManager().arialBold18;
			} else if (HUD.font.isCurrentMode("Robotobold")) {
				fr = Client.getInstance().getFontManager().robotobold18;
			} else if (HUD.font.isCurrentMode("JelloFont")) {
				fr = Client.getInstance().getFontManager().jellolight20;
			} else if (HUD.font.isCurrentMode("Comfortaa")) {
				// fr = Client.getInstance().getFontManager().jellolight20;
			}

			enabledModList.sort((o1,
					o2) -> fr.getStringWidth(
							o2.getName() + ((o2.getDisplayName() != null) ? o2.getDisplayName() + " " : ""))
							- fr.getStringWidth(
									o1.getName() + ((o1.getDisplayName() != null) ? o1.getDisplayName() + " " : "")));
			needsort = false;
		}
		return enabledModList;
	}

	public static Mod getModByName(String mod) {
		for (Mod m : modList) {
			if (!m.getName().equalsIgnoreCase(mod))
				continue;
			return m;
		}
		return null;
	}

	public static Mod getModule(String mod) {
		for (Mod m : modList) {
			if (!m.getName().equalsIgnoreCase(mod))
				continue;
			return m;
		}
		return null;
	}
}
