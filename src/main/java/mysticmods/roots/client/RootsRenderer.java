package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RootsRenderer {
  public static final int FULL_LIGHT = 0xF000F0;
  public static final int FULL_SKY_LIGHT = LightTexture.pack(0, 15);

  public static TextureAtlasSprite whiteIcon;

  @SubscribeEvent
  public static void onStitchPre(TextureStitchEvent.Pre event) {
    if (!event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
      return;
    }

    event.addSprite(new ResourceLocation(RootsAPI.MODID, "block/overlay/overlay_white"));
  }

  @SubscribeEvent
  public static void onStitchPost(TextureStitchEvent.Post event) {
    TextureAtlas map = event.getAtlas();
    if (!map.location().equals(TextureAtlas.LOCATION_BLOCKS)) {
      return;
    }
    whiteIcon = map.getSprite(new ResourceLocation(RootsAPI.MODID, "block/overlay/overlay_white"));
  }

  public static int getColorARGB(@Nonnull FluidStack fluidStack) {
    return fluidStack.getFluid().getAttributes().getColor(fluidStack);
  }

  public static int getColorARGB(@Nonnull FluidStack fluidStack, float fluidScale) {
    if (fluidStack.isEmpty()) {
      return -1;
    }
    int color = getColorARGB(fluidStack);
    if (fluidStack.getFluid().getAttributes().isGaseous(fluidStack)) {
      //TODO: We probably want to factor in the fluid's alpha value somehow
      return getColorARGB(getRed(color), getGreen(color), getBlue(color), Math.min(1, fluidScale + 0.2F));
    }
    return color;
  }

  public static int getColorARGB(float red, float green, float blue, float alpha) {
    return getColorARGB((int) (255 * red), (int) (255 * green), (int) (255 * blue), alpha);
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

  public static void renderObject(@Nullable Model3D object, @Nonnull PoseStack matrix, VertexConsumer buffer, int argb, int light, int overlay, RenderResizableCuboid.FaceDisplay faceDisplay) {
    renderObject(object, matrix, buffer, argb, light, overlay, faceDisplay, true);
  }

  public static void renderObject(@Nullable Model3D object, @Nonnull PoseStack matrix, VertexConsumer buffer, int argb, int light, int overlay, RenderResizableCuboid.FaceDisplay faceDisplay, boolean fakeDisableDiffuse) {
    if (object != null) {
      RenderResizableCuboid.renderCube(object, matrix, buffer, argb, light, overlay, faceDisplay, fakeDisableDiffuse);
    }
  }

  public static void renderObject(@Nullable Model3D object, @Nonnull PoseStack matrix, VertexConsumer buffer, int[] colors, int light, int overlay, RenderResizableCuboid.FaceDisplay faceDisplay) {
    if (object != null) {
      RenderResizableCuboid.renderCube(object, matrix, buffer, colors, light, overlay, faceDisplay, true);
    }
  }
}
