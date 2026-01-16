package mysticmods.roots.client.render;

import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.client.model.SylvanSpiderModel;
import mysticmods.roots.entity.SylvanSpiderEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.resources.ResourceLocation;

public class SylvanSpiderRenderer extends MobRenderer<SylvanSpiderEntity, SylvanSpiderModel> {
  private static final ResourceLocation SPIDER_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/spider/spider.png");

  public SylvanSpiderRenderer(EntityRendererProvider.Context p_174401_) {
    this(p_174401_, ModelHolder.SYLVAN_SPIDER);
  }

  public SylvanSpiderRenderer(EntityRendererProvider.Context context, ModelLayerLocation layer) {
    super(context, new SylvanSpiderModel(context.bakeLayer(layer)), 0.8F);
    this.addLayer(new SpiderEyesLayer<>(this));
  }

  protected float getFlipDegrees(SylvanSpiderEntity livingEntity) {
    return 180.0F;
  }

  public ResourceLocation getTextureLocation(SylvanSpiderEntity entity) {
    return SPIDER_LOCATION;
  }
}
