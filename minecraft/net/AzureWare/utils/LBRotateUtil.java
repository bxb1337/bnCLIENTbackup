package net.AzureWare.utils;

import java.io.IOException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public final class LBRotateUtil {
    private static Minecraft mc = Minecraft.getMinecraft();
    public static boolean l;
    public static float ll;
    public static float l1;
    private static int l11;
    public static boolean lll;
    public static float[] ll1;
    private static double llll;
    private static double lll1;
    private static double lmc;

    public static double ll(Entity entity){
        float[] arrf = LBRotateUtil.l(entity);
        if (arrf == null) {
            return 0.0;
        }
        return LBRotateUtil.l(arrf[0], arrf[1]);
    }

    public static double l(float f, float f2){
        return Math.sqrt((double)(Math.pow((double)Math.abs((double)LBRotateUtil.l((double)(ll1[0] % 360.0f), (double)f)), (double)2.0) + Math.pow((double)Math.abs((double)LBRotateUtil.l((double)ll1[1], (double)f2)), (double)2.0)));
    }

    public static void l1(){
        l = false;
        l11 = 0;
        ll = 0.0f;
        l1 = 0.0f;
    }

    public static void l(Packet packet){
        if (packet instanceof C03PacketPlayer) {
            C03PacketPlayer c03PacketPlayer = (C03PacketPlayer)packet;
            if (l && !lll && (ll != ll1[0] || l1 != ll1[1])) {
                c03PacketPlayer.yaw = ll;
                c03PacketPlayer.pitch = l1;
                c03PacketPlayer.rotating = true;
            }
            if (c03PacketPlayer.rotating) {
                ll1 = new float[]{c03PacketPlayer.yaw, c03PacketPlayer.pitch};
            }
        }
    }

    static {
        lll = false;
        ll1 = new float[]{0.0f, 0.0f};
        llll = Math.random() / 3;
        lll1 = Math.random() / 3;
        lmc = Math.random() / 3;
    }

    public static void update(){
        if (l && ++l11 > 15) {
            LBRotateUtil.l1();
        }
        Random random = new Random();
        if (random.nextGaussian() * 100.0 > 80.0) {
            llll = Math.random() / 3;
        }
        if (random.nextGaussian() * 100.0 > 80.0) {
            lll1 = Math.random() / 3;
        }
        if (random.nextGaussian() * 100.0 > 80.0) {
            lmc = Math.random() / 3;
        }
    }

    public static Vec3 l(AxisAlignedBB axisAlignedBB){
        return new Vec3(axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * 0.5, axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * 0.5, axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * 0.5);
    }

    public static float[] l(float[] arrf, float[] arrf2, float f){
        double d = LBRotateUtil.l((double)arrf2[0], (double)arrf[0]);
        double d2 = LBRotateUtil.l((double)arrf2[1], (double)arrf[1]);
        float[] arrf3 = arrf;
        arrf3[0] = (float)((double)arrf3[0] + (d > (double)f ? (double)f : (d < (double)(- f) ? (double)(- f) : d)));
        float[] arrf4 = arrf;
        arrf4[1] = (float)((double)arrf4[1] + (d2 > (double)f ? (double)f : (d2 < (double)(- f) ? (double)(- f) : d2)));
        return arrf;
    }

    public static Vec3 ll(){
        return new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
    }

    private static double l(double d, double d2){
        return ((d - d2) % 360.0 + 540.0) % 360.0 - 180.0;
    }

    public static Vec3 l(AxisAlignedBB axisAlignedBB, boolean bl){
        if (bl) {
            return new Vec3(axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * (llll * 0.3 + 1.0), axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * (lll1 * 0.3 + 1.0), axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * (lmc * 0.3 + 1.0));
        }
        return new Vec3(axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * llll * 0.8, axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * lll1 * 0.8, axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * lmc * 0.8);
    }

    public boolean l(){
        return true;
    }

    public static Vec3 ll(float f, float f2){
        float f3 = MathHelper.cos((float)((- f2) * 0.017453292f - 3.1415927f));
        float f4 = MathHelper.sin((float)((- f2) * 0.017453292f - 3.1415927f));
        float f5 = - MathHelper.cos((float)((- f) * 0.017453292f));
        float f6 = MathHelper.sin((float)((- f) * 0.017453292f));
        return new Vec3((double)(f4 * f5), (double)f6, (double)(f3 * f5));
    }

    public static void l(Entity entity, boolean bl, boolean bl2, float f){
        float[] arrf;
        EntityPlayerSP entityPlayerSP = mc.thePlayer;
        double d = entity.posX + (bl2 ? (entity.posX - entity.prevPosX) * (double)f : 0.0) - (entityPlayerSP.posX + (bl2 ? entityPlayerSP.posX - entityPlayerSP.prevPosX : 0.0));
        double d2 = entity.getEntityBoundingBox().minY + (bl2 ? (entity.getEntityBoundingBox().minY - entity.prevPosY) * (double)f : 0.0) + (double)entity.getEyeHeight() - 0.15 - (entityPlayerSP.getEntityBoundingBox().minY + (bl2 ? entityPlayerSP.posY - entityPlayerSP.prevPosY : 0.0)) - (double)entityPlayerSP.getEyeHeight();
        double d3 = entity.posZ + (bl2 ? (entity.posZ - entity.prevPosZ) * (double)f : 0.0) - (entityPlayerSP.posZ + (bl2 ? entityPlayerSP.posZ - entityPlayerSP.prevPosZ : 0.0));
        double d4 = d;
        double d5 = d3;
        double d6 = Math.sqrt((double)(d4 * d4 + d5 * d5));
        float f2 = (float)entityPlayerSP.getItemInUseCount() / 20.0f;
        if ((f2 = (f2 * f2 + f2 * 2.0f) / 3.0f) > 1.0f) {
            f2 = 1.0f;
        }
        float f3 = (float)(Math.atan2((double)d3, (double)d) * 180.0 / 3.141592653589793) - 90.0f;
        float f4 = f2;
        float f5 = f2;
        double d7 = d6;
        float f6 = f2;
        float f7 = (float)(- Math.toDegrees((double)Math.atan((double)(((double)(f4 * f4) - Math.sqrt((double)((double)(f5 * f5 * f2 * f2) - 0.006000000052154064 * (0.006000000052154064 * (d7 * d7) + d2 * 2.0 * (double)(f6 * f6))))) / (d6 * 0.006000000052154064)))));
        if (f2 < 0.1f) {
            arrf = LBRotateUtil.l(LBRotateUtil.l(entity.getEntityBoundingBox()), true);
            f3 = arrf[0];
            f7 = arrf[1];
        }
        if (bl) {
            LBRotateUtil.l1(f3, f7);
            return;
        }
        arrf = LBRotateUtil.l(new float[]{entityPlayerSP.rotationYaw, entityPlayerSP.rotationPitch}, new float[]{f3, f7}, 10 + new Random().nextInt(6));
        if (arrf == null) {
            return;
        }
        entityPlayerSP.rotationYaw = arrf[0];
        entityPlayerSP.rotationPitch = arrf[1];
    }

    public static void l1(float f, float f2){
        if (Double.isNaN((double)f) || Double.isNaN((double)f2)) {
            return;
        }
        ll = f;
        l1 = f2;
        l = true;
        l11 = 0;
    }

    public static float[] l(Entity entity) {
        if (entity == null || mc.thePlayer == null) {
            return null;
        }
        return LBRotateUtil.l(LBRotateUtil.l(entity.getEntityBoundingBox(), false), true);
    }

    public static float[] l(Vec3 vec3, boolean bl){
        Vec3 vec32 = LBRotateUtil.ll();
        if (bl) {
            vec32.addVector(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ);
        }
        double d = vec3.xCoord - vec32.xCoord;
        double d2 = vec3.yCoord - vec32.yCoord;
        double d3 = vec3.zCoord - vec32.zCoord;
        double d4 = d;
        double d5 = d3;
        double d6 = Math.sqrt((double)(d4 * d4 + d5 * d5));
        float f = (float)Math.toDegrees((double)Math.atan2((double)d3, (double)d)) - 90.0f;
        float f2 = (float)(- Math.toDegrees((double)Math.atan2((double)d2, (double)d6)));
        return new float[]{MathHelper.wrapAngleTo180_float((float)f), MathHelper.wrapAngleTo180_float((float)f2)};
    }

    public static void l(BlockPos blockPos){
        if (blockPos == null) {
            return;
        }
        double d = (double)blockPos.getX() + 0.5 - mc.thePlayer.posX;
        double d2 = (double)blockPos.getY() + 0.5 - (mc.thePlayer.getEntityBoundingBox().minY + (double)mc.thePlayer.getEyeHeight());
        double d3 = (double)blockPos.getZ() + 0.5 - mc.thePlayer.posZ;
        double d4 = d;
        double d5 = d3;
        double d6 = Math.sqrt((double)(d4 * d4 + d5 * d5));
        float f = (float)(Math.atan2((double)d3, (double)d) * 180.0 / 3.141592653589793) - 90.0f;
        float f2 = (float)(- Math.atan2((double)d2, (double)d6) * 180.0 / 3.141592653589793);
        LBRotateUtil.l1(mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float((float)(f - mc.thePlayer.rotationYaw)), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float((float)(f2 - mc.thePlayer.rotationPitch)));
    }
}
