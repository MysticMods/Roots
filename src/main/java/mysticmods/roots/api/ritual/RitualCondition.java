package mysticmods.roots.api.ritual;

import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.blockentity.BoundedBlockEntity;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class RitualCondition extends DescribedRegistryEntry<RitualCondition> {
  public RitualCondition() {
  }

  @Override
  protected String getDescriptor() {
    return "ritual_condition";
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.RITUAL_CONDITION_REGISTRY.get().getKey(this);
  }

  public static class LevelRitualCondition extends RitualCondition {
    private final RitualConditionTypes.LevelConditionType condition;

    public LevelRitualCondition(RitualConditionTypes.LevelConditionType condition) {
      super();
      this.condition = condition;
    }

    // TODO: Mutation of completedPositions but also return value
    public Set<BlockPos> test(/* IMMUTABLE */ Set<BlockPos> completedPositions, Level level, @Nullable Player player, Ritual ritual, BoundedBlockEntity pyre) {
      BoundingBox bounds = pyre.getBoundingBox();
      Set<BlockPos> newCompletedPositions = new HashSet<>();
      if (bounds != null) {
        BlockPos.betweenClosedStream(pyre.getBoundingBox()).forEach(mPos -> {
          BlockPos pos = mPos.immutable();
          if (completedPositions.contains(pos) || newCompletedPositions.contains(pos)) {
            return;
          }

          newCompletedPositions.addAll(condition.test(pos, level, player, ritual, pyre));
          newCompletedPositions.add(pos);
        });
      }
      return newCompletedPositions;
    }
  }

  public static class PlayerRitualCondition extends RitualCondition {
    private final RitualConditionTypes.PlayerConditionType condition;

    public PlayerRitualCondition(RitualConditionTypes.PlayerConditionType condition) {
      super();
      this.condition = condition;
    }

    public boolean test(Level level, @Nullable Player player, Ritual ritual, BoundedBlockEntity pyre) {
      return condition.test(level, player, ritual, pyre);
    }
  }
}
