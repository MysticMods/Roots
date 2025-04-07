package mysticmods.roots.growth.harvest;

import mysticmods.roots.api.growth.HarvestFunction;
import mysticmods.roots.util.HarvestUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record HarvestGlowBerries() implements HarvestFunction {
  @Override
  public void harvest(Level level, BlockPos pos, BlockState state, LivingEntity entity, @Nullable IntegerProperty ageProperty, int maximumAge, @Nullable Item seedItem) {
    if (state.hasProperty(CaveVines.BERRIES)) {
      List<ItemStack> stacks = Block.getDrops(state, (ServerLevel) level, pos, null);
      level.setBlock(pos, state.setValue(CaveVines.BERRIES, false), 3);
      double d0 = (double) EntityType.ITEM.getHeight() / 2.0;
      double d1 = (double)pos.getX() + 0.5 + Mth.nextDouble(level.random, -0.25, 0.25);
      double d2 = (double)pos.getY() + 0.5 + Mth.nextDouble(level.random, -0.25, 0.25) - d0;
      double d3 = (double)pos.getZ() + 0.5 + Mth.nextDouble(level.random, -0.25, 0.25);
      for (ItemStack stack : stacks) {
        ItemEntity itemEntity = new ItemEntity(level, d1, d2, d3, stack);
        itemEntity.setDefaultPickUpDelay();
        if (!HarvestUtil.capture(itemEntity)) {
          level.addFreshEntity(itemEntity);
        }
      }
    }
  }
}
