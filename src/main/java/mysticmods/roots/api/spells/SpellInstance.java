package mysticmods.roots.api.spells;

import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class SpellInstance {
  private final Spell spell;
  private final Set<Modifier> enabledModifiers = new HashSet<>();
  private long cooldown;

  public SpellInstance(Spell spell) {
    this.spell = spell;
  }

  public SpellInstance(Spell spell, Modifier... modifiers) {
    this(spell, Arrays.asList(modifiers));
  }

  public SpellInstance (Spell spell, Collection<Modifier> modifiers) {
    this(spell);
    enabledModifiers.addAll(modifiers);
  }

  protected SpellInstance(CompoundTag tag) {
    spell = Registries.SPELL_REGISTRY.get().getValue(new ResourceLocation(tag.getString("spell")));
    ListTag modifiers = tag.getList("modifiers", 8);
    for (int i = 0; i < modifiers.size(); i++) {
      enabledModifiers.add(Registries.MODIFIER_REGISTRY.get().getValue(new ResourceLocation(modifiers.getString(i))));
    }
    cooldown = tag.getLong("cooldown");
  }

  public Spell getSpell() {
    return spell;
  }

  public Set<Modifier> getEnabledModifiers() {
    return enabledModifiers;
  }

  public long getCooldown() {
    return cooldown;
  }

  public void setCooldown(long cooldown) {
    this.cooldown = cooldown;
  }

  public boolean hasModifier(Modifier modifier) {
    return enabledModifiers.contains(modifier);
  }

  public CompoundTag toNBT() {
    CompoundTag result = new CompoundTag();
    result.putString("spell", spell.getKey().toString());
    ListTag modifiers = new ListTag();
    enabledModifiers.forEach(o -> modifiers.add(StringTag.valueOf(o.getKey().toString())));
    result.put("modifiers", modifiers);
    result.putLong("cooldown", cooldown);
    return result;
  }

  public static SpellInstance fromNBT(CompoundTag nbt) {
    return new SpellInstance(nbt);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SpellInstance that = (SpellInstance) o;

    if (cooldown != that.cooldown) return false;
    if (!spell.equals(that.spell)) return false;
    return enabledModifiers.equals(that.enabledModifiers);
  }

  @Override
  public int hashCode() {
    int result = spell.hashCode();
    result = 31 * result + enabledModifiers.hashCode();
    result = 31 * result + (int) (cooldown ^ (cooldown >>> 32));
    return result;
  }
}
