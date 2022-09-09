package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.blockentity.GroveCrafterBlockEntityRenderer;
import mysticmods.roots.client.blockentity.MortarBlockEntityRenderer;
import mysticmods.roots.client.blockentity.PedestalBlockEntityRenderer;
import mysticmods.roots.client.blockentity.PyreBlockEntityRenderer;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModBlocks;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
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
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.INFERNO_BULB_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.MOONGLOW_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.CLOUD_BERRY_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.PERESKIA_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.SPIRITLEAF_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.STALICRIPE_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.WILDEWHEET_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.WILDROOT_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.BAFFLECAP_BLOCK.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.GROVE_MOSS.get(), cutout);
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
  public static void onColorHandlerBlock(RegisterColorHandlersEvent.Block event) {
    event.getBlockColors().register((pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null ? BiomeColors.getAverageWaterColor(pLevel, pPos) : -1, ModBlocks.UNENDING_BOWL.get());
  }

  @SubscribeEvent
  public static void onColorHandlerItem(RegisterColorHandlersEvent.Item event) {
    event.getItemColors().register((stack, index) -> index == 1 ? OverworldBiomes.NORMAL_WATER_COLOR : -1, ModBlocks.UNENDING_BOWL.get());
  }
}
