package mysticmods.roots.client.render;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.FennecModel;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.entity.FennecEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class FennecRenderer extends MobRenderer<FennecEntity, FennecModel> {

  public FennecRenderer(@Nonnull EntityRendererProvider.Context context) {
    super(context, new FennecModel(context.bakeLayer(ModelHolder.FENNEC)), 0.25f);
  }

  @Override
  @Nonnull
  public ResourceLocation getTextureLocation(@Nonnull FennecEntity entity) {
    return RootsAPI.rl("textures/entity/fennec.png");
  }
}
