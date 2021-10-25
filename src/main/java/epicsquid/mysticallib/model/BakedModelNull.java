package epicsquid.mysticallib.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class BakedModelNull implements IBakedModel {
  protected Function<ResourceLocation, TextureAtlasSprite> getter;
  protected VertexFormat format;

  public BakedModelNull(@Nullable VertexFormat format, @Nullable Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
    this.getter = bakedTextureGetter;
    this.format = format;
  }

  @Override
  @Nonnull
  public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
    return new ArrayList<>();
  }

  @Override
  public boolean isAmbientOcclusion() {
    return true;
  }

  @Override
  public boolean isGui3d() {
    return true;
  }

  @Override
  public boolean isBuiltInRenderer() {
    return false;
  }

  @Override
  @Nonnull
  public TextureAtlasSprite getParticleTexture() {
    return null;
  }

  @Override
  @Nonnull
  public ItemOverrideList getOverrides() {
    return new ItemOverrideList(Arrays.asList());
  }

  @Override
  @Nonnull
  public Pair<? extends IBakedModel, javax.vecmath.Matrix4f> handlePerspective(@Nonnull ItemCameraTransforms.TransformType type) {
    Matrix4f matrix;
    if (DefaultTransformations.blockTransforms.containsKey(type)) {
      matrix = DefaultTransformations.blockTransforms.get(type).getMatrix();
      return Pair.of(this, matrix);
    }
    return net.minecraftforge.client.ForgeHooksClient.handlePerspective(this, type);
  }

}
