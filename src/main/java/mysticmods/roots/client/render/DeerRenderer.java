package mysticmods.roots.client.render;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.DeerModel;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.entity.DeerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class DeerRenderer extends MobRenderer<DeerEntity, DeerModel> {

  public DeerRenderer(@Nonnull EntityRendererProvider.Context context) {
    super(context, new DeerModel(context.bakeLayer(ModelHolder.DEER)), 0.35F);
  }

  @Override
  @Nonnull
  public ResourceLocation getTextureLocation(@Nonnull DeerEntity entity) {
    if (entity.getId() % 20 == 0) {
      return RootsAPI.rl("textures/entity/rudolph.png");
    }
    return RootsAPI.rl("textures/entity/deer.png");
  }
}
