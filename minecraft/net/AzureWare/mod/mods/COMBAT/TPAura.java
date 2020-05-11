package net.AzureWare.mod.mods.COMBAT;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.mod.mods.PLAYER.Teams;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.JigsawReach;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.RotationUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class TPAura extends Mod {
	private TimeHelper timer = new TimeHelper();
	private Value<Boolean> block = new Value("TPAura_AutoBlock", true);
    private Value<Double> reach = new Value("TPAura_Range", 4.2D, 0.1D, 100.0D);
    private Value<Double> delay = new Value("TPAura_Delay", 100D, 0.1D, 1000.0D);
    public Value<Double> maxTargets = new Value("TPAura_Targets", Double.valueOf(3.0D), Double.valueOf(1.0D), Double.valueOf(20.0D), 1.0D);
	private EntityOtherPlayerMP ent;

    public TPAura() {
        super("TPAura", Category.COMBAT);
    }

    public static EntityLivingBase getClosestEntity1() {
        EntityLivingBase closestEntity = null;
        Iterator<?> var2 = Minecraft.getMinecraft().theWorld.loadedEntityList.iterator();

        while (var2.hasNext()) {
            Object o = var2.next();
            EntityLivingBase entityplayer = (EntityLivingBase) o;
            if (!(o instanceof EntityPlayerSP) && !entityplayer.isDead && entityplayer.getHealth() > 0.0F && Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entityplayer) && !entityplayer.getName().equals(Minecraft.getMinecraft().thePlayer.getName()) && (closestEntity == null || Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entityplayer) < Minecraft.getMinecraft().thePlayer.getDistanceToEntity(closestEntity))) {
                closestEntity = entityplayer;
            }
        }

        return closestEntity;
    }
    
    @EventTarget
    public void onUpdate(EventUpdate e) {
    	float xAdd = new Random().nextInt(2) - new Random().nextFloat() * 2;
		Entity en = getOptimalTarget();
        if ((en == null) || (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) > reach.getValueState() && !Minecraft.getMinecraft().thePlayer.isInvisible())) {
            return;
        }
        
        float[] rots = RotationUtil.getRotations(en);
    	ent.setPositionAndRotation(en.posX, en.posY, en.posZ, rots[0], rots[1]);
    	ent.rotationYawHead = rots[0];
        ent.rotationYaw = rots[0];
        ent.rotationPitch = rots[1];
        ent.motionX = ent.motionY = 100;
    	ent.swingItem();
    	
    	if(timer.isDelayComplete(delay.getValueState().longValue())) {
    		ArrayList<Vec3> posBack = new ArrayList<Vec3>();
    		posBack.add(new Vec3(mc.thePlayer.posX,mc.thePlayer.posY,mc.thePlayer.posZ));
    		
    		ArrayList<Vec3> pos = new ArrayList<Vec3>();
    		pos.add(new Vec3(en.posX,en.posY,en.posZ));
    		
    		if(block.getValueState()) mc.gameSettings.keyBindUseItem.pressed = true;
    		if(mc.thePlayer.getDistanceToEntity(en) < 5) {
    			JigsawReach.attackInf((EntityLivingBase) en);
    		}else {
    			JigsawReach.infiniteReach(reach.getValueState(), 9.5, 9, posBack, pos, (EntityLivingBase)en);
    		}
    			
    		
    		
            mc.thePlayer.swingItem();
           
    		timer.reset();
    	}

    }

    @Override
    public void onDisable() {
    	mc.gameSettings.keyBindUseItem.pressed = false;
    	this.mc.theWorld.removeEntityFromWorld(-1);
        super.onDisable();
        mc.timer.timerSpeed = 1;
    }

    @Override
    public void onEnable() {
        double x = this.mc.thePlayer.posX;
        double y = this.mc.thePlayer.posY;
        double z = this.mc.thePlayer.posZ;
        float yaw = this.mc.thePlayer.rotationYaw;
        float pitch = this.mc.thePlayer.rotationPitch;
        ent = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile());
        ent.inventory = this.mc.thePlayer.inventory;
        ent.inventoryContainer = this.mc.thePlayer.inventoryContainer;
        this.mc.theWorld.addEntityToWorld(-1, ent);
        super.onEnable();
    }
    
    private Entity getOptimalTarget() {
        List<Entity> load = new ArrayList<Entity>();
        for (Entity o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof Entity) {
                Entity ent = o;
                if (!this.validEntity(ent)) {
                    continue;
                }
                load.add(ent);
            }
        }
        if (load.isEmpty()) {
            return null;
        }
        return this.getTarget(load);
    }
    
    private Entity getTarget(List<Entity> list) {
        this.sortList(list);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(new Random().nextInt(maxTargets.getValueState().intValue()));
    }
    
    private void sortList(List<Entity> weed) {
    	weed.sort((o1, o2) -> (int)(o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
    }
    
    private boolean validEntity(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity == this.mc.thePlayer) {
            return false;
        }
        if (entity.getName().equalsIgnoreCase("?6Dealer")) {
            return false;
        }
        if ((double)this.mc.thePlayer.getDistanceToEntity(entity) > reach.getValueState() + 1.0) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (this.mc.thePlayer.isDead) {
            return false;
        }
        if (entity instanceof EntityPlayer && (Teams.isOnSameTeam(entity) || AntiBot.isBot(entity))) {
            return false;
        }
        if (!(entity instanceof EntityLivingBase)) {
            return false;
        }
        return true;
    }
}
