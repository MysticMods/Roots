package mysticmods.roots.event.setup;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
  @SubscribeEvent
  public static void init(FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
      RenderType rendertype = RenderType.cutoutMipped();
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.WILDROOT_CROP.get(), rendertype);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.CLOUD_BERRY_CROP.get(), rendertype);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.DEWGONIA_CROP.get(), rendertype);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.INFERNAL_BULB_CROP.get(), rendertype);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.MOONGLOW_LEAF_CROP.get(), rendertype);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.PERESKIA_CROP.get(), rendertype);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.SPIRIT_HERB_CROP.get(), rendertype);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.WILDEWHEET_CROP.get(), rendertype);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.Crops.STALICRIPE_CROP.get(), rendertype);
    });
  }
}
