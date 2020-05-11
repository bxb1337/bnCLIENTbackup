package net.AzureWare.mod.mods.PLAYER;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import io.netty.util.internal.ThreadLocalRandom;
import net.AzureWare.events.EventKey;
import net.AzureWare.events.EventPostMotion;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventSafeWalk;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.CombatUtil;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.utils.Wrapper;
import net.AzureWare.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class DebugScaffold
extends Mod {

    private Value<Double> delayValue = new Value<Double>("DebugScaffold_Delay", 250.0, 40.0, 1000.0, 10.0);
    private Value<Double> zoom = new Value("DebugScaffold_TimerBoost", Double.valueOf(1.3D), Double.valueOf(1.0D), Double.valueOf(1.5D), 0.01D);
    private Value<Boolean> setSpeed = new Value<Boolean>("DebugScaffold_SetSpeed", false);

    private Value<Double> SetBoost = new Value<Double>("DebugScaffold_SetSpeed", 0.0, 0.0, 1.0, 0.01);
    public static Value<Boolean> timer = new Value<Boolean>("DebugScaffold_Timer", true);
    private Value<Boolean> tower = new Value<Boolean>("DebugScaffold_Tower", false);
    private Value<Boolean> noSwing = new Value<Boolean>("DebugScaffold_NoSwing", false);
    private Value<Boolean> silent = new Value<Boolean>("DebugScaffold_Silent", false);
    private Value expand = new Value<Boolean>("DebugScaffold_Expand", false);
    public static Value<String> towermode = new Value("DebugScaffold", "TowerMode", 0);
    public static Value<String> mode = new Value("DebugScaffold", "Mode", 0);
    private double olddelay;
    private BlockPos blockpos;
    private EnumFacing facing;
    private boolean rotated = false;
    private boolean should = false;
    private static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
    TimeHelper kickTimer = new TimeHelper();
    TimeHelper spacetimer = new TimeHelper();
    private TimeHelper time = new TimeHelper();
    private TimeHelper timer2 = new TimeHelper();
    private TimeHelper delay = new TimeHelper();
    private TimeHelper towerTimer = new TimeHelper();
    private BlockData blockData;

    public static boolean onServer(String server) {
        return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains(server);
     }
    
    public DebugScaffold() {
        super("DebugScaffold", Category.MOVEMENT);
        DebugScaffold.mode.mode.add("WatchDoge");
        DebugScaffold.mode.mode.add("AAC");
        mode.mode.add("Debug");

       // DebugScaffold.mode.mode.add("CubeCraft");
       //  mode.mode.add("Gomme");
       // towermode.mode.add("AAC");
       // towermode.mode.add("Spartan");
      //  towermode.mode.add("NCP");
        towermode.mode.add("Legit");
        towermode.mode.add("test");

        }

	@EventTarget
	public void onKey(EventKey e) {
		if(e.getKey() == Keyboard.KEY_SPACE) {
			timer2.reset();
			spacetimer.reset();
		}
	}


    private boolean couldBlockBePlaced() {
        double x = Minecraft.thePlayer.posX;
        double z = Minecraft.thePlayer.posZ;
        double d = Wrapper.getDoubleRandom(0.22, 0.25);
        switch (DebugScaffold.$SWITCH_TABLE$net$minecraft$util$EnumFacing()[Minecraft.thePlayer.getHorizontalFacing().ordinal()]) {
            case 3: {
                if (Minecraft.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 0.1, z + d)).getBlock() != Blocks.air) break;
                return true;
            }
            case 4: {
                if (Minecraft.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 0.1, z - d)).getBlock() != Blocks.air) break;
                return true;
            }
            case 5: {
                if (Minecraft.theWorld.getBlockState(new BlockPos(x + d, Minecraft.thePlayer.posY - 0.1, Minecraft.thePlayer.posZ)).getBlock() != Blocks.air) break;
                return true;
            }
            case 6: {
                if (Minecraft.theWorld.getBlockState(new BlockPos(x - d, Minecraft.thePlayer.posY - 0.1, Minecraft.thePlayer.posZ)).getBlock() != Blocks.air) break;
                return true;
            }
        }
        return false;
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
    	this.setDisplayName(mode.getModeAt(towermode.getCurrentMode())); //towermode.getModeAt(towermode.getCurrentMode()));
    	
        	
        
        
        if (mode.isCurrentMode("WatchDoge")) {
            Timer.timerSpeed = 1.0f;
           if(!kickTimer.isDelayComplete(1900L) && this.timer.getValueState().booleanValue()) {
				mc.timer.timerSpeed= zoom.getValueState().floatValue();
	    		this.setDisplayName(" Bursting" + " "+ towermode.getModeAt(towermode.getCurrentMode()));  //towermode.getModeAt(towermode.getCurrentMode()));
			}
           else {
				mc.timer.timerSpeed= 1f;
	    	//	this.setDisplayName(" Hypixel" + " " ); //towermode.getModeAt(towermode.getCurrentMode()));

			}
        }

    	if(!mc.gameSettings.keyBindRight.pressed && !mc.gameSettings.keyBindLeft.pressed && !mc.gameSettings.keyBindForward.pressed && mc.gameSettings.keyBindJump.pressed && tower.getValueState()) {
    		if (this.towermode.isCurrentMode("Legit")) {
                this.legit();
            } else if (this.towermode.isCurrentMode("test")) {
                this.slowlyAACTower();
            }
    	} else {
            towerTimer.reset();
    	}

    



        if (Minecraft.thePlayer != null) {
            this.blockData = (Boolean)this.expand.getValueState() != false ? this.getBlockData(new BlockPos(Minecraft.thePlayer.posX - Math.sin(PlayerUtil.getDirection()) * 1.0, Minecraft.thePlayer.posY - 0.75, Minecraft.thePlayer.posZ + Math.cos(PlayerUtil.getDirection()) * 1.0), 1) : this.getBlockData(new BlockPos(Minecraft.thePlayer).add(0.0, -0.75, 0.0), 1);
            int block = this.getBlockItem();
            Item item = Minecraft.thePlayer.inventory.getStackInSlot(block).getItem();
            if (!mode.isCurrentMode("Gomme")) {
                if (block != -1 && item != null && item instanceof ItemBlock) {
                    if (this.silent.getValueState().booleanValue()) {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(block));
                    }
                }

                new java.util.Random();
                if (this.blockData != null && block != -1 && item != null && item instanceof ItemBlock) {
                    Vec3 rot2 = this.getBlockSide(this.blockData.pos, this.blockData.face);
                    float[] rot = CombatUtil.getRotationsNeededBlock(rot2.xCoord, rot2.yCoord, rot2.zCoord);
                    event.pitch = mode.isCurrentMode("Debug") ? 82.500114f : rot[1];
                    if (!mode.isCurrentMode("CubeCraft")) {
                        event.yaw = rot[0];
                    } else if (this.mc.gameSettings.keyBindForward.pressed) {
                        event.yaw = Minecraft.thePlayer.rotationYaw >= 180.0f ? Minecraft.thePlayer.rotationYaw - 180.0f + (float)new Random().nextInt(5) : Minecraft.thePlayer.rotationYaw + 180.0f - (float)new Random().nextInt(5);
                    } else if (this.mc.gameSettings.keyBindBack.pressed) {
                        event.yaw = Minecraft.thePlayer.rotationYaw;
                    } else if (this.mc.gameSettings.keyBindLeft.pressed) {
                        event.yaw = Minecraft.thePlayer.rotationYaw + 90.0f;
                    } else if (this.mc.gameSettings.keyBindRight.pressed) {
                        event.yaw = Minecraft.thePlayer.rotationYaw - 90.0f;
                    }
                }
            } else {
                if (this.rotated) {
                    PlayerUtil.setSpeed(0.03877341815081586);
                } else {
                    PlayerUtil.setSpeed(0.08621806584246793);
                }
                if (PlayerUtil.MovementInput()) {
                    PlayerUtil.setSpeed(0.13);
                }
                this.rotated = false;
                this.blockpos = null;
                this.facing = null;
                if (block != -1 && item != null && item instanceof ItemBlock) {
                    if (this.silent.getValueState().booleanValue()) {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(block));
                    }
                    BlockPos pos = new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0, Minecraft.thePlayer.posZ);
                    this.setBlockAndFacing(pos);
                    if (Minecraft.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
                        this.rotated = true;
                    }
                    float[] rot21 = CombatUtil.getRotationsNeededBlock(this.blockpos.getX(), this.blockpos.getY(), this.blockpos.getZ());
                    float[] rot = CombatUtil.getIntaveRots(this.blockpos, this.facing);
                    event.yaw = (float)((double)rot[0] + this.getDoubleRandom(-0.1, 0.1));
                    event.pitch = 82.500114f;
                }
            }
            Wrapper.setLook(event.yaw, event.pitch);
        }
    }

    private double getDoubleRandom(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    @EventTarget
    public void onPost(EventPostMotion event) {

    	  if (Minecraft.thePlayer != null && this.blockData != null) {
              int block1 = this.getBlockItem();
              Random rand = new Random();
              Item item1 = Minecraft.thePlayer.inventory.getStackInSlot(block1).getItem();
              if (block1 != -1 && item1 != null && item1 instanceof ItemBlock) {

                  Vec3 hitVec = new Vec3(this.blockData.pos).addVector(0.5, 0.5, 0.5).add(new Vec3(this.blockData.face.getDirectionVec()).scale(0.5));
                  if ((!mode.isCurrentMode("Debug")  || this.delay.isDelayComplete((long)(mode.isCurrentMode("Gomme") ? Wrapper.getRandomLong(0L, 100L) : (mode.isCurrentMode("Debug") ? 0.0 : (double)this.delayValue.getValueState().intValue() + this.getDoubleRandom(30.0, 80.0))))) && Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.inventory.getStackInSlot(block1), this.blockData.pos, this.blockData.face, hitVec)) {
                      this.delay.reset();
                      this.blockData = null;
                      this.time.reset();
                      if (this.noSwing.getValueState().booleanValue()) {
                          Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                      } else {
                          Minecraft.thePlayer.swingItem();
                      }
                  } else if (!mode.isCurrentMode("Debug")) {
                      if (this.delay.isDelayComplete(this.delayValue.getValueState().intValue() + rand.nextInt(50)) && Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.inventory.getStackInSlot(block1), this.blockData.pos, this.blockData.face, hitVec)) {
                          this.delay.reset();
                          this.blockData = null;
                          this.time.reset();
                          if (this.noSwing.getValueState().booleanValue()) {
                              Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                          } else {
                              Minecraft.thePlayer.swingItem();
                          }
                      } else if (this.delay.isDelayComplete(this.delayValue.getValueState().longValue()) && mode.isCurrentMode("WatchDoge")) {
                          if (Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.inventory.getStackInSlot(block1), this.blockData.pos, this.blockData.face, hitVec)) {
                              this.delay.reset();
                              this.blockData = null;
                              if (this.noSwing.getValueState().booleanValue()) {
                                  Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                              } else {
                                  Minecraft.thePlayer.swingItem();
                              }
                          }
                          this.delay.reset();
                      }
                  }
              }
          }
        
    }

    @EventTarget
    public void onSafe(EventSafeWalk e) {
        e.setSafe(true);
      
    }

    private boolean canPlace(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 vec3) {
        return heldStack.getItem() instanceof ItemBlock ? ((ItemBlock)heldStack.getItem()).canPlaceBlockOnSide(worldIn, hitPos, side, player, heldStack) : false;
    }

    private void setBlockAndFacing(BlockPos bp) {
        if (Minecraft.theWorld.getBlockState(bp.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(0, -1, 0);
            this.facing = EnumFacing.UP;
        } else if (Minecraft.theWorld.getBlockState(bp.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(-1, 0, 0);
            this.facing = EnumFacing.EAST;
        } else if (Minecraft.theWorld.getBlockState(bp.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(1, 0, 0);
            this.facing = EnumFacing.WEST;
        } else if (Minecraft.theWorld.getBlockState(bp.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(0, 0, -1);
            this.facing = EnumFacing.SOUTH;
        } else if (Minecraft.theWorld.getBlockState(bp.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(0, 0, 1);
            this.facing = EnumFacing.NORTH;
        } else {
            bp = null;
            this.facing = null;
        }
    }

    private void sendCurrentItem() {
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
    }

    private int getBlockItem() {
        int block = -1;
        int i = 8;
        while (i >= 0) {
            if (Minecraft.thePlayer.inventory.getStackInSlot(i) != null && Minecraft.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && (Minecraft.thePlayer.getHeldItem() == Minecraft.thePlayer.inventory.getStackInSlot(i) || this.silent.getValueState().booleanValue())) {
                block = i;
            }
            --i;
        }
        return block;
    }


    public Vec3 getBlockSide(BlockPos pos, EnumFacing face) {
        return face == EnumFacing.NORTH ? new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() - 0.5) : (face == EnumFacing.EAST ? new Vec3((double)pos.getX() + 0.5, pos.getY(), pos.getZ()) : (face == EnumFacing.SOUTH ? new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() + 0.5) : (face == EnumFacing.WEST ? new Vec3((double)pos.getX() - 0.5, pos.getY(), pos.getZ()) : new Vec3(pos.getX(), pos.getY(), pos.getZ()))));
    }

    @Override
    public void onEnable() {
    	kickTimer.reset();
    	timer2.reset();
    	spacetimer.reset();
        towerTimer.reset();

    	super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.sendCurrentItem();
        this.mc.gameSettings.keyBindSneak.pressed = false;
        Timer.timerSpeed = 1.0f;
    	spacetimer.reset();

    }

    static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
        int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
        if ($SWITCH_TABLE$net$minecraft$util$EnumFacing != null) {
            return var10000;
        }
        int[] var0 = new int[EnumFacing.values().length];
        try {
            var0[EnumFacing.DOWN.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            var0[EnumFacing.EAST.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            var0[EnumFacing.NORTH.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            var0[EnumFacing.SOUTH.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            var0[EnumFacing.UP.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            var0[EnumFacing.WEST.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
        return var0;
    }
    public BlockData getBlockData(BlockPos pos, int i) {
        return this.mc.theWorld.getBlockState(pos.add(0, 0, i)).getBlock() != Blocks.air ? 
        		new BlockData(pos.add(0, 0, i), EnumFacing.NORTH) : 
        			(this.mc.theWorld.getBlockState(pos.add(0, 0, - i)).getBlock() != Blocks.air ?
        					new BlockData(pos.add(0, 0, - i), EnumFacing.SOUTH) :
        						(this.mc.theWorld.getBlockState(pos.add(i, 0, 0)).getBlock() != Blocks.air 
        						? new BlockData(pos.add(i, 0, 0), EnumFacing.WEST) 
        								: (this.mc.theWorld.getBlockState(pos.add(- i, 0, 0)).getBlock() != Blocks.air 
        								? new BlockData(pos.add(- i, 0, 0), EnumFacing.EAST) 
        										: (this.mc.theWorld.getBlockState(pos.add(0, - i, 0)).getBlock() != Blocks.air 
        										? new BlockData(pos.add(0, - i, 0), EnumFacing.UP) : this.mc.theWorld.getBlockState(pos.add(1, 0, i)).getBlock() != Blocks.air ? 
        								        		new BlockData(pos.add(1, 0, i), EnumFacing.NORTH) : 
        								        			(this.mc.theWorld.getBlockState(pos.add(-1, 0, - i)).getBlock() != Blocks.air ?
        								        					new BlockData(pos.add(-1, 0, - i), EnumFacing.SOUTH) :
        								        						(this.mc.theWorld.getBlockState(pos.add(i, 0, 1)).getBlock() != Blocks.air 
        								        						? new BlockData(pos.add(i, 0, 1), EnumFacing.WEST) 
        								        								: (this.mc.theWorld.getBlockState(pos.add(- i, 0, -1)).getBlock() != Blocks.air 
        								        								? new BlockData(pos.add(- i, 0, -1), EnumFacing.EAST) 
        								        										: this.mc.theWorld.getBlockState(pos.add(-1, 0, i)).getBlock() != Blocks.air ? 
        								        								        		new BlockData(pos.add(-1, 0, i), EnumFacing.NORTH) : 
        								        								        			(this.mc.theWorld.getBlockState(pos.add(1, 0, - i)).getBlock() != Blocks.air ?
        								        								        					new BlockData(pos.add(1, 0, - i), EnumFacing.SOUTH) :
        								        								        						(this.mc.theWorld.getBlockState(pos.add(i, 0, -1)).getBlock() != Blocks.air 
        								        								        						? new BlockData(pos.add(i, 0, -1), EnumFacing.WEST) 
        								        								        								: (this.mc.theWorld.getBlockState(pos.add(- i, 0, 1)).getBlock() != Blocks.air 
        								        								        								? new BlockData(pos.add(- i, 0, 1), EnumFacing.EAST) 
        								        								        										: null))))))))));
    }

    public class BlockData {
        public BlockPos pos;
        public EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face) {
            this.pos = position;
            this.face = face;
        }
    }
    
    
    
    




    private void legit() {
        if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.jump();
        }
    }

    private void aac() {
        if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.jump();
            this.mc.thePlayer.motionY = 0.395;
        }
        this.mc.thePlayer.motionY -= 0.002300000051036477;
    }

    private void slowlyAACTower() {
    	  BlockPos underPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
          Block underBlock = mc.theWorld.getBlockState(underPos).getBlock();
          BlockData data = getBlockData(underPos);
          if (PlayerUtil.isOnGround(0.76) && !PlayerUtil.isOnGround(0.75) && mc.thePlayer.motionY > 0.23 && mc.thePlayer.motionY < 0.25) {
              mc.thePlayer.motionY = (Math.round(mc.thePlayer.posY) - mc.thePlayer.posY);
          }
          if (PlayerUtil.isOnGround(0.0001)) {   
          
          }else if(mc.thePlayer.motionY > 0.1 && mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 0.0001 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 0.0001){
             
          	mc.thePlayer.motionY = 0;
          }
          
      	if(PlayerUtil.isMoving2()){
            if (PlayerUtil.isOnGround(0.76) && !PlayerUtil.isOnGround(0.75) && mc.thePlayer.motionY > 0.23 && mc.thePlayer.motionY < 0.25) {
                mc.thePlayer.motionY = (Math.round(mc.thePlayer.posY) - mc.thePlayer.posY);
            }
            if (PlayerUtil.isOnGround(0.0001)) {            	
                mc.thePlayer.motionY = 0.42;
                mc.thePlayer.motionX *= 0.9;
                mc.thePlayer.motionZ *= 0.9;          	
            }else if(mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 0.0001 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 0.0001){
                mc.thePlayer.motionY = 0;
            }
    	}else{
    		mc.thePlayer.motionX = 0;
    		mc.thePlayer.motionZ = 0;
    		mc.thePlayer.jumpMovementFactor = 0;
    		if (isAirBlock(underBlock) && data != null) {
                mc.thePlayer.motionY = 0.4196;
                mc.thePlayer.motionX *= 0.75;
                mc.thePlayer.motionZ *= 0.75;
            }
    	}
    		
      } 
    

    public boolean isAirBlock(Block block) {
        if (block.getMaterial().isReplaceable()) {
            if (block instanceof BlockSnow && block.getBlockBoundsMaxY() > 0.125) {
                return false;
            }
            return true;
        }

        return false;
    }
    
    
private BlockData getBlockData(BlockPos pos) {
    	
        if (isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos1 = pos.add(-1, 0, 0);
        if (isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos2 = pos.add(1, 0, 0);
        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.add(0, 0, 1);
        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.add(0, 0, -1);
        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos19 = pos.add(-2, 0, 0);
        if (isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos29 = pos.add(2, 0, 0);
        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos39 = pos.add(0, 0, 2);
        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos49 = pos.add(0, 0, -2);
        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos5 = pos.add(0, -1, 0);
        if (isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos6 = pos5.add(1, 0, 0);
        if (isPosSolid(pos6.add(0, -1, 0))) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos6.add(-1, 0, 0))) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos6.add(1, 0, 0))) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos6.add(0, 0, 1))) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos6.add(0, 0, -1))) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos7 = pos5.add(-1, 0, 0);
        if (isPosSolid(pos7.add(0, -1, 0))) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos7.add(-1, 0, 0))) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos7.add(1, 0, 0))) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos7.add(0, 0, 1))) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos7.add(0, 0, -1))) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos8 = pos5.add(0, 0, 1);
        if (isPosSolid(pos8.add(0, -1, 0))) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos8.add(-1, 0, 0))) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos8.add(1, 0, 0))) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos8.add(0, 0, 1))) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos8.add(0, 0, -1))) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos9 = pos5.add(0, 0, -1);
        if (isPosSolid(pos9.add(0, -1, 0))) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos9.add(-1, 0, 0))) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos9.add(1, 0, 0))) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos9.add(0, 0, 1))) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos9.add(0, 0, -1))) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }
  
  private boolean isPosSolid(BlockPos pos) {
      Block block = mc.theWorld.getBlockState(pos).getBlock();
      if ((block.getMaterial().isSolid() || !block.isTranslucent() || block.isFullCube() || block instanceof BlockLadder || block instanceof BlockCarpet
              || block instanceof BlockSnow || block instanceof BlockSkull)
              && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer)) {
          return true;
      }
      return false;
  }
  
  
	private void spartan() {
        double blockBelow = -2.0;
        if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.motionY = 0.41999998688697815;
        }
        if (this.mc.thePlayer.motionY < 0.0 && !(this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ).add(0.0, blockBelow, 0.0)).getBlock() instanceof BlockAir)) {
            this.mc.thePlayer.motionY = -10.0;
        }
    }

    private void ncp() {
        double blockBelow = -2.0;
        if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.motionY = 0.41999998688697815;
        }
        if (this.mc.thePlayer.motionY < 0.1 && !(this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ).add(0.0, blockBelow, 0.0)).getBlock() instanceof BlockAir)) {
            this.mc.thePlayer.motionY = -10.0;
        }
    }


}