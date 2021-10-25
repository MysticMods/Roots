package epicsquid.mysticallib.model.block;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.model.CustomModelBase;
import epicsquid.mysticallib.model.ModelUtil;
import epicsquid.mysticallib.model.parts.Cube;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class BakedModelCube extends BakedModelBlock {
  private Cube cube;

  public BakedModelCube(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, CustomModelBase model) {
    super(format, bakedTextureGetter, model);
    cube = ModelUtil
        .makeCube(format, 0, 0, 0, 1, 1, 1, ModelUtil.FULL_FACES, new TextureAtlasSprite[] { texwest, texeast, texdown, texup, texnorth, texsouth }, -1);
  }

  @Override
  @Nonnull
  public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
    List<BakedQuad> quads = super.getQuads(state, side, rand);
    cube.addToList(quads, side);
    return quads;
  }

}
