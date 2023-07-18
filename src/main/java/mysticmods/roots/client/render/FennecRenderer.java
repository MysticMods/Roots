package mysticmods.mysticalworld.client.render;

import mysticmods.mysticalworld.MysticalWorld;
import mysticmods.mysticalworld.client.model.ModelHolder;
import mysticmods.mysticalworld.client.model.FennecModel;
import mysticmods.mysticalworld.entity.FennecEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class Fennec extends MobRenderer<FennecEntity, FennecModel> {

  public Fennec(@Nonnull EntityRendererProvider.Context context) {
    super(context, new FennecModel(context.bakeLayer(ModelHolder.FENNEC)), 0.25f);
  }

  @Override
  @Nonnull
  public ResourceLocation getTextureLocation(@Nonnull FennecEntity entity) {
    return new ResourceLocation(MysticalWorld.MODID + ":textures/entity/fennec.png");
  }
}
