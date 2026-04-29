package mysticmods.roots.event.setup;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.P;
import mysticmods.roots.item.KnifeItem;
import mysticmods.roots.item.RunicShearsItem;
import mysticmods.roots.item.util.RootsCauldronInteractions;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = RootsAPI.MODID)
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
      fire.setFlammable(ModBlocks.HANGING_GROVE_MOSS.get(), 1, 1);
      fire.setFlammable(ModBlocks.CREEPING_GROVE_MOSS.get(), 1, 1);
      fire.setFlammable(ModBlocks.WILD_AUBERGINE.get(), 60, 100);

      DispenserBlock.registerProjectileBehavior(ModItems.LIVING_ARROW.get());

      KnifeItem.KnifeDispenseBehaviour behaviour = new KnifeItem.KnifeDispenseBehaviour();
      DispenserBlock.registerBehavior(ModItems.WOODEN_KNIFE.get(), behaviour);
      DispenserBlock.registerBehavior(ModItems.STONE_KNIFE.get(), behaviour);
      DispenserBlock.registerBehavior(ModItems.COPPER_KNIFE.get(), behaviour);
      DispenserBlock.registerBehavior(ModItems.IRON_KNIFE.get(), behaviour);
      DispenserBlock.registerBehavior(ModItems.GOLDEN_KNIFE.get(), behaviour);
      DispenserBlock.registerBehavior(ModItems.SILVER_KNIFE.get(), behaviour);
      DispenserBlock.registerBehavior(ModItems.DIAMOND_KNIFE.get(), behaviour);
      DispenserBlock.registerBehavior(ModItems.NETHERITE_KNIFE.get(), behaviour);

      DispenserBlock.registerBehavior(ModItems.WOODEN_SHEARS.get(), new ShearsDispenseItemBehavior());
      DispenserBlock.registerBehavior(ModItems.RUNIC_SHEARS.get(), new RunicShearsItem.RunicShearsDispenseBehaviour());

      CauldronInteraction.WATER.map().put(ModItems.APOTHECARY_POUCH.get(), RootsCauldronInteractions.CLEAN_POUCH);
      CauldronInteraction.WATER.map().put(ModItems.COMPONENT_POUCH.get(), RootsCauldronInteractions.CLEAN_POUCH);
      CauldronInteraction.WATER.map().put(ModItems.HERB_POUCH.get(), RootsCauldronInteractions.CLEAN_POUCH);
      CauldronInteraction.WATER.map().put(ModItems.SYLVAN_POUCH.get(), RootsCauldronInteractions.CLEAN_POUCH);
    });
  }
}
