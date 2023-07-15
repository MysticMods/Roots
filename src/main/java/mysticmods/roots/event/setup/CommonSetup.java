package mysticmods.roots.event.setup;

import mysticmods.roots.advancements.Advancements;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.network.Networking;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {
  @SubscribeEvent
  public static void init(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      Networking.INSTANCE.registerMessages();
      FlowerPotBlock FLOWER_POT = (FlowerPotBlock) Blocks.FLOWER_POT;
      FLOWER_POT.addPlant(ModBlocks.STONEPETAL.getId(), ModBlocks.POTTED_STONEPETAL);
      FLOWER_POT.addPlant(ModBlocks.BAFFLECAP.getId(), ModBlocks.POTTED_BAFFLECAP);
      TierSortingRegistry.registerTier(RootsAPI.LIVING_TOOL_TIER, RootsAPI.LIVING_TOOL_TIER_ID, List.of(), List.of(Tiers.STONE));
      Advancements.init();
    });
  }
}
