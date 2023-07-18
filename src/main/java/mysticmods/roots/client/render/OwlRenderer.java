package mysticmods.mysticalworld.client.render;

import mysticmods.mysticalworld.MysticalWorld;
import mysticmods.mysticalworld.client.model.ModelHolder;
import mysticmods.mysticalworld.client.model.OwlModel;
import mysticmods.mysticalworld.entity.OwlEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class OwlRenderer extends MobRenderer<OwlEntity, OwlModel> {

  public OwlRenderer(@Nonnull EntityRendererProvider.Context context) {
    super(context, new OwlModel(context.bakeLayer(ModelHolder.OWL)), 0.25f);
  }

  @Override
  @Nonnull
  public ResourceLocation getTextureLocation(@Nonnull OwlEntity entity) {
    return new ResourceLocation(MysticalWorld.MODID + ":textures/entity/owl.png");
  }
}