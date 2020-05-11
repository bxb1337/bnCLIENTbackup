package net.AzureWare.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventTick;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.main.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.MovementInput;

public class EntitySpeed extends Mod {
	// private Value<String> mode = new Value("EntitySpeed", "Mode", 0);
	public static Value<Double> speed = new Value("EntitySpeed_Speed", 1.0d, 0.0d, 5.0d, 0.1d);
	public static Value<Double> motion = new Value("EntitySpeed_MotionY", 3.0d, 0.1d, 5.0d, 0.1d);
	public static Value<Boolean> onGround = new Value("EntitySpeed_Ground", false);

	public EntitySpeed() {
		super("EntitySpeed", Category.MOVEMENT);

	}

//	@By Kody

	@EventTarget
	public void onUpdate(EventUpdate e) {
        if (EntitySpeed.mc.thePlayer.isRiding() || EntitySpeed.mc.thePlayer.ridingEntity != null) {
            double speed = this.speed.getValueState();
            MovementInput movementInput = EntitySpeed.mc.thePlayer.movementInput;
            double forward = movementInput.moveForward;
            double strafe = movementInput.moveStrafe;
            float yaw = EntitySpeed.mc.thePlayer.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                EntitySpeed.mc.thePlayer.ridingEntity.motionX = 0.0;
                EntitySpeed.mc.thePlayer.ridingEntity.motionZ = 0.0;
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += (float)(forward > 0.0 ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += (float)(forward > 0.0 ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    } else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                EntitySpeed.mc.thePlayer.ridingEntity.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
                EntitySpeed.mc.thePlayer.ridingEntity.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
            }
            double Y = this.motion.getValueState();
            EntitySpeed.mc.thePlayer.ridingEntity.motionY = 0.0;
            if (EntitySpeed.mc.gameSettings.keyBindJump.isKeyDown()) {
                EntitySpeed.mc.thePlayer.ridingEntity.motionY = Y;
            }
            if (EntitySpeed.mc.gameSettings.keyBindSprint.isKeyDown()) {
                EntitySpeed.mc.thePlayer.ridingEntity.motionY = -Y;
            }

        }
        
	}

}
