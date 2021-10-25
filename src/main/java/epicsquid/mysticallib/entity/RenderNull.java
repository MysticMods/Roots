package epicsquid.mysticallib.entity;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.entity.DefaultRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * Renders an empty entity
 */
public class RenderNull extends DefaultRenderer {

  public RenderNull(@Nonnull EntityRendererManager renderManager) {
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
    public DefaultRenderer createRenderFor(@Nonnull EntityRendererManager manager) {
      return new RenderNull(manager);
    }
  }
}
