package epicsquid.mysticallib.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.ItemOverrideList;
import org.apache.commons.lang3.tuple.Pair;

import epicsquid.mysticallib.hax.Hax;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

public class BakedModelBlockUnlitWrapper implements net.minecraft.client.renderer.model.IBakedModel {

  private net.minecraft.client.renderer.model.IBakedModel internal;
  private TreeMap<Integer, List<net.minecraft.client.renderer.model.BakedQuad>> quads = new TreeMap<>();

  public BakedModelBlockUnlitWrapper(@Nonnull IBakedModel model) {
    this.internal = model;
  }

  @Override
  @Nonnull
  public List<net.minecraft.client.renderer.model.BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, long rand) {
    if (side == null) {
      int stateid = Block.getStateId(state);
      if (!quads.containsKey(stateid)) {
        List<BakedQuad> list = new ArrayList<>();
        for (Direction f : Direction.values()) {
          list.addAll(internal.getQuads(state, f, rand));
        }
        list.addAll(internal.getQuads(state, null, rand));
        for (int i = 0; i < list.size(); i++) {
          try {
            Hax.bakedQuadFace.set(list.get(i), null);
          } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
          }
        }
        quads.put(stateid, list);
      }
      return quads.get(stateid);
    }
    return new ArrayList<>();
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
