package mysticmods.roots.client;

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
  public static void clientSetup(FMLClientSetupEvent event) {
    RenderType cutout = RenderType.cutoutMipped();
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.Decoration.Wildwood.WILDWOOD_LEAVES.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.FEY_LIGHT.get(), cutout);
  }
}
