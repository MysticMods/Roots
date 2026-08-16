package mysticmods.roots.api.modifier;

import com.google.common.collect.ImmutableMap;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

public class ModifierTrees {
  private static ImmutableMap<ResourceKey<Ritual>, ModifierTree<Ritual, RitualModifier>> RITUAL_MODIFIER_TREES;
  private static ImmutableMap<ResourceKey<Spell>, ModifierTree<Spell, SpellModifier>> SPELL_MODIFIER_TREES;
  private static boolean initialized = false;

  public static ModifierTree<Ritual, RitualModifier> getRitual(ResourceKey<Ritual> ritualKey) {
    if (!initialized) {
      throw new IllegalStateException("ModifierTrees not initialized");
    }
    return RITUAL_MODIFIER_TREES.get(ritualKey);
  }

  public static ModifierTree<Ritual, RitualModifier> getRitual(Holder<Ritual> ritualHolder) {
    if (!initialized) {
      throw new IllegalStateException("ModifierTrees not initialized");
    }
    return RITUAL_MODIFIER_TREES.get(ritualHolder.getKey());
  }

  public static ModifierTree<Ritual, RitualModifier> getRitual(Ritual ritual) {
    if (!initialized) {
      throw new IllegalStateException("ModifierTrees not initialized");
    }
    return RITUAL_MODIFIER_TREES.get(ritual.builtInRegistryHolder().getKey());
  }

  public static ModifierTree<Spell, SpellModifier> getSpell(ResourceKey<Spell> spellKey) {
    if (!initialized) {
      throw new IllegalStateException("ModifierTrees not initialized");
    }
    return SPELL_MODIFIER_TREES.get(spellKey);
  }

  public static ModifierTree<Spell, SpellModifier> getSpell(Holder<Spell> spellHolder) {
    if (!initialized) {
      throw new IllegalStateException("ModifierTrees not initialized");
    }
    return SPELL_MODIFIER_TREES.get(spellHolder.getKey());
  }

  public static ModifierTree<Spell, SpellModifier> getSpell(Spell spell) {
    if (!initialized) {
      throw new IllegalStateException("ModifierTrees not initialized");
    }
    return SPELL_MODIFIER_TREES.get(spell.builtInRegistryHolder().getKey());
  }

  public static void initialize() {
    ImmutableMap.Builder<ResourceKey<Ritual>, ModifierTree<Ritual, RitualModifier>> builder = ImmutableMap.builder();
    RootsRegistries.RITUALS.holders().forEach(holder -> {
      ModifierTree<Ritual, RitualModifier> tree = new ModifierTree<>(holder, RootsRegistries.Keys.RITUAL_MODIFIERS);
      for (RitualModifier modifier : RootsRegistries.RITUAL_MODIFIERS) {
        if (modifier.getApplicable().equals(holder.getKey())) {
          tree.addModifier(modifier.builtInRegistryHolder());
        }
      }
      var validated = tree.validateParents();
      if (!validated.isEmpty()) {
        throw new IllegalStateException("Ritual " + holder.getKey() + " has modifiers with missing parents: " + validated);
      }
      tree.position();
      builder.put(holder.key(), tree);
    });
    RITUAL_MODIFIER_TREES = builder.build();

    ImmutableMap.Builder<ResourceKey<Spell>, ModifierTree<Spell, SpellModifier>> spellBuilder = ImmutableMap.builder();
    RootsRegistries.SPELLS.holders().forEach(holder -> {
      if (holder.is(RootsTags.Spells.INVALID)) {
        return;
      }
      ModifierTree<Spell, SpellModifier> tree = new ModifierTree<>(holder, RootsRegistries.Keys.SPELL_MODIFIERS);
      for (SpellModifier modifier : RootsRegistries.SPELL_MODIFIERS) {
        if (modifier.getApplicable().equals(holder.getKey())) {
          tree.addModifier(modifier.builtInRegistryHolder());
        }
      }
      var validated = tree.validateParents();
      if (!validated.isEmpty()) {
        throw new IllegalStateException("Spell " + holder.getKey() + " has modifiers with missing parents: " + validated);
      }
      tree.position();
      spellBuilder.put(holder.key(), tree);
    });
    SPELL_MODIFIER_TREES = spellBuilder.build();

    initialized = true;
  }

  public static SpellModifierSet without (Spell spell, SpellModifierSet modifiers, SpellModifier without) {
    ModifierTree<Spell, SpellModifier> tree = getSpell(spell);
    if (tree == null) {
      throw new IllegalStateException("Spell " + spell.builtInRegistryHolder().getKey() + " has no modifier tree?!");
    }

    var instance = tree.instance(modifiers, null);

    if (!instance.disable(without)) {
      return modifiers;
    }

    return new SpellModifierSet(instance.modifiersSet()).validated();
  }

  public static SpellModifierSet with (Spell spell, SpellModifierSet modifiers, SpellModifier with) {
    ModifierTree<Spell, SpellModifier> tree = getSpell(spell);
    if (tree == null) {
      throw new IllegalStateException("Spell " + spell.builtInRegistryHolder().getKey() + " has no modifier tree?!");
    }

    var instance = tree.instance(modifiers, null);

    if (!instance.enable(with)) {
      return modifiers;
    }

    return new SpellModifierSet(instance.modifiersSet()).validated();
  }
}
