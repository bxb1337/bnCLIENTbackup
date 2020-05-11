package net.AzureWare.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class RayTraceUtil {
   protected static Minecraft mc = Minecraft.getMinecraft();
   private float startX;
   private float startY;
   private float startZ;
   private float endX;
   private float endY;
   private float endZ;
   private static final float MAX_STEP = 0.1F;
   private ArrayList positions = new ArrayList();
   private EntityLivingBase entity;

   public RayTraceUtil(EntityLivingBase entity) {
      this.startX = (float)this.mc.thePlayer.posX;
      this.startY = (float)this.mc.thePlayer.posY + 1.0F;
      this.startZ = (float)this.mc.thePlayer.posZ;
      this.endX = (float)entity.posX;
      this.endY = (float)entity.posY + entity.height / 2.0F;
      this.endZ = (float)entity.posZ;
      this.entity = entity;
      this.positions.clear();
      this.addPositions();
   }

   private void addPositions() {
      float diffX = this.endX - this.startX;
      float diffY = this.endY - this.startY;
      float diffZ = this.endZ - this.startZ;
      float currentX = 0.0F;
      float currentY = 1.0F;
      float currentZ = 0.0F;
      int steps = (int)Math.max(Math.abs(diffX) / 0.1F, Math.max(Math.abs(diffY) / 0.1F, Math.abs(diffZ) / 0.1F));

      for(int i = 0; i <= steps; ++i) {
         this.positions.add(new Vector3f(currentX, currentY, currentZ));
         currentX += diffX / (float)steps;
         currentY += diffY / (float)steps;
         currentZ += diffZ / (float)steps;
      }

   }

   private boolean isInBox(Vector3f point, EntityLivingBase target) {
      AxisAlignedBB box = target.getEntityBoundingBox();
      double posX = this.mc.thePlayer.posX + (double)point.x;
      double posY = this.mc.thePlayer.posY + (double)point.y;
      double posZ = this.mc.thePlayer.posZ + (double)point.z;
      boolean x = posX >= box.minX - 0.25D && posX <= box.maxX + 0.25D;
      boolean y = posY >= box.minY && posY <= box.maxY;
      boolean z = posZ >= box.minZ - 0.25D && posZ <= box.maxZ + 0.25D;
      return x && z && y;
   }

   public ArrayList getPositions() {
      return this.positions;
   }

   public EntityLivingBase getEntity() {
	      new ArrayList();
	      double dist = (double)this.mc.thePlayer.getDistanceToEntity(this.entity);
	      EntityLivingBase entity = this.entity;
	      Iterator var6 = this.mc.theWorld.loadedEntityList.iterator();

	      while(true) {
	         EntityLivingBase e;
	         do {
	            do {
	               Object o;
	               do {
	                  if(!var6.hasNext()) {
	                     return entity;
	                  }

	                  o = var6.next();
	               } while(!(o instanceof EntityLivingBase));

	               e = (EntityLivingBase)o;
	            } while((double)this.mc.thePlayer.getDistanceToEntity(e) >= dist);
	         } while(this.mc.thePlayer == e);

	         Iterator var9 = this.getPositions().iterator();

	         while(var9.hasNext()) {
	            Vector3f vec = (Vector3f)var9.next();
	            if(this.isInBox(vec, e) && this.mc.thePlayer.getDistanceToEntity(e) < this.mc.thePlayer.getDistanceToEntity(entity)) {
	               entity = e;
	            }
	         }
	      }
	   }

   public static Entity rayTrace(float yaw, float pitch, double range) {
	      Entity entity = mc.getRenderViewEntity();
	      if(entity != null) {
	         Minecraft var10000 = mc;
	         if(Minecraft.getMinecraft().theWorld != null) {
	            mc.pointedEntity = null;
	            float partialTicks = 1.0F;
	            mc.objectMouseOver = entity.rayTrace(range, partialTicks);
	            Vec3 vec3 = entity.getPositionEyes(partialTicks);
	            var10000 = mc;
	            Vec3 vec31 = Minecraft.getMinecraft().thePlayer.getVectorForRotation(pitch, yaw);
	            Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);
	            Entity pointedEntity = null;
	            Vec3 vec33 = null;
	            float f = 1.0F;
	            var10000 = mc;
	            final Predicate field_180132_d = null;
	            List list = Minecraft.getMinecraft().theWorld.getEntitiesInAABBexcluding(mc.getRenderViewEntity(), mc.getRenderViewEntity().getEntityBoundingBox().addCoord(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range).expand((double)f, (double)f, (double)f), Predicates.and(field_180132_d, Entity::canBeCollidedWith));
	            double d2 = range;

	            for(int i = 0; i < list.size(); ++i) {
	               Entity entity1 = (Entity)list.get(i);
	               float f1 = entity1.getCollisionBorderSize();
	               AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
	               MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
	               if(axisalignedbb.isVecInside(vec3)) {
	                  if(d2 >= 0.0D) {
	                     pointedEntity = entity1;
	                     vec33 = movingobjectposition == null?vec3:movingobjectposition.hitVec;
	                     d2 = 0.0D;
	                  }
	               } else if(movingobjectposition != null) {
	                  double d3 = vec3.distanceTo(movingobjectposition.hitVec);
	                  if(d3 < d2 || d2 == 0.0D) {
	                     boolean flag2 = false;

	                     if(entity1 == entity.ridingEntity && !flag2) {
	                        if(d2 == 0.0D) {
	                           pointedEntity = entity1;
	                           vec33 = movingobjectposition.hitVec;
	                        }
	                     } else {
	                        pointedEntity = entity1;
	                        vec33 = movingobjectposition.hitVec;
	                        d2 = d3;
	                     }
	                  }
	               }
	            }

	            if(pointedEntity != null && (d2 < range || mc.objectMouseOver == null)) {
	               mc.objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);
	               if(pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
	                  return pointedEntity;
	               }
	            }
	         }
	      }

	      return null;
	   }
}
