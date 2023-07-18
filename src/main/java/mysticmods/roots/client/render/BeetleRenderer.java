package mysticmods.roots.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.BeetleModel;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.client.model.armor.ArmorModel;
import mysticmods.roots.client.player.ShoulderRenderLayer;
import mysticmods.roots.entity.BeetleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class BeetleRenderer extends MobRenderer<BeetleEntity, BeetleModel> {

  public BeetleRenderer(@Nonnull EntityRendererProvider.Context context) {
    super(context, new BeetleModel(context.bakeLayer(ModelHolder.BEETLE)), 0.05f);
    ArmorModel.init(context);
    ShoulderRenderLayer.beetleModel = getModel();
  }

  @Override
  protected void scale(BeetleEntity entity, PoseStack matrix, float partialTickTime) {
    matrix.scale(0.45f, 0.45f, 0.45f);
  }

  @Override
  @Nonnull
  public ResourceLocation getTextureLocation(@Nonnull BeetleEntity entity) {
    return new ResourceLocation(RootsAPI.MODID, "textures/entity/beetle_blue.png");
  }
}