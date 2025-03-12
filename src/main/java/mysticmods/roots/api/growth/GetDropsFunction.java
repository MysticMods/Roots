package mysticmods.roots.api.growth;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface GetDropsFunction {
  List<ItemStack> getDrops(Level level, BlockPos blockPos, BlockState blockState, Item seedItem, @Nullable IntegerProperty ageProperty, int maximumAge, @Nullable LivingEntity entity);
}
