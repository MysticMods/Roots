package mysticmods.roots.growth.drops;

import mysticmods.roots.api.growth.GetDropsFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public record GenerateCropsDrop () implements GetDropsFunction {
  @Override
  public List<ItemStack> getDrops(Level level, BlockPos blockPos, BlockState blockState, Item seedItem, @Nullable IntegerProperty ageProperty, int maximumAge, @Nullable LivingEntity entity) {
    return Collections.emptyList();
  }
}
