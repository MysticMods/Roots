package epicsquid.roots.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateUtil {
  public static Map<Block, List<IProperty<?>>> skipProperties = new HashMap<>();

  public static void ignoreState(Block state, IProperty<?> property) {
    List<IProperty<?>> thisProperty = skipProperties.computeIfAbsent(state, (block) -> new ArrayList<>());
    thisProperty.add(property);
  }

  /**
   * @param state1 Reference state
   * @param state2 State we're comparing to
   * @return Returns true if the states are of the same block, and state2 contains
   * all of the same property keys as state2, and that the values of these
   * properties match.
   */
  public static boolean compareStates(IBlockState state1, IBlockState state2) {
    // This should cover most options
    if (state1.getBlock() != state2.getBlock()) return false;

    ImmutableMap<IProperty<?>, Comparable<?>> state1_props = state1.getProperties();
    ImmutableMap<IProperty<?>, Comparable<?>> state2_props = state2.getProperties();

    if (state1_props.size() < state2_props.size()) return false;

    List<IProperty<?>> skips = skipProperties.computeIfAbsent(state1.getBlock(), (block) -> new ArrayList<>());

    for (Map.Entry<IProperty<?>, Comparable<?>> entry : state1_props.entrySet()) {
      if (skips.contains(entry.getKey())) continue;

      if (!state2_props.containsKey(entry.getKey())) return false;

      Comparable<?> comp2 = state2_props.get(entry.getKey());

      if (entry.getKey().getValueClass().cast(entry.getValue()) != entry.getKey().getValueClass().cast(comp2))
        return false;
    }

    return true;
  }
}
