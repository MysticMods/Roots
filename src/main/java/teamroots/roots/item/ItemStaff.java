package teamroots.roots.item;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.EventManager;
import teamroots.roots.RegistryManager;
import teamroots.roots.Roots;
import teamroots.roots.event.SpellEvent;
import teamroots.roots.spell.SpellBase;
import teamroots.roots.spell.SpellRegistry;
import teamroots.roots.util.Misc;

public class ItemStaff extends ItemBase {
	public ItemStaff(String name, boolean addToTab){
		super(name, addToTab);
		setMaxStackSize(1);
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		if (oldStack.hasTagCompound() && newStack.hasTagCompound()){
			return slotChanged || oldStack.getTagCompound().getInteger("selected") != newStack.getTagCompound().getInteger("selected");
		}
		return slotChanged;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){;
		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()){
			if (!stack.hasTagCompound()){
				stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("selected", 0);
				stack.getTagCompound().setString("spell0", "null");
				stack.getTagCompound().setString("spell1", "null");
				stack.getTagCompound().setString("spell2", "null");
				stack.getTagCompound().setString("spell3", "null");
			}
			stack.getTagCompound().setInteger("selected", stack.getTagCompound().getInteger("selected")+1);
			if (stack.getTagCompound().getInteger("selected") > 3){
				stack.getTagCompound().setInteger("selected", 0);
			}
			return new ActionResult<ItemStack>(EnumActionResult.PASS,player.getHeldItem(hand));
		}
		else {
			if (stack.hasTagCompound()){
				if (!stack.getTagCompound().hasKey("cooldown")){
					SpellBase spell = SpellRegistry.spellRegistry.get(stack.getTagCompound().getString("spell"+stack.getTagCompound().getInteger("selected")));
					if (spell != null){
						SpellEvent event = new SpellEvent(player,spell);
						MinecraftForge.EVENT_BUS.post(event);
						spell = event.getSpell();
						if (spell.castType == SpellBase.EnumCastType.INSTANTANEOUS){
							if (spell.costsMet(player)){
								spell.cast(player);
								spell.enactCosts(player);
								stack.getTagCompound().setInteger("cooldown", event.getCooldown());
								stack.getTagCompound().setInteger("lastCooldown", event.getCooldown());
								return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,player.getHeldItem(hand));
							}
						}
						else if (spell.castType == SpellBase.EnumCastType.CONTINUOUS){
							player.setActiveHand(hand);
							return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,stack);
						}
					}
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL,player.getHeldItem(hand));
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count){
		if (stack.hasTagCompound() && player instanceof EntityPlayer){
			if (!stack.getTagCompound().hasKey("cooldown")){
				SpellBase spell = SpellRegistry.spellRegistry.get(stack.getTagCompound().getString("spell"+stack.getTagCompound().getInteger("selected")));
				if (spell != null){
					if (spell.castType == SpellBase.EnumCastType.CONTINUOUS){
						if (spell.costsMet((EntityPlayer)player)){
							spell.cast((EntityPlayer)player);
							spell.enactTickCosts((EntityPlayer)player);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft){
		if (stack.hasTagCompound()){
			SpellBase spell = SpellRegistry.spellRegistry.get(stack.getTagCompound().getString("spell"+stack.getTagCompound().getInteger("selected")));
			if (spell != null){
				SpellEvent event = new SpellEvent((EntityPlayer)entity,spell);
				MinecraftForge.EVENT_BUS.post(event);
				if (spell.castType == SpellBase.EnumCastType.CONTINUOUS){
					stack.getTagCompound().setInteger("cooldown", event.getCooldown());
					stack.getTagCompound().setInteger("lastCooldown", event.getCooldown());
				}
			}
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected){
		if (stack.hasTagCompound()){
			if (stack.getTagCompound().hasKey("cooldown")){
				stack.getTagCompound().setInteger("cooldown", stack.getTagCompound().getInteger("cooldown")-1);
				if (stack.getTagCompound().getInteger("cooldown") <= 0){
					stack.getTagCompound().removeTag("cooldown");
					stack.getTagCompound().removeTag("lastCooldown");
				}
			}
		}
	}
	
	public static void createData(ItemStack stack, String spellName){
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("selected", 0);
			stack.getTagCompound().setString("spell0", "null");
			stack.getTagCompound().setString("spell1", "null");
			stack.getTagCompound().setString("spell2", "null");
			stack.getTagCompound().setString("spell3", "null");
		}
		stack.getTagCompound().setString("spell"+stack.getTagCompound().getInteger("selected"), spellName);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced){
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("selected", 0);
			stack.getTagCompound().setString("spell0", "null");
			stack.getTagCompound().setString("spell1", "null");
			stack.getTagCompound().setString("spell2", "null");
			stack.getTagCompound().setString("spell3", "null");
		}
		else {
			tooltip.add(I18n.format("roots.tooltip.staff.selected")+(stack.getTagCompound().getInteger("selected")+1));
			SpellBase spell = SpellRegistry.spellRegistry.get(stack.getTagCompound().getString("spell"+stack.getTagCompound().getInteger("selected")));
			if (spell != null){
				tooltip.add("");
				spell.addToolTip(tooltip);
			}
		}
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack){
		if (stack.hasTagCompound()){
			SpellBase spell = SpellRegistry.spellRegistry.get(stack.getTagCompound().getString("spell"+stack.getTagCompound().getInteger("selected")));
			if (stack.getTagCompound().hasKey("cooldown") && spell != null){
				return true;
			}
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack){
		if (stack.hasTagCompound()){
			SpellBase spell = SpellRegistry.spellRegistry.get(stack.getTagCompound().getString("spell"+stack.getTagCompound().getInteger("selected")));
			if (spell != null){
				double factor = 0.5f*(Math.sin(6.0f*Math.toRadians(EventManager.ticks+Minecraft.getMinecraft().getRenderPartialTicks()))+1.0f);
				return Misc.intColor((int)(255*(spell.red1*factor+spell.red2*(1.0-factor))), (int)(255*(spell.green1*factor+spell.green2*(1.0-factor))), (int)(255*(spell.blue1*factor+spell.blue2*(1.0-factor))));
			}
		}
		return Misc.intColor(255, 255, 255);
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack){
		if (stack.hasTagCompound()){
			if (stack.getTagCompound().hasKey("cooldown")){
				return (double)stack.getTagCompound().getInteger("cooldown")/(double)stack.getTagCompound().getInteger("lastCooldown");
			}
		}
		return 0;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack){
		return 72000;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack){
		if (stack.hasTagCompound()){
			SpellBase spell = SpellRegistry.spellRegistry.get(stack.getTagCompound().getString("spell"+stack.getTagCompound().getInteger("selected")));
			if (spell != null){
				if (spell.castType == SpellBase.EnumCastType.CONTINUOUS){
					return EnumAction.BOW;
				}
				else {
					return EnumAction.NONE;
				}
			}
		}
		return EnumAction.NONE;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void initModel(){
		ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName().toString()), new ModelResourceLocation(getRegistryName().toString()+"_1")
				, new ModelResourceLocation(Roots.MODID+":shiny_rod"), new ModelResourceLocation(Roots.MODID+":shiny_rod_1")
				, new ModelResourceLocation(Roots.MODID+":moon_rod"), new ModelResourceLocation(Roots.MODID+":moon_rod_1"));
		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition(){
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				ResourceLocation baseName = getRegistryName();
				if (stack.getDisplayName().compareToIgnoreCase("Shiny Rod") == 0){
					baseName = new ResourceLocation(Roots.MODID+":shiny_rod");
				}
				if (stack.getDisplayName().compareToIgnoreCase("Cutie Moon Rod") == 0){
					baseName = new ResourceLocation(Roots.MODID+":moon_rod");
				}
				if (stack.hasTagCompound()){
					String s = stack.getTagCompound().getString("spell"+stack.getTagCompound().getInteger("selected"));
					if (SpellRegistry.spellRegistry.containsKey(s)){
						return new ModelResourceLocation(baseName.toString()+"_1");
					}
					else {
						return new ModelResourceLocation(baseName.toString());
					}
				}
				return new ModelResourceLocation(baseName.toString());
			}
		});
	}
	
	public static class StaffColorHandler implements IItemColor {
		@Override
		public int getColorFromItemstack(ItemStack stack, int tintIndex) {
			if (stack.hasTagCompound() && stack.getItem() instanceof ItemStaff){
				if (stack.getDisplayName().compareToIgnoreCase("Shiny Rod") == 0 || stack.getDisplayName().compareToIgnoreCase("Cutie Moon Rod") == 0){
					SpellBase spell = SpellRegistry.spellRegistry.get(stack.getTagCompound().getString("spell"+stack.getTagCompound().getInteger("selected")));
					if (spell != null){
						if (tintIndex == 0){
							int r = (int)(255*spell.red1);
							int g = (int)(255*spell.green1);
							int b = (int)(255*spell.blue1);
							return (r << 16) + (g << 8) + b;
						}
						if (tintIndex == 1){
							int r = (int)(255*spell.red2);
							int g = (int)(255*spell.green2);
							int b = (int)(255*spell.blue2);
							return (r << 16) + (g << 8) + b;
						}
					}
					return Misc.intColor(255, 255, 255);
				}
				else {
					SpellBase spell = SpellRegistry.spellRegistry.get(stack.getTagCompound().getString("spell"+stack.getTagCompound().getInteger("selected")));
					if (tintIndex == 1){
						int r = (int)(255*spell.red1);
						int g = (int)(255*spell.green1);
						int b = (int)(255*spell.blue1);
						return (r << 16) + (g << 8) + b;
					}
					if (tintIndex == 2){
						int r = (int)(255*spell.red2);
						int g = (int)(255*spell.green2);
						int b = (int)(255*spell.blue2);
						return (r << 16) + (g << 8) + b;
					}
					return Misc.intColor(255, 255, 255);
				}
			}
			return Misc.intColor(255, 255, 255);
		}
	}
}
