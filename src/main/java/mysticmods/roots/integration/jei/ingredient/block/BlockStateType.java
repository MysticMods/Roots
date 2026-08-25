package mysticmods.roots.integration.jei.ingredient.block;

import mysticmods.roots.api.test.world.PartialBlockState;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record BlockStateType(PartialBlockState partial, BlockState state, @Nullable BlockState grownState,
                             ItemStack stack) implements IBlockType {
  public BlockStateType(PartialBlockState state) {
    this(state, state.build(), IBlockType.getGrownState(state.build()), new ItemStack(state.block()));
  }

  public BlockStateType(BlockState state) {
    this(new PartialBlockState(state), state, IBlockType.getGrownState(state), new ItemStack(state.getBlock()));
  }

  private Component getPropertyValueString(Map.Entry<Property<?>, Comparable<?>> entry) {
    Property<?> property = entry.getKey();
    Comparable<?> comparable = entry.getValue();
    String s = Util.getPropertyName(property, comparable);
    if (Boolean.TRUE.equals(comparable)) {
      s = ChatFormatting.GREEN + s;
    } else if (Boolean.FALSE.equals(comparable)) {
      s = ChatFormatting.RED + s;
    }

    return Component.literal(property.getName() + ": " + s);
  }

  @Override
  public BlockState renderState() {
    if (grownState != null) {
      return grownState;
    }
    return state;
  }

  @Override
  public List<Component> additionalTooltipLines() {
    List<Component> tooltip = new ArrayList<>();
    for (Map.Entry<Property<?>, Comparable<?>> v : state.getValues().entrySet()) {
      if (partial.getProperties().contains(v.getKey().getName())) {
        tooltip.add(getPropertyValueString(v));
      }
    }
    return tooltip;
  }
}
