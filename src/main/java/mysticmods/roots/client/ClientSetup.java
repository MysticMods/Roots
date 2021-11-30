package mysticmods.roots.client;

import mysticmods.roots.Roots;
import mysticmods.roots.init.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Roots.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
  @SubscribeEvent
  public static void clientSetup(FMLClientSetupEvent event) {
    RenderType cutout = RenderType.cutoutMipped();
    RenderTypeLookup.setRenderLayer(ModBlocks.Decoration.Wildwood.WILDWOOD_LEAVES.get(), cutout);
    RenderTypeLookup.setRenderLayer(ModBlocks.FEY_LIGHT.get(), cutout);
  }
}
