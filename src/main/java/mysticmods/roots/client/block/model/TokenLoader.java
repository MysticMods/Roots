package mysticmods.roots.client.block.model;

/*public class TokenLoader implements IGeometryLoader<TokenLoader.TokenGeometry> {
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

  @EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD, defaultValue = Dist.CLIENT)
  public static class BakeEvents {
    @SubscribeEvent
    public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
      event.register("token_loader", new TokenLoader());
    }

    @SubscribeEvent
    public static void onModelRegister (ModelEvent.RegisterAdditional event) {
      for (ResourceLocation ritual : RootsRegistries.RITUAL_REGISTRY.get().getKeys()) {
        event.register(new ResourceLocation(ritual.getNamespace(), "item/ritual_" + ritual.getPath()));
      }
      for (ResourceLocation spell : RootsRegistries.SPELL_REGISTRY.get().getKeys()) {
        event.register(new ResourceLocation(spell.getNamespace(), "item/spell_" + spell.getPath()));
      }
    }
  }
}*/
