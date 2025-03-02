package mysticmods.roots.client.render;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.entity.projectile.LivingArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LivingArrowRenderer extends ArrowRenderer<LivingArrowEntity> {
  public static final ResourceLocation LIVING_ARROW_TEXTURE = RootsAPI.rl("textures/entity/living_arrow.png");

  public LivingArrowRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public ResourceLocation getTextureLocation(LivingArrowEntity entity) {
    return LIVING_ARROW_TEXTURE;
  }
}
