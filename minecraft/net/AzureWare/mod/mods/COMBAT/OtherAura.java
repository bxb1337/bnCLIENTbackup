//Code by Margele.

package net.AzureWare.mod.mods.COMBAT;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventAttack;
import net.AzureWare.events.EventPostMotion;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventRender;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.mods.COMBAT.AntiBot;
import net.AzureWare.mod.mods.PLAYER.Teams;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.CombatUtil;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.RotationUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.utils.Wrapper;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class OtherAura extends Mod {
	
    private EntityLivingBase target;
    
    private TimeHelper timer = new TimeHelper();
    private TimeHelper switchTimer = new TimeHelper();
    private TimeHelper switchClear = new TimeHelper();
    
    private Value targetMode = new Value("OtherAura", "TargetMode", 0);
    
    private Value<Double> range = new Value("OtherAura_Range", 4.5, 0.1, 6.0);
    private Value<Double> delay = new Value("OtherAura_Delay", 100.0, 25.0, 1000.0);
    private Value<Double> switchDelay = new Value("OtherAura_SwitchDelay", 100.0, 25.0, 1000.0);
    private Value<Double> mistake = new Value("OtherAura_Mistakes", 10.0, 0.0, 100.0);
    
    private Value<Boolean> block = new Value("OtherAura_AutoBlock", false);
    private Value<Boolean> swing = new Value("OtherAura_NoSwing", false);
    private Value<Boolean> raycast = new Value("OtherAura_RayCast", false);
    private Value<Boolean> jitter = new Value("OtherAura_JitterShake", true);
    private Value<Boolean> attackPlayers = new Value("OtherAura_AttackPlayers", true);
    private Value<Boolean> attackAnimals = new Value("OtherAura_AttackAnimals", true);
    private Value<Boolean> attackMobs = new Value("OtherAura_AttackMobs", true);
	private Value<Boolean> attackInvisible = new Value("OtherAura_AttackInvisible", true);
	private Value<Boolean> throughtBlock = new Value("OtherAura_ThoughtBlock", true);

	private int currentTarget;
    
	public volatile static ArrayList<Entity> attackList = new ArrayList<>();
	
    public OtherAura() {
        super("OtherAura", Category.COMBAT);
		targetMode.addValue("Range");
		targetMode.addValue("FOV");
		targetMode.addValue("Health");
		targetMode.addValue("Armor");
		targetMode.addValue("Dynamic");
    }

    //From Slowly.
	@EventTarget
	public void onRender(EventRender e) {
    	if(!isValidTarget(target)) {
    		return;
    	}
    	
        double x;
        double y;
        double z;
        double width;
        double height;
        float red;
        float green;
        float blue;
        float alpha;
        float lineRed;
        float lineGreen;
        float lineBlue;
        float lineAlpha;
        float lineWdith;
        Color color = new Color(Colors.BLUE.c);
        
        mc.getRenderManager();
        x = target.lastTickPosX + (target.posX - target.lastTickPosX) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosX;
        mc.getRenderManager();
        y = target.lastTickPosY + (target.posY - target.lastTickPosY) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosY;
        mc.getRenderManager();
        z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosZ;
        if(target instanceof EntityPlayer) {
           x -= 0.275D;
           z -= 0.275D;
           y += (double)target.getEyeHeight() - 0.225D - (target.isSneaking()?0.25D:0.0D);
           width = 0.275D;
           GL11.glPushMatrix();
           GL11.glEnable(3042);
           GL11.glBlendFunc(770, 771);
           height = -0.25D * (double)(Math.abs(target.rotationPitch) / 90.0F);
           GL11.glTranslated(0.0D, height, 0.0D);
           GL11.glTranslated(x + width, y + width, z + width);
           GL11.glRotated((double)(-target.rotationYaw % 360.0F), 0.0D, 1.0D, 0.0D);
           GL11.glTranslated(-(x + width), -(y + width), -(z + width));
           GL11.glTranslated(x + width, y + width, z + width);
           GL11.glRotated((double)target.rotationPitch, 1.0D, 0.0D, 0.0D);
           GL11.glTranslated(-(x + width), -(y + width), -(z + width));
           GL11.glDisable(3553);
           GL11.glEnable(2848);
           GL11.glDisable(2929);
           GL11.glDepthMask(false);
           GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 1.0F);
           GL11.glLineWidth(1.0F);
           RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - 0.0025D, y - 0.0025D, z - 0.0025D, x + 0.55D + 0.0025D, y + 0.55D + 0.0025D, z + 0.55D + 0.0025D));
           GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 0.5F);
           RenderUtil.drawBoundingBox(new AxisAlignedBB(x - 0.0025D, y - 0.0025D, z - 0.0025D, x + 0.55D + 0.0025D, y + 0.55D + 0.0025D, z + 0.55D + 0.0025D));
           GL11.glDisable(2848);
           GL11.glEnable(3553);
           GL11.glEnable(2929);
           GL11.glDepthMask(true);
           GL11.glDisable(3042);
           GL11.glPopMatrix();
	     } else {
             width = target.getEntityBoundingBox().maxX - target.getEntityBoundingBox().minX;
             height = target.getEntityBoundingBox().maxY - target.getEntityBoundingBox().minY + 0.25D;
             red = 0.0F;
             green = 0.5F;
             blue = 1.0F;
             alpha = 0.5F;
             lineRed = 0.0F;
             lineGreen = 0.5F;
             lineBlue = 1.0F;
             lineAlpha = 1.0F;
             lineWdith = 2.0F;
             RenderUtil.drawEntityESP(x, y, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue, lineAlpha, lineWdith);
          }
	}
    
	@Override
	public void onDisable() {
		currentTarget = 0;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        this.mc.playerController.onStoppedUsingItem(mc.thePlayer);
		super.onDisable();
	}
	
	@Override
    public void onEnable() {
        this.timer.reset();
        this.target = null;
        super.onEnable();
    }
    
    public EntityLivingBase getTarget() {
		this.setDisplayName("Living:" + attackList.size());
        return (EntityLivingBase)attackList.get(0);
    }
    
    /*
    @EventTarget
    public void onPre(EventPreMotion e) {
        target = getTarget(); //寻找最佳目标
        if (target != null) {
        	Entity rayCast = PlayerUtil.raycast(target);
            if(rayCast != null || !raycast.getValueState()) {
            	if(!mc.thePlayer.isBlocking() && 
            		mc.thePlayer.getCurrentEquippedItem() != null && 
            		block.getValueState() && 
            		mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            		
            		KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            		if(mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem())) {
            			mc.getItemRenderer().resetEquippedProgress2();
            		}
            		
            		PlayerUtil.tellPlayer("[Debug]Block");
            	}
            	
                float[] rotations = CombatUtil.getRotations(target); //获取目标实体的位置
                e.yaw = rotations[0]; //水平
                e.pitch = rotations[1]; //垂直
                
                if(jitter.getValueState()) {
                	e.yaw = e.yaw + new Random().nextFloat() * 8 - 5;
                	e.pitch = e.pitch + new Random().nextFloat() * 2 - 1;
                }
            }
        }
    }
    */
    
    @EventTarget
    public void onUpdate(EventUpdate e) {
    	setupTargets();
    }
    

	public boolean onServer(String server) {
	    return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains(server);
	}
	 
	
    @EventTarget
    public void onPost(EventPostMotion e) {
    	if (target != null && timer.isDelayComplete(new Random().nextInt(50) - 25 + delay.getValueState().longValue())) {
    		if(mc.thePlayer.isBlocking() && 
    		   mc.thePlayer.getCurrentEquippedItem() != null && 
    		   block.getValueState() && 
    		   mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
		         KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
		         this.mc.playerController.onStoppedUsingItem(mc.thePlayer);
		    	 PlayerUtil.tellPlayer("[AzureWare]De Block");
    		}
			  
    		
    		attackEntity(target); //攻击实体
            timer.reset();
        }
    }

    public void setupTargets() {
    	for(Entity e : mc.theWorld.loadedEntityList) {
    		if(e instanceof EntityLivingBase) {
                if(isValidTarget(e) && !attackList.contains(e))
                    attackList.add(e);
                if(!isValidTarget(e) && attackList.contains(e))
                    attackList.remove(e);
                if(mc.thePlayer.getDistanceToEntity(e) > 10 && attackList.contains(e))
                    attackList.remove(e);
    		}
    	}
    }
    
    public boolean isValidTarget(Entity ent) {
	       if (ent == mc.thePlayer) {
	    	   return false;
	       }
	       if (ent instanceof EntityPlayer && !attackPlayers.getValueState()) {
	    	   return false;
	       }
	       if ((ent instanceof EntityAnimal || ent instanceof EntitySquid || ent instanceof EntityArmorStand) && !attackAnimals.getValueState()) {
	    	   return false;
	       }
	       if ((ent instanceof EntityMob || ent instanceof EntityVillager || ent instanceof EntityBat) && !attackMobs.getValueState()) {
	    	   return false;
	       }
	       if (mc.thePlayer.getDistanceToEntity(ent) > range.getValueState()) {
	    	   return false;
	       }
	       if (ent.isDead) {
	    	   return false;
	       }
	       if (ent.isInvisible() && !attackInvisible.getValueState()) {
	    	   return false;
	       }
	       if (mc.thePlayer.isDead) {
	    	   return false;
	       }
	       if (ent instanceof EntityPlayer && (Teams.isOnSameTeam(ent) || AntiBot.isBot(ent))) {
	    	   return false;
	       }
	       if (!throughtBlock.getValueState() && ClientUtil.isBlockBetween(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), new BlockPos(ent.posX, ent.posY + ent.getEyeHeight(), ent.posZ))) {
	    	   return false;
	       }
	       if(ent instanceof EntityArmorStand) {
	    	   return false;
	       }
	       return true;
    }
    
    
    public void attackEntity(Entity ent) {
    	if(!isValidTarget(ent)) {
    		return;
    	}
    	
    	EventManager.call(new EventAttack(ent));
        
        if (!this.swing.getValueState()) {
            mc.thePlayer.swingItem(); //摇摆物品
        }
        
        if(new Random().nextInt(100) > mistake.getValueState().intValue()) {
        	PlayerUtil.tellPlayer("[AzureWare]Attack");
        	mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
        }else {
        	PlayerUtil.tellPlayer("[AzureWare]Mistake");
        }
        
    }
}
