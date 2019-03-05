package epicsquid.roots.item;

import javax.annotation.Nonnull;

import com.google.common.collect.Sets;

import epicsquid.mysticallib.item.ItemToolBase;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicCarvingRecipe;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
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
      if (!offhand.isEmpty() && HerbRegistry.containsHerbItem(offhand.getItem())) {
        RunicCarvingRecipe recipe = ModRecipes.getRunicCarvingRecipe(world.getBlockState(pos), HerbRegistry.getHerbByItem(offhand.getItem()));
        if (recipe != null) {
          world.setBlockState(pos, recipe.getRuneBlock());

          if (!player.isCreative()) {
            player.getHeldItemOffhand().shrink(1);
            player.getHeldItemMainhand().damageItem(1, player);
          }

          return EnumActionResult.SUCCESS;
        }
      } else {
        // Used to get terramoss from a block of cobble. This can also be done using runic shears.
        IBlockState block = world.getBlockState(pos);
        if (block.getBlock() == Blocks.MOSSY_COBBLESTONE) {
          if (!world.isRemote) {
            world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
            world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), new ItemStack(ModItems.terra_moss)));
            player.getHeldItem(hand).damageItem(1, player);
          }
          world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
        }
      }
    }
    return EnumActionResult.PASS;
  }
}