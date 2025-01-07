package mysticmods.roots.event.setup;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModCompost;
import mysticmods.roots.init.P;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonSetup {
  @SubscribeEvent
  public static void init(FMLCommonSetupEvent event) {
    // TODO: Should this be enqueued?
    // Networking.INSTANCE.registerMessages();

    // TODO: Stone plant -- was that even used???
    for (PropertyHolder<?> prop : P.unclaimed()) {
      RootsAPI.LOG.error("Unclaimed property: {}", prop.id());
    }

    event.enqueueWork(() -> {
      /*      Chicken.FOOD_ITEMS = CompoundIngredient.of(Chicken.FOOD_ITEMS, Ingredient.of(RootsTags.Items.SEEDS));*/
      FlowerPotBlock FLOWER_POT = (FlowerPotBlock) Blocks.FLOWER_POT;
      FLOWER_POT.addPlant(ModBlocks.STONEPETAL.getId(), ModBlocks.POTTED_STONEPETAL);
      FLOWER_POT.addPlant(ModBlocks.BAFFLECAP.getId(), ModBlocks.POTTED_BAFFLECAP);
      FLOWER_POT.addPlant(ModBlocks.WILDWOOD_SAPLING.getId(), ModBlocks.POTTED_WILDWOOD_SAPLING);

      ModCompost.init();

      // TODO: Flammability
      /*      Advancements.init();*/


    });
  }
}
