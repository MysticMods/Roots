package mysticmods.roots.api.ritual.condition;

import mysticmods.roots.api.registry.DescribedRegistryEntry;
import mysticmods.roots.api.blockentity.BoundedBlockEntity;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PlayerCondition extends DescribedRegistryEntry<PlayerCondition> {
  private final Type condition;

  public PlayerCondition(Type condition) {
    this.condition = condition;
  }

  @Override
  protected String getDescriptor() {
    return "player_condition";
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.RITUAL_PLAYER_CONDITION.get().getKey(this);
  }

  public boolean test(Level level, @Nullable Player player, Ritual ritual, BoundedBlockEntity pyre) {
    return condition.test(level, player, ritual, pyre);
  }

  @FunctionalInterface
  public interface Type {
    boolean test(Level level, @Nullable Player player, Ritual ritual, BoundedBlockEntity pyre);
  }
}
