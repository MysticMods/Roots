package epicsquid.mysticallib.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import epicsquid.mysticallib.model.block.BakedModelBlock;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class CustomModelBlock extends CustomModelBase {
  private Constructor<? extends BakedModelBlock> ctor;

  // TODO make this work with IBlockModel
  public CustomModelBlock(@Nonnull Class<? extends BakedModelBlock> block, @Nonnull ResourceLocation particle, @Nonnull ResourceLocation all) {
    this(block, particle, all, all, all, all, all, all);
  }

  public CustomModelBlock(@Nonnull Class<? extends BakedModelBlock> block, @Nonnull ResourceLocation particle, @Nonnull ResourceLocation west,
      @Nonnull ResourceLocation east, @Nonnull ResourceLocation down, @Nonnull ResourceLocation up, @Nonnull ResourceLocation north,
      @Nonnull ResourceLocation south) {
    try {
      ctor = block.getConstructor(IModelState.class, VertexFormat.class, Function.class, CustomModelBase.class);
    } catch (NoSuchMethodException | SecurityException e) {
      e.printStackTrace();
    }
    addTexture("particle", particle);
    addTexture("north", north);
    addTexture("south", south);
    addTexture("east", east);
    addTexture("west", west);
    addTexture("up", up);
    addTexture("down", down);
  }

  public CustomModelBlock(@Nonnull Class<? extends BakedModelBlock> block, @Nonnull ResourceLocation particle,
      @Nonnull Pair<String, ResourceLocation>... textures) {
    try {
      ctor = block.getConstructor(IModelState.class, VertexFormat.class, Function.class, CustomModelBase.class);
    } catch (NoSuchMethodException | SecurityException e) {
      e.printStackTrace();
    }
    addTexture("particle", particle);
    for (Pair<String, ResourceLocation> p : textures) {
      addTexture(p.getLeft(), p.getRight());
    }
  }

  @Override
  @Nonnull
  public IBakedModel bake(@Nullable IModelState state, @Nonnull VertexFormat format,
      @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
    try {
      return ctor.newInstance(state, format, bakedTextureGetter, this);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return new BakedModelBlock(state, DefaultVertexFormats.BLOCK, bakedTextureGetter, this);
  }

  @Override
  @Nonnull
  public IModelState getDefaultState() {
    return TRSRTransformation.identity();
  }
}
