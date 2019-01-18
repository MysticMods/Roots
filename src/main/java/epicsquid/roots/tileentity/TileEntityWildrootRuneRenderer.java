package epicsquid.roots.tileentity;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.util.RgbColor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityWildrootRuneRenderer  extends TileEntitySpecialRenderer<TileEntityWildrootRune> {

    @Override
    public void render(TileEntityWildrootRune tei, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(tei.getRune() != null && tei.getIncenseBurner() != null){
            if(!tei.getRune().isCharged(tei)){
                return;
            }
            if(tei.getWorld().getWorldTime() % 5 == 0){
                RgbColor color = tei.getRune().getColor();
                for(int part = 0; part < 5; part++){
                    ParticleUtil.spawnParticleGlow(getWorld(),
                            tei.getPos().getX() + Util.rand.nextFloat(), tei.getPos().getY() + Util.rand.nextFloat(), tei.getPos().getZ() + Util.rand.nextFloat(), 0,0,0,
                            color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1, 2, 100);
                }
            }

        }
    }

}
