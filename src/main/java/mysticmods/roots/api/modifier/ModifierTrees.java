package mysticmods.roots.api.modifier;

import com.google.common.collect.ImmutableMap;
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
    if (initialized) {
      throw new IllegalStateException("ModifierTrees already initialized");
    }
    ImmutableMap.Builder<ResourceKey<Ritual>, ModifierTree<Ritual, RitualModifier>> builder = ImmutableMap.builder();
    RootsRegistries.RITUALS.holders().forEach(holder -> {
      ModifierTree<Ritual, RitualModifier> tree = new ModifierTree<>(holder);
      for (RitualModifier modifier : RootsRegistries.RITUAL_MODIFIERS)
        if (modifier.getApplicable().equals(holder.getKey())) {
          tree.addModifier(modifier.builtInRegistryHolder());
        }
      builder.put(holder.key(), tree);
    });
    RITUAL_MODIFIER_TREES = builder.build();

    ImmutableMap.Builder<ResourceKey<Spell>, ModifierTree<Spell, SpellModifier>> spellBuilder = ImmutableMap.builder();
    RootsRegistries.SPELLS.holders().forEach(holder -> {
      ModifierTree<Spell, SpellModifier> tree = new ModifierTree<>(holder);
      for (SpellModifier modifier : RootsRegistries.SPELL_MODIFIERS)
        if (modifier.getApplicable().equals(holder.getKey())) {
          tree.addModifier(modifier.builtInRegistryHolder());
        }
      spellBuilder.put(holder.key(), tree);
    });
    SPELL_MODIFIER_TREES = spellBuilder.build();
    initialized = true;
  }
}
