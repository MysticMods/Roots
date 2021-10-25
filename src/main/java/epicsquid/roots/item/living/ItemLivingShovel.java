package epicsquid.roots.item.living;

import epicsquid.mysticallib.item.ItemShovelBase;
import epicsquid.roots.item.ILivingRepair;
import epicsquid.roots.recipe.ingredient.RootsIngredients;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class ItemLivingShovel extends ItemShovelBase implements ILivingRepair {
  public ItemLivingShovel(ToolMaterial material, String name) {
    super(material, name, 3, 192, () -> Ingredient.EMPTY);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected, 40);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public ActionResultType onItemUse(PlayerEntity player, World worldIn, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    Block block = worldIn.getBlockState(pos).getBlock();

    if (facing != Direction.DOWN && worldIn.isAirBlock(pos.up()) && (block == Blocks.GRASS || block == Blocks.DIRT)) {
      if (!worldIn.isRemote) {
        worldIn.playSound(null, pos, Blocks.GRASS_PATH.getSoundType().getStepSound(), SoundCategory.BLOCKS, 1F, 1F);
        worldIn.setBlockState(pos, Blocks.GRASS_PATH.getDefaultState());
        if (!player.capabilities.isCreativeMode) {
          stack.damageItem(1, player);
        }
      }
      return ActionResultType.SUCCESS;
    }

    return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && RootsIngredients.BARK.test(repair);
  }
}
