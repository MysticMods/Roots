package epicsquid.mysticallib;

import epicsquid.mysticallib.particle.ParticleBase;
import epicsquid.mysticallib.particle.ParticleRegistry;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.FluidTextureUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map.Entry;

public class LibEvents {
  public static int ticks = 0;

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onTextureStitchPre(TextureStitchEvent.Pre event) {
    FluidTextureUtil.initTextures(event.getMap());
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onTextureStitch(TextureStitchEvent.Pre event) {
    for (Entry<Class<? extends ParticleBase>, List<ResourceLocation>> e : ParticleRegistry.particleMultiTextures.entrySet()) {
      e.getValue().forEach(event.getMap()::registerSprite);
    }
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void onRenderAfterWorld(RenderWorldLastEvent event) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      GlStateManager.pushMatrix();
      //noinspection ConstantConditions
      ClientProxy.particleRenderer.renderParticles(MysticalLib.proxy.getPlayer(), event.getPartialTicks());
      GlStateManager.popMatrix();
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      ticks++;
      ClientProxy.particleRenderer.updateParticles();
    }
  }
}
