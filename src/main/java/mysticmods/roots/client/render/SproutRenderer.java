package mysticmods.roots.client.render;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.client.model.SproutModel;
import mysticmods.roots.entity.SproutEntity;
import mysticmods.roots.init.ModEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class SproutRenderer extends MobRenderer<SproutEntity, SproutModel> {
  public SproutRenderer(EntityRendererProvider.Context context) {
    super(context, new SproutModel(context.bakeLayer(ModelHolder.SPROUT)), 0.15f);
  }

  private static Map<EntityType<?>, ResourceLocation> textures = null;

  @Override
  public ResourceLocation getTextureLocation(SproutEntity entity) {
    if (textures == null) {
      textures = new HashMap<>();
      textures.put(ModEntities.GREEN_SPROUT.get(), new ResourceLocation(RootsAPI.MODID, "textures/entity/sprout_green.png"));
      textures.put(ModEntities.TAN_SPROUT.get(), new ResourceLocation(RootsAPI.MODID, "textures/entity/sprout_tan.png"));
      textures.put(ModEntities.PURPLE_SPROUT.get(), new ResourceLocation(RootsAPI.MODID, "textures/entity/sprout_purple.png"));
      textures.put(ModEntities.RED_SPROUT.get(), new ResourceLocation(RootsAPI.MODID, "textures/entity/sprout_red.png"));
    }
    ResourceLocation result = textures.get(entity.getType());
    if (result == null) {
      result = textures.get(ModEntities.RED_SPROUT.get());
    }

    return result;
  }
}
