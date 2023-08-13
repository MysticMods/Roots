package mysticmods.roots.api.spell;

import mysticmods.roots.api.SpellLike;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.registry.ICostedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.registry.StyledRegistryEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public abstract class Spell extends StyledRegistryEntry<Spell> implements ICostedRegistryEntry, SpellLike {
  protected final Type type;
  protected final List<Cost> costs = new ArrayList<>();
  protected final Set<Modifier> modifiers = new HashSet<>();
  protected int cooldown = 0;
  protected double reach = 0.0;
  protected final int color1, color2;

  public Spell(Type type, ChatFormatting color, List<Cost> costs, int color1, int color2) {
    this.type = type;
    this.color = color;
    setCosts(costs);
    this.color1 = color1;
    this.color2 = color2;
  }

  public int getColor1() {
    return color1;
  }

  public int getColor2() {
    return color2;
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

  public abstract SpellProperty<Integer> getCooldownProperty();

  public SpellProperty<Double> getReachProperty () {
    return null;
  }

  public int getCooldown() {
    return cooldown;
  }

  public Type getType() {
    return type;
  }

  public void addModifier(Modifier modifier) {
    modifiers.add(modifier);
  }

  protected void initializeProperties() {
    SpellProperty<Integer> cooldownProperty = getCooldownProperty();
    if (cooldownProperty != null) {
      this.cooldown = cooldownProperty.getValue();
    } else {
      throw new IllegalStateException("Spell " + this + " has no cooldown property!");
    }
    SpellProperty<Double> reachProperty = getReachProperty();
    if (reachProperty != null) {
      this.reach = reachProperty.getValue();
    }
  }

  public abstract void initialize();

  public void init() {
    initializeProperties();
    initialize();
  }

  public abstract void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks);

  // TODO: How to handle reach
  protected double getRange (Player pPlayer) {
    return pPlayer.getReachDistance() + reach;
  }

  protected BlockHitResult pick (Player pPlayer, double range) {
    return pick(pPlayer, range, false);
  }

  protected BlockHitResult pick (Player pPlayer, double range, boolean fluids) {
    return (BlockHitResult) pPlayer.pick(range, 1f, fluids);
  }

  protected BlockHitResult pick (Player pPlayer) {
    return pick(pPlayer, false);
  }

  protected BlockHitResult pick (Player pPlayer, boolean fluids) {
    return (BlockHitResult) pPlayer.pick(getRange(pPlayer), 1f, fluids);
  }

  // TODO: Pick entity

  @Override
  protected String getDescriptor() {
    return "spell";
  }

  public boolean is(ResourceLocation key) {
    return Registries.SPELL_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }

  public boolean is(ResourceKey<Spell> key) {
    return Registries.SPELL_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }

  public boolean is(Predicate<ResourceKey<Spell>> key) {
    return Registries.SPELL_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }

  public boolean is(TagKey<Spell> key) {
    return Registries.SPELL_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.SPELL_REGISTRY.get().getKey(this);
  }

  @Override
  public boolean isBold() {
    return true;
  }

  @Override
  public Spell getAsSpell() {
    return this;
  }

  public enum Type {
    INSTANT,
    CONTINUOUS
  }
}
