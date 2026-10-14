package mysticmods.roots.ritual;

import mysticmods.roots.api.modifier.RitualModifierSet;
import mysticmods.roots.api.ritual.IRitualInstance;
import mysticmods.roots.api.ritual.Ritual;

import java.util.UUID;

public class RitualInstance implements IRitualInstance.Identified {
  private final Ritual ritual;
  private final UUID ritualId;
  private int lifetime;
  private RitualModifierSet modifiers = RitualModifierSet.EMPTY;

  public RitualInstance(Ritual ritual) {
    this.ritual = ritual;
    this.ritualId = UUID.randomUUID();
    this.lifetime = this.getDuration();
  }

  @Override
  public Ritual getRitual() {
    return ritual;
  }

  @Override
  public int getLifetime() {
    return lifetime;
  }

  @Override
  public void tickLifetime() {
    this.lifetime--;
  }

  @Override
  public RitualModifierSet getEnabledModifiers() {
    return modifiers;
  }

  @Override
  public void setEnabledModifiers(RitualModifierSet modifiers) {
    this.modifiers = modifiers;
  }

  @Override
  public void setLifetime(int value) {
    this.lifetime = value;
  }

  @Override
  public UUID getId() {
    return ritualId;
  }
}
