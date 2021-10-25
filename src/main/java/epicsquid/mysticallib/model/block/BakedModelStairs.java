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
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StairsBlock.EnumHalf;
import net.minecraft.block.StairsBlock.EnumShape;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class BakedModelStairs extends BakedModelBlock {
  private Cube cube_down, cube_up;
  private Cube q_down_nx, q_down_px, q_down_nz, q_down_pz;
  private Cube q_up_nx, q_up_px, q_up_nz, q_up_pz;
  private Cube e_down_nxnz, e_down_pxnz, e_down_pxpz, e_down_nxpz;
  private Cube e_up_nxnz, e_up_pxnz, e_up_pxpz, e_up_nxpz;

  public static Vec4f FULL_FACE_UV = new Vec4f(0, 0, 16, 16);
  public static Vec4f BOTTOM_SIDE_UV = new Vec4f(0, 8, 16, 8);
  public static Vec4f TOP_SIDE_UV = new Vec4f(0, 0, 16, 8);
  public static Vec4f[] bottomUV = new Vec4f[] { BOTTOM_SIDE_UV, BOTTOM_SIDE_UV, FULL_FACE_UV, FULL_FACE_UV, BOTTOM_SIDE_UV, BOTTOM_SIDE_UV };
  public static Vec4f[] topUV = new Vec4f[] { TOP_SIDE_UV, TOP_SIDE_UV, FULL_FACE_UV, FULL_FACE_UV, TOP_SIDE_UV, TOP_SIDE_UV };

  public BakedModelStairs(@Nonnull IModelState state, @Nonnull VertexFormat format, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter,
      @Nonnull CustomModelBase model) {
    super(format, bakedTextureGetter, model);
    TextureAtlasSprite[] texes = new TextureAtlasSprite[] { texwest, texeast, texdown, texup, texnorth, texsouth };
    cube_down = ModelUtil.makeCube(format, 0, 0, 0, 1, 0.5, 1, null, texes, 0).setNoCull(Direction.UP);
    cube_up = ModelUtil.makeCube(format, 0, 0.5, 0, 1, 0.5, 1, null, texes, 0).setNoCull(Direction.DOWN);
    q_up_nx = ModelUtil.makeCube(format, 0, 0.5, 0, 0.5, 0.5, 1, null, texes, 0).setNoCull(Direction.EAST);
    q_up_px = ModelUtil.makeCube(format, 0.5, 0.5, 0, 0.5, 0.5, 1, null, texes, 0).setNoCull(Direction.WEST);
    q_up_nz = ModelUtil.makeCube(format, 0, 0.5, 0, 1, 0.5, 0.5, null, texes, 0).setNoCull(Direction.SOUTH);
    q_up_pz = ModelUtil.makeCube(format, 0, 0.5, 0.5, 1, 0.5, 0.5, null, texes, 0).setNoCull(Direction.NORTH);
    q_down_nx = ModelUtil.makeCube(format, 0, 0, 0, 0.5, 0.5, 1, null, texes, 0).setNoCull(Direction.EAST);
    q_down_px = ModelUtil.makeCube(format, 0.5, 0, 0, 0.5, 0.5, 1, null, texes, 0).setNoCull(Direction.WEST);
    q_down_nz = ModelUtil.makeCube(format, 0, 0, 0, 1, 0.5, 0.5, null, texes, 0).setNoCull(Direction.SOUTH);
    q_down_pz = ModelUtil.makeCube(format, 0, 0, 0.5, 1, 0.5, 0.5, null, texes, 0).setNoCull(Direction.NORTH);
    e_up_nxnz = ModelUtil.makeCube(format, 0, 0.5, 0, 0.5, 0.5, 0.5, null, texes, 0).setNoCull(Direction.EAST).setNoCull(Direction.SOUTH);
    e_up_pxnz = ModelUtil.makeCube(format, 0.5, 0.5, 0, 0.5, 0.5, 0.5, null, texes, 0).setNoCull(Direction.WEST).setNoCull(Direction.SOUTH);
    e_up_pxpz = ModelUtil.makeCube(format, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, null, texes, 0).setNoCull(Direction.WEST).setNoCull(Direction.NORTH);
    e_up_nxpz = ModelUtil.makeCube(format, 0, 0.5, 0.5, 0.5, 0.5, 0.5, null, texes, 0).setNoCull(Direction.EAST).setNoCull(Direction.NORTH);
    e_down_nxnz = ModelUtil.makeCube(format, 0, 0, 0, 0.5, 0.5, 0.5, null, texes, 0).setNoCull(Direction.EAST).setNoCull(Direction.SOUTH);
    e_down_pxnz = ModelUtil.makeCube(format, 0.5, 0, 0, 0.5, 0.5, 0.5, null, texes, 0).setNoCull(Direction.WEST).setNoCull(Direction.SOUTH);
    e_down_pxpz = ModelUtil.makeCube(format, 0.5, 0, 0.5, 0.5, 0.5, 0.5, null, texes, 0).setNoCull(Direction.WEST).setNoCull(Direction.NORTH);
    e_down_nxpz = ModelUtil.makeCube(format, 0, 0, 0.5, 0.5, 0.5, 0.5, null, texes, 0).setNoCull(Direction.EAST).setNoCull(Direction.NORTH);
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
      cube_down.addToList(quads, side);
      q_up_pz.addToList(quads, side);
    } else {
      EnumHalf half = state.getValue(StairsBlock.HALF);
      EnumShape shape = state.getValue(StairsBlock.SHAPE);
      Direction face = state.getValue(StairsBlock.FACING);
      if (half == EnumHalf.BOTTOM) {
        cube_down.addToList(quads, side);
        if (shape == EnumShape.STRAIGHT) {
          switch (face) {
          case DOWN:
            break;
          case EAST:
            q_up_px.addToList(quads, side);
            break;
          case NORTH:
            q_up_nz.addToList(quads, side);
            break;
          case SOUTH:
            q_up_pz.addToList(quads, side);
            break;
          case UP:
            break;
          case WEST:
            q_up_nx.addToList(quads, side);
            break;
          default:
            break;
          }
        }
        if (shape == EnumShape.OUTER_LEFT) {
          switch (face) {
          case DOWN:
            break;
          case EAST:
            e_up_pxnz.addToList(quads, side);
            break;
          case NORTH:
            e_up_nxnz.addToList(quads, side);
            break;
          case SOUTH:
            e_up_pxpz.addToList(quads, side);
            break;
          case UP:
            break;
          case WEST:
            e_up_nxpz.addToList(quads, side);
            break;
          default:
            break;
          }
        }
        if (shape == EnumShape.OUTER_RIGHT) {
          switch (face) {
          case DOWN:
            break;
          case EAST:
            e_up_pxpz.addToList(quads, side);
            break;
          case NORTH:
            e_up_pxnz.addToList(quads, side);
            break;
          case SOUTH:
            e_up_nxpz.addToList(quads, side);
            break;
          case UP:
            break;
          case WEST:
            e_up_nxnz.addToList(quads, side);
            break;
          default:
            break;
          }
        }
        if (shape == EnumShape.INNER_LEFT) {
          switch (face) {
          case DOWN:
            break;
          case EAST:
            q_up_px.addToList(quads, side);
            e_up_nxnz.addToList(quads, side);
            break;
          case NORTH:
            q_up_nz.addToList(quads, side);
            e_up_nxpz.addToList(quads, side);
            break;
          case SOUTH:
            q_up_pz.addToList(quads, side);
            e_up_pxnz.addToList(quads, side);
            break;
          case UP:
            break;
          case WEST:
            q_up_nx.addToList(quads, side);
            e_up_pxpz.addToList(quads, side);
            break;
          default:
            break;
          }
        }
        if (shape == EnumShape.INNER_RIGHT) {
          switch (face) {
          case DOWN:
            break;
          case EAST:
            q_up_px.addToList(quads, side);
            e_up_nxpz.addToList(quads, side);
            break;
          case NORTH:
            q_up_nz.addToList(quads, side);
            e_up_pxpz.addToList(quads, side);
            break;
          case SOUTH:
            q_up_pz.addToList(quads, side);
            e_up_nxnz.addToList(quads, side);
            break;
          case UP:
            break;
          case WEST:
            q_up_nx.addToList(quads, side);
            e_up_pxnz.addToList(quads, side);
            break;
          default:
            break;
          }
        }
      } else if (half == EnumHalf.TOP) {
        cube_up.addToList(quads, side);
        if (shape == EnumShape.STRAIGHT) {
          switch (face) {
          case DOWN:
            break;
          case EAST:
            q_down_px.addToList(quads, side);
            break;
          case NORTH:
            q_down_nz.addToList(quads, side);
            break;
          case SOUTH:
            q_down_pz.addToList(quads, side);
            break;
          case UP:
            break;
          case WEST:
            q_down_nx.addToList(quads, side);
            break;
          default:
            break;
          }
        }
        if (shape == EnumShape.OUTER_LEFT) {
          switch (face) {
          case DOWN:
            break;
          case EAST:
            e_down_pxnz.addToList(quads, side);
            break;
          case NORTH:
            e_down_nxnz.addToList(quads, side);
            break;
          case SOUTH:
            e_down_pxpz.addToList(quads, side);
            break;
          case UP:
            break;
          case WEST:
            e_down_nxpz.addToList(quads, side);
            break;
          default:
            break;
          }
        }
        if (shape == EnumShape.OUTER_RIGHT) {
          switch (face) {
          case DOWN:
            break;
          case EAST:
            e_down_pxpz.addToList(quads, side);
            break;
          case NORTH:
            e_down_pxnz.addToList(quads, side);
            break;
          case SOUTH:
            e_down_nxpz.addToList(quads, side);
            break;
          case UP:
            break;
          case WEST:
            e_down_nxnz.addToList(quads, side);
            break;
          default:
            break;
          }
        }
        if (shape == EnumShape.INNER_LEFT) {
          switch (face) {
          case DOWN:
            break;
          case EAST:
            q_down_px.addToList(quads, side);
            e_down_nxnz.addToList(quads, side);
            break;
          case NORTH:
            q_down_nz.addToList(quads, side);
            e_down_nxpz.addToList(quads, side);
            break;
          case SOUTH:
            q_down_pz.addToList(quads, side);
            e_down_pxnz.addToList(quads, side);
            break;
          case UP:
            break;
          case WEST:
            q_down_nx.addToList(quads, side);
            e_down_pxpz.addToList(quads, side);
            break;
          default:
            break;
          }
        }
        if (shape == EnumShape.INNER_RIGHT) {
          switch (face) {
          case DOWN:
            break;
          case EAST:
            q_down_px.addToList(quads, side);
            e_down_nxpz.addToList(quads, side);
            break;
          case NORTH:
            q_down_nz.addToList(quads, side);
            e_down_pxpz.addToList(quads, side);
            break;
          case SOUTH:
            q_down_pz.addToList(quads, side);
            e_down_nxnz.addToList(quads, side);
            break;
          case UP:
            break;
          case WEST:
            q_down_nx.addToList(quads, side);
            e_down_pxnz.addToList(quads, side);
            break;
          default:
            break;
          }
        }
      }
    }
  }

}
