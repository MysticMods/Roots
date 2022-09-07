package mysticmods.roots.api.spell;

import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SpellInstance {
  private final Spell spell;
  private final Set<Modifier> enabledModifiers = new HashSet<>();
  private int cooldown;

  public SpellInstance(Spell spell) {
    this.spell = spell;
  }

  public SpellInstance(Spell spell, Modifier... modifiers) {
    this(spell, Arrays.asList(modifiers));
  }

  public SpellInstance(Spell spell, Collection<Modifier> modifiers) {
    this(spell);
    enabledModifiers.addAll(modifiers);
  }

  protected SpellInstance(CompoundTag tag) {
    spell = Registries.SPELL_REGISTRY.get().getValue(new ResourceLocation(tag.getString("spell")));
    ListTag modifiers = tag.getList("modifiers", 8);
    for (int i = 0; i < modifiers.size(); i++) {
      enabledModifiers.add(Registries.MODIFIER_REGISTRY.get().getValue(new ResourceLocation(modifiers.getString(i))));
    }
    cooldown = tag.getInt("cooldown");
  }

  public Spell getSpell() {
    return spell;
  }

  public Set<Modifier> getEnabledModifiers() {
    return enabledModifiers;
  }

  public int getCooldown() {
    return cooldown;
  }

  public void setCooldown(int cooldown) {
    this.cooldown = cooldown;
  }

  public void setCooldown(Player pPlayer) {
    if (getMaxCooldown() > 0) {
      setCooldown(pPlayer.tickCount + getMaxCooldown());
    }
  }

  public boolean hasModifier(Modifier modifier) {
    return enabledModifiers.contains(modifier);
  }

  public Spell.Type getType() {
    return spell.getType();
  }

  // TODO: handle making sure modifiers are correct for this spell
  public void addModifier(Modifier modifier) {
    if (modifier.getSpell() != this.getSpell()) {
      throw new IllegalStateException("Tried to add a modifier to SpellInstance for '" + this.getSpell() + "' but the modifier '" + modifier + "' is for '" + modifier.getSpell() + "'");
    }
    enabledModifiers.add(modifier);
  }

  public void removeModifier(Modifier modifier) {
    enabledModifiers.remove(modifier);
  }

  public int getMaxCooldown() {
    return spell.getCooldown();
  }

  // TODO:
  // returns:
  //   true - the spell can be cast
  //   false - the spell cannot be cast
  public boolean canCast(Player pCaster) {
    if (getMaxCooldown() < 0) {
      return true;
    }

    int diff = pCaster.tickCount - getCooldown();
    return diff <= 0 || Math.abs(diff) > getMaxCooldown();
  }

  public void cast(Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, int ticks) {
    spell.cast(pPlayer, pStack, pHand, costs, this, ticks);
  }

  public CompoundTag toNBT() {
    CompoundTag result = new CompoundTag();
    return toNBT(result);
  }

  public CompoundTag toNBT(CompoundTag result) {
    result.putString("spell", spell.getKey().toString());
    ListTag modifiers = new ListTag();
    enabledModifiers.forEach(o -> modifiers.add(StringTag.valueOf(o.getKey().toString())));
    result.put("modifiers", modifiers);
    result.putInt("cooldown", cooldown);
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
    result = 31 * result + (cooldown ^ (cooldown >>> 32));
    return result;
  }
}
