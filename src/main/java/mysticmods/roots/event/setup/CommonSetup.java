package mysticmods.roots.event.setup;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.P;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.SpecialPlantable;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonSetup {
  @SubscribeEvent
  public static void init(FMLCommonSetupEvent event) {
    for (PropertyHolder<?> prop : P.unclaimed()) {
      RootsAPI.LOG.error("Unclaimed property: {}", prop.id());
    }

    event.enqueueWork(() -> {
      FlowerPotBlock flowerPot = (FlowerPotBlock) Blocks.FLOWER_POT;
      flowerPot.addPlant(ModBlocks.STONEPETAL.getId(), ModBlocks.POTTED_STONEPETAL);
      flowerPot.addPlant(ModBlocks.BAFFLECAP.getId(), ModBlocks.POTTED_BAFFLECAP);
      flowerPot.addPlant(ModBlocks.WILDWOOD_SAPLING.getId(), ModBlocks.POTTED_WILDWOOD_SAPLING);

      FireBlock fire = (FireBlock) Blocks.FIRE;
      fire.setFlammable(ModBlocks.THATCH.get(), 60, 20); // Hay block equivalent
      fire.setFlammable(ModBlocks.WILDWOOD_LOG.get(), 5, 5);
      fire.setFlammable(ModBlocks.WILDWOOD_WOOD.get(), 5, 5);
      fire.setFlammable(ModBlocks.STRIPPED_WILDWOOD_WOOD.get(), 5, 5);
      fire.setFlammable(ModBlocks.STRIPPED_WILDWOOD_LOG.get(), 5, 5);
      fire.setFlammable(ModBlocks.WILDWOOD_GATE.get(), 5, 20);
      fire.setFlammable(ModBlocks.WILDWOOD_FENCE.get(), 5, 20);
      fire.setFlammable(ModBlocks.WILDWOOD_SLAB.get(), 5, 20);
      fire.setFlammable(ModBlocks.WILDWOOD_PLANKS.get(), 5, 20);
      fire.setFlammable(ModBlocks.WILDWOOD_STAIRS.get(), 5, 20);
      fire.setFlammable(ModBlocks.WILDWOOD_LEAVES.get(), 30, 6);
      fire.setFlammable(ModBlocks.STONEPETAL.get(), 60, 100);
      fire.setFlammable(ModBlocks.RUNED_WILDWOOD_LOG.get(), 1, 1);
      fire.setFlammable(ModBlocks.RUNED_ACACIA_LOG.get(), 1, 1);
      fire.setFlammable(ModBlocks.RUNED_BIRCH_LOG.get(), 1, 1);
      fire.setFlammable(ModBlocks.RUNED_DARK_OAK_LOG.get(), 1, 1);
      fire.setFlammable(ModBlocks.RUNED_JUNGLE_LOG.get(), 1, 1);
      fire.setFlammable(ModBlocks.RUNED_OAK_LOG.get(), 1, 1);
      fire.setFlammable(ModBlocks.RUNED_SPRUCE_LOG.get(), 1, 1);
      fire.setFlammable(ModBlocks.RUNED_MANGROVE_LOG.get(), 1, 1);
      fire.setFlammable(ModBlocks.HANGING_GROVE_MOSS.get(), 1, 1);
      fire.setFlammable(ModBlocks.CREEPING_GROVE_MOSS.get(), 1, 1);
      fire.setFlammable(ModBlocks.WILD_AUBERGINE.get(), 60, 100);
    });
  }
}
