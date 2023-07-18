package mysticmods.mysticalworld.client.render;

import mysticmods.mysticalworld.client.model.DeerModel;
import mysticmods.mysticalworld.client.model.ModelHolder;
import mysticmods.mysticalworld.entity.DeerEntity;
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
      return new ResourceLocation("mysticalworld:textures/entity/rudolph.png");
    }
    return new ResourceLocation("mysticalworld:textures/entity/deer.png");
  }
}
