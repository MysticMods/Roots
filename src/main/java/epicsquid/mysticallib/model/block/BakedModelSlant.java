package epicsquid.mysticallib.model.block;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.block.BlockSlantBase;
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

public class BakedModelSlant extends BakedModelBlock {
  private Segment segm_down_north, segm_down_east, segm_down_south, segm_down_west;
  private Segment segm_up_north, segm_up_east, segm_up_south, segm_up_west;
  private Segment segm_mid_nxnz, segm_mid_pxnz, segm_mid_pxpz, segm_mid_nxpz;

  public BakedModelSlant(@Nonnull IModelState state, @Nonnull VertexFormat format, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter,
      @Nonnull CustomModelBase model) {
    super(format, bakedTextureGetter, model);
    TextureAtlasSprite[] texes = new TextureAtlasSprite[] { texwest, texeast, texdown, texup, texnorth, texsouth };
    segm_down_north = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, texes, -1);
    segm_down_south = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, texes, -1);
    segm_down_west = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, texes, -1);
    segm_down_east = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, texes, -1);
    segm_up_north = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, -1);
    segm_up_south = ModelUtil.makeSegm(format, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, -1);
    segm_up_west = ModelUtil.makeSegm(format, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, -1);
    segm_up_east = ModelUtil.makeSegm(format, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, -1);
    segm_mid_nxnz = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, texes, -1);
    segm_mid_pxnz = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, texes, -1);
    segm_mid_pxpz = ModelUtil.makeSegm(format, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, texes, -1);
    segm_mid_nxpz = ModelUtil.makeSegm(format, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, texes, -1);
  }

  @Override
  @Nonnull
  public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
    List<BakedQuad> quads = super.getQuads(state, side, rand);
    if (state == null) {
      segm_down_south.addToList(quads, side);
    } else {
      int vert = state.getValue(BlockSlantBase.VERT);
      int dir = state.getValue(BlockSlantBase.DIR);
      if (vert == 0) {
        switch (dir) {
        case 0:
          segm_down_north.addToList(quads, side);
          break;
        case 1:
          segm_down_south.addToList(quads, side);
          break;
        case 2:
          segm_down_west.addToList(quads, side);
          break;
        case 3:
          segm_down_east.addToList(quads, side);
          break;
        }
      }
      if (vert == 1) {
        switch (dir) {
        case 0:
          segm_mid_nxnz.addToList(quads, side);
          break;
        case 1:
          segm_mid_nxpz.addToList(quads, side);
          break;
        case 2:
          segm_mid_pxpz.addToList(quads, side);
          break;
        case 3:
          segm_mid_pxnz.addToList(quads, side);
          break;
        }
      }
      if (vert == 2) {
        switch (dir) {
        case 0:
          segm_up_north.addToList(quads, side);
          break;
        case 1:
          segm_up_south.addToList(quads, side);
          break;
        case 2:
          segm_up_west.addToList(quads, side);
          break;
        case 3:
          segm_up_east.addToList(quads, side);
          break;
        }
      }
    }
    return quads;
  }

}
