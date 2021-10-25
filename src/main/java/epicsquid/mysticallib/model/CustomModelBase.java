package epicsquid.mysticallib.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class CustomModelBase implements IModel {
  public Map<String, ResourceLocation> textures = new HashMap<String, ResourceLocation>();

  public CustomModelBase() {
  }

  @Nonnull
  public CustomModelBase addTexture(@Nonnull String name, @Nonnull ResourceLocation texture) {
    textures.put(name, texture);
    return this;
  }

  @Override
  @Nonnull
  public Collection<ResourceLocation> getDependencies() {
    return ImmutableList.of();
  }

  @Override
  @Nonnull
  public Collection<ResourceLocation> getTextures() {
    return textures.values();
  }

  @Override
  @Nonnull
  public IBakedModel bake(@Nullable IModelState state, @Nonnull VertexFormat format,
      @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
    return new BakedModelNull(DefaultVertexFormats.BLOCK, bakedTextureGetter);
  }

  @Override
  @Nonnull
  public IModelState getDefaultState() {
    return TRSRTransformation.identity();
  }
}
