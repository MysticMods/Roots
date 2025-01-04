package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;


import java.util.List;
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
    generator.addProvider(event.includeClient(), new RootsAtlasGenerator(output, provider, helper));
    generator.addProvider(true, RootsLootTableProvider.create(output, provider));
    generator.addProvider(event.includeServer(), new RootsEntityTagsProvider(output, provider, helper));
    generator.addProvider(event.includeServer(), new RootsBlockEntityTagsProvider(output, provider, helper));
    generator.addProvider(event.includeClient(), new RootsLangProvider(output));
    generator.addProvider(event.includeServer(), new AdvancementProvider(output, provider, helper, List.of(new RootsAdvancementGenerator())));
  }
}
