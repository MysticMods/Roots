package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.client.blockentity.*;
import mysticmods.roots.client.gui.layer.*;
import mysticmods.roots.client.gui.screen.*;
import mysticmods.roots.client.item.WildwoodChestItemRenderer;
import mysticmods.roots.client.layer.AquaBubbleRenderLayer;
import mysticmods.roots.client.model.*;
import mysticmods.roots.client.model.armor.AntlerHatModel;
import mysticmods.roots.client.model.armor.ArmorModel;
import mysticmods.roots.client.model.armor.BeetleArmorModel;
import mysticmods.roots.client.particle.bolt.BoltRenderer;
import mysticmods.roots.client.particle.bolt.RenderPreset;
import mysticmods.roots.client.particle.screen.DesaturateScreenParticle;
import mysticmods.roots.client.particle.screen.EmptyProvider;
import mysticmods.roots.client.particle.screen.RiseBounceScreenParticle;
import mysticmods.roots.client.particle.screen.ScreenParticleEngine;
import mysticmods.roots.client.particle.world.*;
import mysticmods.roots.client.particle.world.emitter.EntityEmitter;
import mysticmods.roots.client.particle.world.emitter.SylvanLightEmitter;
import mysticmods.roots.client.render.*;
import mysticmods.roots.init.*;
import mysticmods.roots.item.util.DyeableWithDefault;
import mysticmods.roots.mixin.accessor.AccessorMixinOverworldBiomes;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.io.IOException;

@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT)
public class ClientSetup {
  @SubscribeEvent
  public static void clientSetup(FMLClientSetupEvent event) {
    ModelHolder.init();
  }

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(ModBlockEntities.PEDESTAL.get(), PedestalBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.MORTAR.get(), MortarBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.GROVE_CRAFTER.get(), GroveCrafterBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.PYRE.get(), PyreBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.VISIBLE.get(), VisibleBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.GROVE_STONE.get(), GroveStoneBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.ENCHANTED_TURF.get(), EnchantedTurfBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.FUNGAL_TRANSMUTER.get(), FungalTransmuterBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.WILDWOOD_CHEST.get(), WildwoodChestRenderer::new);
  }

  private static final BlockColor GRASS =
      (p_276237_, p_276238_, p_276239_, p_276240_) -> p_276238_ != null && p_276239_ != null
          ? BiomeColors.getAverageGrassColor(p_276238_, p_276239_)
          : GrassColor.getDefaultColor();

  @SubscribeEvent
  public static void onColorHandlerBlock(RegisterColorHandlersEvent.Block event) {
    event.register((pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null ? BiomeColors.getAverageWaterColor(pLevel, pPos) : -1, ModBlocks.UNENDING_BOWL.get());
    event.register(GRASS,
        ModBlocks.ENCHANTED_TURF.get()
    );
    event.register((pState, pLevel, pPos, pTintIndex) -> {
      return 0x99adad;
    }, ModBlocks.RUNIC_DUST.get());
  }

  @SubscribeEvent
  public static void onColorHandlerItem(RegisterColorHandlersEvent.Item event) {
    event.register((stack, index) -> index == 1 ? AccessorMixinOverworldBiomes.rootsGetNormalWaterColor() : -1, ModBlocks.UNENDING_BOWL.get());
    event.register((stack, index) -> {
      if (index != 0) {
        SpellStorage storage = stack.get(ModAttachments.SPELL_STORAGE);
        if (storage == null) {
          return FastColor.ARGB32.opaque(0xbae38a);
        }
        ISpellInstance spell = storage.getCurrentSpell();
        if (spell == null) {
          return FastColor.ARGB32.opaque(0xbae38a);
        }
        if (index == 1) {
          return FastColor.ARGB32.opaque(spell.getSpell().getColor1());
        } else if (index == 2) {
          return FastColor.ARGB32.opaque(spell.getSpell().getColor2());
        }
      }
      return -1;
    }, ModItems.STAFF.get());
    event.register((stack, index) -> {
      DyeableWithDefault dye = stack.get(ModAttachments.DYEABLE);
      if (dye == DyeableWithDefault.DEFAULT || dye == null) {
        return -1;
      }
      DyeColor color = dye.color();
      if (index == 0 && color != null) {
        return color.getTextureDiffuseColor();
      }
      return -1;
    }, ModItems.HERB_POUCH.get(), ModItems.APOTHECARY_POUCH.get(), ModItems.COMPONENT_POUCH.get());
    event.register((p_92687_, p_92688_) -> {
      BlockState blockstate = ((BlockItem) p_92687_.getItem()).getBlock().defaultBlockState();
      return GRASS.getColor(blockstate, null, null, p_92688_);
    }, ModItems.ENCHANTED_TURF.get());
  }

  @SubscribeEvent
  public static void onRegisterEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(ModEntities.BEETLE.value(), BeetleRenderer::new);
    event.registerEntityRenderer(ModEntities.DEER.value(), DeerRenderer::new);
    event.registerEntityRenderer(ModEntities.DUCK.value(), DuckRenderer::new);
    event.registerEntityRenderer(ModEntities.OWL.value(), OwlRenderer::new);
    event.registerEntityRenderer(ModEntities.FENNEC.value(), FennecRenderer::new);
    event.registerEntityRenderer(ModEntities.GREEN_SPROUT.value(), SproutRenderer::new);
    event.registerEntityRenderer(ModEntities.TAN_SPROUT.value(), SproutRenderer::new);
    event.registerEntityRenderer(ModEntities.RED_SPROUT.value(), SproutRenderer::new);
    event.registerEntityRenderer(ModEntities.PURPLE_SPROUT.value(), SproutRenderer::new);
    event.registerEntityRenderer(ModEntities.SNOW_SPROUT.value(), SproutRenderer::new);
    event.registerEntityRenderer(ModEntities.MELODY_SPROUT.value(), SproutRenderer::new);
    event.registerEntityRenderer(ModEntities.METEOR.value(), MeteorRenderer::new);
    event.registerEntityRenderer(ModEntities.WILDFIRE.value(), WildfireRenderer::new);
    event.registerEntityRenderer(ModEntities.TEMPORAL_MORASS.value(), NoopRenderer::new);
    event.registerEntityRenderer(ModEntities.LIGHT_DRIFTER.value(), NoopRenderer::new);
    event.registerEntityRenderer(ModEntities.ROSE_THORNS.value(), RoseThornsRenderer::new);
    event.registerEntityRenderer(ModEntities.LIVING_ARROW.value(), LivingArrowRenderer::new);
    event.registerEntityRenderer(ModEntities.JERBOA.value(), JerboaRenderer::new);
    event.registerEntityRenderer(ModEntities.SYLVAN_SPIDER.value(), SylvanSpiderRenderer::new);
  }

  @SubscribeEvent
  public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    event.registerLayerDefinition(ModelHolder.BEETLE, BeetleModel::createBodyLayer);
    event.registerLayerDefinition(ModelHolder.DEER, DeerModel::createBodyLayer);
    event.registerLayerDefinition(ModelHolder.DUCK, DuckModel::createBodyLayer);
    event.registerLayerDefinition(ModelHolder.OWL, OwlModel::createBodyLayer);
    event.registerLayerDefinition(ModelHolder.FENNEC, FennecModel::createBodyLayer);
    event.registerLayerDefinition(ModelHolder.SPROUT, SproutModel::createBodyLayer);
    event.registerLayerDefinition(ModelHolder.ANTLER_ARMOR, AntlerHatModel::createBodyLayer);
    event.registerLayerDefinition(ModelHolder.BEETLE_ARMOR, BeetleArmorModel::createBodyLayer);
    event.registerLayerDefinition(ModelHolder.METEOR, MeteorModel::createBodyLayer);
    event.registerLayerDefinition(ModelHolder.ROSE_THORNS, RoseThornsModel::createBodyLayer);
    event.registerLayerDefinition(ModelHolder.JERBOA, JerboaModel::createBodyLayer);
    event.registerLayerDefinition(ModelHolder.SYLVAN_SPIDER, SylvanSpiderModel::createSpiderBodyLayer);
  }

  public static final ResourceLocation WARNING_OVERLAY = RootsAPI.rl("warning");
  public static final ResourceLocation HERB_ALERT_OVERLAY = RootsAPI.rl("herb_alert");
  public static final ResourceLocation HUD_OVERLAY = RootsAPI.rl("hud");
  public static final ResourceLocation AQUA_BUBBLE_OVERLAY = RootsAPI.rl("aqua_bubble");
  public static final ResourceLocation LIGHT_DRIFTER_OVERLAY = RootsAPI.rl("light_drifter");
  public static final ResourceLocation CANCEL_EFFECT_OVERLAY = RootsAPI.rl("cancel_effect");

  @SubscribeEvent
  public static void registerGuiLayers(RegisterGuiLayersEvent event) {
    event.registerBelow(VanillaGuiLayers.CROSSHAIR, HUD_OVERLAY, HudOverlay::render);
    event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, WARNING_OVERLAY, WarningOverlay::render);
    event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, AQUA_BUBBLE_OVERLAY, AquaBubbleOverlay::render);
    event.registerBelow(VanillaGuiLayers.HOTBAR, HERB_ALERT_OVERLAY, HerbOverlay::render);
    event.registerBelow(VanillaGuiLayers.OVERLAY_MESSAGE, LIGHT_DRIFTER_OVERLAY, LightDrifterPositionOverlay::render);
    event.registerBelow(VanillaGuiLayers.OVERLAY_MESSAGE, CANCEL_EFFECT_OVERLAY, CancelEffectOverlay::render);
  }

  @SubscribeEvent
  public static void registerLayers(EntityRenderersEvent.AddLayers event) {
    PlayerRenderer render = event.getSkin(PlayerSkin.Model.WIDE);
    if (render != null) {
      render.addLayer(new AquaBubbleRenderLayer(render));
    }
    render = event.getSkin(PlayerSkin.Model.SLIM);
    if (render != null) {
      render.addLayer(new AquaBubbleRenderLayer(render));
    }
  }

  @SubscribeEvent
  public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
    IClientItemExtensions deferToArmorModel = new IClientItemExtensions() {
      @Override
      public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        HumanoidModel<?> model = ArmorModel.getModel(itemStack);
        return model != null ? model : original;
      }
    };

    IClientItemExtensions wildwoodChest = new IClientItemExtensions() {
      @Override
      public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return WildwoodChestItemRenderer.getInstance();
      }
    };

    event.registerItem(deferToArmorModel, ModItems.BEETLE_HELMET, ModItems.BEETLE_BOOTS, ModItems.BEETLE_CHESTPLATE, ModItems.BEETLE_LEGGINGS, ModItems.ANTLER_HAT);
    event.registerItem(wildwoodChest, ModItems.WILDWOOD_CHEST);
  }

  @SubscribeEvent
  public static void onRecipeCategories(RegisterRecipeBookCategoriesEvent event) {
    event.registerRecipeCategoryFinder(ModRecipes.PYRE.get(), (o) -> RecipeBookCategories.UNKNOWN);
    event.registerRecipeCategoryFinder(ModRecipes.MORTAR.get(), (o) -> RecipeBookCategories.UNKNOWN);
    event.registerRecipeCategoryFinder(ModRecipes.GROVE.get(), (o) -> RecipeBookCategories.UNKNOWN);
    event.registerRecipeCategoryFinder(ModRecipes.KNIFE.get(), (o) -> RecipeBookCategories.UNKNOWN);
    event.registerRecipeCategoryFinder(ModRecipes.RUNIC_BLOCK.get(), (o) -> RecipeBookCategories.UNKNOWN);
    event.registerRecipeCategoryFinder(ModRecipes.RUNIC_ENTITY.get(), (o) -> RecipeBookCategories.UNKNOWN);
    event.registerRecipeCategoryFinder(ModRecipes.SUMMON_CREATURES.get(), (o) -> RecipeBookCategories.UNKNOWN);
    event.registerRecipeCategoryFinder(ModRecipes.TRANSMUTATION.get(), o -> RecipeBookCategories.UNKNOWN);
  }

  // TODO: Fallback to default particle shader if Iris or whatever
  @SubscribeEvent
  public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
    event.registerShader(new ShaderInstance(event.getResourceProvider(), RootsShaders.LOW_DISCARD_PARTICLE_SHADER_LOCATION, DefaultVertexFormat.PARTICLE), (s) -> RootsShaders.LOW_DISCARD_PARTICLE_SHADER = s);
    event.registerShader(new ShaderInstance(event.getResourceProvider(), RootsShaders.DISSOLVE_SHADER_LOCATION, DefaultVertexFormat.BLOCK), (s) -> RootsShaders.DISSOLVE_SHADER = s);
    event.registerShader(new ShaderInstance(event.getResourceProvider(), RootsShaders.RENDERTYPE_ENTITY_CUTOUT_DISSOLVE, DefaultVertexFormat.NEW_ENTITY), (s) -> RootsShaders.RENDERTYPE_ENTITY_CUTOUT_DISSOLVE_SHADER = s);
    event.registerShader(new ShaderInstance(event.getResourceProvider(), RootsShaders.RENDERTYPE_ENTITY_TRANSLUCENT_CULL_DISSOLVE, DefaultVertexFormat.NEW_ENTITY), (s) -> RootsShaders.RENDERTYPE_ENTITY_TRANSLUCENT_CULL_DISSOLVE_SHADER = s);
    event.registerShader(new ShaderInstance(event.getResourceProvider(), RootsShaders.RENDERTYPE_ENTITY_NO_OUTLINE_DISSOLVE, DefaultVertexFormat.NEW_ENTITY), (s) -> RootsShaders.RENDERTYPE_ENTITY_NO_OUTLINE_DISSOLVE_SHADER = s);
    event.registerShader(new ShaderInstance(event.getResourceProvider(), RootsShaders.RENDERTYPE_ENTITY_CUTOUT_NO_CULL_DISSOLVE, DefaultVertexFormat.NEW_ENTITY), (s) -> RootsShaders.RENDERTYPE_ENTITY_CUTOUT_NO_CULL_DISSOLVE_SHADER = s);
    event.registerShader(new ShaderInstance(event.getResourceProvider(), RootsShaders.RENDERTYPE_ENTITY_SOLID_DISSOLVE, DefaultVertexFormat.NEW_ENTITY), (s) -> RootsShaders.RENDERTYPE_ENTITY_SOLID_DISSOLVE_SHADER = s);
    event.registerShader(new ShaderInstance(event.getResourceProvider(), RootsShaders.RENDERTYPE_ENTITY_TRANSLUCENT_DISSOLVE, DefaultVertexFormat.NEW_ENTITY), (s) -> RootsShaders.RENDERTYPE_ENTITY_TRANSLUCENT_DISSOLVE_SHADER = s);
  }

  @SubscribeEvent
  public static void onRegisterParticle(RegisterParticleProvidersEvent event) {
    event.registerSpriteSet(ModParticles.PYRE.get(), PyreParticle.Provider::new);
    event.registerSpriteSet(ModParticles.PYRE_LEAF.get(), PyreLeafParticle.Provider::new);
    event.registerSpriteSet(ModParticles.METEOR.get(), MeteorParticle.Provider::new);
    event.registerSpriteSet(ModParticles.SYLVAN_LIGHT.get(), SylvanLightParticle.Provider::new);
    event.registerSpriteSet(ModParticles.WILDFIRE.get(), WildfireParticle.Provider::new);
    event.registerSpriteSet(ModParticles.GROWTH.get(), GrowthParticle.Provider::new);
    event.registerSpriteSet(ModParticles.CHANNEL_TARGET.get(), ChannelTargetCastParticle.Provider::new);
    event.registerSpriteSet(ModParticles.ANIMAL_HARVEST.get(), AnimalHarvestParticle.Provider::new);
    event.registerSpriteSet(ModParticles.SPROUT_PORTAL.get(), SproutPortalParticle.Provider::new);
    event.registerSpriteSet(ModParticles.SPIRAL.get(), SpiralParticle.Provider::new);
    event.registerSpriteSet(ModParticles.GROVE_STONE.get(), GroveStoneParticle.Provider::new);
    event.registerSpriteSet(ModParticles.WHIRLWIND.get(), WhirlwindParticle.Provider::new);
    event.registerSpriteSet(ModParticles.TEST.get(), TestParticle.Provider::new);
    event.registerSpriteSet(ModParticles.CHANNEL.get(), ChannelCastParticle.Provider::new);
    event.registerSpriteSet(ModParticles.CHANNEL_FAIL.get(), ChannelNoCastParticle.Provider::new);
    event.registerSpriteSet(ModParticles.CHANNEL_JAUNT.get(), ChannelJauntParticle.Provider::new);
    event.registerSpriteSet(ModParticles.AIR_BUBBLE.get(), AirBubbleParticle.Provider::new);
    event.registerSpriteSet(ModParticles.DISARM.get(), DisarmParticle.Provider::new);
    event.registerSpriteSet(ModParticles.SMOKE.get(), SmokeParticle.Provider::new);
    event.registerSpriteSet(ModParticles.WIND.get(), WindParticle.Provider::new);
    event.registerSpriteSet(ModParticles.SKY_SOARER.get(), SkySoarerParticle.Provider::new);
    event.registerSpriteSet(ModParticles.SKY_SOARER_PUFF.get(), SkySoarerPuffParticle.Provider::new);
    event.registerSpriteSet(ModParticles.EXTENSION.get(), ExtensionParticle.Provider::new);
    event.registerSpriteSet(ModParticles.SHATTER_BEAM.get(), ShatterBeamParticle.Provider::new);
    event.registerSpriteSet(ModParticles.MAGNETISM.get(), MagnetismParticle.Provider::new);
    event.registerSpriteSet(ModParticles.PETAL_SHELL.get(), PetalShellParticle.Provider::new);
    event.registerSpriteSet(ModParticles.LIFE_DRAIN.get(), LifeDrainParticle.Provider::new);
    event.registerSpriteSet(ModParticles.LIFE_DRAINED.get(), LifeDrainedParticle.Provider::new);
    event.registerSpriteSet(ModParticles.DANDELION.get(), DandelionParticle.Provider::new);
    event.registerSpriteSet(ModParticles.PETAL.get(), PetalParticle.Provider::new);
    event.registerSpriteSet(ModParticles.HARVEST.get(), HarvestParticle.Provider::new);
    event.registerSpriteSet(ModParticles.DESATURATE.get(), EmptyProvider::new);
    event.registerSpriteSet(ModParticles.SATURATE.get(), HealParticle.Provider::new);
    event.registerSpriteSet(ModParticles.HEAL.get(), HealParticle.Provider::new);
    event.registerSpriteSet(ModParticles.NONDETECTION.get(), NondetectionParticle.Provider::new);
    event.registerSpriteSet(ModParticles.FOG.get(), FogParticle.Provider::new);
    event.registerSpriteSet(ModParticles.SANCTUARY.get(), SanctuaryParticle.Provider::new);
    event.registerSpriteSet(ModParticles.ROSE_THORNS.get(), RoseThornsParticle.Provider::new);
    event.registerSpriteSet(ModParticles.LIGHT.get(), LightParticle.SmallProvider::new);
    event.registerSpriteSet(ModParticles.SPARKLE.get(), LightParticle.SmallProvider::new);
    event.registerSpriteSet(ModParticles.TEMPORAL_MORASS.get(), MoveRandomlyInAABBParticle.Provider::new);
    event.registerSpriteSet(ModParticles.GROVE_CRAFTER.get(), GroveCrafterParticle.Provider::new);

    event.registerSpecial(ModParticles.GROVE_ITEM.get(), new GroveCrafterItemParticle.Provider());
    event.registerSpecial(ModParticles.DISARM_EMITTER.get(), new EntityEmitter.DisarmProvider());
    event.registerSpecial(ModParticles.SKY_SOARER_EMITTER.get(), new EntityEmitter.SkySoarerProvider());
    event.registerSpecial(ModParticles.SYLVAN_LIGHT_EMITTER.get(), new SylvanLightEmitter.Provider());
    event.registerSpecial(ModParticles.LIFE_DRAIN_EMITTER.get(), new EntityEmitter.LifeDrainProvider());

    BoltRenderer.registerRenderPreset(RenderPreset.LIGHTNING, RootsRenderTypes.ROOTS_LIGHTNING);

    ScreenParticleEngine.register(ModParticles.DESATURATE.get(), new DesaturateScreenParticle.Provider());
    ScreenParticleEngine.register(ModParticles.SATURATE.get(), new RiseBounceScreenParticle.Provider());
    ScreenParticleEngine.register(ModParticles.HEAL.get(), new RiseBounceScreenParticle.Provider());
  }

  @SubscribeEvent
  public static void onRegisterScreens(RegisterMenuScreensEvent event) {
    event.register(ModContainers.HERB_POUCH.get(), HerbPouchScreen::new);
    event.register(ModContainers.SYLVAN_POUCH.get(), SylvanPouchScreen::new);
    event.register(ModContainers.APOTHECARY_POUCH.get(), ApothecaryPouchScreen::new);
    event.register(ModContainers.COMPONENT_POUCH.get(), ComponentPouchScreen::new);
    event.register(ModContainers.QUIVER.get(), QuiverScreen::new);
    event.register(ModContainers.MORTAR.get(), MortarScreen::new);
    event.register(ModContainers.PYRE.get(), PyreScreen::new);
    event.register(ModContainers.GROVE.get(), GroveScreen::new);
  }

  public static final ResourceLocation GIFT_BOX_KEY = RootsAPI.rl("item/gift_box");
  public static final ModelResourceLocation GIFT_BOX = new ModelResourceLocation(GIFT_BOX_KEY, "standalone");
  public static BakedModel GIFT_BOX_MODEL;

  public static final ResourceLocation GEAS_KEY = RootsAPI.rl("item/geas");
  public static final ModelResourceLocation GEAS = new ModelResourceLocation(GEAS_KEY, "standalone");
  public static BakedModel GEAS_MODEL;

  public static final ResourceLocation NO_GROVE_STONE_KEY = RootsAPI.rl("item/no_grove_stone");
  public static final ModelResourceLocation NO_GROVE_STONE = new ModelResourceLocation(NO_GROVE_STONE_KEY, "standalone");
  public static BakedModel NO_GROVE_STONE_MODEL;

  @SubscribeEvent
  public static void onRegisterGeometry(ModelEvent.RegisterAdditional event) {
    event.register(GIFT_BOX);
    event.register(GEAS);
    event.register(NO_GROVE_STONE);
  }

  @SubscribeEvent
  public static void onBakeModels(ModelEvent.BakingCompleted event) {
    GIFT_BOX_MODEL = event.getModels().get(GIFT_BOX);
    GEAS_MODEL = event.getModels().get(GEAS);
    NO_GROVE_STONE_MODEL = event.getModels().get(NO_GROVE_STONE);
  }
}
