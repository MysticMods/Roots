package mysticmods.roots.client.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.item.TokenItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class TokenLoader implements IGeometryLoader<TokenLoader.TokenGeometry> {
  private static TokenGeometry instance = null;

  @Override
  public TokenGeometry read(JsonObject contents, JsonDeserializationContext ctx) {
    if (instance == null) {
      instance = new TokenGeometry();
    }
    return instance;
  }


  static class TokenGeometry implements IUnbakedGeometry<TokenGeometry> {
    TokenGeometry() {
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
      return new TokenOverrideModel(spriteGetter.apply(new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation())));
    }

    @Override
    public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
      return Collections.emptyList();
    }
  }

  private static class TokenOverrideModel implements BakedModel {
    private final ItemOverrides overrideList;
    private final TextureAtlasSprite particle;

    public TokenOverrideModel(TextureAtlasSprite particle) {
      this.overrideList = new TokenOverrideList();
      this.particle = particle;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom) {
      return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
      return false;
    }

    @Override
    public boolean isGui3d() {
      return false;
    }

    @Override
    public boolean usesBlockLight() {
      return false;
    }

    @Override
    public boolean isCustomRenderer() {
      return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
      return particle;
    }

    @Override
    public ItemOverrides getOverrides() {
      return overrideList;
    }
  }

  private static class TokenOverrideList extends ItemOverrides {
    @Nullable
    @Override
    public BakedModel resolve(BakedModel pModel, ItemStack pStack, @Nullable ClientLevel pLevel, @Nullable LivingEntity pEntity, int pSeed) {
      ResourceLocation res = TokenItem.getModelLocation(pStack);
      if (res == TokenItem.INVALID_MODEL) {
        return pModel;
      }

      return Minecraft.getInstance().getModelManager().getModel(res);
    }
  }

  @Mod.EventBusSubscriber(modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
  public static class BakeEvents {
    @SubscribeEvent
    public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
      event.register("token_loader", new TokenLoader());
    }

    @SubscribeEvent
    public static void onModelRegister (ModelEvent.RegisterAdditional event) {
      for (ResourceLocation ritual : Registries.RITUAL_REGISTRY.get().getKeys()) {
        event.register(new ResourceLocation(ritual.getNamespace(), "item/ritual_" + ritual.getPath()));
      }
      for (ResourceLocation spell : Registries.SPELL_REGISTRY.get().getKeys()) {
        event.register(new ResourceLocation(spell.getNamespace(), "item/spell_" + spell.getPath()));
      }
    }
  }
}
