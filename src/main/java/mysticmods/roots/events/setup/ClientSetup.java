package mysticmods.roots.events.setup;

import mysticmods.roots.Roots;
import mysticmods.roots.init.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid= Roots.MODID, bus= Mod.EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class ClientSetup {
  @SubscribeEvent
  public static void init(FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
      RenderType rendertype = RenderType.cutoutMipped();
      RenderTypeLookup.setRenderLayer(ModBlocks.Crops.WILDROOT_CROP.get(), rendertype);
      RenderTypeLookup.setRenderLayer(ModBlocks.Crops.CLOUD_BERRY_CROP.get(), rendertype);
      RenderTypeLookup.setRenderLayer(ModBlocks.Crops.DEWGONIA_CROP.get(), rendertype);
      RenderTypeLookup.setRenderLayer(ModBlocks.Crops.INFERNAL_BULB_CROP.get(), rendertype);
      RenderTypeLookup.setRenderLayer(ModBlocks.Crops.MOONGLOW_LEAF_CROP.get(), rendertype);
      RenderTypeLookup.setRenderLayer(ModBlocks.Crops.PERESKIA_CROP.get(), rendertype);
      RenderTypeLookup.setRenderLayer(ModBlocks.Crops.SPIRIT_HERB_CROP.get(), rendertype);
      RenderTypeLookup.setRenderLayer(ModBlocks.Crops.WILDEWHEET_CROP.get(), rendertype);
      RenderTypeLookup.setRenderLayer(ModBlocks.Crops.STALICRIPE_CROP.get(), rendertype);
    });
  }
}
