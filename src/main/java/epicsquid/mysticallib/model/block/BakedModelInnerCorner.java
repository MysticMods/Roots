package epicsquid.mysticallib.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.block.BlockCornerBase;
import epicsquid.mysticallib.model.CustomModelBase;
import epicsquid.mysticallib.model.ModelUtil;
import epicsquid.mysticallib.model.parts.Segment;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class BakedModelInnerCorner extends BakedModelBlock {
  private Segment segm_down_nxnz_1, segm_down_pxnz_1, segm_down_pxpz_1, segm_down_nxpz_1;
  private Segment segm_down_nxnz_2, segm_down_pxnz_2, segm_down_pxpz_2, segm_down_nxpz_2;
  private Segment segm_up_nxnz_1, segm_up_pxnz_1, segm_up_pxpz_1, segm_up_nxpz_1;
  private Segment segm_up_nxnz_2, segm_up_pxnz_2, segm_up_pxpz_2, segm_up_nxpz_2;

  public BakedModelInnerCorner(@Nonnull IModelState state, @Nonnull VertexFormat format,
      @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, @Nonnull CustomModelBase model) {
    super(format, bakedTextureGetter, model);
    TextureAtlasSprite[] texes = new TextureAtlasSprite[] { texwest, texeast, texdown, texup, texnorth, texsouth };
    segm_down_nxnz_1 = ModelUtil
        .makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, new boolean[] { true, true, true, true, true, false }, texes,
            -1);
    segm_down_nxnz_2 = ModelUtil
        .makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, new boolean[] { false, true, false, true, true, true }, texes,
            -1);
    segm_down_pxnz_1 = ModelUtil
        .makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, new boolean[] { true, true, true, true, true, false }, texes,
            -1);
    segm_down_pxnz_2 = ModelUtil
        .makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, new boolean[] { true, false, false, true, true, true }, texes,
            -1);
    segm_down_pxpz_1 = ModelUtil
        .makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, new boolean[] { true, true, true, true, false, true }, texes,
            -1);
    segm_down_pxpz_2 = ModelUtil
        .makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, new boolean[] { true, false, false, true, true, true }, texes,
            -1);
    segm_down_nxpz_1 = ModelUtil
        .makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, new boolean[] { true, true, true, true, false, true }, texes,
            -1);
    segm_down_nxpz_2 = ModelUtil
        .makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, new boolean[] { false, true, false, true, true, true }, texes,
            -1);
    segm_up_nxnz_1 = ModelUtil
        .makeSegm(format, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, new boolean[] { true, true, true, true, true, false }, texes,
            -1);
    segm_up_nxnz_2 = ModelUtil
        .makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, new boolean[] { false, true, true, false, true, true }, texes,
            -1);
    segm_up_pxnz_1 = ModelUtil
        .makeSegm(format, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, new boolean[] { true, true, true, true, true, false }, texes,
            -1);
    segm_up_pxnz_2 = ModelUtil
        .makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, new boolean[] { true, false, true, false, true, true }, texes,
            -1);
    segm_up_pxpz_1 = ModelUtil
        .makeSegm(format, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, new boolean[] { true, true, true, true, false, true }, texes,
            -1);
    segm_up_pxpz_2 = ModelUtil
        .makeSegm(format, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, new boolean[] { true, false, true, false, true, true }, texes,
            -1);
    segm_up_nxpz_1 = ModelUtil
        .makeSegm(format, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, new boolean[] { true, true, true, true, false, true }, texes,
            -1);
    segm_up_nxpz_2 = ModelUtil
        .makeSegm(format, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, new boolean[] { false, true, true, false, true, true }, texes,
            -1);
  }

  @Override
  @Nonnull
  public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
    List<BakedQuad> quads = new ArrayList<>();
    getFaceQuads(quads, side, state);
    return quads;
  }

  private void getFaceQuads(@Nonnull List<BakedQuad> quads, @Nullable EnumFacing side, @Nullable IBlockState state) {
    if (state == null) {
      segm_down_pxpz_1.addToList(quads, side);
      segm_down_pxpz_2.addToList(quads, side);
    } else {
      boolean up = state.getValue(BlockCornerBase.UP);
      int dir = state.getValue(BlockCornerBase.DIR);
      if (!up) {
        switch (dir) {
        case 2:
          segm_down_nxnz_1.addToList(quads, side);
          segm_down_nxnz_2.addToList(quads, side);
          break;
        case 3:
          segm_down_pxnz_1.addToList(quads, side);
          segm_down_pxnz_2.addToList(quads, side);
          break;
        case 0:
          segm_down_pxpz_1.addToList(quads, side);
          segm_down_pxpz_2.addToList(quads, side);
          break;
        case 1:
          segm_down_nxpz_1.addToList(quads, side);
          segm_down_nxpz_2.addToList(quads, side);
          break;
        }
      } else {
        switch (dir) {
        case 2:
          segm_up_nxnz_1.addToList(quads, side);
          segm_up_nxnz_2.addToList(quads, side);
          break;
        case 3:
          segm_up_pxnz_1.addToList(quads, side);
          segm_up_pxnz_2.addToList(quads, side);
          break;
        case 0:
          segm_up_pxpz_1.addToList(quads, side);
          segm_up_pxpz_2.addToList(quads, side);
          break;
        case 1:
          segm_up_nxpz_1.addToList(quads, side);
          segm_up_nxpz_2.addToList(quads, side);
          break;
        }
      }
    }
  }

}
