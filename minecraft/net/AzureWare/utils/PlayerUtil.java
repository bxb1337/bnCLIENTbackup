package net.AzureWare.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Multimap;

import net.AzureWare.Client;
import net.AzureWare.events.EventMove;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.mods.COMBAT.Killaura;
import net.AzureWare.mod.mods.COMBAT.SkillAura;
import net.AzureWare.mod.mods.PLAYER.AimBot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.main.Main;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class PlayerUtil {
	private static Minecraft mc;

	static {
		PlayerUtil.mc = Minecraft.getMinecraft();
	}

	public static double getPosForSetPosX(double value) {
		double yaw = Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw);
		double x = -Math.sin(yaw) * value;
		return x;
	}
    public static boolean canEntityBeSeen(Entity e){
    	Vec3 vec1 = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),mc.thePlayer.posZ);

    	AxisAlignedBB box = e.getEntityBoundingBox();
        Vec3 vec2 = new Vec3(e.posX, e.posY + (e.getEyeHeight()/1.32F),e.posZ);	
        double minx = e.posX - 0.25;
        double maxx = e.posX + 0.25;
        double miny = e.posY;
        double maxy = e.posY + Math.abs(e.posY - box.maxY) ;
        double minz = e.posZ - 0.25;
        double maxz = e.posZ + 0.25;
        boolean see =  mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(maxx,miny,minz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx,miny,minz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	  
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx,miny,maxz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(maxx,miny,maxz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    
	    vec2 = new Vec3(maxx, maxy,minz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	  
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx, maxy,minz);	
	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(minx, maxy,maxz - 0.1);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    vec2 = new Vec3(maxx, maxy,maxz);	
	    see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
	    if(see)
	    	return true;
	    
	
    	return false;
    }
	public static boolean onServer(String server) {
		if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains(server)) {
			return true;
		}
		return false;
	}

	public static double getPosForSetPosZ(double value) {
		double yaw = Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw);
		double z = Math.cos(yaw) * value;
		return z;
	}

	public static Block getBlock(double x, double y, double z) {
		return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
	}

	public void portMove(float yaw, float multiplyer, float up) {
		double moveX = -Math.sin(Math.toRadians((double) yaw)) * (double) multiplyer;
		double moveZ = Math.cos(Math.toRadians((double) yaw)) * (double) multiplyer;
		double moveY = (double) up;
		mc.thePlayer.setPosition(moveX + mc.thePlayer.posX, moveY + mc.thePlayer.posY, moveZ + mc.thePlayer.posZ);
	}

	public static Entity raycast(Entity entiy) {
		Entity var2 = mc.thePlayer;
		Vec3 var9 = entiy.getPositionVector().add(new Vec3(0, entiy.getEyeHeight(), 0));
		Vec3 var7 = mc.thePlayer.getPositionVector().add(new Vec3(0, mc.thePlayer.getEyeHeight(), 0));
		Vec3 var10 = null;
		float var11 = 1.0F;
		AxisAlignedBB a = mc.thePlayer.getEntityBoundingBox()
				.addCoord(var9.xCoord - var7.xCoord, var9.yCoord - var7.yCoord, var9.zCoord - var7.zCoord)
				.expand(var11, var11, var11);
		List var12 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(var2, a);
		double var13 = Killaura.reach.getValueState().doubleValue() + 0.45f;
		Entity b = null;
		for (int var15 = 0; var15 < var12.size(); ++var15) {
			Entity var16 = (Entity) var12.get(var15);

			if (var16.canBeCollidedWith()) {
				float var17 = var16.getCollisionBorderSize();
				AxisAlignedBB var18 = var16.getEntityBoundingBox().expand((double) var17, (double) var17,
						(double) var17);
				MovingObjectPosition var19 = var18.calculateIntercept(var7, var9);

				if (var18.isVecInside(var7)) {
					if (0.0D < var13 || var13 == 0.0D) {
						b = var16;
						var10 = var19 == null ? var7 : var19.hitVec;
						var13 = 0.0D;
					}
				} else if (var19 != null) {
					double var20 = var7.distanceTo(var19.hitVec);

					if (var20 < var13 || var13 == 0.0D) {
						b = var16;
						var10 = var19.hitVec;
						var13 = var20;
					}
				}
			}
		}
		return b;
	}

	public static boolean canBlock() {
		Iterator var1 = mc.theWorld.loadedEntityList.iterator();

		Entity o;
		do {
			do {
				do {
					do {
						if (!var1.hasNext()) {
							return false;
						}
						o = (Entity) var1.next();
					} while (!(o instanceof EntityPlayer));
				} while (o.isDead);
			} while (o == mc.thePlayer);
		} while (mc.thePlayer.getDistanceToEntity(o) > 8.0F);
		return true;
	}

	public static double getBaseMoveSpeed() {
		double baseSpeed = 0.2873;
		if (!Client.INSTANCE)
			return 0;
		if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
		}
		return baseSpeed;
	}

	public static float getDirection() {
		if (!Client.INSTANCE)
			return 0;
		float yaw = PlayerUtil.mc.thePlayer.rotationYaw;
		if (PlayerUtil.mc.thePlayer.moveForward < 0.0f) {
			yaw += 180.0f;
		}
		float forward = 1.0f;
		if (PlayerUtil.mc.thePlayer.moveForward < 0.0f) {
			forward = -0.5f;
		} else if (PlayerUtil.mc.thePlayer.moveForward > 0.0f) {
			forward = 0.5f;
		}
		if (PlayerUtil.mc.thePlayer.moveStrafing > 0.0f) {
			yaw -= 90.0f * forward;
		}
		if (PlayerUtil.mc.thePlayer.moveStrafing < 0.0f) {
			yaw += 90.0f * forward;
		}
		yaw *= 0.017453292f;
		return yaw;
	}

	public static boolean isInWater() {
		return PlayerUtil.mc.theWorld.getBlockState(
				new BlockPos(PlayerUtil.mc.thePlayer.posX, PlayerUtil.mc.thePlayer.posY, PlayerUtil.mc.thePlayer.posZ))
				.getBlock().getMaterial() == Material.water;
	}

	public static void toFwd(final double speed) {
		final float yaw = PlayerUtil.mc.thePlayer.rotationYaw * 0.017453292f;
		final EntityPlayerSP thePlayer = PlayerUtil.mc.thePlayer;
		thePlayer.motionX -= MathHelper.sin(yaw) * speed;
		final EntityPlayerSP thePlayer2 = PlayerUtil.mc.thePlayer;
		thePlayer2.motionZ += MathHelper.cos(yaw) * speed;
	}

	public static void setSpeed(double speed) {
		PlayerUtil.mc.thePlayer.motionX = -(Math.sin(getDirection()) * speed);
		PlayerUtil.mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
	}
	public static void setSpeed(final EventMove moveEvent, final double moveSpeed) {
        setSpeed(moveEvent, moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
    }

    public static void setSpeed(final EventMove moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;

        if (forward == 0.0 && strafe == 0.0) {
            moveEvent.setZ(0);
            moveEvent.setX(0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            final double sin = Math.sin(Math.toRadians(yaw + 90.0f));

            moveEvent.setX((forward * moveSpeed * cos + strafe * moveSpeed * sin));
            moveEvent.setZ((forward * moveSpeed * sin - strafe * moveSpeed * cos));
        }
    }
	public static double getSpeed() {
		if (!Client.INSTANCE)
			return 0;
		return Math.sqrt(Minecraft.getMinecraft().thePlayer.motionX * Minecraft.getMinecraft().thePlayer.motionX
				+ Minecraft.getMinecraft().thePlayer.motionZ * Minecraft.getMinecraft().thePlayer.motionZ);
	}

	public static Block getBlockUnderPlayer(final EntityPlayer inPlayer) {
		return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0, inPlayer.posZ));
	}

	public static Block getBlock(final BlockPos pos) {
		return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
	}

	public static Block getBlockAtPosC(final EntityPlayer inPlayer, final double x, final double y, final double z) {
		return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
	}

	public static ArrayList<Vector3f> vanillaTeleportPositions(final double tpX, final double tpY, final double tpZ,
			final double speed) {
		final ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
		final Minecraft mc = Minecraft.getMinecraft();
		final double posX = tpX - mc.thePlayer.posX;
		final double posY = tpY - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight() + 1.1);
		final double posZ = tpZ - mc.thePlayer.posZ;
		final float yaw = (float) (Math.atan2(posZ, posX) * 180.0 / 3.141592653589793 - 90.0);
		final float pitch = (float) (-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0
				/ 3.141592653589793);
		double tmpX = mc.thePlayer.posX;
		double tmpY = mc.thePlayer.posY;
		double tmpZ = mc.thePlayer.posZ;
		double steps = 1.0;
		for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY,
				tpZ); d += speed) {
			++steps;
		}
		for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY,
				tpZ); d += speed) {
			tmpX = mc.thePlayer.posX - Math.sin(getDirection(yaw)) * d;
			tmpZ = mc.thePlayer.posZ + Math.cos(getDirection(yaw)) * d;
			tmpY -= (mc.thePlayer.posY - tpY) / steps;
			positions.add(new Vector3f((float) tmpX, (float) tmpY, (float) tmpZ));
		}
		positions.add(new Vector3f((float) tpX, (float) tpY, (float) tpZ));
		return positions;
	}

	public static float getDirection(float yaw) {
		if (!Client.INSTANCE)
			return 0;
		if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0f) {
			yaw += 180.0f;
		}
		float forward = 1.0f;
		if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0f) {
			forward = -0.5f;
		} else if (Minecraft.getMinecraft().thePlayer.moveForward > 0.0f) {
			forward = 0.5f;
		}
		if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0.0f) {
			yaw -= 90.0f * forward;
		}
		if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0.0f) {
			yaw += 90.0f * forward;
		}
		yaw *= 0.017453292f;
		return yaw;
	}

	public static double getDistance(final double x1, final double y1, final double z1, final double x2,
			final double y2, final double z2) {
		final double d0 = x1 - x2;
		final double d2 = y1 - y2;
		final double d3 = z1 - z2;
		return MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
	}

	public static boolean MovementInput() {
		if (!Client.INSTANCE)
			return true;
		return PlayerUtil.mc.gameSettings.keyBindForward.pressed || PlayerUtil.mc.gameSettings.keyBindLeft.pressed
				|| PlayerUtil.mc.gameSettings.keyBindRight.pressed || PlayerUtil.mc.gameSettings.keyBindBack.pressed;
	}

	public static void blockHit(Entity en, boolean value) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = mc.thePlayer.getCurrentEquippedItem();

		if (mc.thePlayer.getCurrentEquippedItem() != null && en != null && value) {
			if (stack.getItem() instanceof ItemSword && mc.thePlayer.swingProgress > 0.2) {
				mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
			}
		}
	}

	public static float getItemAtkDamage(ItemStack itemStack) {
		final Multimap multimap = itemStack.getAttributeModifiers();
		if (!multimap.isEmpty()) {
			final Iterator iterator = multimap.entries().iterator();
			if (iterator.hasNext()) {
				final Map.Entry entry = (Entry) iterator.next();
				final AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
				double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2
						? attributeModifier.getAmount()
						: attributeModifier.getAmount() * 100.0;

				if (attributeModifier.getAmount() > 1.0) {
					return 1.0f + (float) damage;
				}
				return 1.0f;
			}
		}
		return 1.0f;
	}

	public static int bestWeapon(Entity target) {
		if (!Client.INSTANCE)
			return 999;
		Minecraft mc = Minecraft.getMinecraft();
		int firstSlot = mc.thePlayer.inventory.currentItem = 0;
		int bestWeapon = -1;
		int j = 1;

		for (byte i = 0; i < 9; i++) {
			mc.thePlayer.inventory.currentItem = i;
			ItemStack itemStack = mc.thePlayer.getHeldItem();

			if (itemStack != null) {
				int itemAtkDamage = (int) getItemAtkDamage(itemStack);
				itemAtkDamage += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);

				if (itemAtkDamage > j) {
					j = itemAtkDamage;
					bestWeapon = i;
				}
			}
		}

		if (bestWeapon != -1) {
			return bestWeapon;
		} else {
			return firstSlot;
		}
	}

	public static void shiftClick(Item i) {
		for (int i1 = 9; i1 < 37; ++i1) {
			ItemStack itemstack = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();

			if (itemstack != null && itemstack.getItem() == i) {
				mc.playerController.windowClick(0, i1, 0, 1, mc.thePlayer);
				break;
			}
		}
	}

	public static boolean hotbarIsFull() {
		for (int i = 0; i <= 36; ++i) {
			ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i);

			if (itemstack == null) {
				return false;
			}
		}

		return true;
	}

	public static void tellPlayer(String string) {
		if (!Client.INSTANCE)
			return;
		if (string != null && mc.thePlayer != null)
			mc.thePlayer.addChatMessage(new ChatComponentText(string));

	}

	public static boolean isInLiquid() {
		if (mc.thePlayer.isInWater()) {
			return true;
		}
		boolean inLiquid = false;
		final int y = (int) mc.thePlayer.getEntityBoundingBox().minY;
		for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper
				.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper
					.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
				final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null && block.getMaterial() != Material.air) {
					if (!(block instanceof BlockLiquid))
						return false;
					inLiquid = true;
				}
			}
		}
		return inLiquid;
	}

	public static boolean isOnGround(double height) {
		if (!Client.INSTANCE)
			return true;
		if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
				mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static int getJumpEffect() {
		if (mc.thePlayer.isPotionActive(Potion.jump))
			return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
		else
			return 0;
	}

	public static int getSpeedEffect() {
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
			return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
		else
			return 0;
	}

	public static boolean isMoving2() {
		return ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F));
	}

	public static double defaultSpeed() {
		double baseSpeed = 0.2873D;
		if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();

			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}

	public static double getArBaseMoveSpeed() {
		double baseSpeed = 0.2875D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			baseSpeed *= 1.0D
					+ 0.2D * (double) (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		}

		return baseSpeed;
	}

	public static void setMotion(double speed) {
		if (!Client.INSTANCE)
			return;
		double forward = mc.thePlayer.movementInput.moveForward;
		double strafe = mc.thePlayer.movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if ((forward == 0.0D) && (strafe == 0.0D)) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
		} else {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 45 : -45);
				}
				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1;
				} else if (forward < 0.0D) {
					forward = -1;
				}
			}
			mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
			mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
		}
	}

	public static float[] faceEntity(Entity p_706251, float curYaw, float curPitch, double yawSpeed,
			double pitchSpeed) {
		double var6;
		float pitch;
		double var4 = p_706251.posX - CombatUtil.mc.thePlayer.posX;
		double var8 = p_706251.posZ - CombatUtil.mc.thePlayer.posZ;
		if (p_706251 instanceof EntityLivingBase) {
			EntityLivingBase var14 = (EntityLivingBase) p_706251;
			var6 = var14.posY + (double) var14.getEyeHeight()
					- (CombatUtil.mc.thePlayer.posY + (double) CombatUtil.mc.thePlayer.getEyeHeight());
		} else {
			var6 = (p_706251.getEntityBoundingBox().minY + p_706251.getEntityBoundingBox().maxY) / 2.0
					- (CombatUtil.mc.thePlayer.posY + (double) CombatUtil.mc.thePlayer.getEyeHeight());
		}
		double var141 = MathHelper.sqrt_double((double) (var4 * var4 + var8 * var8));
		float var12 = (float) (Math.atan2((double) var8, (double) var4) * 180.0 / 3.141592653589793) - 90.0f;
		float var13 = (float) (-(Math.atan2((double) var6, (double) var141) * 180.0 / 3.141592653589793));
		float yaw = (float) CombatUtil.updateRotation(curYaw, var12, yawSpeed);
		return new float[] { yaw, pitch = (float) CombatUtil.updateRotation(curPitch, var13, pitchSpeed) };
	}

	private static double updateRotation(double p_706631, double p_706632, double d) {
		double var4 = MathHelper.wrapAngleTo180_double((double) (p_706632 - p_706631));
		if (var4 > d) {
			var4 = d;
		}
		if (var4 < -d) {
			var4 = -d;
		}
		return p_706631 + var4;
	}

	public static void setMoveSpeed(double moveSpeed) {
		MovementInput var10000 = mc.thePlayer.movementInput;
		float forward = MovementInput.moveForward;
		var10000 = mc.thePlayer.movementInput;
		float strafe = MovementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if ((double) forward == 0.0D && (double) strafe == 0.0D) {
			mc.thePlayer.motionX = 0.0D;
			mc.thePlayer.motionZ = 0.0D;
		}

		byte d = 45;
		if ((double) forward != 0.0D) {
			if ((double) strafe > 0.0D) {
				yaw += (float) ((double) forward > 0.0D ? -d : d);
			} else if ((double) strafe < 0.0D) {
				yaw += (float) ((double) forward > 0.0D ? d : -d);
			}

			strafe = 0.0F;
			if ((double) forward > 0.0D) {
				forward = 1.0F;
			} else if ((double) forward < 0.0D) {
				forward = -1.0F;
			}
		}

		double xDist = (double) forward * moveSpeed * Math.cos(Math.toRadians((double) (yaw + 90.0F)))
				+ (double) strafe * moveSpeed * Math.sin(Math.toRadians((double) (yaw + 90.0F)));
		double zDist = (double) forward * moveSpeed * Math.sin(Math.toRadians((double) (yaw + 90.0F)))
				- (double) strafe * moveSpeed * Math.cos(Math.toRadians((double) (yaw + 90.0F)));
		mc.thePlayer.motionX = xDist;
		mc.thePlayer.motionZ = zDist;
	}

	public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
		return Minecraft.getMinecraft().theWorld
				.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
	}

	public static double setRandom(double min, double max) {
		Random random = new Random();
		return min + random.nextDouble() * (max - min);
	}

	public static float setRandom(float min, float max) {
		Random random = new Random();
		return min + random.nextFloat() * (max - min);
	}

	public static boolean isMoving() {
		if ((!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isSneaking())) {
			return ((mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F));
		}
		return false;
	}

	public static void setMoveSpeedAris(EventMove event, double speed) {
		double forward = (double) Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
		double strafe = (double) Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
		double yaw = (double) Minecraft.getMinecraft().thePlayer.rotationYaw;
		if (forward != 0.0D || strafe != 0.0D) {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += forward > 0.0D ? -45.0D : 45.0D;
				} else if (strafe < 0.0D) {
					yaw += forward > 0.0D ? 45.0D : -45.0D;
				}

				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1.0D;
				} else if (forward < 0.0D) {
					forward = -1.0D;
				}
			}

			event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 88.0D))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 87.9000015258789D)));
			event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 88.0D))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 87.9000015258789D)));
		}

	}

	public static void setMoveSpeedAris(EventPreMotion event, double speed) {
		double forward = (double) Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
		double strafe = (double) Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
		double yaw = (double) Minecraft.getMinecraft().thePlayer.rotationYaw;
		if (forward != 0.0D || strafe != 0.0D) {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += forward > 0.0D ? -45.0D : 45.0D;
				} else if (strafe < 0.0D) {
					yaw += forward > 0.0D ? 45.0D : -45.0D;
				}

				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1.0D;
				} else if (forward < 0.0D) {
					forward = -1.0D;
				}
			}

			event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 88.0D))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 87.9000015258789D)));
			event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 88.0D))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 87.9000015258789D)));
		}

	}

	public static IBlockState getStateFromPos(BlockPos a1) {
		return Minecraft.getMinecraft() != null && Minecraft.getMinecraft().theWorld != null
				? Minecraft.getMinecraft().theWorld.getBlockState(a1)
				: null;
	}

	public static Block getBlockFromPos(BlockPos a1) {
		IBlockState v1 = getStateFromPos(a1);
		return v1 == null ? null : v1.getBlock();
	}

	public static void GetSpeed(EventMove e, double d) {
		double v3 = (double) Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
		double v5 = (double) Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
		double v7 = (double) Minecraft.getMinecraft().thePlayer.rotationYaw;
		if (v3 != 0.0D || v5 != 0.0D) {
			if (v3 != 0.0D) {
				if (v5 > 0.0D) {
					v7 += (double) (v3 > 0.0D ? -45 : 45);
				} else if (v5 < 0.0D) {
					v7 += (double) (v3 > 0.0D ? 45 : -45);
				}

				v5 = 0.0D;
				if (v3 > 0.0D) {
					v3 = 1.0D;
				} else if (v3 < 0.0D) {
					v3 = -1.0D;
				}
			}

			e.setX(mc.thePlayer.motionX = v3 * d * Math.cos(Math.toRadians(v7 + 88.0D))
					+ v5 * d * Math.sin(Math.toRadians(v7 + 87.9000015258789D)));
			e.setZ(mc.thePlayer.motionZ = v3 * d * Math.sin(Math.toRadians(v7 + 88.0D))
					- v5 * d * Math.cos(Math.toRadians(v7 + 87.9000015258789D)));
		}

	}

	public static double getSeSpeed() {
		double v2 = 0.27630001306533813D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			v2 *= 1.0D + (double) (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) * 0.2D;
		}

		return v2;
	}

	public static void SetSeSpeed(double movespeed) {
		double v3 = (double) Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
		double v5 = (double) Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
		double v7 = (double) Minecraft.getMinecraft().thePlayer.rotationYaw;
		if (v3 != 0.0D || v5 != 0.0D) {
			if (v3 != 0.0D) {
				if (v5 > 0.0D) {
					v7 += (double) (v3 > 0.0D ? -45 : 45);
				} else if (v5 < 0.0D) {
					v7 += (double) (v3 > 0.0D ? 45 : -45);
				}

				v5 = 0.0D;
				if (v3 > 0.0D) {
					v3 = 1.0D;
				} else if (v3 < 0.0D) {
					v3 = -1.0D;
				}
			}

			mc.thePlayer.motionX = v3 * movespeed * Math.cos(Math.toRadians(v7 + 88.0D))
					+ v5 * movespeed * Math.sin(Math.toRadians(v7 + 87.9000015258789D));
			mc.thePlayer.motionZ = v3 * movespeed * Math.sin(Math.toRadians(v7 + 88.0D))
					- v5 * movespeed * Math.cos(Math.toRadians(v7 + 87.9000015258789D));
		}

	}

	public static void setMotion(EventMove e, double speed) {
		double forward = (double) mc.thePlayer.movementInput.moveForward;
		double strafe = (double) mc.thePlayer.movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if (forward == 0.0D && strafe == 0.0D) {
			if (e != null) {
				e.setX(0.0D);
				e.setZ(0.0D);
			} else {
				mc.thePlayer.motionX = 0.0D;
				mc.thePlayer.motionZ = 0.0D;

			}
		} else {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (float) (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (float) (forward > 0.0D ? 45 : -45);
				}

				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1.0D;
				} else if (forward < 0.0D) {
					forward = -1.0D;
				}
			}

			if (e != null) {
				e.setX(forward * speed * Math.cos(Math.toRadians((double) (yaw + 90.0F)))
						+ strafe * speed * Math.sin(Math.toRadians((double) (yaw + 90.25F))));
				e.setZ(forward * speed * Math.sin(Math.toRadians((double) (yaw + 90.0F)))
						- strafe * speed * Math.cos(Math.toRadians((double) (yaw + 90.25F))));
			} else {
				mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians((double) (yaw + 90.0F)))
						+ strafe * speed * Math.sin(Math.toRadians((double) (yaw + 90.25F)));
				mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians((double) (yaw + 90.0F)))
						- strafe * speed * Math.cos(Math.toRadians((double) (yaw + 90.25F)));
			}
		}

	}

	public static void setStrafe(double var1) {
		if (var1 == 0.0D) {
			mc.thePlayer.motionX = mc.thePlayer.motionZ = var1;
		}

		float var3 = clampRotation2();
		mc.thePlayer.motionX = -Math.sin((double) var3) * var1;
		mc.thePlayer.motionZ = Math.cos((double) var3) * var1;
	}

	protected void setStrafe2(double var1, float var3) {
		if (var1 == 0.0D) {
			mc.thePlayer.motionX = mc.thePlayer.motionZ = var1;
		}

		mc.thePlayer.motionX = -Math.sin((double) var3) * var1;
		mc.thePlayer.motionZ = Math.cos((double) var3) * var1;
	}

	protected void setStrafe3(double var1, double var3) {
		if (var1 == 0.0D) {
			mc.thePlayer.motionX = mc.thePlayer.motionZ = var1;
		}

		float var5 = clampRotation(var3);
		mc.thePlayer.motionX = -Math.sin((double) var5) * var1;
		mc.thePlayer.motionZ = Math.cos((double) var5) * var1;
	}

	protected void motion2(double var1) {
		if (var1 == 0.0D) {
			mc.thePlayer.motionX = mc.thePlayer.motionZ = var1;
		}

		float var3 = mc.thePlayer.rotationYaw * 0.017453292F;
		mc.thePlayer.motionX = -Math.sin((double) var3) * var1;
		mc.thePlayer.motionZ = Math.cos((double) var3) * var1;
	}

	protected void setStrafe4(double var1) {
		float var3 = clampRotation2();
		mc.thePlayer.motionX -= Math.sin((double) var3) * var1;
		mc.thePlayer.motionZ += Math.cos((double) var3) * var1;
	}

	protected void motion(double var1) {
		float var3 = mc.thePlayer.rotationYaw * 0.017453292F;
		mc.thePlayer.motionX -= (double) MathHelper.sin(var3) * var1;
		mc.thePlayer.motionZ += (double) MathHelper.cos(var3) * var1;
	}

	public static double getDistance() {
		return Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
	}

	protected double getSomething() {
		return Math.toDegrees(-Math.atan2(mc.thePlayer.motionX, mc.thePlayer.motionZ));
	}

	protected static float clampRotation2() {
		float var1 = mc.thePlayer.rotationYaw;
		float var2 = 1.0F;
		if (mc.thePlayer.movementInput.moveForward < 0.0F) {
			var1 += 180.0F;
			var2 = -0.5F;
		} else if (mc.thePlayer.movementInput.moveForward > 0.0F) {
			var2 = 0.5F;
		}

		if (mc.thePlayer.movementInput.moveStrafe > 0.0F) {
			var1 -= 90.0F * var2;
		}

		if (mc.thePlayer.movementInput.moveStrafe < 0.0F) {
			var1 += 90.0F * var2;
		}

		var1 *= 0.017453292F;
		return var1;
	}

	protected float clampRotation(double var1) {
		float var3 = mc.thePlayer.rotationYaw;
		float var4 = 1.0F;
		if (mc.thePlayer.movementInput.moveForward < 0.0F) {
			var3 += 180.0F;
			var4 = -0.5F;
		} else if (mc.thePlayer.movementInput.moveForward > 0.0F) {
			var4 = 0.5F;
		}

		if (mc.thePlayer.movementInput.moveStrafe > 0.0F) {
			var3 = (float) ((double) var3 - (double) (90.0F * var4) * var1);
		}

		if (mc.thePlayer.movementInput.moveStrafe < 0.0F) {
			var3 = (float) ((double) var3 + (double) (90.0F * var4) * var1);
		}

		var3 *= 0.017453292F;
		return var3;
	}

	protected double getSpeed2() {
		double var1 = 0.2873D;
		if (!mc.thePlayer.isSprinting()) {
			var1 -= 0.105D;
		}

		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int var3 = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			var1 *= 1.0D + 0.15D * (double) (var3 + 1);
		}

		return var1;
	}

	public static double getSpeedOnIce() {
		double var1 = 0.2873D;
		if (!mc.thePlayer.isSprinting()) {
			var1 -= 0.105D;
		}

		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int var3 = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			var1 *= 1.0D + 0.38D * (double) (var3 + 1);
		}

		Block var4 = mc.theWorld.getBlockState(new BlockPos(MathHelper.floor_double(mc.thePlayer.posX),
				MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY) - 1,
				MathHelper.floor_double(mc.thePlayer.posZ))).getBlock();
		if (var4 == Blocks.ice || var4 == Blocks.packed_ice) {
			var1 *= 4.0D;
		}

		return var1;
	}

	public static double round(double var1, int var3) {
		if (var3 < 0) {
			throw new IllegalArgumentException();
		} else {
			BigDecimal var4 = new BigDecimal(var1);
			var4 = var4.setScale(var3, RoundingMode.HALF_UP);
			return var4.doubleValue();
		}
	}
	public static float[] getFacePos(Vec3 vec) {
		double diffX = vec.xCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffY = vec.yCoord + 0.5
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		double diffZ = vec.zCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}

	
	public static boolean isNotItem(Object o) {
		if (!(o instanceof EntityLivingBase)) {
			return false;
		}
		return true;
	}

}
