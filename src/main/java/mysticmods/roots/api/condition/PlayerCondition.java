package mysticmods.roots.api.condition;

import mysticmods.roots.api.registry.DescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
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
    return Registries.PLAYER_CONDITION_REGISTRY.get().getKey(this);
  }

  public boolean test(Level level, @Nullable Player player) {
    return condition.test(level, player);
  }

  @FunctionalInterface
  public interface Type {
    boolean test(Level level, @Nullable Player player);
  }
}
