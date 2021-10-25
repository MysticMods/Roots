package epicsquid.mysticallib.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.model.CustomModelBase;
import epicsquid.mysticallib.model.ModelUtil;
import epicsquid.mysticallib.model.parts.Cube;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class BakedModelTrapDoor extends BakedModelBlock {

  private Cube cube_down, cube_up, cube_east, cube_west, cube_south, cube_north;

  public BakedModelTrapDoor(@Nonnull IModelState state, @Nonnull VertexFormat format,
      @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, @Nonnull CustomModelBase model) {
    super(format, bakedTextureGetter, model);
    TextureAtlasSprite[] texes = new TextureAtlasSprite[] { texwest, texeast, texdown, texup, texnorth, texsouth };
    cube_down = ModelUtil.makeCube(format, 0, 0, 0, 1, 0.1875, 1, null, texes, 0).setNoCull(EnumFacing.DOWN);
    cube_up = ModelUtil.makeCube(format, 0, 0.8125, 0, 1, 0.1875, 1, null, texes, 0).setNoCull(EnumFacing.UP);
    cube_west = ModelUtil.makeCube(format, 0.8125, 0, 0, 0.1875, 1, 1, null, texes, 0).setNoCull(EnumFacing.WEST);
    cube_east = ModelUtil.makeCube(format, 0, 0, 0, 0.1875, 1, 1, null, texes, 0).setNoCull(EnumFacing.EAST);
    cube_north = ModelUtil.makeCube(format, 0, 0, 0.8125, 1, 1, 0.1875, null, texes, 0).setNoCull(EnumFacing.NORTH);
    cube_south = ModelUtil.makeCube(format, 0, 0, 0, 1, 1, 0.1875, null, texes, 0).setNoCull(EnumFacing.SOUTH);
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
      cube_down.addToList(quads, side);
    } else {
      BlockTrapDoor.DoorHalf half = state.getValue(BlockTrapDoor.HALF);
      boolean open = state.getValue(BlockTrapDoor.OPEN);
      if (!open) {
        if (half == BlockTrapDoor.DoorHalf.BOTTOM) {
          cube_down.addToList(quads, side);
        } else {
          cube_up.addToList(quads, side);
        }
      } else {
        switch (state.getValue(BlockTrapDoor.FACING)) {
        case EAST:
          cube_east.addToList(quads, side);
          break;
        case SOUTH:
          cube_south.addToList(quads, side);
          break;
        case WEST:
          cube_west.addToList(quads, side);
          break;
        case NORTH:
          cube_north.addToList(quads, side);
          break;
        }
      }
    }
  }
}
