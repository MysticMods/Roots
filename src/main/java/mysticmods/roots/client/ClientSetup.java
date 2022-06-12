package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.block.entity.*;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModBlocks;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
  @SubscribeEvent
  public static void clientSetup(FMLClientSetupEvent event) {
    RenderType cutout = RenderType.cutoutMipped();
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILDWOOD_LEAVES.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.FEY_LIGHT.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.CLOUD_BERRY_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.DEWGONIA_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.INFERNAL_BULB_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.MOONGLOW_LEAF_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.CLOUD_BERRY_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.PERESKIA_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.SPIRIT_HERB_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.STALICRIPE_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.WILDEWHEET_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.WILDROOT_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.SACRED_MOSS.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_ROOTS.get(), cutout);
  }

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(ModBlockEntities.PEDESTAL.get(), PedestalBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.MORTAR.get(), MortarBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.GROVE_CRAFTER.get(), GroveCrafterBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.PYRE.get(), PyreBlockEntityRenderer::new);
  }

  @SubscribeEvent
  public static void onColorHandlerBlock (ColorHandlerEvent.Block event) {
    event.getBlockColors().register((pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null ? BiomeColors.getAverageWaterColor(pLevel, pPos) : -1, ModBlocks.UNENDING_BOWL.get());
  }

  @SubscribeEvent
  public static void onColorHandlerItem (ColorHandlerEvent.Item event) {
    event.getItemColors().register((stack, index) -> index == 1 ? OverworldBiomes.NORMAL_WATER_COLOR : -1, ModBlocks.UNENDING_BOWL.get());
  }
}
