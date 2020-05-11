package net.AzureWare.mod.mods.MOVEMENT;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventMove;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventRender;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.COMBAT.Killaura;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.RotationUtil;
import net.AzureWare.value.Value;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
public class TargetStrafe extends Mod {
	public static Value<Double> Radius = new Value("TargetStrafe_Radius", 1.0, 0.0, 6.0, 0.01);
	public static Value<Boolean> ESP = new Value("TargetStrafe_ESP", true);
	public static Value<Boolean> WallCheck = new Value("TargetStrafe_WallCheck", true);
	public TargetStrafe() {
		super("TargetStrafe", Category.MOVEMENT);
	}
    private int direction = -1;
    private void switchDirection() {
        if (direction == 1) {
            direction = -1;
        } else {
            direction = 1;
        }
    }
    public final boolean doStrafeAtSpeed(EventMove event, final double moveSpeed) {
        final boolean strafe = canStrafe();

        if (strafe) {
            float[] rotations = RotationUtil.getRotations(Killaura.target);
            if (mc.thePlayer.getDistanceToEntity(Killaura.target) <= Radius.getValueState()) {
                PlayerUtil.setSpeed(event, moveSpeed, rotations[0], direction, 0);
            } else {
                PlayerUtil.setSpeed(event, moveSpeed, rotations[0], direction, 1);
            }
        }

        return strafe;
    }
	@EventTarget
    public final void onUpdate(EventPreMotion event) {
        if (mc.thePlayer.isCollidedHorizontally) {
            switchDirection();
        }
    }
	@EventTarget
    public final void onRender3D(EventRender event) {
        if (ESP.getValueState()) {
    		for (Entity e2 : mc.theWorld.getLoadedEntityList()) {
    			if(Check(e2) || e2 == Killaura.target)
    			{
    				drawCircle(e2, event.getPartialTicks(), Radius.getValueState());
    			}
    		}
            
        }
    }
	private boolean Check(Entity e2) {
		if (!e2.isEntityAlive())
			return false;
		if (e2 == mc.thePlayer)
			return false;
		if (e2 instanceof EntityPlayer)
			return true;
		return false;
	}
    private void drawCircle(Entity entity, float partialTicks, double rad) {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        RenderUtil.startDrawing();
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glLineWidth(1.0f);
        glBegin(GL_LINE_STRIP);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        Color color = Color.WHITE;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;
        if(entity == Killaura.target && ModManager.getModByName("Speed").isEnabled())
        {
        	color = Color.GREEN;
        }
        final float r = ((float) 1 / 255) * color.getRed();
        final float g = ((float) 1 / 255) * color.getGreen();
        final float b = ((float) 1 / 255) * color.getBlue();

        final double pix2 = Math.PI * 2.0D;

        for (int i = 0; i <= 90; ++i) {
            glColor3f(r, g, b);
            glVertex3d(x + rad * Math.cos(i * pix2 / 45.0), y, z + rad * Math.sin(i * pix2 / 45.0));
        }

        glEnd();
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        RenderUtil.stopDrawing();
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }
	
    public boolean canStrafe() {
        return ModManager.getModByName("Killaura").isEnabled() && Killaura.target != null && this.isEnabled();
    }
}
