package mysticmods.roots.event.setup;

import mysticmods.roots.advancements.Advancements;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.network.Networking;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {
  @SubscribeEvent
  public static void init(FMLCommonSetupEvent event) {
    // TODO: Should this be enqueued?
    Networking.INSTANCE.registerMessages();

    TierSortingRegistry.registerTier(RootsAPI.LIVING_TOOL_TIER, RootsAPI.LIVING_TOOL_TIER_ID, List.of(), List.of(Tiers.STONE));
    // TODO: Stone plant -- was that even used???

    event.enqueueWork(() -> {
      ModEntities.registerEntities();
      Chicken.FOOD_ITEMS = CompoundIngredient.of(Chicken.FOOD_ITEMS, Ingredient.of(RootsTags.Items.SEEDS));
      FlowerPotBlock FLOWER_POT = (FlowerPotBlock) Blocks.FLOWER_POT;
      FLOWER_POT.addPlant(ModBlocks.STONEPETAL.getId(), ModBlocks.POTTED_STONEPETAL);
      FLOWER_POT.addPlant(ModBlocks.BAFFLECAP.getId(), ModBlocks.POTTED_BAFFLECAP);

      // TODO: Flammability
      Advancements.init();


    });
  }
}
