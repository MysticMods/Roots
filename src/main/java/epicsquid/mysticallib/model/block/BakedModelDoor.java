package epicsquid.mysticallib.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.model.CustomModelBase;
import epicsquid.mysticallib.model.ModelUtil;
import epicsquid.mysticallib.model.parts.Cube;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class BakedModelDoor extends BakedModelBlock {

  private Cube cube_east, cube_west, cube_south, cube_north;

  public BakedModelDoor(@Nonnull IModelState state, @Nonnull VertexFormat format, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter,
      @Nonnull CustomModelBase model) {
    super(format, bakedTextureGetter, model);
    TextureAtlasSprite[] texes = new TextureAtlasSprite[] { texwest, texeast, texdown, texup, texnorth, texsouth };
    cube_west = ModelUtil.makeCube(format, 0.8125, 0, 0, 0.1875, 1, 1, null, texes, 0).setNoCull(Direction.WEST);
    cube_east = ModelUtil.makeCube(format, 0, 0, 0, 0.1875, 1, 1, null, texes, 0).setNoCull(Direction.EAST);
    cube_north = ModelUtil.makeCube(format, 0, 0, 0.8125, 1, 1, 0.1875, null, texes, 0).setNoCull(Direction.NORTH);
    cube_south = ModelUtil.makeCube(format, 0, 0, 0, 1, 1, 0.1875, null, texes, 0).setNoCull(Direction.SOUTH);
  }

  @Override
  @Nonnull
  public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, long rand) {
    List<BakedQuad> quads = new ArrayList<>();
    getFaceQuads(quads, side, state);
    return quads;
  }

  private void getFaceQuads(@Nonnull List<BakedQuad> quads, @Nullable Direction side, @Nullable BlockState state) {
    if (state == null) {
      cube_east.addToList(quads, side);
    } else {
      DoorBlock.EnumHingePosition hinge = state.getValue(DoorBlock.HINGE);
      boolean open = state.getValue(DoorBlock.OPEN);
      switch (state.getValue(DoorBlock.FACING)) {
      case EAST:
        if (open) {
          if (hinge == DoorBlock.EnumHingePosition.LEFT) {
            cube_south.addToList(quads, side);
          } else {
            cube_north.addToList(quads, side);
          }
        } else {
          cube_east.addToList(quads, side);
        }
        break;
      case SOUTH:
        if (open) {
          if (hinge == DoorBlock.EnumHingePosition.LEFT) {
            cube_west.addToList(quads, side);
          } else {
            cube_east.addToList(quads, side);
          }
        } else {
          cube_south.addToList(quads, side);
        }
        break;
      case WEST:
        if (open) {
          if (hinge == DoorBlock.EnumHingePosition.LEFT) {
            cube_north.addToList(quads, side);
          } else {
            cube_south.addToList(quads, side);
          }
        } else {
          cube_west.addToList(quads, side);
        }
        break;
      case NORTH:
        if (open) {
          if (hinge == DoorBlock.EnumHingePosition.LEFT) {
            cube_east.addToList(quads, side);
          } else {
            cube_west.addToList(quads, side);
          }
        } else {
          cube_north.addToList(quads, side);
        }
        break;
      }
    }
  }
}
