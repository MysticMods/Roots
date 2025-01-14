package mysticmods.roots.api.spell;

import com.google.common.base.Suppliers;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.registry.*;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SpellModifier implements IDescribedRegistryEntry, ICostedRegistryEntry, IParentChild<SpellModifier> {
  @Nullable
  protected Holder<SpellModifier> parent;
/*  protected final Set<SpellModifier> children = new ObjectLinkedOpenHashSet<>();*/
  protected final Holder<Spell> spell;
  protected final List<Cost> defaultCosts;
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
    // TODO:
    return Collections.emptyList();
  }

  public Holder<Spell> getSpell() {
    return spell;
  }

  public void initialize() {
    getSpell().value().addModifier(this);
    resolve();
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
  public SpellModifier getParent() {
    return parent.value();
  }

  @Override
  public Set<SpellModifier> getChildren() {
    return Collections.emptySet();
/*    return children;*/
  }

  @Override
  public void addChild(SpellModifier child) {
/*    children.add(child);*/
  }


  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("spell_modifier", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }
}
