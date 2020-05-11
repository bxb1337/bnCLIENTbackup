package net.AzureWare.mod.mods.PLAYER;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InvCleaner extends Mod {
public Value<String> Mode = new Value<String>("InvCleaner", "Mode", 0);
public Value<Boolean> Food = new Value<Boolean>("InvCleaner_Food", true);
public Value<Boolean> sort = new Value<Boolean>("InvCleaner_sort", true);
public Value<Boolean> Archery = new Value<Boolean>("InvCleaner_Archery", true);
public Value<Boolean> Sword = new Value<Boolean>("InvCleaner_Sword", true);
public Value<Boolean> InvCleaner = new Value<Boolean>("InvCleaner_InvCleaner", true);
public Value<Boolean> UHC = new Value<Boolean>("InvCleaner_UHC", false);
public Value<Double> Delay = new Value<Double>("InvCleaner_Delay", 1.0, 0.0, 10.0, 1.0);
public Value<Double> BlockCap = new Value<Double>("InvCleaner_BlockCap", 128.0, 0.0, 256.0, 8.0);

	public InvCleaner() {
		super("InvCleaner", Category.PLAYER);
		Mode.mode.add("Basic");
		Mode.mode.add("OpenInv");
	}
    public static int weaponSlot = 36, pickaxeSlot = 37, axeSlot = 38, shovelSlot = 39;
    TimeHelper timer = new TimeHelper();
    ArrayList<Integer>whitelistedItems = new ArrayList<>();

    public void onEnable() {
        super.onEnable();
    }
    @EventTarget
    public void onEvent(EventPreMotion event) {
    	if(mc.thePlayer.openContainer instanceof ContainerChest && mc.currentScreen instanceof GuiContainer)return;
        long delay = this.Delay.getValueState().longValue()*50;
        EventPreMotion em =(EventPreMotion)event;
            this.setDisplayName(this.Mode.getModeName());

            
//            if( super.isEnabled())
//            for(int type = 1; type < 5; type++){
//        		if(mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()){
//        			ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
//        			if(!isBestArmor(is, type)){
//        				return;
//        			}
//        		}else if(invContainsType(type-1)){
//        			return;
//        		}
//            }
            if(this.Mode.getModeName() == "OpenInv" && !(mc.currentScreen instanceof GuiInventory)){
            	return;
            }
            
            if(mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat){
            	if(timer.hasReached(delay) && weaponSlot >= 36){         

            		if (!mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getHasStack()){
            			getBestWeapon(weaponSlot);
                	
            		}else{
            			if(!isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())){           			
                			getBestWeapon(weaponSlot);
                		}
            		}
            	}
            	if(sort.getValueState()) {
                	if(timer.hasReached(delay) && pickaxeSlot >= 36){
                		getBestPickaxe(pickaxeSlot);
                	}
                	if(timer.hasReached(delay) && shovelSlot >= 36){
                		getBestShovel(shovelSlot);
                	}
                	if(timer.hasReached(delay) && axeSlot >= 36){
                		getBestAxe(axeSlot);
                	}
            	}

            	if(timer.hasReached(delay) && this.InvCleaner.getValueState())
                for (int i = 9; i < 45; i++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if(shouldDrop(is, i)){
                        	drop(i);
                        	timer.reset();
                        	if(delay > 0)
                        	break;
                        }
                    }
                }              
            }
    	
    }
    public void shiftClick(int slot){
    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
    }
    public void swap(int slot1, int hotbarSlot){
    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
    }
    public void drop(int slot){
    	mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
    }
    public boolean isBestWeapon(ItemStack stack){
    	float damage = getDamage(stack);
    	for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getDamage(is) > damage && (is.getItem() instanceof ItemSword || !this.Sword.getValueState()))
                	return false;
            }
        }
    	if((stack.getItem() instanceof ItemSword || !this.Sword.getValueState())){
    		return true;
    	}else{
    		return false;
    	}
    	
    }

    public void getBestWeapon(int slot){
    	for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(isBestWeapon(is) && getDamage(is) > 0 && (is.getItem() instanceof ItemSword || !this.Sword.getValueState())){
                	swap(i, slot - 36);
            		timer.reset();
                	break;
                }
            }
        }
    }
 
    private float getDamage(ItemStack stack) {
    	float damage = 0;
    	Item item = stack.getItem();
    	if(item instanceof ItemTool){
    		ItemTool tool = (ItemTool)item;
    		damage += tool.getMaxDamage();
    	}
    	if(item instanceof ItemSword){
    		ItemSword sword = (ItemSword)item;
    		damage += sword.getDamageVsEntity();
    	}
    	damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + 
    			EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
        return damage;
    }
    public boolean shouldDrop(ItemStack stack, int slot){
    	
    	if(stack.getDisplayName().contains("µã»÷")){
    		return false;
    	}
    	if(stack.getDisplayName().contains("ÓÒ¼ü")){
    		return false;
    	}
    	if(stack.getDisplayName().toLowerCase().contains("(right click)")){
    		return false;
    	}
    	if(UHC.getValueState()) {
        	if(stack.getDisplayName().toLowerCase().contains("Í·")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("apple")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("head")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("gold")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("crafting table")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("stick")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("and") && stack.getDisplayName().toLowerCase().contains("ril")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("axe of perun")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("barbarian")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("bloodlust")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("dragonchest")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("dragon sword")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("dragon armor")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("excalibur")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("exodus")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("fusion armor")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("hermes boots")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("hide of leviathan")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("scythe")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("seven-league boots")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("shoes of vidar")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("apprentice")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("master")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("vorpal")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("enchanted")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("spiked")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("tarnhelm")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("philosopher")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("anvil")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("panacea")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("fusion")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("excalibur")){
        		return false;
        	}
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	if(stack.getDisplayName().toLowerCase().contains("Ñ§Í½")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("´óÊ¦ÂÞÅÌ")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("Õ¶Ê×Ö®½£")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("¸½Ä§")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("¾ÞÁúÖ®½£")){
        		return false;
        	}
        	
        	if(stack.getDisplayName().toLowerCase().contains("¾ÞÁúÖ®¼×")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÈÐ¼×")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("Æß¹úÕ½Ñ¥")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("±ù¶·ºþ")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÕÜÈË")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÌúÕè")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("Æ»¹û")){
        		return false;
        	}
        	
        	if(stack.getDisplayName().toLowerCase().contains("½ð")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÓÀÉúÖ®¾Æ")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("Çð±ÈÌØÖ®¹­")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("¶ÍÂ¯")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("backpack")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("¾Û±äÖ®¼×")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("±³°ü")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÔÂÉñ")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÓÀÉú")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("³±Ï«")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("À×¸«")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÍõÕßÖ®½£")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("°²¶¼Èð¶û")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ËÀÉñÁ­µ¶")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("·áÈÄÖ®½Ç")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("Î¬´ïÕ½Ñ¥")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("¶á»êÖ®ÈÐ")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÂùÈËÖ®¼×")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("ÇÔÔôÖ®Ñ¥")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("hermes")){
        		return false;
        	}
        	if(stack.getDisplayName().toLowerCase().contains("barbarian")){
        		return false;
        	}
    	}


    	
    	if((slot == weaponSlot && isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) ||
    			(slot == pickaxeSlot && isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack()) && pickaxeSlot >= 0) ||
    			(slot == axeSlot && isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack()) && axeSlot >= 0) ||
    			(slot == shovelSlot && isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack()) && shovelSlot >= 0) ){
    		return false;
    	}
    	if(stack.getItem() instanceof ItemArmor){
    		for(int type = 1; type < 5; type++){
    			if(mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()){
        			ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
        			if(isBestArmor(is, type)){
        				continue;
        			}
        		}
    			if(isBestArmor(stack, type)){
    				return false;
    			}
    		}
    	}
    	if (this.BlockCap.getValueState().intValue()!=0 && stack.getItem() instanceof ItemBlock &&
    			(getBlockCount() > this.BlockCap.getValueState().intValue())){
    		return true;
    	}
    	if(stack.getItem() instanceof ItemPotion){
    		if(isBadPotion(stack)){
    			return true;
    		}
    	}
    	if(stack.getItem() instanceof ItemFood && this.Food.getValueState() && !(stack.getItem() instanceof ItemAppleGold)){
    		return true;
    	}
    	if(stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor){
    		return true;
    	}
    	if((stack.getItem() instanceof ItemBow || stack.getItem().getUnlocalizedName().contains("arrow")) && (Boolean) this.Archery.getValueState()){
    		return true;
    	}
    	
    	if(((stack.getItem().getUnlocalizedName().contains("tnt")) ||
                        (stack.getItem().getUnlocalizedName().contains("stick")) ||
                        (stack.getItem().getUnlocalizedName().contains("egg")) ||
                        (stack.getItem().getUnlocalizedName().contains("string")) ||
                        (stack.getItem().getUnlocalizedName().contains("cake")) ||
                        (stack.getItem().getUnlocalizedName().contains("mushroom")) ||
                        (stack.getItem().getUnlocalizedName().contains("flint")) ||
                        (stack.getItem().getUnlocalizedName().contains("compass")) ||
                        (stack.getItem().getUnlocalizedName().contains("dyePowder")) ||
                        (stack.getItem().getUnlocalizedName().contains("feather")) ||
                        (stack.getItem().getUnlocalizedName().contains("bucket")) ||
                        (stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect")) ||
                        (stack.getItem().getUnlocalizedName().contains("snow")) ||
                        (stack.getItem().getUnlocalizedName().contains("fish")) ||
                        (stack.getItem().getUnlocalizedName().contains("enchant")) ||
                        (stack.getItem().getUnlocalizedName().contains("exp")) ||
                        (stack.getItem().getUnlocalizedName().contains("shears")) ||
                        (stack.getItem().getUnlocalizedName().contains("anvil")) ||
                        (stack.getItem().getUnlocalizedName().contains("torch")) ||
                        (stack.getItem().getUnlocalizedName().contains("seeds")) ||
                        (stack.getItem().getUnlocalizedName().contains("leather")) ||
                        (stack.getItem().getUnlocalizedName().contains("reeds")) ||
                        (stack.getItem().getUnlocalizedName().contains("skull")) ||
                        (stack.getItem().getUnlocalizedName().contains("record")) ||
                        (stack.getItem().getUnlocalizedName().contains("snowball")) ||
                        (stack.getItem() instanceof ItemGlassBottle) ||
                        (stack.getItem().getUnlocalizedName().contains("piston")))){
    		return true;
    	}            
    	
    	return false;
    }
    public ArrayList<Integer>getWhitelistedItem(){
    	return whitelistedItems;
    }
    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock ) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }
    
    private void getBestPickaxe(int slot){
    	for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				
				if(isBestPickaxe(is) && pickaxeSlot != i){
					if(!isBestWeapon(is))
					if(!mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()){	
						swap(i, pickaxeSlot - 36);
						timer.reset();
    					if(this.Delay.getValueState().longValue() > 0)
    						return;
					}else if(!isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())){
						swap(i, pickaxeSlot - 36);
						timer.reset();
    					if(this.Delay.getValueState().longValue() > 0)
    						return;
					}
				
				}
			}
    	}
    }
    private void getBestShovel(int slot){
    	for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				
				if(isBestShovel(is) && shovelSlot != i){
					if(!isBestWeapon(is))
					if(!mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()){
						swap(i, shovelSlot - 36);
						timer.reset();
    					if(this.Delay.getValueState().longValue() > 0)
    						return;
					}else if(!isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())){	
						swap(i, shovelSlot - 36);
						timer.reset();
    					if(this.Delay.getValueState().longValue() > 0)
    						return;
					}
				
				}
			}
    	}
    }
    private void getBestAxe(int slot){

    	for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				
				if(isBestAxe(is) && axeSlot != i){
					if(!isBestWeapon(is))
					if(!mc.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()){
						swap(i, axeSlot - 36);
						timer.reset();
    					if(this.Delay.getValueState().longValue() > 0)
    						return;
					}else if(!isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())){
						swap(i, axeSlot - 36);
						timer.reset();
    					if(this.Delay.getValueState().longValue() > 0)
    						return;
					}
				
				}
			}
    	}
    }
    private boolean isBestPickaxe(ItemStack stack){
     	Item item = stack.getItem();
    	if(!(item instanceof ItemPickaxe))
    		return false;
    	float value = getToolEffect(stack);
    	for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe){                	
                	return false;
                }
                	
            }
        }
    	return true;
    }
    private boolean isBestShovel(ItemStack stack){
    	Item item = stack.getItem();
    	if(!(item instanceof ItemSpade))
    		return false;
    	float value = getToolEffect(stack);
    	for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getToolEffect(is) > value && is.getItem() instanceof ItemSpade){                	
                	return false;
                }
                	
            }
        }
    	return true;
    }
    private boolean isBestAxe(ItemStack stack){
    	Item item = stack.getItem();
    	if(!(item instanceof ItemAxe))
    		return false;
    	float value = getToolEffect(stack);
    	for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)){                
                	return false;
                }
                	
            }
        }
    	return true;
    }
    private float getToolEffect(ItemStack stack){
    	Item item = stack.getItem();
    	if(!(item instanceof ItemTool))
    		return 0;
    	String name = item.getUnlocalizedName();
    	ItemTool tool = (ItemTool)item;
    	float value = 1;
    	if(item instanceof ItemPickaxe){
    		value = tool.getStrVsBlock(stack, Blocks.stone);
    		if(name.toLowerCase().contains("gold")){
    			value -= 5;
    		}
    	}else if(item instanceof ItemSpade){
    		value = tool.getStrVsBlock(stack, Blocks.dirt);
    		if(name.toLowerCase().contains("gold")){
    			value -= 5;
    		}
    	}else if(item instanceof ItemAxe){
    		value = tool.getStrVsBlock(stack, Blocks.log);
    		if(name.toLowerCase().contains("gold")){
    			value -= 5;
    		}
    	}else
    		return 1f;
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D;
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)/100d;
    	return value;
    }
    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if(potion.getEffects(stack) == null)
            	return true;
            for (final Object o : potion.getEffects(stack)) {
                final PotionEffect effect = (PotionEffect) o;
                if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                	return true;
                }
            }
        }
        return false;
    }
    boolean invContainsType(int type){
    	
    	for(int i = 9; i < 45; i++){
    		if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				Item item = is.getItem();
				if(item instanceof ItemArmor){
					ItemArmor armor = (ItemArmor)item;
					if(type == armor.armorType){
						return true;
					}	
				}
    		}
    	}
    	return false;
    }
    public static boolean isBestArmor(ItemStack stack, int type){
    	float prot = getProtection(stack);
    	String strType = "";
    	if(type == 1){
    		strType = "helmet";
    	}else if(type == 2){
    		strType = "chestplate";
    	}else if(type == 3){
    		strType = "leggings";
    	}else if(type == 4){
    		strType = "boots";
    	}
    	if(!stack.getUnlocalizedName().contains(strType)){
    		return false;
    	}
    	for (int i = 5; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
                	return false;
            }
        }
    	return true;
    }
    public static float getProtection(ItemStack stack){
    	float prot = 0;
    	if ((stack.getItem() instanceof ItemArmor)) {
    		ItemArmor armor = (ItemArmor)stack.getItem();
    		prot += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075D;
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)/100d;
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)/100d;
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack)/100d;
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)/50d;   	
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack)/100d;   	
    	}
	    return prot;
    }
}
