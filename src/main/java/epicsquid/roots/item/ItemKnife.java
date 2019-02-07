package epicsquid.roots.item;

import javax.annotation.Nonnull;

import com.google.common.collect.Sets;

import epicsquid.mysticallib.item.ItemToolBase;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicCarvingRecipe;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemKnife extends ItemToolBase {

  public ItemKnife(String name, ToolMaterial material) {
    super(name, 2.0f, -1.6f, material, Sets.newHashSet(Blocks.PLANKS, Blocks.LOG, Blocks.LOG2));
  }

  @Override
  public float getDestroySpeed(ItemStack stack, IBlockState state) {
    if (state.getBlock() instanceof BlockLog) {
      return this.efficiency;
    }
    return super.getDestroySpeed(stack, state);
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    stack.damageItem(1, attacker);
    return true;
  }

  @Override
  public boolean canHarvestBlock(IBlockState block) {
    if (block.getBlock() instanceof BlockLog) {
      return false;
    }
    return true;
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {
    return super.onBlockDestroyed(stack, world, state, pos, entity);
  }

  @Override
  @Nonnull
  public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (hand == EnumHand.MAIN_HAND) {
      ItemStack offhand = player.getHeldItemOffhand();
      if (!offhand.isEmpty() && offhand.getItem() instanceof ItemPouch) {
        RunicCarvingRecipe recipe = ModRecipes.getRunicCarvingRecipe(world.getBlockState(pos), ItemPouch.getHerb(offhand));
        if (recipe != null) {
          world.setBlockState(pos, recipe.getRuneBlock());
          String plantName = ItemPouch.getPlantName(offhand);

          if (!player.isCreative()) {
            ItemPouch.setQuantity(offhand, plantName, ItemPouch.getQuantity(offhand, plantName) - 1);
            player.getHeldItemMainhand().damageItem(1, player);
          }

          return EnumActionResult.SUCCESS;
        }
      }
    }
    return EnumActionResult.PASS;
  }
}