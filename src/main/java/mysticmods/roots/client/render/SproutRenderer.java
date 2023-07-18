package mysticmods.mysticalworld.client.render;

import mysticmods.mysticalworld.client.model.ModelHolder;
import mysticmods.mysticalworld.client.model.SproutModel;
import mysticmods.mysticalworld.entity.SproutEntity;
import mysticmods.mysticalworld.init.ModEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SproutRenderer extends MobRenderer<SproutEntity, SproutModel> {
  public SproutRenderer(EntityRendererProvider.Context context) {
    super(context, new SproutModel(context.bakeLayer(ModelHolder.SPROUT)), 0.15f);
  }

  @Override
  public ResourceLocation getTextureLocation(SproutEntity entity) {
    if (entity.getType() == ModEntities.GREEN_SPROUT.get()) {
      return new ResourceLocation("mysticalworld:textures/entity/sprout_green.png");
    } else if (entity.getType() == ModEntities.TAN_SPROUT.get()) {
      return new ResourceLocation("mysticalworld:textures/entity/sprout_tan.png");
    } else if (entity.getType() == ModEntities.PURPLE_SPROUT.get()) {
      return new ResourceLocation("mysticalworld:textures/entity/sprout_purple.png");
    } else {
      return new ResourceLocation("mysticalworld:textures/entity/sprout_red.png");
    }
  }
}
