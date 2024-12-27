package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.reference.Groves;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.List;
import java.util.function.Supplier;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModGroves {
  public static final RegistryEntry<Grove> PRIMAL = grove(Groves.PRIMAL, Grove::new, ChatFormatting.GOLD, RootsTags.Herbs.PRIMAL, RootsTags.Groves.PRIMAL_ALIGNED, RootsTags.Groves.PRIMAL_OPPOSED, RootsTags.Spells.PRIMAL_ALIGNED, RootsTags.Spells.PRIMAL_OPPOSED, RootsTags.Rituals.PRIMAL_ALIGNED, RootsTags.Rituals.PRIMAL_OPPOSED);

  public static final RegistryEntry<Grove> FAIRY = grove(Groves.FAIRY, Grove::new, ChatFormatting.LIGHT_PURPLE, RootsTags.Herbs.FAIRY, RootsTags.Groves.FAIRY_ALIGNED, RootsTags.Groves.FAIRY_OPPOSED, RootsTags.Spells.FAIRY_ALIGNED, RootsTags.Spells.FAIRY_OPPOSED, RootsTags.Rituals.FAIRY_ALIGNED, RootsTags.Rituals.FAIRY_OPPOSED);

  public static final RegistryEntry<Grove> TWILIGHT = grove(Groves.TWILIGHT, Grove::new, ChatFormatting.DARK_PURPLE, RootsTags.Herbs.TWILIGHT, RootsTags.Groves.TWILIGHT_ALIGNED, RootsTags.Groves.TWILIGHT_OPPOSED, RootsTags.Spells.TWILIGHT_ALIGNED, RootsTags.Spells.TWILIGHT_OPPOSED, RootsTags.Rituals.TWILIGHT_ALIGNED, RootsTags.Rituals.TWILIGHT_OPPOSED);

  public static final RegistryEntry<Grove> FUNGAL = grove(Groves.FUNGAL, Grove::new, ChatFormatting.DARK_AQUA, RootsTags.Herbs.FUNGAL, RootsTags.Groves.FUNGAL_ALIGNED, RootsTags.Groves.FUNGAL_OPPOSED, RootsTags.Spells.FUNGAL_ALIGNED, RootsTags.Spells.FUNGAL_OPPOSED, RootsTags.Rituals.FUNGAL_ALIGNED, RootsTags.Rituals.FUNGAL_OPPOSED);

  public static final RegistryEntry<Grove> SPROUT = grove(Groves.SPROUT, Grove::new, ChatFormatting.GREEN, RootsTags.Herbs.SPROUT, RootsTags.Groves.SPROUT_ALIGNED, RootsTags.Groves.SPROUT_OPPOSED, RootsTags.Spells.SPROUT_ALIGNED, RootsTags.Spells.SPROUT_OPPOSED, RootsTags.Rituals.SPROUT_ALIGNED, RootsTags.Rituals.SPROUT_OPPOSED);

  public static final RegistryEntry<Grove> ELEMENTAL = grove(Groves.ELEMENTAL, Grove::new, ChatFormatting.DARK_RED, RootsTags.Herbs.ELEMENTAL, RootsTags.Groves.ELEMENTAL_ALIGNED, RootsTags.Groves.ELEMENTAL_OPPOSED, RootsTags.Spells.ELEMENTAL_ALIGNED, RootsTags.Spells.ELEMENTAL_OPPOSED, RootsTags.Rituals.ELEMENTAL_ALIGNED, RootsTags.Rituals.ELEMENTAL_OPPOSED);

  public static final RegistryEntry<Grove> WILD = grove(Groves.WILD, Grove::new, ChatFormatting.YELLOW, RootsTags.Herbs.WILD, RootsTags.Groves.WILD_ALIGNED, RootsTags.Groves.WILD_OPPOSED, RootsTags.Spells.WILD_ALIGNED, RootsTags.Spells.WILD_OPPOSED, RootsTags.Rituals.WILD_ALIGNED, RootsTags.Rituals.WILD_OPPOSED);

  private static <T extends Grove> RegistryEntry<T> grove(ResourceKey<Grove> key, GroveConstructor<T> constructor, ChatFormatting color, TagKey<Herb> herbs, TagKey<Grove> alignedGroves, TagKey<Grove> opposedGroves, TagKey<Spell> alignedSpells, TagKey<Spell> opposedSpells, TagKey<Ritual> alignedRituals, TagKey<Ritual> opposedRituals) {
    return REGISTRATE.simple(key.location().getPath(), RootsAPI.GROVE_REGISTRY, groveBuilder(constructor, color, herbs, alignedGroves, opposedGroves, alignedSpells, opposedSpells, alignedRituals, opposedRituals));
  }

  private static <T extends Grove> NonNullSupplier<T> groveBuilder(GroveConstructor<T> constructor, ChatFormatting color, TagKey<Herb> herbs, TagKey<Grove> alignedGroves, TagKey<Grove> opposedGroves, TagKey<Spell> alignedSpells, TagKey<Spell> opposedSpells, TagKey<Ritual> alignedRituals, TagKey<Ritual> opposedRituals) {
    return () -> constructor.create(color, herbs, alignedGroves, opposedGroves, alignedSpells, opposedSpells, alignedRituals, opposedRituals);
  }

  private interface GroveConstructor<T extends Grove> {
    T create(ChatFormatting color, TagKey<Herb> herbs, TagKey<Grove> alignedGroves, TagKey<Grove> opposedGroves, TagKey<Spell> alignedSpells, TagKey<Spell> opposedSpells, TagKey<Ritual> alignedRituals, TagKey<Ritual> opposedRituals);
  }

  public static void load() {
  }
}
