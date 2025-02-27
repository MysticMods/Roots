package mysticmods.roots.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class MossModelLoader {
  public static class MossModel implements IUnbakedGeometry<MossModel> {
    private final BlockModel actual;

    public MossModel(BlockModel actual) {
      this.actual = actual;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
      return new BakedMossModel(actual.bake(baker, spriteGetter, modelState), overrides);
    }
  }

  public static class BakedMossModel extends BakedModelWrapper<BakedModel> {
    private final ItemOverrides overrides;
    public BakedMossModel(BakedModel originalModel, ItemOverrides overrides) {
      super(originalModel);
      this.overrides = overrides;
    }

    @Override
    public ItemOverrides getOverrides() {
      return overrides;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
      return super.getQuads(state, side, rand);
    }

    @Override
    public boolean useAmbientOcclusion() {
      return super.useAmbientOcclusion();
    }

    @Override
    public TriState useAmbientOcclusion(BlockState state, ModelData data, RenderType renderType) {
      return super.useAmbientOcclusion(state, data, renderType);
    }

    @Override
    public boolean isGui3d() {
      return super.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
      return super.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
      return super.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
      return super.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
      return super.getTransforms();
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform) {
      return super.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
    }

    @Override
    public TextureAtlasSprite getParticleIcon(ModelData data) {
      return super.getParticleIcon(data);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
      return super.getQuads(state, side, rand, extraData, renderType);
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
      return super.getModelData(level, pos, state, modelData);
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
      return super.getRenderTypes(state, rand, data);
    }

    @Override
    public List<RenderType> getRenderTypes(ItemStack itemStack, boolean fabulous) {
      return super.getRenderTypes(itemStack, fabulous);
    }

    @Override
    public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous) {
      return super.getRenderPasses(itemStack, fabulous);
    }
  }

  public static final class Loader implements IGeometryLoader<MossModel> {
    public static final Loader INSTANCE = new Loader();

    private Loader() {
    }

    @Override
    public MossModel read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
      BlockModel unopened = deserializationContext.deserialize(GsonHelper.getAsJsonObject(modelContents, "model"), BlockModel.class);
      return new MossModel(unopened);
    }
  }
}
