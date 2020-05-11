
package net.AzureWare.utils;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;


public final class PlaceInfo {

    private final BlockPos blockPos;

    private final EnumFacing enumFacing;

    private Vec3 vec3;
    public static final Companion Companion = new Companion();


    public PlaceInfo(BlockPos blockPos, EnumFacing enumFacing, Vec3 vec3) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
        this.vec3 = vec3;
    }

    public final BlockPos getBlockPos() {
        return this.blockPos;
    }


    public final EnumFacing getEnumFacing() {
        return this.enumFacing;
    }


    public final Vec3 getVec3() {
        return this.vec3;
    }

    public final void setVec3( Vec3 vec3) {
        this.vec3 = vec3;
    }

    public PlaceInfo(BlockPos blockPos, EnumFacing enumFacing, Vec3 vec3, int n) {
    	this(blockPos, enumFacing, vec3);
    	if ((n & 4) != 0) {
            vec3 = new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5);
        }
    }


    public static final PlaceInfo get( BlockPos blockPos) {
        return Companion.get(blockPos);
    }
    
    public static final Block getBlock( BlockPos blockPos) {
        WorldClient var10000 = Minecraft.getMinecraft().theWorld;
        Block var2;
        if(var10000 != null) {
           IBlockState var1 = var10000.getBlockState(blockPos);
           if(var1 != null) {
              var2 = var1.getBlock();
              return var2;
           }
        }

        var2 = null;
        return var2;
     }
    public static final IBlockState getState(BlockPos blockPos) {
        IBlockState var10000 = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
        return var10000;
     }

    public static final boolean canBeClicked(BlockPos blockPos) {
        Block var10000 = getBlock(blockPos);
        boolean var2;
        if(var10000 != null?var10000.canCollideCheck(getState(blockPos), false):false) {
           WorldClient var1 = Minecraft.getMinecraft().theWorld;
           if(var1.getWorldBorder().contains(blockPos)) {
              var2 = true;
              return var2;
           }
        }

        var2 = false;
        return var2;
     }

}
 class Companion {

    public final PlaceInfo get(BlockPos blockPos) {
        PlaceInfo placeInfo;
        if (PlaceInfo.canBeClicked((BlockPos)blockPos.add(0, -1, 0))) {
            BlockPos blockPos2 = blockPos.add(0, -1, 0);
         
            return new PlaceInfo(blockPos2, EnumFacing.UP, null, 4);
        }
        if (PlaceInfo.canBeClicked((BlockPos)blockPos.add(0, 0, 1))) {
            BlockPos blockPos3 = blockPos.add(0, 0, 1);
            return new PlaceInfo(blockPos3, EnumFacing.NORTH, null, 4);
        }
        if (PlaceInfo.canBeClicked((BlockPos)blockPos.add(-1, 0, 0))) {
            BlockPos blockPos4 = blockPos.add(-1, 0, 0);
            return new PlaceInfo(blockPos4, EnumFacing.EAST, null, 4);
        }
        if (PlaceInfo.canBeClicked((BlockPos)blockPos.add(0, 0, -1))) {
            BlockPos blockPos5 = blockPos.add(0, 0, -1);
            return new PlaceInfo(blockPos5, EnumFacing.SOUTH, null, 4);
        }
        if (PlaceInfo.canBeClicked((BlockPos)blockPos.add(1, 0, 0))) {
            BlockPos blockPos6 = blockPos.add(1, 0, 0);
            return new PlaceInfo(blockPos6, EnumFacing.WEST, null, 4);
            
           
        } else {
            placeInfo = null;
        }
        return placeInfo;
    }

    Companion() {
    }

}
