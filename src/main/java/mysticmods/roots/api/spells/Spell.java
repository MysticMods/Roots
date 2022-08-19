package mysticmods.roots.api.spells;

import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.IHasCost;
import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Spell extends DescribedRegistryEntry<Spell> implements IHasCost {
  protected final Type type;
  protected final List<Cost> costs = new ArrayList<>();
  protected final Set<Modifier> modifiers = new HashSet<>();
  protected final int cooldown;
  protected final int color1;
  protected final int color2;

  public Spell(Type type, List<Cost> costs, int cooldown, int color1, int color2) {
    this.type = type;
    setCosts(costs);
    this.cooldown = cooldown;
    this.color1 = color1;
    this.color2 = color2;
  }

  @Override
  public List<Cost> getCosts() {
    return costs;
  }

  @Override
  public void setCosts(List<Cost> costs) {
    this.costs.clear();
    this.costs.addAll(costs);
  }

  public Set<Modifier> getModifiers() {
    return modifiers;
  }

  public int getCooldown() {
    return cooldown;
  }

  public int getColor1() {
    return color1;
  }

  public int getColor2() {
    return color2;
  }

  public Type getType() {
    return type;
  }

  public void addModifier(Modifier modifier) {
    modifiers.add(modifier);
  }

  public void initialize() {
  }

  public abstract void cast(Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks);

  @Override
  protected String getDescriptor() {
    return "spell";
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.SPELL_REGISTRY.get().getKey(this);
  }

  public enum Type {
    INSTANT,
    CONTINUOUS
  }
}
