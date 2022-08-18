package mysticmods.roots.api.ritual;

import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class RitualCondition extends DescribedRegistryEntry<RitualCondition> implements RitualConditionTypes.ConditionType {
  private final RitualConditionTypes.ConditionType condition;

  public RitualCondition(RitualConditionTypes.ConditionType condition) {
    this.condition = condition;
  }

  @Override
  protected String getDescriptor() {
    return "ritual_condition";
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.RITUAL_CONDITION_REGISTRY.get().getKey(this);
  }

  // TODO: This should be implemented as an area-wide thing, not a single block
  @Override
  public Set<BlockPos> test(BlockPos pos, Level level, @Nullable Player player, Ritual ritual, BlockEntity pyre) {
    return condition.test(pos, level, player, ritual, pyre);
  }
}
