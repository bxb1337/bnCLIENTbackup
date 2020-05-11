//Code by Margele.

package net.AzureWare.mod.mods.COMBAT;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
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
import net.AzureWare.mod.mods.PLAYER.Teams;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.CombatUtil;
import net.AzureWare.utils.FlatColors;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.RayTraceUtil;
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

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import org.lwjgl.opengl.GL11;

public class SkillAura extends Mod {
   TimeHelper kms = new TimeHelper();
   public ArrayList targets = new ArrayList();
   public ArrayList attackedTargets = new ArrayList();
   public static EntityLivingBase curTarget = null;
   public static EntityLivingBase curBot = null;
   public static EntityLivingBase lastTarget = null;
   public static Value autoBlock = new Value("SkillAura_AutoBlock", Boolean.valueOf(true));
   public static Value reach = new Value("SkillAura_Reach", Double.valueOf(3.8D), Double.valueOf(0.1D), Double.valueOf(7.0D));
   public Value attackDelay = new Value("SkillAura_Delay", Double.valueOf(120.0D), Double.valueOf(0.0D), Double.valueOf(1000.0D), 10.0D);
   public Value cracksize = new Value("SkillAura_CrackSize", Double.valueOf(1.0D), Double.valueOf(1.0D), Double.valueOf(10.0D), 1.0D);
   public static Value slow = new Value("SkillAura_SlowDown", Boolean.valueOf(false));
   public Value attackRandomDelay = new Value("SkillAura_Random", Boolean.valueOf(true));
   public Value attackPlayers = new Value("SkillAura_AttackPlayers", Boolean.valueOf(true));
   public Value attackAnimals = new Value("SkillAura_AttackAnimals", Boolean.valueOf(true));
   public Value attackMobs = new Value("SkillAura_AttackMobs", Boolean.valueOf(true));
   public Value aRayTrace = new Value("SkillAura_RayTrace", Boolean.valueOf(true));
   public Value blockRayTrace = new Value("SkillAura_BlockRayTrace", Boolean.valueOf(true));
   public Value rotations = new Value("SkillAura_Rotations", Boolean.valueOf(true));
   public Value speed = new Value("SkillAura_RotationSpeed", Double.valueOf(25.0D), Double.valueOf(1.0D), Double.valueOf(50.0D), 1.0D);
   public Value switchDelay = new Value("SkillAura_SwitchTicks", Double.valueOf(2.0D), Double.valueOf(0.0D), Double.valueOf(10.0D), 1.0D);
   public Value maxTargets = new Value("SkillAura_MaxTargets", Double.valueOf(2.0D), Double.valueOf(1.0D), Double.valueOf(20.0D), 1.0D);
   public Value noswing = new Value("SkillAura_NoSwing", Boolean.valueOf(false));
   public Value startDelay = new Value("SkillAura_StartDelay", Boolean.valueOf(true));
   public Value openInv = new Value("SkillAura_AttackInInventory", Boolean.valueOf(true));
   public Value invisible = new Value("SkillAura_AttackInvisibles", Boolean.valueOf(false));
   public Value espMode = new Value("SkillAura", "ESP", 0);
   private TimeHelper test = new TimeHelper();
   private boolean doBlock = false;
   private boolean unBlock = false;
   private Random random = new Random();
   private long lastMs;
   private int delay = 0;
   private int tick = 0;
   private float curYaw = 0.0F;
   private float curPitch = 0.0F;
   private float lastYaw;

   public SkillAura() {
      super("SkillAura", Category.COMBAT);
      this.showValue = reach;
      this.espMode.addValue("None");
      this.espMode.addValue("Box");
      this.espMode.addValue("Flat Box");
   }

   @EventTarget
   public void onRender(EventRender render) {
      if(curTarget != null && !this.espMode.isCurrentMode("None")) {
         Color color = new Color(Colors.BLUE.c);
         if(curTarget.hurtTime > 0) {
            color = new Color(FlatColors.RED.c);
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
         if(this.espMode.isCurrentMode("Box")) {
            x = curTarget.lastTickPosX + (curTarget.posX - curTarget.lastTickPosX) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            y = curTarget.lastTickPosY + (curTarget.posY - curTarget.lastTickPosY) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            z = curTarget.lastTickPosZ + (curTarget.posZ - curTarget.lastTickPosZ) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            if(curTarget instanceof EntityPlayer) {
               x -= 0.275D;
               z -= 0.275D;
               y += (double)curTarget.getEyeHeight() - 0.225D - (curTarget.isSneaking()?0.25D:0.0D);
               width = 0.275D;
               GL11.glPushMatrix();
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               height = -0.25D * (double)(Math.abs(curTarget.rotationPitch) / 90.0F);
               GL11.glTranslated(0.0D, height, 0.0D);
               GL11.glTranslated(x + width, y + width, z + width);
               GL11.glRotated((double)(-curTarget.rotationYaw % 360.0F), 0.0D, 1.0D, 0.0D);
               GL11.glTranslated(-(x + width), -(y + width), -(z + width));
               GL11.glTranslated(x + width, y + width, z + width);
               GL11.glRotated((double)curTarget.rotationPitch, 1.0D, 0.0D, 0.0D);
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
               width = curTarget.getEntityBoundingBox().maxX - curTarget.getEntityBoundingBox().minX;
               height = curTarget.getEntityBoundingBox().maxY - curTarget.getEntityBoundingBox().minY + 0.25D;
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
         } else {
            x = curTarget.lastTickPosX + (curTarget.posX - curTarget.lastTickPosX) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            y = curTarget.lastTickPosY + (curTarget.posY - curTarget.lastTickPosY) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            z = curTarget.lastTickPosZ + (curTarget.posZ - curTarget.lastTickPosZ) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            if(curTarget instanceof EntityPlayer) {
               x -= 0.5D;
               z -= 0.5D;
               y += (double)curTarget.getEyeHeight() + 0.35D - (curTarget.isSneaking()?0.25D:0.0D);
               width = 0.5D;
               GL11.glPushMatrix();
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               height = -0.25D * (double)(Math.abs(curTarget.rotationPitch) / 90.0F);
               GL11.glTranslated(x + width, y + width, z + width);
               GL11.glRotated((double)(-curTarget.rotationYaw % 360.0F), 0.0D, 1.0D, 0.0D);
               GL11.glTranslated(-(x + width), -(y + width), -(z + width));
               GL11.glDisable(3553);
               GL11.glEnable(2848);
               GL11.glDisable(2929);
               GL11.glDepthMask(false);
               GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 1.0F);
               GL11.glLineWidth(2.0F);
               RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 0.05D, z + 1.0D));
               GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 0.5F);
               RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 0.05D, z + 1.0D));
               GL11.glDisable(2848);
               GL11.glEnable(3553);
               GL11.glEnable(2929);
               GL11.glDepthMask(true);
               GL11.glDisable(3042);
               GL11.glPopMatrix();
            } else {
               width = curTarget.getEntityBoundingBox().maxZ - curTarget.getEntityBoundingBox().minZ;
               height = 0.1D;
               red = 0.0F;
               green = 0.5F;
               blue = 1.0F;
               alpha = 0.5F;
               lineRed = 0.0F;
               lineGreen = 0.5F;
               lineBlue = 1.0F;
               lineAlpha = 1.0F;
               lineWdith = 2.0F;
               RenderUtil.drawEntityESP(x, y + (double)curTarget.getEyeHeight() + 0.25D, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue, lineAlpha, lineWdith);
            }
         }

      }
   }

   @EventTarget
   public void onPre(EventPreMotion event) {
      Iterator i = this.targets.iterator();

      while(i.hasNext()) {
         EntityLivingBase rand = (EntityLivingBase)i.next();
      
      }

      if(!((Boolean)this.openInv.getValueState()).booleanValue() && this.mc.currentScreen != null) {
         this.lastMs = System.currentTimeMillis() + 1000L;
         this.test.setLastMs(1000);
      } else {
         this.doBlock = false;
         this.clear();
         this.findTargets(event);
         this.setCurTarget();
         if(((Boolean)this.aRayTrace.getValueState()).booleanValue() && curTarget != null) {
            RayTraceUtil var4 = new RayTraceUtil(curTarget);
            if(var4.getEntity() != curTarget) {
               curBot = var4.getEntity();
            }
         }

         if(curTarget != null) {
            this.switchDelay();
            Random var5 = new Random();
            int var6;
            if(((Boolean)this.rotations.getValueState()).booleanValue()) {
               if(this.tick == 0) {
                  this.doAttack();
                  lastTarget = curTarget;
                  event.pitch = this.curPitch;
                  event.yaw = (float)((double)this.curYaw + Wrapper.getDoubleRandom(3.0D, 8.0D));
                  if((double)this.mc.thePlayer.getDistanceToEntity(curTarget) < ((Double)reach.getValueState()).doubleValue()) {
                     for(var6 = 0; (double)var6 < ((Double)this.cracksize.getValueState()).doubleValue(); ++var6) {
                        this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT_MAGIC);
                        this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT);
                     }
                  }
               } else {
                  event.yaw = this.mc.thePlayer.rotationYaw + (this.curYaw + (float)var5.nextInt(9) - 5.0F - this.mc.thePlayer.rotationYaw) / 2.0F;
               }
            } else if(this.tick == 0) {
               this.doAttack();
               lastTarget = curTarget;
               if((double)this.mc.thePlayer.getDistanceToEntity(curTarget) < ((Double)reach.getValueState()).doubleValue()) {
                  for(var6 = 0; (double)var6 < ((Double)this.cracksize.getValueState()).doubleValue(); ++var6) {
                     this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT_MAGIC);
                     this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT);
                  }
               }
            }
         } else {
            this.targets.clear();
            this.attackedTargets.clear();
            this.lastMs = System.currentTimeMillis();
            if(this.unBlock) {
               this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
               this.mc.thePlayer.itemInUseCount = 0;
               this.unBlock = false;
            }
         }

         Wrapper.setLook(event.yaw, event.pitch);
      }
   }

   private void switchDelay() {
      if(lastTarget != null && lastTarget != curTarget) {
         ++this.tick;
      } else {
         this.tick = 0;
      }

      if((double)this.tick > ((Double)this.switchDelay.getValueState()).doubleValue() + (double)(((Boolean)this.attackRandomDelay.getValueState()).booleanValue()?this.random.nextInt(3):0)) {
         this.tick = 0;
      }

   }

   private void setRotation() {
      float[] rotations = PlayerUtil.faceEntity(curTarget, this.curYaw, this.curPitch, Wrapper.getDoubleRandom((double)((Double)this.speed.getValueState()).floatValue(), (double)(((Double)this.speed.getValueState()).floatValue() + 3.0F)), Wrapper.getDoubleRandom((double)((Double)this.speed.getValueState()).floatValue(), (double)(((Double)this.speed.getValueState()).floatValue() + 3.0F)));
      this.curYaw = rotations[0];
      this.curPitch = rotations[1];
      if(this.curPitch > 90.0F) {
         this.curPitch = 90.0F;
      } else if(this.curPitch < -90.0F) {
         this.curPitch = -90.0F;
      }

   }

   private void doAttack() {
      this.setRotation();
      byte ticks = 1;
      byte MAX_TICK = 100;
      if((double)this.mc.thePlayer.getDistanceToEntity(curTarget) <= ((Double)reach.getValueState()).doubleValue() && this.tick == 0 && this.test.isDelayComplete((long)(((Double)this.attackDelay.getValueState()).intValue() - 20 + (((Boolean)this.attackRandomDelay.getValueState()).booleanValue()?this.random.nextInt(50):0)))) {
         this.test.reset();
         boolean miss = this.random.nextInt(50) + 1 == 39;
         if(this.mc.thePlayer.isBlocking() || this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && ((Boolean)autoBlock.getValueState()).booleanValue()) {
            this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            this.unBlock = false;
         }

         if(!this.mc.thePlayer.isBlocking() && !((Boolean)autoBlock.getValueState()).booleanValue() && this.mc.thePlayer.itemInUseCount > 0) {
            this.mc.thePlayer.itemInUseCount = 0;
         }

         this.attack(miss);
         this.doBlock = true;
         if(!miss) {
            this.attackedTargets.add(curTarget);
         }
      }

      if(System.currentTimeMillis() - this.lastMs > (long)(this.delay + ticks * MAX_TICK)) {
         this.lastMs = System.currentTimeMillis();
         this.delay = (int)((double)((Double)this.attackDelay.getValueState()).intValue() + (((Boolean)this.attackRandomDelay.getValueState()).booleanValue()?(double)this.random.nextInt(100):0.0D)) - ticks * MAX_TICK;
         if(this.delay < 0) {
            this.delay = 0;
         }
      }

   }

   @EventTarget
   public void onPost(EventPostMotion event) {
      if(curTarget != null && (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && ((Boolean)autoBlock.getValueState()).booleanValue() || this.mc.thePlayer.isBlocking()) && this.doBlock) {
         this.mc.thePlayer.itemInUseCount = this.mc.thePlayer.getHeldItem().getMaxItemUseDuration();
         this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, this.mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
         this.unBlock = true;
      }

   }

   private void attack(boolean fake) {
      this.mc.thePlayer.onCriticalHit(curTarget);
      if(!((Boolean)this.noswing.getValueState()).booleanValue()) {
         this.mc.thePlayer.swingItem();
      }

      if(!fake) {
         this.doBlock = true;
         double eX = curTarget.posX + (curTarget.posX - curTarget.lastTickPosX);
         eX += eX > curTarget.posX?0.5D:(eX == curTarget.posX?0.0D:-0.5D);
         double eZ = curTarget.posZ + (curTarget.posZ - curTarget.lastTickPosZ);
         eZ += eZ > curTarget.posZ?0.5D:(eZ == curTarget.posZ?0.0D:-0.5D);
         this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(curBot != null?curBot:curTarget, net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK));
         curBot = null;
      }

   }

   private void setCurTarget() {
      Iterator var2 = this.targets.iterator();

      while(var2.hasNext()) {
         EntityLivingBase ent = (EntityLivingBase)var2.next();
         if(!this.attackedTargets.contains(ent)) {
            curTarget = ent;
            break;
         }

         if(this.attackedTargets.size() == this.targets.size()) {
            if(this.attackedTargets.size() > 0) {
               this.attackedTargets.clear();
            }

            this.setCurTarget();
         }
      }

   }

   private void clear() {
      curTarget = null;
      curBot = null;
      Iterator var2 = this.targets.iterator();

      while(var2.hasNext()) {
         EntityLivingBase ent = (EntityLivingBase)var2.next();
         if(!this.isValidEntity(ent)) {
            this.targets.remove(ent);
            if(this.attackedTargets.contains(ent)) {
               this.attackedTargets.remove(ent);
            }
         }
      }

   }

   private void findTargets(EventPreMotion event) {
      int maxSize = (int)(this.maxTargets.disabled?1.0D:((Double)this.maxTargets.getValueState()).doubleValue());
      Iterator var4 = this.mc.theWorld.loadedEntityList.iterator();

      while(var4.hasNext()) {
         Object o = var4.next();
         if(o instanceof EntityLivingBase) {
            EntityLivingBase curEnt = (EntityLivingBase)o;
            if(this.isValidEntity(curEnt) && !this.targets.contains(curEnt)) {


               this.targets.add(curEnt);
            }
         }

         if(this.targets.size() >= maxSize) {
            break;
         }
      }

      this.targets.sort((ent1, ent2) -> {
         float e1 = CombatUtil.getRotations(ent1)[0];
         float e2 = CombatUtil.getRotations(ent2)[0];
         return e1 < e2?1:(e1 == e2?0:-1);
      });
   }

   private boolean isValidEntity(EntityLivingBase ent) {
      return ent == null?false:(ent == this.mc.thePlayer?false:(ent.getName().equalsIgnoreCase("\u00a76Dealer")?false:(ent instanceof EntityPlayer && !((Boolean)this.attackPlayers.getValueState()).booleanValue()?false:((ent instanceof EntityAnimal || ent instanceof EntitySquid || ent instanceof EntityArmorStand) && !((Boolean)this.attackAnimals.getValueState()).booleanValue()?false:((ent instanceof EntityMob || ent instanceof EntityVillager || ent instanceof EntityBat) && !((Boolean)this.attackMobs.getValueState()).booleanValue()?false:((double)this.mc.thePlayer.getDistanceToEntity(ent) > ((Double)reach.getValueState()).doubleValue() + 1.0D?false:(!ent.isDead && ent.getHealth() > 0.0F?(ent.isInvisible() && !((Boolean)this.invisible.getValueState()).booleanValue()?false:(ent instanceof EntityPlayer && (this.mc.thePlayer.isDead?false:(ent instanceof EntityPlayer && !((Boolean)this.blockRayTrace.getValueState()).booleanValue() || this.mc.thePlayer.canEntityBeSeen(ent))))):false)))))));
   }

   public void onEnable() {
      this.curYaw = this.mc.thePlayer.rotationYaw;
      this.curPitch = this.mc.thePlayer.rotationPitch;
      if(((Boolean)this.startDelay.getValueState()).booleanValue()) {
         this.test.setLastMs(125);
      }

      super.onEnable();
   }

   public void onDisable() {
      this.targets.clear();
      this.attackedTargets.clear();
      curTarget = null;
      this.mc.thePlayer.itemInUseCount = 0;
      this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
      super.onDisable();
   }

   private float getYawDifference(float yaw, EntityLivingBase target) {
      return CombatUtil.getYawDifference(yaw, CombatUtil.getRotations(target)[0]);
   }
}
