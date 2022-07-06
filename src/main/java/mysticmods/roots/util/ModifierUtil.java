package mysticmods.roots.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.spells.Spell;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class ModifierUtil {
  private static final Map<ResourceLocation, ResourceLocation> MODIFIER_TO_SPELL_MAP = new Object2ObjectLinkedOpenHashMap<>();
  private static final Map<ResourceLocation, Set<ResourceLocation>> SPELL_TO_MODIFIERS_MAP = new Object2ObjectLinkedOpenHashMap<>();

  @Nullable
  public static <T extends IForgeRegistryEntry<T>> T getRegistryEntry(ResourceKey<T> key) {
    Registry<T> registry = ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(ResourceKey.createRegistryKey(key.location()));
    return registry.get(key);
  }

  public static void registerModifier(Spell spell, Modifier... modifiers) {
    SPELL_TO_MODIFIERS_MAP.computeIfAbsent(spell.getKey().location(), (k) -> new ObjectLinkedOpenHashSet<>()).addAll(Stream.of(modifiers).map(o -> o.getKey().location()).toList());
    for (Modifier modifier : modifiers) {
      MODIFIER_TO_SPELL_MAP.put(modifier.getKey().location(), spell.getKey().location());
    }
  }

  @Nullable
  public static ResourceLocation spellForModifier(Modifier modifier) {
    return spellForModifier(modifier.getKey());
  }

  @Nullable
  public static ResourceLocation spellForModifier(ResourceKey<Modifier> modifier) {
    return spellForModifier(modifier.location());
  }


  @Nullable
  public static ResourceLocation spellForModifier(ResourceLocation modifier) {
    return MODIFIER_TO_SPELL_MAP.get(modifier);
  }

  @Nullable
  public static Set<ResourceLocation> modifiersForSpell(Spell spell) {
    return modifiersForSpell(spell.getKey());
  }

  @Nullable
  public static Set<ResourceLocation> modifiersForSpell(ResourceKey<Spell> spell) {
    return modifiersForSpell(spell.location());
  }

  @Nullable
  public static Set<ResourceLocation> modifiersForSpell(ResourceLocation spell) {
    return SPELL_TO_MODIFIERS_MAP.get(spell);
  }
}
