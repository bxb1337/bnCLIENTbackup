package net.AzureWare.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventMove;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventPullback;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

import java.util.List;
import java.util.Random;

public class Fly_InJected extends Mod {
    int level = 1;
    double moveSpeed, lastDist;
    private boolean b2;
    public Value<String> mode = new Value<String>("Fly1", "Mode", 0);
    public int stage;
    private boolean decreasing2, hypixelboost, canboost,nigga;
    private double starty;
    private float timervalue;
    TimeHelper timer = new TimeHelper();

    public Fly_InJected() {
        super("Fly1", Category.MOVEMENT);
        mode.addValue("Hypxiel");
        mode.addValue("HypixelFast");
    }

    @EventTarget
    public void onPullback(EventPullback event) {
        set(false);
    }

    @Override
    public void onEnable() {
        stage = 0;
        level = 1;
        moveSpeed = 0.1D;
        lastDist = 0.0D;
        if (mode.isCurrentMode("HypixelFast")) {
            canboost = true;
            {
                double motionY = 0.40123128;
                timervalue = 1.0F;
                if (mc.thePlayer.onGround) {
                    if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.isCollidedVertically) {
                        if (mc.thePlayer.isPotionActive(Potion.jump))
                            motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                        mc.thePlayer.motionY = motionY;
                    }
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.28E-10D, mc.thePlayer.posZ);
                    level = 1;
                    moveSpeed = 0.1D;
                    hypixelboost = true;
                    lastDist = 0.0D;
                } else {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.26E-10D, mc.thePlayer.posZ);
                }
                timer.reset();
            }
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        level = 1;
        moveSpeed = 0.1D;
        b2 = false;
        lastDist = 0.0D;
        super.onDisable();
    }

    public static double getBaseMoveSpeed() {
        double n = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }

    private void setSpeed(double speed) {
        Minecraft.getMinecraft().thePlayer.motionX = -(Math.sin(getDirection()) * speed);
        Minecraft.getMinecraft().thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }

    public static float getDirection() {
        float var1 = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (Minecraft.getMinecraft().thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        } else {
            forward = 1.0f;
        }
        if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        var1 *= 0.017453292f;
        return var1;
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (mode.isCurrentMode("Hypxiel")) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
            } else {
                mc.thePlayer.motionY = 0;
               // PlayerUtil.setSpeed(PlayerUtil.getBaseMoveSpeed() + 0.005);
                stage += 1;
                switch (stage) {
                    case 1:
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-12D, mc.thePlayer.posZ);
                        break;
                    case 2:
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0E-12D, mc.thePlayer.posZ);
                        break;
                    case 3:
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-12D, mc.thePlayer.posZ);
                        stage = 0;
                        break;
                    default:
                        break;
                }
            }
        }
        if (mode.isCurrentMode("HypixelFast")) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
            if (canboost && hypixelboost) {
                timervalue += decreasing2 ? -0.01 : 0.05;
                if (timervalue >= 1.4) {
                    decreasing2 = true;
                }
                if (timervalue <= 0.9) {
                    decreasing2 = false;
                }
                if (timer.delay(2000)) {
                    canboost = false;
                }
            }
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.4, mc.thePlayer.posZ);
                mc.thePlayer.motionY = 0.8;
                mc.thePlayer.motionX *= 0.1;
                mc.thePlayer.motionZ *= 0.1;
            }
            if ((mc.thePlayer.ticksExisted % 2) == 0) {
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + getRandomInRange(0.00000000000001235423532523523532521, 0.0000000000000123542353252352353252 * 10), mc.thePlayer.posZ);
            }
            mc.thePlayer.motionY = 0;
        }
    }

    public static double getRandomInRange(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max) {
            scaled = max;
        }
        double shifted = scaled + min;

        if (shifted > max) {
            shifted = max;
        }
        return shifted;
    }
    @EventTarget
    public void onMove(EventMove event) {
        if(mode.isCurrentMode("HypixelFast")) {
            float yaw = mc.thePlayer.rotationYaw;
            double strafe = mc.thePlayer.movementInput.moveStrafe;
            double forward = mc.thePlayer.movementInput.moveForward;
            double mx = -Math.sin(Math.toRadians(yaw)), mz = Math.cos(Math.toRadians(yaw));
            if (forward == 0.0F && strafe == 0.0F) {
                event.setX(0);
                event.setZ(0);
            }
            if (forward != 0 && strafe != 0) {
                forward = forward * Math.sin(Math.PI / 4);
                strafe = strafe * Math.cos(Math.PI / 4);
            }
            if (hypixelboost) {
                if (level != 1 || mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
                    if (level == 2) {
                        level = 3;
                        moveSpeed *= 2.1499999D;
                    } else if (level == 3) {
                        level = 4;
                        double difference = 0.73D * (lastDist - getBaseMoveSpeed());
                        moveSpeed = lastDist - difference;
                    } else {
                        if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                            level = 1;
                        }
                        moveSpeed = lastDist - lastDist / 159.0D;
                    }
                } else {
                    level = 2;
                    double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.706 : 2.034;
                    moveSpeed = boost * getBaseMoveSpeed() - 0.01D;
                }
                moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                event.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
                event.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
                if (forward == 0.0F && strafe == 0.0F) {
                    event.setX(0.0);
                    event.setZ(0.0);
                }
                if (timer.delay(1700) && hypixelboost) {
                    hypixelboost = false;
                }
            }
        }
    }

    public void damagePlayer(int damage) {
        if (damage < 1)
            damage = 1;
        if (damage > MathHelper.floor_double(mc.thePlayer.getMaxHealth()))
            damage = MathHelper.floor_double(mc.thePlayer.getMaxHealth());

        double offset = 0.0625;
        if (mc.thePlayer != null && mc.getNetHandler() != null && mc.thePlayer.onGround) {
            for (int i = 0; i <= ((3 + damage) / offset); i++) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY + offset + (double)(new Random()).nextFloat() * 1.0E-6D, mc.thePlayer.posZ, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((3 + damage) / offset))));
            }
        }
    }

    private void setMotion(EventMove em, double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
            em.setX(0.0D);
            em.setZ(0.0D);
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
            em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
            em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
        }
    }

}
