package mysticmods.roots.api.ritual;

import mysticmods.roots.api.RitualLike;
import mysticmods.roots.api.modifier.RitualModifier;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

// TODO: Actually use this
public interface IRitualInstance extends RitualLike {
  Ritual getRitual();

  default MutableComponent getName() {
    return getRitual().getName();
  }

  int getLifetime();

  default int getDuration() {
    return getRitual().getDuration();
  }

  Set<RitualModifier> getEnabledModifiers();

  default boolean hasModifier(RitualModifier modifier) {
    return getEnabledModifiers().contains(modifier);
  }

  default boolean hasModifier(Holder<RitualModifier> modifier) {
    return getEnabledModifiers().contains(modifier.value());
  }

  @Nullable
  default RitualInstanceData getRitualData() {
    return null;
  }

  @Override
  default Ritual asRitual() {
    return getRitual();
  }

  default boolean isEmpty() {
    return false;
  }

  static IRitualInstance.SimpleRitual of(Ritual spell) {
    return new IRitualInstance.SimpleRitual(spell);
  }

  record SimpleRitual(Ritual ritual) implements IRitualInstance {
    @Override
    public Ritual getRitual() {
      return ritual();
    }

    @Override
    public int getLifetime() {
      return getDuration();
    }

    @Override
    public Set<RitualModifier> getEnabledModifiers() {
      return Collections.emptySet();
    }

    @Override
    public boolean hasModifier(RitualModifier modifier) {
      return false;
    }

    @Override
    public boolean hasModifier(Holder<RitualModifier> modifier) {
      return false;
    }
  }
}
