package epicsquid.roots.item.terrastone;

import epicsquid.mysticallib.item.ItemHoeBase;
import epicsquid.roots.config.ToolConfig;
import epicsquid.roots.item.ILivingRepair;
import epicsquid.roots.recipe.ingredient.RootsIngredients;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTerrastoneHoe extends ItemHoeBase implements ILivingRepair {
	public ItemTerrastoneHoe(ToolMaterial material, String name) {
		super(material, name, 3, 565, () -> Ingredient.EMPTY);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		update(stack, worldIn, entityIn, itemSlot, isSelected, 20);
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);
		
		if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
			return EnumActionResult.FAIL;
		} else {
			int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
			if (hook != 0) return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
			
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			IBlockState farmland;
			if (ToolConfig.HoeMoisturises) {
				farmland = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7);
			} else {
				farmland = Blocks.FARMLAND.getDefaultState();
			}
			
			if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
				if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
					this.setBlock(itemstack, player, worldIn, pos, farmland);
					return EnumActionResult.SUCCESS;
				}
				
				if (block == Blocks.DIRT) {
					switch (iblockstate.getValue(BlockDirt.VARIANT)) {
						case DIRT:
							this.setBlock(itemstack, player, worldIn, pos, farmland);
							return EnumActionResult.SUCCESS;
						case COARSE_DIRT:
							this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
							return EnumActionResult.SUCCESS;
					}
				}
			}
			
			return EnumActionResult.PASS;
		}
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		if (!worldIn.isRemote) {
			stack.damageItem(1, entityLiving);
		}
		
		final Block block = state.getBlock();
		final Material material = state.getMaterial();
		if (block instanceof net.minecraftforge.common.IShearable) return true;
		return material == Material.LEAVES || material == Material.VINE || super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, net.minecraft.entity.player.EntityPlayer player) {
		if (player.world.isRemote || player.capabilities.isCreativeMode) {
			return false;
		}
		if (!ToolConfig.HoeSilkTouch) {
			return super.onBlockStartBreak(itemstack, pos, player);
		}
		final IBlockState state = player.world.getBlockState(pos);
		final Block block = state.getBlock();
		final Material material = state.getMaterial();
		if (block instanceof net.minecraftforge.common.IShearable && (material == Material.VINE || material == Material.LEAVES)) {
			net.minecraftforge.common.IShearable target = (net.minecraftforge.common.IShearable) block;
			if (target.isShearable(itemstack, player.world, pos)) {
				java.util.List<ItemStack> drops = target.onSheared(itemstack, player.world, pos,
						net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, itemstack));
				java.util.Random rand = new java.util.Random();
				
				for (ItemStack stack : drops) {
					float f = 0.7F;
					double d = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
					double d1 = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
					double d2 = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
					net.minecraft.entity.item.EntityItem entityitem = new net.minecraft.entity.item.EntityItem(player.world, (double) pos.getX() + d, (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
					entityitem.setDefaultPickupDelay();
					player.world.spawnEntity(entityitem);
				}
				
				itemstack.damageItem(1, player);
				player.addStat(net.minecraft.stats.StatList.getBlockStats(block));
				player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11); //TODO: Move to IShearable implementors in 1.12+
				return true;
			}
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("");
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return toRepair.getItem() == this && RootsIngredients.MOSSY_COBBLESTONE.test(repair);
	}
}
