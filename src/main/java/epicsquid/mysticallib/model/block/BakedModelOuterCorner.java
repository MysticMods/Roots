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

public class BakedModelOuterCorner extends BakedModelBlock {
  private Segment segm_down_nxnz, segm_down_pxnz, segm_down_pxpz, segm_down_nxpz;
  private Segment segm_up_nxnz, segm_up_pxnz, segm_up_pxpz, segm_up_nxpz;

  public BakedModelOuterCorner(@Nonnull IModelState state, @Nonnull VertexFormat format,
      @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, @Nonnull CustomModelBase model) {
    super(format, bakedTextureGetter, model);
    TextureAtlasSprite[] texes = new TextureAtlasSprite[] { texwest, texeast, texdown, texup, texnorth, texsouth };
    segm_down_nxnz = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, texes, -1);
    segm_down_pxnz = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, texes, -1);
    segm_down_pxpz = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, texes, -1);
    segm_down_nxpz = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, texes, -1);
    segm_up_nxnz = ModelUtil.makeSegm(format, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, -1);
    segm_up_pxnz = ModelUtil.makeSegm(format, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, -1);
    segm_up_pxpz = ModelUtil.makeSegm(format, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, -1);
    segm_up_nxpz = ModelUtil.makeSegm(format, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, -1);
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
      segm_down_pxpz.addToList(quads, side);
    } else {
      boolean up = state.getValue(BlockCornerBase.UP);
      int dir = state.getValue(BlockCornerBase.DIR);
      if (!up) {
        switch (dir) {
        case 0:
          segm_down_nxnz.addToList(quads, side);
          break;
        case 1:
          segm_down_pxnz.addToList(quads, side);
          break;
        case 2:
          segm_down_pxpz.addToList(quads, side);
          break;
        case 3:
          segm_down_nxpz.addToList(quads, side);
          break;
        }
      } else {
        switch (dir) {
        case 0:
          segm_up_nxnz.addToList(quads, side);
          break;
        case 1:
          segm_up_pxnz.addToList(quads, side);
          break;
        case 2:
          segm_up_pxpz.addToList(quads, side);
          break;
        case 3:
          segm_up_nxpz.addToList(quads, side);
          break;
        }
      }
    }
  }

}
