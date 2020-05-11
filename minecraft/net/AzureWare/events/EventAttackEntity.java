package net.AzureWare.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class EventAttackEntity implements Event {
   private EntityPlayer playerIn;
   public Entity target;

   public EventAttackEntity(EntityPlayer playerIn, Entity target) {
      this.playerIn = playerIn;
      this.target = target;
   }

   public EntityPlayer getPlayerIn() {
      return this.playerIn;
   }

   public Entity getTarget() {
      return this.target;
   }
}
