package epicsquid.mysticallib.model.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import epicsquid.mysticallib.model.CustomModelItem;
import epicsquid.mysticallib.model.DefaultTransformations;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ItemLayerModel;

public class BakedModelItem implements IBakedModel {

  protected Function<ResourceLocation, TextureAtlasSprite> getter;
  protected VertexFormat format;
  protected ImmutableList.Builder<BakedQuad> quads = ImmutableList.builder();
  protected List<TextureAtlasSprite> layers = new ArrayList<>();
  protected CustomModelItem model;
  protected @Nonnull List<BakedQuad> layerQuads = new ArrayList<>();
  protected TextureAtlasSprite particle = null;

  public BakedModelItem(@Nonnull VertexFormat format, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter,
      @Nonnull CustomModelItem model) {
    this.getter = bakedTextureGetter;
    this.format = format;
    int i = 0;
    this.model = model;
    while (model.textures.containsKey("layer" + i)) {
      layers.add(getter.apply(model.textures.get("layer" + i)));
      layerQuads.addAll(ItemLayerModel.getQuadsForSprite(i, layers.get(i), format, Optional.empty()));
      i++;
    }
    if (model.textures.containsKey("particle")) {
      particle = getter.apply(model.textures.get("particle"));
    }
  }

  @Override
  @Nonnull
  public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
    return layerQuads;
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
    return particle;
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
    if (model.handheld) {
      if (DefaultTransformations.handheldTransforms.containsKey(type)) {
        matrix = DefaultTransformations.handheldTransforms.get(type).getMatrix();
        return Pair.of(this, matrix);
      }
    } else {
      if (DefaultTransformations.itemTransforms.containsKey(type)) {
        matrix = DefaultTransformations.itemTransforms.get(type).getMatrix();
        return Pair.of(this, matrix);
      }
    }
    return net.minecraftforge.client.ForgeHooksClient.handlePerspective(this, type);
  }

}
