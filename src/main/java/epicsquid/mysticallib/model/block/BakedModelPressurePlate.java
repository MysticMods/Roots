package epicsquid.mysticallib.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.model.CustomModelBase;
import epicsquid.mysticallib.model.ModelUtil;
import epicsquid.mysticallib.model.parts.Cube;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class BakedModelPressurePlate extends BakedModelBlock {

  private Cube cube_on, cube_off;

  public BakedModelPressurePlate(@Nonnull IModelState state, @Nonnull VertexFormat format,
      @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, @Nonnull CustomModelBase model) {
    super(format, bakedTextureGetter, model);
    TextureAtlasSprite[] texes = new TextureAtlasSprite[] { texwest, texeast, texdown, texup, texnorth, texsouth };
    cube_on = ModelUtil.makeCube(format, 0.0625, 0, 0.0625, 0.875, 0.03125, 0.875, null, texes, 0).setNoCull(Direction.DOWN);
    cube_off = ModelUtil.makeCube(format, 0.0625, 0, 0.0625, 0.875, 0.0625, 0.875, null, texes, 0).setNoCull(Direction.DOWN);
  }

  @Override
  @Nonnull
  public List<net.minecraft.client.renderer.model.BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, long rand) {
    List<net.minecraft.client.renderer.model.BakedQuad> quads = new ArrayList<>();
    getFaceQuads(quads, side, state);
    return quads;
  }

  private void getFaceQuads(@Nonnull List<BakedQuad> quads, @Nullable Direction side, @Nullable BlockState state) {
    if (state == null) {
      cube_off.addToList(quads, side);
    } else {
      boolean on = state.getValue(PressurePlateBlock.POWERED);
      if (on) {
        cube_on.addToList(quads, side);
      } else {
        cube_off.addToList(quads, side);
      }
    }
  }
}
