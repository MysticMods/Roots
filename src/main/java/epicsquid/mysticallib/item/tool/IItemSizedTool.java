package epicsquid.mysticallib.item.tool;

import epicsquid.mysticallib.util.BreakUtil;
import epicsquid.mysticallib.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public interface IItemSizedTool extends IEffectiveTool, IBlacklistingTool {
  float getEfficiency();

  default float getSizedDestroySpeed(ItemStack stack, IBlockState state) {
    Material material = state.getMaterial();
    if (getEffectiveMaterials().contains(material)) {
      return this.getEfficiency();
    }
    return -999;
  }

  default boolean onSizedBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
    final Set<BlockPos> breakableBlocks = BreakUtil.nearbyBlocks(itemstack, pos, player);
    if (breakableBlocks.isEmpty()) {
      //maybeDamage(itemstack, 1, player);
      return false;
    } else {
      int count = 0;
      final World world = player.world;
      for (final BlockPos breakPos : breakableBlocks) {
        if (BreakUtil.harvestBlock(world, breakPos, player)) {
          count++;
        }
      }
      if (count > 0) {
        final int dam = Math.max(3, Util.rand.nextInt(Math.max(1, count - 3)));
        maybeDamage(itemstack, dam, player);
      }
      return false;
    }
  }

  default void maybeDamage(ItemStack stack, int amount, EntityPlayer player) {
    if (!player.isCreative()) {
      stack.damageItem(amount, player);
    }
  }
}
