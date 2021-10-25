package epicsquid.mysticallib.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.model.CustomModelBase;
import epicsquid.mysticallib.model.ModelUtil;
import epicsquid.mysticallib.model.parts.Cube;
import epicsquid.mysticallib.struct.Vec4f;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class BakedModelFence extends BakedModelBlock {

  private Cube post_right, post_left, west, west_top, north, north_top, south, south_top, east, east_top, post;

  public BakedModelFence(@Nonnull IModelState state, @Nonnull VertexFormat format, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter,
      @Nonnull CustomModelBase model) {
    super(format, bakedTextureGetter, model);
    TextureAtlasSprite[] texes = new TextureAtlasSprite[] { texwest, texeast, texdown, texup, texnorth, texsouth };
    post_right = makePostCube(format, 0, 0, 0.375, 0.25, 1, 0.25, null, texes, 0);
    post_left = makePostCube(format, 0.75, 0, 0.375, 0.25, 1, 0.25, null, texes, 0);
    north = ModelUtil.makeCube(format, 0.4375, 0.375, 0, 0.125, 0.1875, 0.375, null, texes, 0);
    south = ModelUtil.makeCube(format, 0.4375, 0.375, 0.625, 0.125, 0.1875, 0.375, null, texes, 0);
    west = ModelUtil.makeCube(format, 0, 0.375, 0.4375, 0.375, 0.1875, 0.125, null, texes, 0);
    east = ModelUtil.makeCube(format, 0.625, 0.375, 0.4375, 0.375, 0.1875, 0.125, null, texes, 0);

    north_top = ModelUtil.makeCube(format, 0.4375, 0.75, 0, 0.125, 0.1875, 0.375, null, texes, 0);
    south_top = ModelUtil.makeCube(format, 0.4375, 0.75, 0.625, 0.125, 0.1875, 0.375, null, texes, 0);
    west_top = ModelUtil.makeCube(format, 0, 0.75, 0.4375, 0.375, 0.1875, 0.125, null, texes, 0);
    east_top = ModelUtil.makeCube(format, 0.625, 0.75, 0.4375, 0.375, 0.1875, 0.125, null, texes, 0);

    post = makePostCube(format, 0.375, 0, 0.375, 0.25, 1, 0.25, null, texes, 0);
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
      post_left.addToList(quads, side);
      post_right.addToList(quads, side);
      west.addToList(quads, side);
      west_top.addToList(quads, side);
    } else {
      boolean cnorth = state.getValue(BlockWall.NORTH);
      boolean csouth = state.getValue(BlockWall.SOUTH);
      boolean cwest = state.getValue(BlockWall.WEST);
      boolean ceast = state.getValue(BlockWall.EAST);

      post.addToList(quads, side);

      if (cnorth) {
        north.addToList(quads, side);
        north_top.addToList(quads, side);
      }
      if (csouth) {
        south.addToList(quads, side);
        south_top.addToList(quads, side);
      }
      if (cwest) {
        west.addToList(quads, side);
        west_top.addToList(quads, side);
      }
      if (ceast) {
        east.addToList(quads, side);
        east_top.addToList(quads, side);
      }
    }
  }

  private Cube makePostCube(VertexFormat format, double x, double y, double z, double w, double h, double l, Vec4f[] uv, TextureAtlasSprite[] sprites,
      int tintIndex) {
    uv = new Vec4f[] { new Vec4f((float) z * 16.0F, (float) (-y) * 16.0F + (16.0F - (float) h * 16.0F), (float) l * 16.0F, (float) h * 16.0F),
        new Vec4f(16.0F - (float) l * 16.0F - (float) z * 16.0F, (float) (-y) * 16.0F + (16.0F - (float) h * 16.0F), (float) l * 16.0F, (float) h * 16.0F),
        new Vec4f(5f, 5f, 6f, 6f), new Vec4f(5f, 5f, 6f, 6f),
        new Vec4f(16.0F - (float) w * 16.0F - (float) x * 16.0F, (float) (-y) * 16.0F + (16.0F - (float) h * 16.0F), (float) w * 16.0F, (float) h * 16.0F),
        new Vec4f((float) x * 16.0F, (float) (-y) * 16.0F + (16.0F - (float) h * 16.0F), (float) w * 16.0F, (float) h * 16.0F) };
    return ModelUtil.makeCube(format, x, y, z, w, h, l, uv, sprites, tintIndex);
  }
}
