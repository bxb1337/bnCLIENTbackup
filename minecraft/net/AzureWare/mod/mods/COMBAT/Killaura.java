package net.AzureWare.mod.mods.COMBAT;

import net.AzureWare.Client;
import net.AzureWare.events.*;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.COMBAT.AntiBot;
import net.AzureWare.mod.mods.PLAYER.Teams;
import net.AzureWare.mod.mods.WORLD.AutoL;
import net.AzureWare.mod.mods.WORLD.HideAndSeek;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.*;
import net.AzureWare.utils.RDBProject.RotationData;
import net.AzureWare.value.Value;
import com.darkmagician6.eventapi.*;

import me.tojatta.api.utilities.angle.Angle;
import me.tojatta.api.utilities.angle.AngleUtility;
import me.tojatta.api.utilities.vector.impl.Vector3;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;

import javax.swing.JOptionPane;
import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3d;

import net.minecraft.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.main.Main;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3d;

import org.lwjgl.opengl.GL11;

import me.tojatta.api.utilities.angle.Angle;
import me.tojatta.api.utilities.angle.AngleUtility;
import me.tojatta.api.utilities.vector.impl.Vector3;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class Killaura extends Mod {
	// 锟酵伙拷锟斤拷锟斤拷锟斤拷
	public static Value<Boolean> autoBlock = new Value<Boolean>("KillAura_AutoBlock", true);
	public Value<String> priority = new Value("KillAura", "Priority", 1);
	public Value<String> rotMode = new Value<String>("KillAura", "RotationMode", 0);
	public Value<Double> hurttime = new Value<Double>("KillAura_HurtTime", 10.0, 1.0, 10.0, 1.0);
	public Value<Double> mistake = new Value<Double>("KillAura_Mistakes", 0.0, 0.0, 20.0, 1);
	public Value<String> blockMode = new Value<String>("KillAura", "BlockMode", 0);

	public static Value<Double> reach = new Value<Double>("KillAura_Range", 4.2, 3.0, 6.0, 0.1);
	public Value<Double> blockReach = new Value<Double>("KillAura_BlockRange", 0.5, 0.0, 3.0, 0.1);
	public Value<Double> cps = new Value<Double>("KillAura_CPS", 10.0, 1.0, 20.0, 1.0);
	public Value<Double> turn = new Value<Double>("KillAura_TurnHeadSpeed", 15.0, 5.0, 120.0, 1.0);
	public static Value<Double> switchsize = new Value<Double>("KillAura_MaxTargets", 1.0, 1.0, 5.0, 1.0);
	public Value<Double> switchDelay = new Value<Double>("KillAura_SwitchDelay", 50d, 0d, 2000d, 10d);
	public Value<Double> yawDiff = new Value<Double>("KillAura_YawDifference", 15.0, 5.0, 90.0, 1.0);
	public Value<Boolean> throughblock = new Value<Boolean>("KillAura_ThroughBlock", true);
	public Value<Boolean> rotations = new Value<Boolean>("KillAura_HeadRotations", true);
	public Value<Boolean> autodisable = new Value<Boolean>("KillAura_AutoDisable", true);
	public Value<Boolean> attackPlayers = new Value<Boolean>("KillAura_Players", true);
	public Value<Boolean> attackAnimals = new Value<Boolean>("KillAura_Animals", false);
	public Value<Boolean> attackMobs = new Value<Boolean>("KillAura_Mobs", false);
	public Value<Boolean> invisible = new Value<Boolean>("KillAura_Invisibles", false);
	public Value<Boolean> targetHUD = new Value<Boolean>("KillAura_ShowTarget", true);
	public Value<Boolean> esp = new Value<Boolean>("KillAura_ESP", true);
	// public Value<Boolean> aac = new Value<Boolean>("KillAura_AAC", false);

	public static boolean isBlocking = false;

	// Utils
	public static ArrayList<EntityLivingBase> targets = new ArrayList();
	public Random random = new Random();
	public static ArrayList<EntityLivingBase> attacked = new ArrayList();

	// 锟斤拷锟斤拷
	public boolean needBlock = false;
	public boolean needUnBlock = false;
	public int index;

	// 实锟斤拷锟斤拷锟�
	public static EntityLivingBase target = null;
	public static EntityLivingBase needHitBot = null;

	// TimeHelper
	public TimeHelper switchTimer = new TimeHelper();
	public TimeHelper attacktimer = new TimeHelper();
	public TimeHelper TickexistCharge = new TimeHelper();

	// 转头
	private AngleUtility angleUtility = new AngleUtility(110, 120, 30, 40);// This is the angle utility
	AxisAlignedBB axisAlignedBB;
	float shouldAddYaw;
	float[] lastRotation = new float[] { 0f, 0f };
	private float rotationYawHead;

	private float[] lastRotations;

//  //锟斤拷锟斤拷
	boolean Crit = false;

	public Killaura() {
		super("KillAura", Category.COMBAT);
		priority.mode.add("Angle");
		priority.mode.add("Range");
		priority.mode.add("Fov");
		rotMode.mode.add("Hypixel");
		rotMode.mode.add("Hypixel-F");
		rotMode.mode.add("Hypixel-AA");
		rotMode.mode.add("Hypixel-FA");
		// rotMode.mode.add("");
		// if (Client.isDebugMode) {
		// rotMode.mode.add("Hypixel-F");
		blockMode.addValue("Hypixel");
		blockMode.addValue("Old");
		System.out.println("KillAura");
		// }

		rotMode.mode.add("Smooth");

		attacked = new ArrayList<EntityLivingBase>();
	}

	float curHealthX = 0f;
	float curAbsorptionAmountX = 0f;
	float curY = new ScaledResolution(this.mc).getScaledHeight();


	private float yaw;
	private float pitch;



	@EventTarget
	public void targetHud(EventRenderGui event) {
        ScaledResolution res = new ScaledResolution(this.mc);//VERDANA20 comfortaa20 
        FontRenderer font = Client.getInstance().getFontManager().VERDANA20;
        if(targetHUD.getValueState())
        {   
        	float Y = res.getScaledHeight();
        	float X = res.getScaledWidth()/1.8f;
    		String Text = "Do not have target";
    		int TextLength = font.getStringWidth(Text);
    		
    		float MaxHealth = 0;
    		float Health = 0;
    		float AbsorptionAmount = 0;
        	if(target != null)
        	{
        	Y = res.getScaledHeight()/1.8f;
        	
    		Text = target.getName();
    		TextLength = font.getStringWidth(Text);
    		
    		MaxHealth = (int) target.getMaxHealth();
    		Health = (int) target.getHealth();
    		AbsorptionAmount = (int) target.getAbsorptionAmount();
        	}
    		
    		if (curY > Y)
    			{
    			curY -= 2;
        		if(curY < Y)
        			++curY;
    			}
    		if (curY < Y)
    			{
    			curY += 2;
        		if(curY > Y)
        			--curY;
    			}

    		if (curHealthX > Health)
    			curHealthX -= 0.2;
    		if (curHealthX < Health)
    			curHealthX += 0.2;

    		if (curAbsorptionAmountX > AbsorptionAmount)
    			curAbsorptionAmountX -= 0.2;
    		if (curAbsorptionAmountX < AbsorptionAmount)
    			curAbsorptionAmountX += 0.2;
//    		if(blockTarget == null)
//    			return;
    		int nextX = 20+8;
    		
    		if(TextLength > (MaxHealth + AbsorptionAmount)  * 5)
    		{
    			if(TextLength > 160)
    			{
    				nextX += TextLength;
    			}else {
    				nextX += 160;
    			}
    		}
    		else
    		{
    			if((MaxHealth + AbsorptionAmount)  * 5 > 160)
    			{
    				nextX += (MaxHealth + AbsorptionAmount)  * 5;
    			}else {
    				nextX += 160;
    			}
    			
    		}
    		
    		RenderUtil.drawImage(new ResourceLocation("Client/newui/background.png"), (int)X, (int)curY, (int)nextX, 100);
//        	RenderUtil.drawFastRoundedRect(X, Y, X+nextX, Y+100, 2, new Color(0,0,0,160).getRGB());
        	
        	RenderUtil.drawImage(new ResourceLocation("Client/Head.png"), (int)X + 4, (int)curY + 4, 20, 20);
    		
    		font.drawString(Text, X + 20 + 4, curY + 7, new Color(255,255,255,255).getRGB());
    		
    		//Draw Name
    		
    		RenderUtil.drawImage(new ResourceLocation("Client/Heart.png"), (int)X + 6, (int)curY + 6 + 20, 16, 16);
    		
    		int color = new Color(250 - (int) (curHealthX / MaxHealth * 10 * 25),
    				0 + (int) (curHealthX / MaxHealth * 10 * 25), 0, 255).getRGB();
    		if(curHealthX > 0.5f)
    		{
    			RenderUtil.drawFastRoundedRect(X + 20 + 4, curY + 20 + 9,
    					X + 20 + 4 + curHealthX * 5, curY + 20 + 8 + 10 , 2, color);
    		}else if (Health < 0.5f && Health != 0.0f) {
    			curHealthX = 0.0f;
    		}
    		
    		
    		if(curAbsorptionAmountX > 0.5f)
    		{
    			RenderUtil.drawRect(X + 20 + 4 + curHealthX * 5, curY + 20 + 9,
    					X + 20 + 4 + curHealthX * 5 - 4, curY + 20 + 8 + 10, color);
    			RenderUtil.drawRect(X + 20 + 4 + curHealthX * 5, curY + 20 + 9,
    					X + 20 + 4 + curHealthX * 5 + 4, curY + 20 + 8 + 10,
    					new Color(255, 225, 100, 255).getRGB());
    			RenderUtil.drawFastRoundedRect(X + 20 + 4 + curHealthX * 5,
    					curY + 20 + 9,
    					X + 20 + 4 + (curHealthX+curAbsorptionAmountX) * 5  ,
    					curY + 20 + 8 + 10, 2, new Color(255, 225, 100, 255).getRGB());
    		}else if (AbsorptionAmount < 0.5f && AbsorptionAmount != 0.0f) {
    			curAbsorptionAmountX = 0.0f;
    		}
    		//琛�鏉�
    		
    		
    		RenderUtil.drawRect((int)X + 6, (int)curY + 4 + 20 + 20  + 3, (int)X + nextX - 6, (int)curY + 4 + 20 + 20  + 4, new Color(255, 255, 255, 160).getRGB());
    		// Line
    		RenderUtil.drawImage(new ResourceLocation("Client/Half_Heart.png"), (int)X + 6, (int)curY + 6 + 20 + 20 + 7, 16, 16);
    		Text = Crit?"Criticals":((!mc.thePlayer.onGround && mc.thePlayer.fallDistance>0) ? "Criticals" : "Normal");
    		TextLength = font.getStringWidth(Text);
    		if(Text == "Normal")
    		{
    			color = Color.GREEN.getRGB();
    		}
    		else
    		{
    			color = Color.ORANGE.getRGB();
    		}
    		font.drawString(Text, (int)X + 6 + 20, (int)curY + 6 + 20 + 20 + 7,color );
    		
    		
    		
    		
    		
    		
    		
    		//Hitting Type
    		
    		
    		RenderUtil.drawImage(new ResourceLocation("Client/Sword.png"), (int)X + 4, (int)curY + 4 + 20 + 20 + 20 + 7, 20, 20);
    		color = Color.GREEN.getRGB();
    		String LoseOrWin = "Finding Player...";
    		int SelfScore = 0;
    		int TargetScore = 0;
    		
    		if(target instanceof EntityPlayer)
    		{
    			EntityPlayer entity = (EntityPlayer) target;
    			for (ItemStack armourStack : entity.inventory.armorInventory)
    				if (armourStack != null) {
    					ItemStack renderStack1 = armourStack.copy();
    					if(Item.getIdFromItem(renderStack1.getItem()) == 298)
    						TargetScore += 1;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 299)
    						TargetScore += 1;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 300)
    						TargetScore += 1;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 301)
    						TargetScore += 1;
    					//鐨潻
    					if(Item.getIdFromItem(renderStack1.getItem()) == 302)
    						TargetScore += 3;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 303)
    						TargetScore += 3;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 304)
    						TargetScore += 3;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 305)
    						TargetScore += 3;
    					//閾剧敳
    					if(Item.getIdFromItem(renderStack1.getItem()) == 306)
    						TargetScore += 4;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 307)
    						TargetScore += 4;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 308)
    						TargetScore += 4;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 309)
    						TargetScore += 4;
    					//閾�
    					if(Item.getIdFromItem(renderStack1.getItem()) == 310)
    						TargetScore += 5;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 311)
    						TargetScore += 5;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 312)
    						TargetScore += 5;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 313)
    						TargetScore += 5;
    					//閽荤煶
    					if(Item.getIdFromItem(renderStack1.getItem()) == 314)
    						TargetScore += 2;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 315)
    						TargetScore += 2;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 316)
    						TargetScore += 2;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 317)
    						TargetScore += 2;
    					//閲�
    				}
    			if(entity.getHeldItem() != null)
    			TargetScore +=entity.getHeldItem().getMaxDamage();
    			TargetScore +=entity.getHealth();
    			TargetScore +=entity.isBlocking()?1:0;
    			TargetScore +=(!entity.onGround && entity.fallDistance >0)?1:0;
    		}
    		if(target instanceof EntityPlayer)
    		{
    			EntityPlayer entity = (EntityPlayer) mc.thePlayer;
    			for (ItemStack armourStack : entity.inventory.armorInventory)
    				if (armourStack != null) {
    					ItemStack renderStack1 = armourStack.copy();
    					if(Item.getIdFromItem(renderStack1.getItem()) == 298)
    						SelfScore += 1;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 299)
    						SelfScore += 1;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 300)
    						SelfScore += 1;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 301)
    						SelfScore += 1;
    					//鐨潻
    					if(Item.getIdFromItem(renderStack1.getItem()) == 302)
    						SelfScore += 3;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 303)
    						SelfScore += 3;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 304)
    						SelfScore += 3;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 305)
    						SelfScore += 3;
    					//閾剧敳
    					if(Item.getIdFromItem(renderStack1.getItem()) == 306)
    						SelfScore += 4;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 307)
    						SelfScore += 4;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 308)
    						SelfScore += 4;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 309)
    						SelfScore += 4;
    					//閾�
    					if(Item.getIdFromItem(renderStack1.getItem()) == 310)
    						SelfScore += 5;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 311)
    						SelfScore += 5;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 312)
    						SelfScore += 5;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 313)
    						SelfScore += 5;
    					//閽荤煶
    					if(Item.getIdFromItem(renderStack1.getItem()) == 314)
    						SelfScore += 2;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 315)
    						SelfScore += 2;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 316)
    						SelfScore += 2;
    					if(Item.getIdFromItem(renderStack1.getItem()) == 317)
    						SelfScore += 2;
    					//閲�
    				}
    			if(entity.getHeldItem() != null)
    			SelfScore +=entity.getHeldItem().getMaxDamage();
    			SelfScore +=entity.getHealth();
    			SelfScore +=entity.isBlocking()?1:0;
    			SelfScore +=(!entity.onGround && entity.fallDistance >0)?1:0;
    			

    			
    			
    		}
			if(SelfScore > TargetScore)
			{
				color = Color.GREEN.getRGB();
				LoseOrWin = "I think you will win.";
			}
			if(SelfScore < TargetScore)
			{
				LoseOrWin = "I think you will lose.";
				color = Color.RED.getRGB();
			}
			if(SelfScore - TargetScore<2 && SelfScore - TargetScore>-2)
			{
				LoseOrWin = "WTF? I DONT THINK ANY.";
				color = Color.YELLOW.getRGB();
			}
    		TextLength = font.getStringWidth(LoseOrWin);
    		font.drawString(LoseOrWin, (int)X + 6 + 20, (int)curY + 6 + 20 + 20 + 20 + 10, color);
    		
    		// Duel Info
        }
	}

	@EventTarget
	public void onRender(EventRender render) { // Copy
		if (target == null || !esp.getValueState()) {
			return;
		}
		mc.getRenderManager();
		double x = Killaura.target.lastTickPosX
				+ (Killaura.target.posX - Killaura.target.lastTickPosX) * mc.timer.renderPartialTicks
				- RenderManager.renderPosX;
		mc.getRenderManager();
		double y = Killaura.target.lastTickPosY
				+ (Killaura.target.posY - Killaura.target.lastTickPosY) * mc.timer.renderPartialTicks
				- RenderManager.renderPosY;
		mc.getRenderManager();
		double z = Killaura.target.lastTickPosZ
				+ (Killaura.target.posZ - Killaura.target.lastTickPosZ) * mc.timer.renderPartialTicks
				- RenderManager.renderPosZ;
		double width = Killaura.target.getEntityBoundingBox().maxX - Killaura.target.getEntityBoundingBox().minX;
		double height = Killaura.target.getEntityBoundingBox().maxY - Killaura.target.getEntityBoundingBox().minY
				+ 0.25;
		RenderUtil.drawEntityESP(x, y, z, width, height, (target.hurtTime > 1) ? 1f : 0.0f,
				(target.hurtTime > 1) ? 0.0f : 1f, 0f, 0.2f, (target.hurtTime > 1) ? 1f : 0f,
				(target.hurtTime > 1) ? 0f : 1f, 0.0f, 1f, 2f);
	}

	public static double getRandomDoubleInRange(double minDouble, double maxDouble) {
		return minDouble >= maxDouble ? minDouble : new Random().nextDouble() * (maxDouble - minDouble) + minDouble;
	}

	@EventTarget
	public void onPre(EventPreMotion event) {
		rotationYawHead = mc.thePlayer.rotationYawHead;
		// 锟斤拷始锟斤拷锟斤拷锟斤拷
		// isBlocking = false;
		// needBlock = false;
		needHitBot = null;

		if (!targets.isEmpty() && index >= targets.size())
			index = 0; // 锟斤拷锟斤拷Switch锟斤拷锟斤拷

		for (EntityLivingBase ent : targets) { // 锟斤拷锟绞碉拷锟�
			if (isValidEntity(ent))
				continue;
			targets.remove(ent);
		}
		// Switch锟斤拷锟斤拷

		getTarget(event); // 锟斤拷实锟斤拷

		if (targets.size() == 0) { // 实锟斤拷锟斤拷锟斤拷为0停止锟斤拷锟斤拷
			target = null;
			attackSpeed = 0;
		} else {
			target = targets.get(index);// 锟斤拷锟矫癸拷锟斤拷锟斤拷Target
			axisAlignedBB = null;
			if (mc.thePlayer.getDistanceToEntity(target) > reach.getValueState()) {
				target = targets.get(0);
			}
		}
		if(ModManager.getModByName("Scaffold").isEnabled())
		{
			target = null;
			return;
		}
		if (target != null) {
			// Switch锟斤拷始
			if (target.hurtTime == 10 && switchTimer.isDelayComplete(switchDelay.getValueState())
					&& targets.size() > 1) {
				switchTimer.reset();
				++index;
			}
			float diff = Math.abs(Math.abs(MathHelper.wrapAngleTo180_float(rotationYawHead))
					- Math.abs(MathHelper.wrapAngleTo180_float(RotationUtil.getRotations(target)[0])));

			if (rotations.getValueState()) { // 扭头
				if (rotMode.isCurrentMode("Hypixel-AA")) {

					float[] rotation = getEntityRotations(target, lastRotations, false,
							turn.getValueState().intValue());
					lastRotations = new float[] { rotation[0], rotation[1] };

					event.setYaw(rotation[0]);
					mc.thePlayer.renderYawOffset = event.getYaw();
					mc.thePlayer.rotationYawHead = event.getYaw();
					

					event.setPitch(rotation[1]);
					mc.thePlayer.renderArmPitch = event.getPitch();
					rotationYawHead = event.getYaw();

				} else if (rotMode.isCurrentMode("Smooth")) {
					double comparison = Math.abs(target.posY - mc.thePlayer.posY) > 1.8
							? Math.abs(target.posY - mc.thePlayer.posY) / Math.abs(target.posY - mc.thePlayer.posY) / 2
							: Math.abs(target.posY - mc.thePlayer.posY);

					Vector3<Double> enemyCoords = new Vector3<>(
							target.getEntityBoundingBox().minX
									+ (target.getEntityBoundingBox().maxX - target.getEntityBoundingBox().minX) / 2,
							(target instanceof EntityPig || target instanceof EntitySpider
									? target.getEntityBoundingBox().minY - target.getEyeHeight() * 1.2
									: target.posY) - comparison,
							target.getEntityBoundingBox().minZ
									+ (target.getEntityBoundingBox().maxZ - target.getEntityBoundingBox().minZ) / 2);

					Vector3<Double> myCoords = new Vector3<>(mc.thePlayer.getEntityBoundingBox().minX
							+ (mc.thePlayer.getEntityBoundingBox().maxX - mc.thePlayer.getEntityBoundingBox().minX) / 2,
							mc.thePlayer.posY,
							mc.thePlayer.getEntityBoundingBox().minZ + (mc.thePlayer.getEntityBoundingBox().maxZ
									- mc.thePlayer.getEntityBoundingBox().minZ) / 2);

					Angle srcAngle = new Angle(lastRotation[0], lastRotation[1]);

					Angle dstAngle = angleUtility.calculateAngle(enemyCoords, myCoords);
					Angle smoothedAngle = angleUtility.smoothAngle(dstAngle, srcAngle,
							turn.getValueState().floatValue() * 8, turn.getValueState().floatValue() * 7.5f);
					Random rand = new Random();
					event.setYaw(smoothedAngle.getYaw() + randomNumber(-2, 2));
					event.setPitch(smoothedAngle.getPitch() + randomNumber(-3, 3));
					lastRotation[0] = event.getYaw();
					mc.thePlayer.renderYawOffset = event.getYaw();
					mc.thePlayer.renderArmPitch = event.pitch;
					lastRotation[1] = event.getPitch();
					rotationYawHead = event.getYaw();
				} else if (rotMode.isCurrentMode("Hypixel-FA")) {
					float[] rot = RotationUtil.getRotations(target);
					Random rand = new Random();
					event.setYaw(rot[0] + rand.nextInt(10) - 5);
					event.setPitch(rot[1] + rand.nextInt(3) - 2);
					rotationYawHead = event.getYaw();
				} else if (rotations.getValueState() && rotMode.isCurrentMode("Hypixel-F")) {// 全锟斤拷
					Iterator var4 = mc.theWorld.loadedEntityList.iterator();

					float[] facing = getAnglesIgnoringNull(target, yaw, pitch);
					lastRotations[0] = facing[0];
					lastRotations[1] = facing[1];
					event.setYaw(facing[0]);
					mc.thePlayer.rotationYawHead = event.getYaw();
					mc.thePlayer.renderYawOffset = event.getYaw();
					mc.thePlayer.renderArmPitch = event.pitch;
					mc.thePlayer.renderYawOffset = event.getYaw();
					event.setPitch(facing[1]);
					rotationYawHead = event.getYaw();

				} else if (rotations.getValueState() && rotMode.isCurrentMode("Hypixel")) {// 全锟斤拷
					Iterator var4 = mc.theWorld.loadedEntityList.iterator();

					float[] facing = RotUtil.getRotations(target);
					lastRotations[0] = facing[0];
					lastRotations[1] = facing[1];
					event.setYaw(facing[0]);
					mc.thePlayer.rotationYawHead = event.getYaw();
					mc.thePlayer.renderYawOffset = event.getYaw();
					mc.thePlayer.renderArmPitch = event.pitch;
					mc.thePlayer.renderYawOffset = event.getYaw();
					event.setPitch(facing[1]);
					rotationYawHead = event.getYaw();

				} else if (rotations.getValueState() && rotMode.isCurrentMode("Hypixel-F")) {// 全锟斤拷
					Iterator var4 = mc.theWorld.loadedEntityList.iterator();

					float[] facing = RotUtil.getRotationNeededHypixelBetter(target);
					lastRotations[0] = facing[0];
					lastRotations[1] = facing[1];
					event.setYaw(facing[0]);
					mc.thePlayer.rotationYawHead = event.getYaw();
					mc.thePlayer.renderYawOffset = event.getYaw();
					mc.thePlayer.renderArmPitch = event.pitch;
					mc.thePlayer.renderYawOffset = event.getYaw();
					event.setPitch(facing[1]);
					rotationYawHead = event.getYaw();

				}
			}
			if (mc.thePlayer.isBlocking()
					|| mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword
							&& autoBlock.getValueState() && isBlocking && blockMode.isCurrentMode("Hypixel")) { // 锟斤拷
				unBlock(!mc.thePlayer.isBlocking() && !autoBlock.getValueState()
						&& mc.thePlayer.getItemInUseCount() > 0);
			}

		} else { // 没实锟斤拷
			lastRotation[0] = mc.thePlayer.rotationYaw;
			targets.clear();
			if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword
					&& autoBlock.getValueState() && isBlocking) {
				unBlock(true);
			}
		}
	}

	private void doBlock(boolean setItemUseInCount) {
		if (setItemUseInCount)
			(mc.thePlayer).itemInUseCount = (mc.thePlayer.getHeldItem().getMaxItemUseDuration());
		mc.thePlayer.sendQueue.getNetworkManager()
				.sendPacket(new C08PacketPlayerBlockPlacement(
						blockMode.isCurrentMode("Hypixel") ? new BlockPos(-1, -1, -1) : BlockPos.ORIGIN, 255,
						mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
		// needUnBlock = true;
		isBlocking = true;
	}

	private void unBlock(boolean setItemUseInCount) {
		if (setItemUseInCount)
			(mc.thePlayer).itemInUseCount = (0);
		mc.thePlayer.sendQueue.getNetworkManager()
				.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
						blockMode.isCurrentMode("Hypixel") ? new BlockPos(-1, -1, -1) : BlockPos.ORIGIN,
						EnumFacing.DOWN));
		// needUnBlock = false;
		isBlocking = false;
	}

	public static float[] getEntityRotations(EntityLivingBase target, float[] lastrotation, boolean aac, int smooth) {
		myAngleUtility angleUtility = new myAngleUtility(aac, smooth);
		Vector3d enemyCoords = new Vector3d(target.posX, target.posY + target.getEyeHeight(), target.posZ);
		Vector3d myCoords = new Vector3d(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				mc.thePlayer.posZ);
		myAngle dstAngle = angleUtility.calculateAngle(enemyCoords, myCoords);
		myAngle srcAngle = new myAngle(lastrotation[0], lastrotation[1]);
		myAngle smoothedAngle = angleUtility.smoothAngle(dstAngle, srcAngle);
		float yaw = smoothedAngle.getYaw();
		float pitch = smoothedAngle.getPitch();
		float yaw2 = MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
		yaw = mc.thePlayer.rotationYaw + yaw2;
		return new float[] { yaw, pitch };
	}

	private int randomNumber(int max, int min) {
		return (int) (Math.random() * (double) (max - min)) + min;
	}

	private void doAttack() {
		// 锟斤拷CPS - Delay
		int aps = cps.getValueState().intValue();
		int delayValue = 1000 / aps + random.nextInt(50) - 30;

		if (attacktimer.isDelayComplete(delayValue - 20 + random.nextInt(50))) { // 锟斤拷锟斤拷Timer
			boolean miss = false;
			boolean isInRange = mc.thePlayer.getDistanceToEntity(target) <= reach.getValueState();

			if (isInRange) {
				attacktimer.reset();
				if (target.hurtTime > hurttime.getValueState() || // Hurttime
						random.nextInt(100) < mistake.getValueState().intValue() // 锟斤拷锟組istakes
				)
					miss = true;

				float diff = Math.abs(Math.abs(MathHelper.wrapAngleTo180_float(rotationYawHead))
						- Math.abs(MathHelper.wrapAngleTo180_float(RotationUtil.getRotations(target)[0])));

				if (diff > yawDiff.getValueState() && !ModManager.getModule("Scaffold").isEnabled()) {
					miss = true;
				}
			}

			if (mc.thePlayer.isBlocking() || mc.thePlayer.getHeldItem() != null
					&& mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock.getValueState()) { // 锟斤拷
				unBlock(!mc.thePlayer.isBlocking() && !autoBlock.getValueState()
						&& mc.thePlayer.getItemInUseCount() > 0);
			}

			if (isInRange) {
				attack(miss); // 锟斤拷锟斤拷锟斤拷锟斤拷miss
			}
			// needBlock = true;
		}
	}

	@EventTarget
	public void onPost(EventPostMotion event) {
		if (target != null)
			doAttack(); // 锟斤拷锟斤拷

		// 锟斤拷锟斤拷
		if (target != null
				&& (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword
						&& autoBlock.getValueState() || mc.thePlayer.isBlocking())
				&& /* needBlock */!isBlocking) { // 锟斤拷
			doBlock(true);
		}
	}

	@EventTarget
	public void onBlockPacket(EventPacket e) {
	}

	int attackSpeed;

	private void attack(boolean mistake) {
		this.Crit = false;

		if (!mistake) {
			needBlock = true; // 确锟斤拷锟斤拷
			ArrayList<EntityLivingBase> list = new ArrayList<EntityLivingBase>();
			for (Entity entity : mc.theWorld.loadedEntityList) {
				float diff = Math.abs(Math.abs(MathHelper.wrapAngleTo180_float(rotationYawHead))
						- Math.abs(MathHelper.wrapAngleTo180_float(RotationUtil.getRotations(entity)[0])));

				if (entity instanceof EntityZombie && entity.isInvisible()
						&& (diff < yawDiff.getValueState() || mc.thePlayer.getDistanceToEntity(target) < 1)
						&& mc.thePlayer.getDistanceToEntity(entity) < reach.getValueState() && entity != mc.thePlayer) {

					list.add((EntityLivingBase) entity);
				}
			}
			if (list.size() == 0)
				list.add(target);
			needHitBot = list.get(random.nextInt(list.size()));

			EventManager.call(new EventAttack(target));

			
			Criticals Crit = (Criticals) ModManager.getModByName("Criticals");
			if(Crit.autoCrit(target))
			{
				PlayerUtil.tellPlayer("Do Criticals");
				this.Crit = true;
				attackSpeed = 0;
			}

			attackSpeed++;
			mc.thePlayer.swingItem();
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK)); // 锟斤拷锟斤拷

			// this.Crit = false;

			if (!attacked.contains(target) && target instanceof EntityPlayer) {
				attacked.add(target);
			}
			needHitBot = null;
		} else {
			mc.thePlayer.swingItem();
		}
	}

	private void getTarget(EventPreMotion event) {
		int maxSize = switchsize.getValueState().intValue(); // 锟斤拷锟绞碉拷锟斤拷锟斤拷锟�

		if (maxSize > 1) {
			setDisplayName("Switch" + " " + this.rotMode.getModeAt(rotMode.getCurrentMode()));
		} else {
			setDisplayName("Single" + " " + this.rotMode.getModeAt(rotMode.getCurrentMode()));
		}

		for (Entity o3 : mc.theWorld.loadedEntityList) { // 锟斤拷锟斤拷实锟斤拷
			EntityLivingBase curEnt;

			if (o3 instanceof EntityLivingBase && isValidEntity(curEnt = (EntityLivingBase) o3)
					&& !targets.contains(curEnt))
				targets.add(curEnt);

			if (targets.size() >= maxSize)
				break; // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
		}

		// 锟斤拷锟斤拷目锟斤拷实锟斤拷
		if (priority.isCurrentMode("Range"))
			targets.sort(
					(o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));

		if (priority.isCurrentMode("Fov"))
			targets.sort(Comparator.comparingDouble(o -> RotationUtil
					.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, RotationUtil.getRotations(o)[0])));

		if (priority.isCurrentMode("Angle")) {
			targets.sort((o1, o2) -> {
				float[] rot1 = RotationUtil.getRotations(o1);
				float[] rot2 = RotationUtil.getRotations(o2);
				return (int) (mc.thePlayer.rotationYaw - rot1[0] - (mc.thePlayer.rotationYaw - rot2[0]));
			});
		}

	}

	@EventTarget
	private void onPacket(EventPacket e) { // No Rotate 锟斤拷止扭头
		if (e.getPacket() instanceof S08PacketPlayerPosLook) {
			S08PacketPlayerPosLook look = (S08PacketPlayerPosLook) e.getPacket();
			look.yaw = (mc.thePlayer.rotationYaw);
			look.pitch = (mc.thePlayer.rotationPitch);
		}
	}

	private boolean isValidEntity(Entity entity) {
		if (entity != null && entity instanceof EntityLivingBase) {
			if (entity.isDead || ((EntityLivingBase) entity).getHealth() <= 0f) {

				return false;
			}

			if (mc.thePlayer.getDistanceToEntity(entity) < (reach.getValueState() + blockReach.getValueState())) {
				if (entity != mc.thePlayer && !mc.thePlayer.isDead
						&& !(entity instanceof EntityArmorStand || entity instanceof EntitySnowman)) {

					if (entity instanceof EntityPlayer && attackPlayers.getValueState()) {
						if (entity.ticksExisted < 30)
							return false;

						if (!mc.thePlayer.canEntityBeSeen(entity) && !throughblock.getValueState())
							return false;

						if (entity.isInvisible() && !invisible.getValueState())
							return false;

						return !AntiBot.isBot(entity) && !Teams.isOnSameTeam(entity);
					}

					if (entity instanceof EntityMob && attackMobs.getValueState()) {
						return !AntiBot.isBot(entity);
					}

					if ((entity instanceof EntityAnimal || entity instanceof EntityVillager)
							&& attackAnimals.getValueState()) {
						return !AntiBot.isBot(entity);
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onEnable() {
		curY = new ScaledResolution(this.mc).getScaledHeight();
		this.Crit = false;
		shouldAddYaw = 0;
		attacked = new ArrayList<EntityLivingBase>();
		axisAlignedBB = null;
		if (mc.thePlayer != null) {
			lastRotation[0] = mc.thePlayer.rotationYaw;
			lastRotations = new float[] { mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch };
		}
		index = 0; // Switch Target指锟斤拷
		super.onEnable();
	}

	@Override
	public void onDisable() {
		curY = new ScaledResolution(this.mc).getScaledHeight();
		this.Crit = false;
		axisAlignedBB = null;
		if (mc.thePlayer != null) {
			lastRotation[0] = mc.thePlayer.rotationYaw;
		}

		targets.clear();
		target = null; // 锟斤拷锟侥匡拷锟� (AutoBlock锟斤拷锟斤拷锟睫革拷)

		unBlock(true);

		super.onDisable();
	}

	public static double isInFov(float var0, float var1, double var2, double var4, double var6) {
		Vec3 var8 = new Vec3((double) var0, (double) var1, 0.0D);
		float[] var9 = getAngleBetweenVecs(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
				new Vec3(var2, var4, var6));
		double var10 = MathHelper.wrapAngleTo180_double(var8.xCoord - (double) var9[0]);
		return Math.abs(var10) * 2.0D;
	}

	public static float[] getAngleBetweenVecs(Vec3 var0, Vec3 var1) {
		double var2 = var1.xCoord - var0.xCoord;
		double var4 = var1.yCoord - var0.yCoord;
		double var6 = var1.zCoord - var0.zCoord;
		double var8 = Math.sqrt(var2 * var2 + var6 * var6);
		float var10 = (float) (Math.atan2(var6, var2) * 180.0D / 3.141592653589793D) - 90.0F;
		float var11 = (float) (-(Math.atan2(var4, var8) * 180.0D / 3.141592653589793D));
		return new float[] { var10, var11 };
		
	
	}

	public static float[] getAnglesIgnoringNull(Entity var0, float var1, float var2) {
		float[] var3 = getAngles(var0);
		if (var3 == null) {
			return new float[] { 0.0F, 0.0F };
		} else {
			float var4 = var3[0];
			float var5 = var3[1];
			return new float[] { var1 + MathHelper.wrapAngleTo180_float(var4 - var1),
					var2 + MathHelper.wrapAngleTo180_float(var5 - var2) + 5.0F };
		}
	}

	public static float[] getAngles(Entity entity) {
		if (entity == null) {
			return null;
		} else {
			double var1 = entity.posX - mc.thePlayer.posX;
			double var3 = entity.posZ - mc.thePlayer.posZ;
			double var5;
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase var7 = (EntityLivingBase) entity;
				var5 = var7.posY + ((double) var7.getEyeHeight() - 0.4D)
						- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
			} else {
				var5 = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D
						- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
			}

			double var11 = (double) MathHelper.sqrt_double(var1 * var1 + var3 * var3);
			float var9 = (float) (Math.atan2(var3, var1) * 180.0D / 3.141592653589793D) - 90.0F;
			float var10 = (float) (-(Math.atan2(var5, var11) * 180.0D / 3.141592653589793D));
			return new float[] { var9, var10 };
		}
	}

	public static boolean isValidToRotate(double var0, double var2) {
		if (mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.getEntityWorld() != null) {
			Iterator var4 = mc.thePlayer.getEntityWorld().loadedEntityList.iterator();

			Entity var5;
			do {
				if (!var4.hasNext()) {
					return false;
				}

				var5 = (Entity) var4.next();
			} while (!(var5 instanceof EntityPlayer) || var5 == mc.thePlayer
					|| (double) mc.thePlayer.getDistanceToEntity(var5) >= var0 || isInFov(mc.thePlayer.rotationYaw,
							mc.thePlayer.rotationPitch, var5.posX, var5.posY, var5.posZ) >= var2);

			return true;
		} else {
			return false;
		}
	}

	public static double normalizeAngle(double var0, double var2) {
		double var4 = Math.abs(var0 % 360.0D - var2 % 360.0D);
		var4 = Math.min(360.0D - var4, var4);
		return Math.abs(var4);
	}

	private double getAngleYaw(EntityLivingBase var1) {
		return (double) getAnglesIgnoringNull(var1, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)[0];
	}

}

class myAngle {
	private float yaw;
	private float pitch;

	public myAngle(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public myAngle() {
		this(0.0f, 0.0f);
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public myAngle constrantAngle() {
		this.setYaw(this.getYaw() % 360F);
		this.setPitch(this.getPitch() % 360F);

		while (this.getYaw() <= -180F) {
			this.setYaw(this.getYaw() + 360F);
		}

		while (this.getPitch() <= -180F) {
			this.setPitch(this.getPitch() + 360F);
		}

		while (this.getYaw() > 180F) {
			this.setYaw(this.getYaw() - 360F);
		}

		while (this.getPitch() > 180F) {
			this.setPitch(this.getPitch() - 360F);
		}

		return this;
	}
}

class myAngleUtility {

	private boolean aac;
	private float smooth;
	private Random random;

	public myAngleUtility(boolean aac, float smooth) {
		this.aac = aac;
		this.smooth = smooth;
		this.random = new Random();
	}

	public myAngle calculateAngle(Vector3d destination, Vector3d source) {
		myAngle angles = new myAngle();
		destination.x += (aac ? randomFloat(-0.75F, 0.75F) : 0.0F) - source.x;
		destination.y += (aac ? randomFloat(-0.25F, 0.5F) : 0.0F) - source.y;
		destination.z += (aac ? randomFloat(-0.75F, 0.75F) : 0.0F) - source.z;
		double hypotenuse = Math.hypot(destination.x, destination.z);
		angles.setYaw((float) (Math.atan2(destination.z, destination.x) * 57.29577951308232D) - 90.0F);
		angles.setPitch(-(float) ((Math.atan2(destination.y, hypotenuse) * 57.29577951308232D)));
		return angles.constrantAngle();
	}

	public myAngle smoothAngle(myAngle destination, myAngle source) {
		myAngle angles = (new myAngle(source.getYaw() - destination.getYaw(),
				source.getPitch() - destination.getPitch())).constrantAngle();
		angles.setYaw(source.getYaw() - angles.getYaw() / 100.0F * smooth);
		angles.setPitch(source.getPitch() - angles.getPitch() / 100.0F * smooth);
		return angles.constrantAngle();
	}

	public float randomFloat(float min, float max) {
		return min + (this.random.nextFloat() * (max - min));
	}

	private static Minecraft mc = Minecraft.getMinecraft();

	public static double isInFov(float var0, float var1, double var2, double var4, double var6) {
		Vec3 var8 = new Vec3((double) var0, (double) var1, 0.0D);
		float[] var9 = getAngleBetweenVecs(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
				new Vec3(var2, var4, var6));
		double var10 = MathHelper.wrapAngleTo180_double(var8.xCoord - (double) var9[0]);
		return Math.abs(var10) * 2.0D;
	}

	public static float[] getAngleBetweenVecs(Vec3 var0, Vec3 var1) {
		double var2 = var1.xCoord - var0.xCoord;
		double var4 = var1.yCoord - var0.yCoord;
		double var6 = var1.zCoord - var0.zCoord;
		double var8 = Math.sqrt(var2 * var2 + var6 * var6);
		float var10 = (float) (Math.atan2(var6, var2) * 180.0D / 3.141592653589793D) - 90.0F;
		float var11 = (float) (-(Math.atan2(var4, var8) * 180.0D / 3.141592653589793D));
		return new float[] { var10, var11 };
	}

	public static float[] getAnglesIgnoringNull(Entity var0, float var1, float var2) {
		float[] var3 = getAngles(var0);
		if (var3 == null) {
			return new float[] { 0.0F, 0.0F };
		} else {
			float var4 = var3[0];
			float var5 = var3[1];
			return new float[] { var1 + MathHelper.wrapAngleTo180_float(var4 - var1),
					var2 + MathHelper.wrapAngleTo180_float(var5 - var2) + 5.0F };
		}
	}

	public static float[] getAngles(Entity entity) {
		if (entity == null) {
			return null;
		} else {
			double var1 = entity.posX - mc.thePlayer.posX;
			double var3 = entity.posZ - mc.thePlayer.posZ;
			double var5;
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase var7 = (EntityLivingBase) entity;
				var5 = var7.posY + ((double) var7.getEyeHeight() - 0.4D)
						- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
			} else {
				var5 = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D
						- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
			}

			double var11 = (double) MathHelper.sqrt_double(var1 * var1 + var3 * var3);
			float var9 = (float) (Math.atan2(var3, var1) * 180.0D / 3.141592653589793D) - 90.0F;
			float var10 = (float) (-(Math.atan2(var5, var11) * 180.0D / 3.141592653589793D));
			return new float[] { var9, var10 };
		}
	}

	public static boolean isValidToRotate(double var0, double var2) {
		if (mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.getEntityWorld() != null) {
			Iterator var4 = mc.thePlayer.getEntityWorld().loadedEntityList.iterator();

			Entity var5;
			do {
				if (!var4.hasNext()) {
					return false;
				}

				var5 = (Entity) var4.next();
			} while (!(var5 instanceof EntityPlayer) || var5 == mc.thePlayer
					|| (double) mc.thePlayer.getDistanceToEntity(var5) >= var0 || isInFov(mc.thePlayer.rotationYaw,
							mc.thePlayer.rotationPitch, var5.posX, var5.posY, var5.posZ) >= var2);

			return true;
		} else {
			return false;
		}
	}

	public static double normalizeAngle(double var0, double var2) {
		double var4 = Math.abs(var0 % 360.0D - var2 % 360.0D);
		var4 = Math.min(360.0D - var4, var4);
		return Math.abs(var4);
	}

	private double getAngleYaw(EntityLivingBase var1) {
		return (double) getAnglesIgnoringNull(var1, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)[0];
	}

}

class RotUtil {
	static Minecraft mc = Minecraft.getMinecraft();
	public static Random rnd = new Random();
	public static float[] rot;

	static {
		rnd.setSeed(133789232L);
	}

	public static float getYawChangeGiven(double posX, double posZ, float yaw) {
		double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
		double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double yawToEntity;
		if (deltaZ < 0.0D && deltaX < 0.0D) {
			yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else if (deltaZ < 0.0D && deltaX > 0.0D) {
			yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else {
			yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
		}

		return MathHelper.wrapAngleTo180_float(-(yaw - (float) yawToEntity));
	}

	public static float[] getRotations(EntityLivingBase ent) {
		double x = ent.posX + (ent.posX - ent.lastTickPosX);
		double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
		double y = ent.posY + (double) (ent.getEyeHeight() / 2.1F);
		return getRotationFromPosition(x, z, y);
	}

	public static float[] getPredictedRotations(EntityLivingBase ent) {
		double x = ent.posX + (ent.posX - ent.lastTickPosX);
		double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
		double y = ent.posY + (double) (ent.getEyeHeight() / 2.0F);
		return getRotationFromPosition(x, z, y);
	}

	public static Vec3d getLook() {
		return func_174806_f(rot[1], rot[0]);
	}

	public static final Vec3d func_174806_f(float p_174806_1_, float p_174806_2_) {
		float var3 = MathHelper.cos(-p_174806_2_ * 0.017453292F - 3.1415927F);
		float var4 = MathHelper.sin(-p_174806_2_ * 0.017453292F - 3.1415927F);
		float var5 = -MathHelper.cos(-p_174806_1_ * 0.017453292F);
		float var6 = MathHelper.sin(-p_174806_1_ * 0.017453292F);
		return new Vec3d((double) (var4 * var5), (double) var6, (double) (var3 * var5));
	}

	public static Vec3d func_174824_e() {
		return new Vec3d(Minecraft.getMinecraft().renderViewEntity.posX,
				Minecraft.getMinecraft().renderViewEntity.posY
						+ (double) Minecraft.getMinecraft().renderViewEntity.getEyeHeight(),
				Minecraft.getMinecraft().renderViewEntity.posZ);
	}

	public static boolean isRotationIn(float[] rotation, AxisAlignedBB box) {
		float[] maxRotations = getMaxRotations(box);
		return maxRotations[0] < rotation[0] && maxRotations[2] < rotation[1] && maxRotations[1] > rotation[0]
				&& maxRotations[3] > rotation[1];
	}

	public static Vec3d[] getCorners(AxisAlignedBB box) {
		return new Vec3d[] { new Vec3d(box.minX, box.minY, box.minZ), new Vec3d(box.maxX, box.minY, box.minZ),
				new Vec3d(box.minX, box.maxY, box.minZ), new Vec3d(box.minX, box.minY, box.maxZ),
				new Vec3d(box.maxX, box.maxY, box.minZ), new Vec3d(box.minX, box.maxY, box.maxZ),
				new Vec3d(box.maxX, box.minY, box.maxZ), new Vec3d(box.maxX, box.maxY, box.maxZ) };
	}

	public static float[] getMaxRotations(AxisAlignedBB box) {
		float minYaw = 2.14748365E9F;
		float maxYaw = -2.14748365E9F;
		float minPitch = 2.14748365E9F;
		float maxPitch = -2.14748365E9F;
		Vec3d[] arrVec3d = getCorners(box);
		int n = arrVec3d.length;

		for (int n2 = 0; n2 < n; ++n2) {
			Vec3d pos = arrVec3d[n2];
			float[] rot = getRotationFromPosition(pos.xCoord, pos.yCoord, pos.zCoord);
			if (rot[0] < minYaw) {
				minYaw = rot[0];
			}

			if (rot[0] > maxYaw) {
				maxYaw = rot[0];
			}

			if (rot[1] < minPitch) {
				minPitch = rot[1];
			}

			if (rot[1] > maxPitch) {
				maxPitch = rot[1];
			}
		}

		return new float[] { minYaw, maxYaw, minPitch, maxPitch };
	}

	public static float[] getRotationNeededHypixelBetter(Entity p) {
		double d1 = p.posX - mc.thePlayer.posX;
		double d2 = p.posY + (double) p.getEyeHeight() - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
		double d3 = p.posZ - mc.thePlayer.posZ;
		double d4 = Math.sqrt(d1 * d1 + d3 * d3);
		float f1 = (float) (Math.atan2(d3, d1) * 180.0D / 3.141592653589793D) - 90.0F;
		float f2 = (float) (-Math.atan2(d2, d4) * 180.0D / 3.141592653589793D);
		return new float[] { f1, f2 };
	}

	public static float changeRotation(float p_706631, float p_706632, float p_706633) {
		float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
		if (var4 > p_706633) {
			var4 = p_706633;
		}

		if (var4 < -p_706633) {
			var4 = -p_706633;
		}

		return p_706631 + var4;
	}

	public static float[] getAverageRotations(List targetList) {
		double posX = 0.0D;
		double posY = 0.0D;
		double posZ = 0.0D;

		Entity ent;
		for (Iterator var8 = targetList.iterator(); var8.hasNext(); posZ += ent.posZ) {
			ent = (Entity) var8.next();
			posX += ent.posX;
			posY += ent.boundingBox.maxY - 2.0D;
		}

		posX /= (double) targetList.size();
		posY /= (double) targetList.size();
		posZ /= (double) targetList.size();
		return new float[] { getRotationFromPosition(posX, posZ, posY)[0],
				getRotationFromPosition(posX, posZ, posY)[1] };
	}

	public static float getStraitYaw() {
		float YAW = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
		if (YAW < 45.0F && YAW > -45.0F) {
			YAW = 0.0F;
		} else if (YAW > 45.0F && YAW < 135.0F) {
			YAW = 90.0F;
		} else if (YAW <= 135.0F && YAW >= -135.0F) {
			YAW = -90.0F;
		} else {
			YAW = 180.0F;
		}

		return YAW;
	}

	public static float[] getBowAngles(Entity entity) {
		double xDelta = (entity.posX - entity.lastTickPosX) * 0.4D;
		double zDelta = (entity.posZ - entity.lastTickPosZ) * 0.4D;
		double d = (double) Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
		d -= d % 0.8D;
		double xMulti = 1.0D;
		double zMulti = 1.0D;
		boolean sprint = entity.isSprinting();
		xMulti = d / 0.8D * xDelta * (sprint ? 1.25D : 1.0D);
		zMulti = d / 0.8D * zDelta * (sprint ? 1.25D : 1.0D);
		double x = entity.posX + xMulti - Minecraft.getMinecraft().thePlayer.posX;
		double z = entity.posZ + zMulti - Minecraft.getMinecraft().thePlayer.posZ;
		double y = Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight()
				- (entity.posY + (double) entity.getEyeHeight());
		double dist = (double) Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
		float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0F;
		double d1 = (double) MathHelper.sqrt_double(x * x + z * z);
		float pitch = (float) (-(Math.atan2(y, d1) * 180.0D / 3.141592653589793D)) + (float) dist * 0.11F;
		return new float[] { yaw, -pitch };
	}

	public static float[] getRotationFromPosition(double x, double z, double y) {
		double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
		double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
		double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2D;
		double dist = (double) MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) (-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D));
		return new float[] { yaw, pitch };
	}

	public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
		float g = 0.006F;
		float sqrt = velocity * velocity * velocity * velocity - g * (g * d3 * d3 + 2.0F * d1 * velocity * velocity);
		return (float) Math
				.toDegrees(Math.atan(((double) (velocity * velocity) - Math.sqrt((double) sqrt)) / (double) (g * d3)));
	}

	public static float getYawChange(float yaw, double posX, double posZ) {
		double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
		double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double yawToEntity = 0.0D;
		if (deltaZ < 0.0D && deltaX < 0.0D) {
			if (deltaX != 0.0D) {
				yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
			}
		} else if (deltaZ < 0.0D && deltaX > 0.0D) {
			if (deltaX != 0.0D) {
				yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
			}
		} else if (deltaZ != 0.0D) {
			yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
		}

		return MathHelper.wrapAngleTo180_float(-(yaw - (float) yawToEntity));
	}

	public static float pq(float v) {
		if ((v %= 360.0F) >= 180.0F) {
			v -= 360.0F;
		}

		if (v < -180.0F) {
			v += 360.0F;
		}

		return v;
	}

	public static float getPitchChange(float pitch, Entity entity, double posY) {
		double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
		double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double deltaY = posY - 2.2D + (double) entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
		double distanceXZ = (double) MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
		return -MathHelper.wrapAngleTo180_float(pitch - (float) pitchToEntity) - 2.5F;
	}

	public static float getNewAngle(float angle) {
		angle %= 360.0F;
		if (angle >= 180.0F) {
			angle -= 360.0F;
		}

		if (angle < -180.0F) {
			angle += 360.0F;
		}

		return angle;
	}

	public static float getDistanceBetweenAngles(float angle1, float angle2) {
		float angle = Math.abs(angle1 - angle2) % 360.0F;
		if (angle > 180.0F) {
			angle = 360.0F - angle;
		}

		return angle;
	}
}
