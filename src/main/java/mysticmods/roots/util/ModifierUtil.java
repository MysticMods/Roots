package mysticmods.roots.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.spells.Spell;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ModifierUtil {
  private static Map<Modifier, Spell> MODIFIER_TO_SPELL_MAP = new Object2ObjectLinkedOpenHashMap<>();
  private static Map<Spell, Set<Modifier>> SPELL_TO_MODIFIERS_MAP = new Object2ObjectLinkedOpenHashMap<>();

  public static void registerModifier (Spell spell, Modifier ... modifiers) {
    SPELL_TO_MODIFIERS_MAP.computeIfAbsent(spell, (k) -> new ObjectLinkedOpenHashSet<>()).addAll(Arrays.asList(modifiers));
    for (Modifier modifier : modifiers) {
      MODIFIER_TO_SPELL_MAP.put(modifier, spell);
    }
  }

  @Nullable
  public static Spell spellForModifier (Modifier modifier) {
    return MODIFIER_TO_SPELL_MAP.get(modifier);
  }

  @Nullable
  public static Set<Modifier> modifiersForSpell (Spell spell) {
    return SPELL_TO_MODIFIERS_MAP.get(spell);
  }
}
