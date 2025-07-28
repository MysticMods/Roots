package mysticmods.roots.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.JerboaModel;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.entity.JerboaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class JerboaRenderer extends MobRenderer<JerboaEntity, JerboaModel> {

  public JerboaRenderer(@Nonnull EntityRendererProvider.Context context) {
    super(context, new JerboaModel(context.bakeLayer(ModelHolder.JERBOA)), 0.05f);
  }

  @Override
  protected void scale(JerboaEntity entity, PoseStack matrix, float partialTickTime) {
    matrix.scale(0.7f, 0.7f, 0.7f);
  }

  @Override
  @Nonnull
  public ResourceLocation getTextureLocation(@Nonnull JerboaEntity entity) {
    return RootsAPI.rl("textures/entity/jerboa_tan.png");
  }
}