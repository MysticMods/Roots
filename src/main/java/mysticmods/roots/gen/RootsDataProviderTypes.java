package mysticmods.roots.gen;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.biome.Biome;

public class RootsDataProviderTypes {
  public static final ProviderType<CompatTagsProvider<Ritual>> RITUAL_TAGS = ProviderType.register("tags/rituals", type -> (p, e) -> new CompatTagsProvider<>(p, type, "rituals", e.getGenerator(), Registries.RITUAL_REGISTRY.get(), e.getExistingFileHelper()));
  public static final ProviderType<CompatTagsProvider<Spell>> SPELL_TAGS = ProviderType.register("tags/spells", type -> (p, e) -> new CompatTagsProvider<>(p, type, "spells", e.getGenerator(), Registries.SPELL_REGISTRY.get(), e.getExistingFileHelper()));
  public static final ProviderType<CompatTagsProvider<Herb>> HERB_TAGS = ProviderType.register("tags/herbs", type -> (p, e) -> new CompatTagsProvider<>(p, type, "herbs", e.getGenerator(), Registries.HERB_REGISTRY.get(), e.getExistingFileHelper()));
  public static final ProviderType<CompatTagsProvider<Modifier>> MODIFIER_TAGS = ProviderType.register("tags/modifiers", type -> (p, e) -> new CompatTagsProvider<>(p, type, "modifiers", e.getGenerator(), Registries.MODIFIER_REGISTRY.get(), e.getExistingFileHelper()));
  public static final ProviderType<RegistrateTagsProvider<Biome>> BIOME_TAGS = ProviderType.register("tags/worldgen/biomes", type -> (p, e) -> new RegistrateTagsProvider<>(p, type, "biomes", e.getGenerator(), BuiltinRegistries.BIOME, e.getExistingFileHelper()));
}
