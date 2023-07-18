package mysticmods.roots.client.render;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.client.model.OwlModel;
import mysticmods.roots.entity.OwlEntity;
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
    return new ResourceLocation(RootsAPI.MODID, "textures/entity/owl.png");
  }
}