package net.AzureWare.mod.mods.PLAYER;

import java.util.Iterator;
import java.util.Objects;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPostMotion;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.value.Value;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class AutoTools extends Mod {
    public AutoTools() {
		super("AutoTools", Category.PLAYER);
		// TODO 自动生成的构造函数存根
	}

	public Entity getItems(double range)
    {
        Entity tempEntity = null;
        double dist = range;
        for(Iterator iterator = mc.theWorld.loadedEntityList.iterator(); iterator.hasNext();)
        {
            Object i = iterator.next();
            Entity entity = (Entity)i;
            if(mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && (entity instanceof EntityItem))
            {
                double curDist = mc.thePlayer.getDistanceToEntity(entity);
                if(curDist <= dist)
                {
                    dist = curDist;
                    tempEntity = entity;
                }
            }
        }

        return tempEntity;
    }
	
	@EventTarget
    public void onAttack(EventPacket e)
    {
        if((e.getPacket() instanceof C02PacketUseEntity) && ((C02PacketUseEntity)e.getPacket()).getAction().equals(net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK))
        {
            boolean checks = !mc.thePlayer.isEating();
            if(checks)
                bestSword(((C02PacketUseEntity)e.getPacket()).getEntityFromWorld(mc.theWorld));
        }
    }

	@EventTarget
    public void onClickBlock(EventPostMotion e)
    {
        boolean checks = !mc.thePlayer.isEating();
        if(checks && mc.playerController.isHittingBlock && !Objects.isNull(mc.objectMouseOver.getBlockPos()))
            bestTool(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(), mc.objectMouseOver.getBlockPos().getZ());
    }

    public void bestSword(Entity targetEntity)
    {
        int bestSlot = 0;
        float f = -1F;
        for(int i1 = 36; i1 < 45; i1++)
            if(mc.thePlayer.inventoryContainer.inventorySlots.toArray()[i1] != null && targetEntity != null)
            {
                ItemStack curSlot = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
                if(curSlot != null && (curSlot.getItem() instanceof ItemSword))
                {
                    ItemSword sword = (ItemSword)curSlot.getItem();
                    if(sword.getDamageVsEntity() > f)
                    {
                        bestSlot = i1 - 36;
                        f = sword.getDamageVsEntity();
                    }
                }
            }

        if(f > -1F)
        {
            mc.thePlayer.inventory.currentItem = bestSlot;
            mc.playerController.updateController();
        }
    }

    public void bestTool(int x, int y, int z)
    {
        int blockId = Block.getIdFromBlock(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
        int bestSlot = 0;
        float f = -1F;
        for(int i1 = 36; i1 < 45; i1++)
            try
            {
                ItemStack curSlot = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
                if(((curSlot.getItem() instanceof ItemTool) || (curSlot.getItem() instanceof ItemSword) || (curSlot.getItem() instanceof ItemShears)) && curSlot.getStrVsBlock(Block.getBlockById(blockId)) > f)
                {
                    bestSlot = i1 - 36;
                    f = curSlot.getStrVsBlock(Block.getBlockById(blockId));
                }
            }
            catch(Exception exception) { }

        if(f != -1F)
        {
            mc.thePlayer.inventory.currentItem = bestSlot;
            mc.playerController.updateController();
        }
    }
}
