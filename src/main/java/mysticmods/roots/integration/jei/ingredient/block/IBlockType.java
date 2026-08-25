package mysticmods.roots.integration.jei.ingredient.block;

import mysticmods.roots.util.GrowthUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IBlockType {
  ItemStack stack();

  BlockState state();

  BlockState renderState();

  default Block block() {
    return state().getBlock();
  }

  default List<Component> additionalTooltipLines() {
    return List.of();
  }

  @Nullable
  static BlockState getGrownState(BlockState state) {
    var record = GrowthUtil.getGrowthRecord(state);
    if (record == null) {
      return null;
    }
    if (record.ageProperty().isEmpty()) {
      return null;
    }
    var property = record.ageProperty().get();
    if (!state.hasProperty(property)) {
      return null;
    }
    return state.setValue(property, record.maximumAge());
  }
}
