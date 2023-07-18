package mysticmods.roots.client.render;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.HellSproutModel;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.entity.HellSproutEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HellSproutRenderer extends MobRenderer<HellSproutEntity, HellSproutModel> {
  public HellSproutRenderer(EntityRendererProvider.Context context) {
    super(context, new HellSproutModel(context.bakeLayer(ModelHolder.HELL_SPROUT)), 0.15f);
  }

  public static ResourceLocation TEXTURE = new ResourceLocation(RootsAPI.MODID, "textures/entity/sprout_hell.png");

  @Override
  public ResourceLocation getTextureLocation(HellSproutEntity entity) {
    return TEXTURE;
  }
}
