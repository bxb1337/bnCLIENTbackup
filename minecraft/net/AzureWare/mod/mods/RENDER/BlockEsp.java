package net.AzureWare.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventBlockRender;
import net.AzureWare.events.EventRender;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockEsp extends Mod {

	public BlockEsp() {
		super("OreTargets", Category.RENDER);
	}

	private Minecraft mc = Minecraft.getMinecraft();
	public static List<BlockPos> toRender = new ArrayList<>();

	public Value<Boolean> dia = new Value<Boolean>("OreTargets_Diamond", true);
	public Value<Boolean> gold = new Value<Boolean>("OreTargets_Gold", true);
	public Value<Boolean> iron = new Value<Boolean>("OreTargets_Iron", true);
	public Value<Boolean> lapis = new Value<Boolean>("OreTargets_Lapis", true);
	public Value<Boolean> emerald = new Value<Boolean>("OreTargets_Emerald", true);
	public Value<Boolean> coal = new Value<Boolean>("OreTargets_Coal", true);
	public Value<Boolean> redstone = new Value<Boolean>("OreTargets_Redstone", true);

	public Value<Boolean> bypass = new Value<Boolean>("OreTargets_TouchingAirOrLiquidTest", true);
	public Value<Double> depth = new Value<Double>("OreTargets_TestDepth", 2d, 1d, 5d, 1d);

	public Value<Boolean> radiusOn = new Value<Boolean>("OreTargets_DistanceLimitEnabled", true);
	public Value<Double> radius = new Value<Double>("OreTargets_DistanceLimit", 10d, 5d, 100d, 5d);

	public Value<Boolean> limitEnabled = new Value<Boolean>("OreTargets_RenderLimitEnabled", true);
	public Value<Double> limit = new Value<Double>("OreTargets_RenderLimit", 10d, 5d, 100d, 5d);

	public Value<Double> refresh_timer = new Value<Double>("OreTargets_RefreshListDelayMillis", 20d, 0d, 1000d, 20d);
	

	public Value<Double> alpha = new Value<Double>("OreTargets_Alpha",0.25d,0d,1d,0.05d);
	public Value<Double> width = new Value<Double>("OreTargets_LineWidth",2.5d,1d,10d,0.5d);
	private TimeHelper refresh = new TimeHelper();

	public void onEnable() {
		toRender.clear();
		mc.renderGlobal.loadRenderers();

	}

	@EventTarget
	public void onTick(EventUpdate e) {
		if (refresh.delay(refresh_timer.getValueState().floatValue())) {
			List<BlockPos> list = toRender;
			list = list.stream().filter(this::test).collect(Collectors.toList());
			toRender = list;
		}
	}

	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	 /*Edit BY EnterBitch */
	/*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */ /*Edit BY EnterBitch */
	@EventTarget
	public void onRenderBlock(EventBlockRender event) {
		BlockPos pos = new BlockPos(event.x, event.y, event.z);

		if (!toRender.contains(pos))
			if (test(pos))
				if (!(toRender.size() > limit.getValueState()) || !limitEnabled.getValueState())
					toRender.add(pos);
		// if(limitEnabled.getValueState() && toRender.size() > limit.getValueState()){
		// toRender = toRender.subList(0, (int) (limit.getValueState()+0));
		// }

	}

	public boolean isTarget(BlockPos pos) {
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

	private Boolean oreTest(BlockPos origPos, Double depth) {
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

		List<Block> legitBlocks = Arrays.asList(Blocks.water, Blocks.lava, Blocks.flowing_lava, Blocks.air,
				Blocks.flowing_water);

		return finalList.stream()
				.anyMatch(blockPos -> legitBlocks.contains(mc.theWorld.getBlockState(blockPos).getBlock()));
	}

	public static float[] getColor(BlockPos pos) {
		Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
		if (Blocks.diamond_ore.equals(block)) {
			return new float[] { 0, 1, 1 };
		} else if (Blocks.lapis_ore.equals(block)) {
			return new float[] { 0, 0, 1 };
		} else if (Blocks.iron_ore.equals(block)) {
			return new float[] { 1, 1, 1 };
		} else if (Blocks.gold_ore.equals(block)) {
			return new float[] { 1, 1, 0 };
		} else if (Blocks.coal_ore.equals(block)) {
			return new float[] { 0, 0, 0 };
		} else if (Blocks.emerald_ore.equals(block)) {
			return new float[] { 0, 1, 0 };
		} else if (Blocks.redstone_ore.equals(block) || Blocks.lit_redstone_ore.equals(block)) {
			return new float[] { 1, 0, 0 };
		}
		return new float[] { 0, 0, 0 };
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

	@EventTarget
	public void onRender(EventRender event) {
		for (BlockPos blockPos : BlockEsp.toRender) {
			renderBlock(blockPos);
		}
	}

	private void renderBlock(BlockPos pos) {
		double x = (double) pos.getX() - (mc.getRenderManager()).renderPosX;
		double y = (double) pos.getY() - (mc.getRenderManager()).renderPosY;
		double z = (double) pos.getZ() - (mc.getRenderManager()).renderPosZ;
		final float[] color = BlockEsp.getColor(pos);
		drawOutlinedBlockESP(x, y, z, color[0], color[1], color[2], alpha.getValueState().floatValue(),
				width.getValueState().floatValue());
	}

	public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue,
			float alpha, float lineWidth) {
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glLineWidth(lineWidth);
		GL11.glColor4f(red, green, blue, alpha);
		drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glDisable(2848);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}

	public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(3, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(3, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(1, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		tessellator.draw();
	}

}
