package epicsquid.roots.tileentity;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

public class TileEntityWildrootRuneRenderer extends TileEntityRenderer<TileEntityWildrootRune> {

  @Override
  public void render(TileEntityWildrootRune tei, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    if (tei.getIncenseBurner() != null && tei.getIncenseBurner().isLit() && tei.effectItemMap.containsKey(tei.getIncenseBurner().burningItem())) {
      if (tei.getWorld().getWorldTime() % 5 == 0) {
        int color = tei.effectItemMap.get(tei.getIncenseBurner().burningItem()).getPotion().getLiquidColor();

        int blue = color & 0xFF;
        int green = (color >> 8) & 0xFF;
        int red = (color >> 16) & 0xFF;

        for (int part = 0; part < 5; part++) {
          ParticleUtil.spawnParticleGlow(getWorld(),
              tei.getPos().getX() + Util.rand.nextFloat(), tei.getPos().getY() + Util.rand.nextFloat(), tei.getPos().getZ() + Util.rand.nextFloat(), 0, 0, 0,
              red, green, blue, 1, 2, 100);
        }
      }

    }

  }

}
