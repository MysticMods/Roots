package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.DescribedEntry;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PlayerCondition extends DescribedEntry {
  public static Codec<PlayerCondition> CODEC = RootsRegistries.PLAYER_CONDITIONS.byNameCodec();

  private final Type condition;

  private final Holder.Reference<PlayerCondition> builtInRegistryHolder =  RootsRegistries.PLAYER_CONDITIONS.createIntrusiveHolder(this);

  public PlayerCondition(Type condition) {
    this.condition = condition;
  }

  @Override
  protected String getDescriptor() {
    return "player_condition";
  }

  public Holder.Reference<PlayerCondition> builtInRegistryHolder() {
    return builtInRegistryHolder;
  }

  public Type getCondition() {
    return condition;
  }

  @Override
  public ResourceLocation getKey() {
    return builtInRegistryHolder().getKey().location();
  }

  public boolean test(Level level, @Nullable Player player) {
    return condition.test(level, player);
  }

  @FunctionalInterface
  public interface Type {
    boolean test(Level level, @Nullable Player player);
  }
}
