package mysticmods.roots.api.ritual;

import mysticmods.roots.api.RitualLike;
import mysticmods.roots.api.modifier.Modifier;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public interface IRitualInstance extends RitualLike {
  Ritual getRitual();

  default MutableComponent getName() {
    return getRitual().getName();
  }

  int getLifetime();

  default int getDuration() {
    return getRitual().getDuration();
  }

  Set<Modifier> getEnabledModifiers();

  default boolean hasModifier(Modifier modifier) {
    return getEnabledModifiers().contains(modifier);
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
    public Set<Modifier> getEnabledModifiers() {
      return Collections.emptySet();
    }
  }
}
