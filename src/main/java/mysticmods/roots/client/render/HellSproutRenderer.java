package mysticmods.mysticalworld.client.render;

import mysticmods.mysticalworld.MysticalWorld;
import mysticmods.mysticalworld.client.model.HellSproutModel;
import mysticmods.mysticalworld.client.model.ModelHolder;
import mysticmods.mysticalworld.entity.HellSproutEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HellSproutRenderer extends MobRenderer<HellSproutEntity, HellSproutModel> {
  public HellSproutRenderer(EntityRendererProvider.Context context) {
    super(context, new HellSproutModel(context.bakeLayer(ModelHolder.HELL_SPROUT)), 0.15f);
  }

  public static ResourceLocation TEXTURE = new ResourceLocation(MysticalWorld.MODID, "textures/entity/sprout_hell.png");

  @Override
  public ResourceLocation getTextureLocation(HellSproutEntity entity) {
    return TEXTURE;
  }
}
