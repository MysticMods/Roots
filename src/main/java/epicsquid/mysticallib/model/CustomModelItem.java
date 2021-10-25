package epicsquid.mysticallib.model;

import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.model.item.BakedModelItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class CustomModelItem extends CustomModelBase {
  public boolean handheld;

  public CustomModelItem(boolean handheld, @Nonnull ResourceLocation... textures) {
    this.handheld = handheld;
    for (int i = 0; i < textures.length; i++) {
      addTexture("layer" + i, textures[i]);
    }
  }

  @Override
  @Nonnull
  public IBakedModel bake(@Nullable IModelState state, @Nonnull VertexFormat format,
      @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
    return new BakedModelItem(format, bakedTextureGetter, this);
  }

  @Override
  @Nonnull
  public IModelState getDefaultState() {
    return TRSRTransformation.identity();
  }
}
