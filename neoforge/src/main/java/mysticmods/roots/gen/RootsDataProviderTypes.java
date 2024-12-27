package mysticmods.roots.gen;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.biome.Biome;

public class RootsDataProviderTypes {
  public static final ProviderType<CompatTagsProvider<Ritual>> RITUAL_TAGS = ProviderType.register("tags/rituals", type -> (p, e) -> new CompatTagsProvider<>(p, type, "rituals", e.getGenerator(), RootsRegistries.RITUAL_REGISTRY.get(), e.getExistingFileHelper()));
  public static final ProviderType<CompatTagsProvider<Spell>> SPELL_TAGS = ProviderType.register("tags/spells", type -> (p, e) -> new CompatTagsProvider<>(p, type, "spells", e.getGenerator(), RootsRegistries.SPELL_REGISTRY.get(), e.getExistingFileHelper()));
  public static final ProviderType<CompatTagsProvider<Herb>> HERB_TAGS = ProviderType.register("tags/herbs", type -> (p, e) -> new CompatTagsProvider<>(p, type, "herbs", e.getGenerator(), RootsRegistries.HERB_REGISTRY.get(), e.getExistingFileHelper()));
  public static final ProviderType<CompatTagsProvider<Grove>> GROVE_TAGS = ProviderType.register("tags/groves", type -> (p, e) -> new CompatTagsProvider<>(p, type, "groves", e.getGenerator(), RootsRegistries.GROVE_REGISTRY.get(), e.getExistingFileHelper()));
  public static final ProviderType<CompatTagsProvider<Modifier>> MODIFIER_TAGS = ProviderType.register("tags/modifiers", type -> (p, e) -> new CompatTagsProvider<>(p, type, "modifiers", e.getGenerator(), RootsRegistries.MODIFIER_REGISTRY.get(), e.getExistingFileHelper()));
  public static final ProviderType<RegistrateTagsProvider<Biome>> BIOME_TAGS = ProviderType.register("tags/worldgen/biomes", type -> (p, e) -> new RegistrateTagsProvider<>(p, type, "biomes", e.getGenerator(), BuiltinRegistries.BIOME, e.getExistingFileHelper()));
}
