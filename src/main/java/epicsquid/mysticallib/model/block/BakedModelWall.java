package epicsquid.mysticallib.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.model.CustomModelBase;
import epicsquid.mysticallib.model.ModelUtil;
import epicsquid.mysticallib.model.parts.Cube;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class BakedModelWall extends BakedModelBlock {
  private Cube post, north, south, west, east;

  public BakedModelWall(@Nonnull IModelState state, @Nonnull VertexFormat format, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter,
      @Nonnull CustomModelBase model) {
    super(format, bakedTextureGetter, model);
    TextureAtlasSprite[] texes = new TextureAtlasSprite[] { texwest, texeast, texdown, texup, texnorth, texsouth };
    post = ModelUtil.makeCube(format, 0.25, 0, 0.25, 0.5, 1, 0.5, null, texes, -1);
    north = ModelUtil.makeCube(format, 0.3125, 0, 0, 0.375, 0.875, 0.5, null, texes, -1);
    south = ModelUtil.makeCube(format, 0.3125, 0, 0.5, 0.375, 0.875, 0.5, null, texes, -1);
    west = ModelUtil.makeCube(format, 0, 0, 0.3125, 0.5, 0.875, 0.375, null, texes, -1);
    east = ModelUtil.makeCube(format, 0.5, 0, 0.3125, 0.5, 0.875, 0.375, null, texes, -1);
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
      post.addToList(quads, side);
      west.addToList(quads, side);
      east.addToList(quads, side);
    } else {
      boolean up = state.getValue(BlockWall.UP);
      boolean cnorth = state.getValue(BlockWall.NORTH);
      boolean csouth = state.getValue(BlockWall.SOUTH);
      boolean cwest = state.getValue(BlockWall.WEST);
      boolean ceast = state.getValue(BlockWall.EAST);
      if (up) {
        post.addToList(quads, side);
      }
      if (cnorth) {
        north.addToList(quads, side);
      }
      if (csouth) {
        south.addToList(quads, side);
      }
      if (cwest) {
        west.addToList(quads, side);
      }
      if (ceast) {
        east.addToList(quads, side);
      }
    }
  }

}
