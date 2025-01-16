package mysticmods.roots.api.spell;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.registry.*;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class SpellModifier implements IDescribed, ICosted, IParentChild<SpellModifier> {
  @Nullable
  protected final Holder<SpellModifier> parent;
  protected final Set<SpellModifier> children = new HashSet<>();
  protected final Holder<Spell> spell;
  protected final List<Cost> defaultCosts;
  protected List<Cost> costs;
  private String descriptionId;

  // Modifier with parent
  public SpellModifier(@Nullable Holder<SpellModifier> parent, Holder<Spell> spell, List<Cost> defaultCosts) {
    this.spell = spell;
    this.parent = parent;
    this.defaultCosts = defaultCosts;
  }

  // Modifier with no parent
  public SpellModifier(Holder<Spell> spell, List<Cost> defaultCosts) {
    this(null, spell, defaultCosts);
  }

  @Override
  public List<Cost> getDefaultCosts() {
    return defaultCosts;
  }

  @Override
  public List<Cost> getCosts() {
    return costs == null ? defaultCosts : costs;
  }

  public Holder<Spell> getSpell() {
    return spell;
  }

  public void init(Holder<SpellModifier> holder) {
    Spell parent = holder.getData(DataMaps.SPELL_MODIFIER_SPELL);
    if (parent == null) {
      RootsAPI.LOG.error("SpellModifier {} has no parent spell!", holder.getKey());
    } else if (parent != this.spell.value()) {
      RootsAPI.LOG.error("SpellModifier {} has a parent spell that is not the same as the spell it is attached to!", holder.getKey());
    } else {
      spell.value().addModifier(this);
    }
    SpellModifier modifierParent = holder.getData(DataMaps.SPELL_MODIFIER_PARENT);
    if (modifierParent == null) {
      // NOP
    } else if (modifierParent != this.parent) {
      RootsAPI.LOG.error("SpellModifier {} has a parent modifier that is not the same as the parent it was constructed with!", holder.getKey());
    } else {
      modifierParent.addChild(this);
    }
    this.costs = holder.getData(DataMaps.SPELL_MODIFIER_COST_DATA);
  }

  public Holder<SpellModifier> builtInRegistryHolder() {
    return RootsRegistries.SPELL_MODIFIERS.wrapAsHolder(this);
  }

  public boolean is(ResourceLocation key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(ResourceKey<SpellModifier> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<SpellModifier>> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(TagKey<SpellModifier> key) {
    return builtInRegistryHolder().is(key);
  }

  @Override
  @Nullable
  public SpellModifier getParent() {
    if (parent == null) {
      return null;
    }
    return parent.value();
  }

  @Override
  public Set<SpellModifier> getChildren() {
    return children;
  }

  @Override
  public void addChild(SpellModifier child) {
    children.add(child);
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("spell_modifier", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }
}
