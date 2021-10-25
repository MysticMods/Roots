package epicsquid.mysticallib.entity;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * Renders an empty entity
 */
public class RenderNull extends RenderEntity {

  public RenderNull(@Nonnull RenderManager renderManager) {
    super(renderManager);
  }

  @Override
  public void doRender(@Nonnull Entity entity, double x, double y, double z, float yaw, float pTicks) {
    //
  }

  @Override
  public void doRenderShadowAndFire(@Nonnull Entity entity, double x, double y, double z, float yaw, float pTicks) {
    //
  }

  public static class Factory implements IRenderFactory {

    @Override
    public RenderEntity createRenderFor(@Nonnull RenderManager manager) {
      return new RenderNull(manager);
    }
  }
}
