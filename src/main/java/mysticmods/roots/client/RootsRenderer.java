package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RootsRenderer {
  public static TextureAtlasSprite whiteIcon;

  @SubscribeEvent
  public static void onStitchPre(TextureStitchEvent.Pre event) {
    // TODO: This shouldn't be done like this; also is it needed any more?
    if (!event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
      return;
    }

    event.addSprite(RootsAPI.rl("block/overlay/overlay_white"));
  }

  @SubscribeEvent
  public static void onStitchPost(TextureStitchEvent.Post event) {
    TextureAtlas map = event.getAtlas();
    if (!map.location().equals(TextureAtlas.LOCATION_BLOCKS)) {
      return;
    }
    whiteIcon = map.getSprite(RootsAPI.rl("block/overlay/overlay_white"));
  }

  public static int getColorARGB(int red, int green, int blue, float alpha) {
    if (alpha < 0) {
      alpha = 0;
    } else if (alpha > 1) {
      alpha = 1;
    }
    int argb = (int) (255 * alpha) << 24;
    argb |= red << 16;
    argb |= green << 8;
    argb |= blue;
    return argb;
  }

  public static float getRed(int color) {
    return (color >> 16 & 0xFF) / 255.0F;
  }

  public static float getGreen(int color) {
    return (color >> 8 & 0xFF) / 255.0F;
  }

  public static float getBlue(int color) {
    return (color & 0xFF) / 255.0F;
  }

  public static float getAlpha(int color) {
    return (color >> 24 & 0xFF) / 255.0F;
  }
}
