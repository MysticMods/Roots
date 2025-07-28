package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.gen.advancement.RootsAdvancementProvider;
import mysticmods.roots.gen.client.RootsAtlasProvider;
import mysticmods.roots.gen.client.RootsBlockStateProvider;
import mysticmods.roots.gen.client.RootsItemModelProvider;
import mysticmods.roots.gen.client.RootsParticleProvider;
import mysticmods.roots.gen.lang.RootsLangProvider;
import mysticmods.roots.gen.loot.RootsLootTableProvider;
import mysticmods.roots.gen.nbt.StructureNbtUpdater;
import mysticmods.roots.gen.neoforge.RootsDataMapProvider;
import mysticmods.roots.gen.neoforge.RootsGlobalLootModifierProvider;
import mysticmods.roots.gen.recipe.RootsRecipeProvider;
import mysticmods.roots.gen.tags.*;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RootsDataGenerators {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    PackOutput output = event.getGenerator().getPackOutput();
    CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
    ExistingFileHelper helper = event.getExistingFileHelper();

    RootsBlockTagProvider blocks;
    generator.addProvider(event.includeServer(), blocks = new RootsBlockTagProvider(output, provider, helper));
    generator.addProvider(event.includeServer(), new RootsItemTagsProvider(output, provider, blocks.contentsGetter(), helper));
    generator.addProvider(event.includeClient(), new RootsAtlasProvider(output, provider, helper));
    generator.addProvider(true, RootsLootTableProvider.create(output, provider));
    generator.addProvider(event.includeServer(), new RootsEntityTagsProvider(output, provider, helper));
    generator.addProvider(event.includeServer(), new RootsBlockEntityTagsProvider(output, provider, helper));
    generator.addProvider(event.includeClient(), new RootsLangProvider(output));
    generator.addProvider(event.includeServer(), new AdvancementProvider(output, provider, helper, List.of(new RootsAdvancementProvider())));
    generator.addProvider(event.includeServer(), new RootsDataMapProvider(output, provider));
    generator.addProvider(event.includeServer(), new RootsHerbTagsProvider(output, provider, RootsAPI.MODID, helper));
    generator.addProvider(event.includeServer(), new RootsSpellTagsProvider(output, provider, RootsAPI.MODID, helper));
    generator.addProvider(event.includeServer(), new RootsGroveTagsProvider(output, provider, RootsAPI.MODID, helper));
    generator.addProvider(event.includeServer(), new RootsRitualTagsProvider(output, provider, RootsAPI.MODID, helper));
    generator.addProvider(event.includeServer(), new RootsStructureTagsProvider(output, provider, helper));
    generator.addProvider(event.includeServer(), new RootsBiomeTagsProvider(output, provider, helper));
    generator.addProvider(event.includeClient(), new RootsBlockStateProvider(output, helper));
    generator.addProvider(event.includeClient(), new RootsItemModelProvider(output, helper));
    generator.addProvider(event.includeServer(), new RootsGlobalLootModifierProvider(output, provider));
    generator.addProvider(event.includeServer(), new StructureNbtUpdater("structures", RootsAPI.MODID, helper, output));
    generator.addProvider(event.includeServer(), new RootsRecipeProvider(output, provider));
    generator.addProvider(event.includeClient(), new RootsParticleProvider(output, helper));
    generator.addProvider(event.includeServer(), new RootsMobEffectsTagsProvider(output, provider, helper));
    generator.addProvider(event.includeServer(), new RootsDamageTagsProvider(output, provider, helper));
    generator.addProvider(event.includeServer(), new RootsAttributeTagsProvider(output, provider, helper));
    generator.addProvider(true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(Component.literal("Roots resources"), DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA), Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE)))));
  }
}
