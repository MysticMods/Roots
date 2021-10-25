package epicsquid.mysticallib.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import epicsquid.mysticallib.hax.Hax;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class BakedModelColorWrapper implements IBakedModel {
  private IBakedModel internal;
  private List<BakedQuad> quads = new ArrayList<>();

  public BakedModelColorWrapper(@Nonnull IBakedModel model) {
    this.internal = model;
  }

  @Override
  @Nonnull
  public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
    if (quads.size() == 0) {
      List<BakedQuad> list = new ArrayList<>();
      for (EnumFacing f : EnumFacing.values()) {
        list.addAll(internal.getQuads(state, f, rand));
      }
      list.addAll(internal.getQuads(state, null, rand));
      for (int i = 0; i < list.size(); i++) {
        try {
          Hax.bakedQuadTint.set(list.get(i), 0);
        } catch (IllegalArgumentException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      quads.addAll(list);
    }
    return quads;
  }

  @Override
  public boolean isAmbientOcclusion() {
    return internal.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {
    return internal.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {
    return internal.isBuiltInRenderer();
  }

  @Override
  @Nonnull
  public TextureAtlasSprite getParticleTexture() {
    return internal.getParticleTexture();
  }

  @Override
  @Nonnull
  public ItemOverrideList getOverrides() {
    return internal.getOverrides();
  }

  @Override
  @Nonnull
  public Pair<? extends IBakedModel, javax.vecmath.Matrix4f> handlePerspective(@Nonnull ItemCameraTransforms.TransformType type) {
    return internal.handlePerspective(type);
  }

}
