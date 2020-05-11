package net.AzureWare.mod.mods.WORLD;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;

import net.AzureWare.Client;
import net.AzureWare.events.EventMotionUpdate;
import net.AzureWare.events.EventMove;
import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPostMotion;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventRender;
import net.AzureWare.events.EventRenderGui;
import net.AzureWare.events.EventSafeWalk;
import net.AzureWare.events.EventTick;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.events.LoopEvent;
import net.AzureWare.events.EventPacket.EventPacketType;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.InvUtils;
import net.AzureWare.utils.PlaceInfo;
import net.AzureWare.utils.PlaceRotation;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.Rotation;
import net.AzureWare.utils.RotationUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Scaffold2 extends Mod {

	public static final List<Block> BLOCK_BLACKLIST = Arrays.asList(Blocks.enchanting_table, Blocks.chest,
			Blocks.ender_chest, Blocks.trapped_chest, Blocks.anvil, Blocks.sand, Blocks.web, Blocks.torch,
			Blocks.crafting_table, Blocks.furnace, Blocks.waterlily, Blocks.dispenser, Blocks.stone_pressure_plate,
			Blocks.wooden_pressure_plate, Blocks.noteblock, Blocks.dropper, Blocks.tnt, Blocks.standing_banner,
			Blocks.wall_banner);

	public static Value<Boolean> tower = new Value<Boolean>("Scaffold_Tower", true);

	private final Value<Boolean> autoBlockValue = new Value<Boolean>("Scaffold_Slient", true);
	public Value<Boolean> rotations = new Value<Boolean>("Scaffold_Rotations", true);
	public Value<String> placeMode = new Value<String>("Scaffold", "PlaceMode", 0);
	private final Value<Boolean> swingValue = new Value<Boolean>("Scaffold_Swing", true);
	private final Value<Boolean> searchValue = new Value<Boolean>("Scaffold_Search", true);
	private final Value<Boolean> KeepRotationValue = new Value<Boolean>("Scaffold_KeepRotation", true);
	private final Value<Boolean> safeWalkValue = new Value<Boolean>("Scaffold_SafeWalk", true);
	private String[] directions = new String[] { "S", "SW", "W", "NW", "N", "NE", "E", "SE" };
	private Rotation lockRotation;
	/**
	 * MODULE
	 */

	// Target block
	private PlaceInfo targetPlace;

	// Auto block slot
	private int slot;

	int curY = 0;

	private Rotation targetRotation;

	private Rotation serverRotation;

	private int keepLength;

	public Scaffold2() {
		super("Scaffold", Category.WORLD);
		placeMode.mode.add("Pre");
		placeMode.mode.add("Post");
	}

	@Override
	public void onEnable() {
		curY = 0;
		super.onEnable();
	}

	@Override
	public void onDisable() {
		if (mc.thePlayer == null)
			return;

		if (slot != mc.thePlayer.inventory.currentItem)
			mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
		super.onDisable();

	}

	@EventTarget
	public void onRender2D(EventRenderGui e) {
		ScaledResolution res = new ScaledResolution(this.mc);
		FontRenderer font = Client.instance.getFontManager().comfortaa20;
		if (slot != -1)
			if (mc.thePlayer != null && mc.thePlayer.inventory.MainInventory[slot] != null
					&& ModManager.getModByName("Scaffold").isEnabled()) {
				if (mc.thePlayer.inventory.MainInventory[slot].getItem() instanceof ItemBlock) {
					String Text = getBlockCount() + " Blocks";
					int AddX = font.getStringWidth(Text) + 4;
					// Rect
					RenderUtil.drawFastRoundedRect(res.getScaledWidth() / 2 - 12 - 4 - AddX / 2, 4,
							res.getScaledWidth() / 2 + 12f + 4 + AddX / 2, 4 + 24 + 6, 2,
							new Color(0, 0, 0, 160).getRGB());

					// ICON
//					RenderUtil.drawImage(new ResourceLocation("Client/Block.png"),
//							res.getScaledWidth() / 2 - 12 - AddX / 2, 8, 24, 24);
					GL11.glPushMatrix();
					RenderItem ir = new RenderItem(mc.getTextureManager(), mc.modelManager);
					GlStateManager.scale(1.6f, 1.6f, 1.6f);
					RenderHelper.enableGUIStandardItemLighting();
					ir.renderItemIntoGUI(mc.thePlayer.inventory.MainInventory[slot],
							(int) ((res.getScaledWidth() / 2 - 12 - AddX / 2) / 1.6f), (int) ((7) / 1.6f));
					RenderHelper.disableStandardItemLighting();
					GlStateManager.enableAlpha();
					GlStateManager.disableCull();
					GlStateManager.disableBlend();
					GlStateManager.disableLighting();
					GlStateManager.clear(256);
					GL11.glPopMatrix();
					// Text
					font.drawStringWithShadow(Text, res.getScaledWidth() / 2 - 12 - 4 - AddX / 2 + 6 + 24, 4 + 10,
							Color.WHITE.getRGB());
				}

			}
	}

	@EventTarget
	public void onPacket(final EventPacket event) {
		if (mc.thePlayer == null)
			return;

		final Packet<?> packet = event.getPacket();

		// AutoBlock
		if (packet instanceof C09PacketHeldItemChange) {
			final C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange) packet;

			slot = packetHeldItemChange.getSlotId();
		}
	}
	@EventTarget
	public void onTick(EventTick event) {
	    if (targetRotation != null && --keepLength <= 0) {
	        reset();
	    }

	}
	@EventTarget
	public void onRotation(EventPacket event) {
		if (event.getType() == EventPacketType.SEND) {
			Packet packet = event.getPacket();
			if (packet instanceof C03PacketPlayer) {
				C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;
				if (!(targetRotation == null || targetRotation.getYaw() == serverRotation.getYaw()
						&& targetRotation.getPitch() == serverRotation.getPitch())) {
					packetPlayer.yaw = targetRotation.getYaw();
					packetPlayer.pitch = targetRotation.getPitch();
					packetPlayer.rotating = true;
				}
				if (packetPlayer.rotating) {
					serverRotation = new Rotation(packetPlayer.yaw, packetPlayer.pitch);
				}
			}
		}

	}

	public void reset() {
		targetRotation = null;
	}

	@EventTarget
	public void onMotion(final EventPreMotion event) {
		findBlock(event);

		// targetPlace
//		System.out.println(mc.thePlayer.rotationPitch);
		if (rotations.getValueState())
			if (targetPlace != null || KeepRotationValue.getValueState())
				if (lockRotation != null) {
//			mc.thePlayer.rotationYaw = lockRotation.getYaw();
//			mc.thePlayer.rotationPitch = lockRotation.getPitch();
					lockRotation.fixedSensitivity(mc.gameSettings.mouseSensitivity);
					targetRotation = lockRotation;
//					mc.thePlayer.renderYawOffset = event.getYaw();
//					mc.thePlayer.rotationYawHead = event.getYaw();
//					
//					mc.thePlayer.renderArmPitch = event.getPitch();;
//			System.out.println(lockRotation.getYaw()+"|"+lockRotation.getPitch());
				}
		if (placeMode.getModeName() == "Pre")
			place();
			if (autoBlockValue.getValueState() ? findAutoBlockBlock() == -1
					: mc.thePlayer.getHeldItem() == null
							|| !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock))
				return;


	}

	@EventTarget
	public void onMotion(final EventPostMotion event) {
//		BlockPos DownPos = null;
//		Block DownBlock = null;
//		int 偏移数值 = 0;
//
//		if(mc.gameSettings.keyBindLeft.isKeyDown())
//		{
//			偏移数值 -=90 - (mc.gameSettings.keyBindForward.isKeyDown()?45:0);
//		}
//		if(mc.gameSettings.keyBindRight.isKeyDown())
//		{
//			偏移数值 +=90 - (mc.gameSettings.keyBindForward.isKeyDown()?45:0);
//		}
//		if(mc.gameSettings.keyBindBack.isKeyDown())
//		{
//			if(偏移数值 > 0)
//			偏移数值 +=90-45;
//			if(偏移数值 < 0)
//			偏移数值 -=90-45;
//			if(偏移数值 == 0)
//			偏移数值 -=180;
//		}
//		switch(this.directions[RotationUtil.wrapAngleToDirection(this.mc.thePlayer.rotationYaw+偏移数值, this.directions.length)])
//		{
//		case "S":
//			DownPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ - 0.5);
//			DownBlock = mc.theWorld.getBlockState(DownPos).getBlock();
//			break;
//		case "SW":
//			DownPos = new BlockPos(mc.thePlayer.posX + 0.5, mc.thePlayer.posY - 2, mc.thePlayer.posZ - 0.5);
//			DownBlock = mc.theWorld.getBlockState(DownPos).getBlock();
//			break;
//		case "W":
//			DownPos = new BlockPos(mc.thePlayer.posX + 0.5, mc.thePlayer.posY - 2, mc.thePlayer.posZ);
//			DownBlock = mc.theWorld.getBlockState(DownPos).getBlock();
//			break;
//		case "NW":
//			DownPos = new BlockPos(mc.thePlayer.posX + 0.5, mc.thePlayer.posY - 2, mc.thePlayer.posZ + 0.5);
//			DownBlock = mc.theWorld.getBlockState(DownPos).getBlock();
//			break;
//		case "N":
//			DownPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ + 0.5);
//			DownBlock = mc.theWorld.getBlockState(DownPos).getBlock();
//			break;
//		case "NE":
//			DownPos = new BlockPos(mc.thePlayer.posX - 0.5, mc.thePlayer.posY - 2, mc.thePlayer.posZ + 0.5);
//			DownBlock = mc.theWorld.getBlockState(DownPos).getBlock();
//			break;
//		case "E":
//			DownPos = new BlockPos(mc.thePlayer.posX - 0.5, mc.thePlayer.posY - 2, mc.thePlayer.posZ);
//			DownBlock = mc.theWorld.getBlockState(DownPos).getBlock();
//			break;
//		case "SE":
//			DownPos = new BlockPos(mc.thePlayer.posX - 0.5, mc.thePlayer.posY - 2, mc.thePlayer.posZ - 0.5);
//			DownBlock = mc.theWorld.getBlockState(DownPos).getBlock();
//			break;
//		}
		double x = mc.thePlayer.posX;
		double y = mc.thePlayer.posY - 1.0;
		double z = mc.thePlayer.posZ;
		BlockPos blockBelow = new BlockPos(x, y, z);
		if (Scaffold2.tower.getValueState()) {
//			if (this.movetower.getValue().booleanValue()) {
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				if (mc.thePlayer.isMoving()) {
					if (PlayerUtil.isOnGround(0.76) && !PlayerUtil.isOnGround(0.75) && mc.thePlayer.motionY > 0.23
							&& mc.thePlayer.motionY < 0.25) {
						mc.thePlayer.motionY = (double) Math.round(mc.thePlayer.posY) - mc.thePlayer.posY;
					}
					if (PlayerUtil.isOnGround(1.0E-4)) {
						mc.thePlayer.motionY = 0.42;
						mc.thePlayer.motionX *= 0.9;
						mc.thePlayer.motionZ *= 0.9;
					} else if (mc.thePlayer.posY >= (double) Math.round(mc.thePlayer.posY) - 1.0E-4
							&& mc.thePlayer.posY <= (double) Math.round(mc.thePlayer.posY) + 1.0E-4) {
						mc.thePlayer.motionY = 0.0;
					}
				} else {
					mc.thePlayer.motionX = 0.0;
					mc.thePlayer.motionZ = 0.0;
					mc.thePlayer.jumpMovementFactor = 0.0f;
					blockBelow = new BlockPos(x, y, z);
					if (mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air && this.targetPlace != null) {
						mc.thePlayer.motionY = 0.4196;
						mc.thePlayer.motionX *= 0.75;
						mc.thePlayer.motionZ *= 0.75;
					}
				}
			}
		}
//			 else if (!mc.thePlayer.isMoving() && mc.gameSettings.keyBindJump.isKeyDown()) {
//				mc.thePlayer.motionX = 0.0;
//				mc.thePlayer.motionZ = 0.0;
//				mc.thePlayer.jumpMovementFactor = 0.0f;
//				blockBelow = new BlockPos(x, y, z);
//				if (mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air && this.targetPlace != null) {
//					mc.thePlayer.motionY = 0.4196;
//					mc.thePlayer.motionX *= 0.75;
//					mc.thePlayer.motionZ *= 0.75;
//				}
//			}
//		}
//		if(DownBlock != null)
//		if(mc.gameSettings.keyBindSneak.isKeyDown() && mc.thePlayer.onGround && DownBlock.getUnlocalizedName().trim().equals("tile.air"))
//		{
//			if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), EnumFacing.DOWN, new Vec3(0, 1, 0)))
//			{
//				if (swingValue.getValueState())
//					mc.thePlayer.swingItem();
//				else
//					mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
//			}
//			return;
//		}
		if (placeMode.getModeName() == "Post")
			place();
	}

	public static int getBlockCount() {

		int blockCount = 0;
		for (int i = 0; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				Item item = is.getItem();
				if (is.getItem() instanceof ItemBlock && isValid(item)) {
					blockCount += is.stackSize;
				}
			}
		}
		return blockCount;
	}

	private static boolean isValid(Item item) {
		if (!(item instanceof ItemBlock)) {
			return false;
		}
		ItemBlock iBlock = (ItemBlock) item;
		Block block = iBlock.getBlock();
		if (BLOCK_BLACKLIST.contains(block)) {
			return false;
		}

		return true;
	}

	public static final Block getBlock(BlockPos blockPos) {
		WorldClient var10000 = Minecraft.getMinecraft().theWorld;
		Block var2;
		if (var10000 != null) {
			IBlockState var1 = var10000.getBlockState(blockPos);
			if (var1 != null) {
				var2 = var1.getBlock();
				return var2;
			}
		}

		var2 = null;
		return var2;
	}

	public static final Material getMaterial(BlockPos blockPos) {
		Block var10000 = getBlock(blockPos);
		return var10000 != null ? var10000.getMaterial() : null;
	}

	public static final boolean isReplaceable(BlockPos blockPos) {
		Material var10000 = getMaterial(blockPos);
		return var10000 != null ? var10000.isReplaceable() : false;
	}

	public static final IBlockState getState(BlockPos blockPos) {
		IBlockState var10000 = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
		return var10000;
	}

	public static final boolean canBeClicked(BlockPos blockPos) {
		Block var10000 = getBlock(blockPos);
		boolean var2;
		if (var10000 != null ? var10000.canCollideCheck(getState(blockPos), false) : false) {
			WorldClient var1 = Minecraft.getMinecraft().theWorld;
			if (var1.getWorldBorder().contains(blockPos)) {
				var2 = true;
				return var2;
			}
		}

		var2 = false;
		return var2;
	}

	/**
	 * Search for new target block
	 */
	private void findBlock(EventPreMotion e) {
		final BlockPos blockPosition = mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ? new BlockPos(mc.thePlayer)
				: new BlockPos(mc.thePlayer.posX,
						mc.thePlayer.posY - (mc.gameSettings.keyBindSneak.isKeyDown() ? 1 : 0), mc.thePlayer.posZ)
								.down();

		if ((!isReplaceable(blockPosition) || search(blockPosition, true, e)))
			return;
		if (searchValue.getValueState()) {
			for (int x = -1; x <= 1; x++)
				for (int z = -1; z <= 1; z++)
					if (search(blockPosition.add(x, 0, z), true, e))
						return;
		}
	}

	/**
	 * Search for placeable block
	 *
	 * @param blockPosition pos
	 * @param checks        visible
	 * @return
	 */
	private boolean search(final BlockPos blockPosition, final boolean checks, EventPreMotion e) {
		if (!isReplaceable(blockPosition))
			return false;

		final Vec3 eyesPos = new Vec3(mc.thePlayer.posX,
				mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

		PlaceRotation placeRotation = null;

		for (final EnumFacing side : EnumFacing.values()) {
			final BlockPos neighbor = blockPosition.offset(side);

			if (!canBeClicked(neighbor))
				continue;

			final Vec3 dirVec = new Vec3(side.getDirectionVec());

			for (double xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.1D) {
				for (double ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.1D) {
					for (double zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.1D) {
						final Vec3 posVec = new Vec3(blockPosition).addVector(xSearch, ySearch, zSearch);
						final double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
						final Vec3 hitVec = posVec
								.add(new Vec3(dirVec.xCoord * 0.5, dirVec.yCoord * 0.5, dirVec.zCoord * 0.5));

						if (checks && (eyesPos.squareDistanceTo(hitVec) > 18D
								|| distanceSqPosVec > eyesPos.squareDistanceTo(posVec.add(dirVec))
								|| mc.theWorld.rayTraceBlocks(eyesPos, hitVec, false, true, false) != null))
							continue;

						// face block
						final double diffX = hitVec.xCoord - eyesPos.xCoord;
						final double diffY = hitVec.yCoord - eyesPos.yCoord;
						final double diffZ = hitVec.zCoord - eyesPos.zCoord;

						final double diffXZ = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);

						final Rotation rotation = new Rotation(
								MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F),
								MathHelper.wrapAngleTo180_float((float) -Math.toDegrees(Math.atan2(diffY, diffXZ))));

						final Vec3 rotationVector = RotationUtil.getVectorForRotation(rotation);
						final Vec3 vector = eyesPos.addVector(rotationVector.xCoord * 4, rotationVector.yCoord * 4,
								rotationVector.zCoord * 4);
						final MovingObjectPosition obj = mc.theWorld.rayTraceBlocks(eyesPos, vector, false, false,
								true);

						if (!(obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
								&& obj.getBlockPos().equals(neighbor)))
							continue;

						if (placeRotation == null || RotationUtil.getRotationDifference(rotation,
								new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)) < RotationUtil
										.getRotationDifference(placeRotation.getRotation(),
												new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)))
							placeRotation = new PlaceRotation(new PlaceInfo(neighbor, side.getOpposite(), hitVec),
									rotation);
					}
				}
			}
		}

		
		
        if (placeRotation == null) {
            return false;
        }
        if (rotations.getValueState()) {
        	targetRotation = placeRotation.getRotation();
            ++keepLength;
            this.lockRotation = placeRotation.getRotation();
        }
        this.targetPlace = placeRotation.getPlaceInfo();
        return true;
	}

	/**
	 * Place target block
	 */
	private void place() {
		if (targetPlace == null) {

			return;
		}

		int blockSlot = -1;
		ItemStack itemStack = mc.thePlayer.getHeldItem();

		if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
			if (!autoBlockValue.getValueState())
				return;

			blockSlot = findAutoBlockBlock();

			if (blockSlot == -1)
				return;

			mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot - 36));
			itemStack = mc.thePlayer.inventoryContainer.getSlot(blockSlot).getStack();
		}

//		boolean callback = false;
//		
//		if(!mc.gameSettings.keyBindSneak.isKeyDown())	
//		{
//			callback = mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, targetPlace.getBlockPos(),
//					targetPlace.getEnumFacing(), targetPlace.getVec3());
//		}
//		else
//		{
//			callback = mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ),
//					EnumFacing.DOWN, new Vec3(0, 1, 0));
//		}

		if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, targetPlace.getBlockPos(),
				targetPlace.getEnumFacing(), targetPlace.getVec3())) {

			if (swingValue.getValueState())
				mc.thePlayer.swingItem();
			else
				mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
		}

//        if (!stayAutoBlock.get() && blockSlot >= 0)
//            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));

		// Reset
		this.targetPlace = null;
	}

	public static int findAutoBlockBlock() {
		for (int i = 36; i < 45; i++) {
			final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

			if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
				final ItemBlock itemBlock = (ItemBlock) itemStack.getItem();
				final Block block = itemBlock.getBlock();
				if (itemStack.stackSize > 0 && block.isFullCube() && !BLOCK_BLACKLIST.contains(block))
					return i;
			}
		}

		for (int i = 36; i < 45; i++) {
			final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

			if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
				final ItemBlock itemBlock = (ItemBlock) itemStack.getItem();
				final Block block = itemBlock.getBlock();

				if (itemStack.stackSize > 0 && !BLOCK_BLACKLIST.contains(block))
					return i;
			}
		}

		return -1;
	}
}