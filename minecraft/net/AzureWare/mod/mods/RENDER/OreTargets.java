package net.AzureWare.mod.mods.RENDER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.mod.Mod;
import net.AzureWare.value.Value;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class OreTargets extends Mod {

	public OreTargets() {
		super("OreTargets", Category.RENDER);
	}
	/*Edit By À¿¬Ëenter»À*/
	private Minecraft mc = Minecraft.getMinecraft();
	public  static List<BlockPos> toRender = new ArrayList<>();

	public  Value<Boolean> dia = new Value<Boolean>("OreTargets_Diamond",true);
	public  Value<Boolean> gold = new Value<Boolean>("OreTargets_Gold",true);
	public  Value<Boolean> iron = new Value<Boolean>("OreTargets_Iron",true);
	public  Value<Boolean> lapis = new Value<Boolean>("OreTargets_Lapis",true);
	public  Value<Boolean> emerald = new Value<Boolean>("OreTargets_Emerald",true);
	public  Value<Boolean> coal = new Value<Boolean>("OreTargets_Coal",true);
	public  Value<Boolean> redstone = new Value<Boolean>("OreTargets_Redstone",true);


	public  Value<Boolean> bypass = new Value<Boolean>("OreTargets_TouchingAirOrLiquidTest",true);
	public  Value<Double> depth = new Value<Double>("OreTargets_TestDepth",2d,1d,5d,1d);

	public  Value<Boolean> radiusOn = new Value<Boolean>("OreTargets_DistanceLimitEnabled",true);
	public  Value<Double> radius = new Value<Double>("OreTargets_DistanceLimit",10d,5d,100d,5d);


	public  Value<Boolean> limitEnabled = new Value<Boolean>("OreTargets_RenderLimitEnabled",true);
	public  Value<Double> limit = new Value<Double>("OreTargets_RenderLimit",10d,5d,100d,5d);

	@Override
	public void onEnable() {
		mc.renderGlobal.loadRenderers();
		toRender.clear();
	}

	public boolean isTarget(BlockPos pos){
		Block block = mc.theWorld.getBlockState(pos).getBlock();
		if (Blocks.diamond_ore.equals(block)) {
			return dia.getValueState();
		} else if (Blocks.lapis_ore.equals(block)) {
			return lapis.getValueState();
		} else if (Blocks.iron_ore.equals(block)) {
			return iron.getValueState();
		} else if (Blocks.gold_ore.equals(block)) {
			return gold.getValueState();
		} else if (Blocks.coal_ore.equals(block)) {
			return coal.getValueState();
		} else if (Blocks.emerald_ore.equals(block)) {
			return emerald.getValueState();
		} else if (Blocks.redstone_ore.equals(block) || Blocks.lit_redstone_ore.equals(block)) {
			return redstone.getValueState();
		}
		return false;
	}
	private Boolean oreTest(BlockPos origPos, Double depth){
		Collection<BlockPos> posesNew = new ArrayList<>();
		Collection<BlockPos> posesLast = new ArrayList<>(Collections.singletonList(origPos));
		Collection<BlockPos> finalList = new ArrayList<>();
		for (int i = 0; i < depth; i++) {
			for (BlockPos blockPos : posesLast) {
				posesNew.add(blockPos.up());
				posesNew.add(blockPos.down());
				posesNew.add(blockPos.north());
				posesNew.add(blockPos.south());
				posesNew.add(blockPos.west());
				posesNew.add(blockPos.east());
			}
			for (BlockPos pos : posesNew) {
				if (posesLast.contains(pos)) {
					posesNew.remove(pos);
				}
			}
			posesLast = posesNew;
			finalList.addAll(posesNew);
			posesNew = new ArrayList<>();
		}




		List<Block> legitBlocks = Arrays.asList(Blocks.water,Blocks.lava,Blocks.flowing_lava,Blocks.air);

		return finalList.stream().anyMatch(blockPos -> legitBlocks.contains( mc.theWorld.getBlockState(blockPos).getBlock()));
	}
	public static  float[] getColor(BlockPos pos){
		Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
		if (Blocks.diamond_ore.equals(block)) {
			return new float[]{0,1,1};
		} else if (Blocks.lapis_ore.equals(block)) {
			return new float[]{0,0,1};
		} else if (Blocks.iron_ore.equals(block)) {
			return new float[]{1,1,1};
		} else if (Blocks.gold_ore.equals(block)) {
			return new float[]{1,1,0};
		} else if (Blocks.coal_ore.equals(block)) {
			return new float[]{0,0,0};
		} else if (Blocks.emerald_ore.equals(block)) {
			return new float[]{0,1,0};
		} else if (Blocks.redstone_ore.equals(block) || Blocks.lit_redstone_ore.equals(block)) {
			return new float[]{1,0,0};
		}
		return new float[]{0,0,0};
	}

	public boolean test(BlockPos pos1) {
		if (!isTarget(pos1)) {
			return false;
		}
		if (bypass.getValueState()) {
			if (!oreTest(pos1, depth.getValueState())) {
				return false;
			}
		}
		if (radiusOn.getValueState()) {
			return !(mc.thePlayer.getDistance(pos1.getX(), pos1.getY(), pos1.getZ()) >= radius.getValueState());
		}
		return true;
	}
}
