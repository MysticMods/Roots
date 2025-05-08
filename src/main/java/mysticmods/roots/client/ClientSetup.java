package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.client.blockentity.*;
import mysticmods.roots.client.gui.layer.HerbLayer;
import mysticmods.roots.client.gui.layer.HudLayer;
import mysticmods.roots.client.gui.layer.WarningLayer;
import mysticmods.roots.client.gui.screen.*;
import mysticmods.roots.client.layer.AquaBubbleRenderLayer;
import mysticmods.roots.client.model.*;
import mysticmods.roots.client.model.armor.AntlerHatModel;
import mysticmods.roots.client.model.armor.ArmorModel;
import mysticmods.roots.client.model.armor.BeetleArmorModel;
import mysticmods.roots.client.particle.*;
import mysticmods.roots.client.particle.emitter.EntityEmitter;
import mysticmods.roots.client.particle.emitter.SylvanLightEmitter;
import mysticmods.roots.client.render.*;
import mysticmods.roots.init.*;
import mysticmods.roots.item.Dyeable;
import mysticmods.roots.mixin.accessor.AccessorMixinOverworldBiomes;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
    event.registerBlockEntityRenderer(ModBlockEntities.GROVE_STONE.get(), BoundedBlockEntityRenderer::new);
  }

  @SubscribeEvent
  public static void onColorHandlerBlock(RegisterColorHandlersEvent.Block event) {
    event.register((pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null ? BiomeColors.getAverageWaterColor(pLevel, pPos) : -1, ModBlocks.UNENDING_BOWL.get());
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
      Dyeable dye = stack.get(ModAttachments.DYEABLE);
      if (dye == Dyeable.DEFAULT || dye == null) {
        return -1;
      }
      DyeColor color = dye.color();
      if (index == 0 && color != null) {
        return color.getTextureDiffuseColor();
      }
      return -1;
    }, ModItems.HERB_POUCH.get(), ModItems.APOTHECARY_POUCH.get(), ModItems.COMPONENT_POUCH.get());
    // TODO: Different string colours
    ModItems.SYLVAN_POUCH.get();
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
    event.registerEntityRenderer(ModEntities.ROSE_THORNS.value(), RoseThornsRenderer::new);
    event.registerEntityRenderer(ModEntities.LIVING_ARROW.value(), LivingArrowRenderer::new);
    event.registerEntityRenderer(ModEntities.JERBOA.value(), JerboaRenderer::new);
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
  }

  public static final ResourceLocation WARNING_LAYER = RootsAPI.rl("warning");
  public static final ResourceLocation HERB_ALERT_LAYER = RootsAPI.rl("herb_alert");
  public static final ResourceLocation HUD_LAYER = RootsAPI.rl("hud");

  @SubscribeEvent
  public static void registerGuiLayers(RegisterGuiLayersEvent event) {
    event.registerBelow(VanillaGuiLayers.CROSSHAIR, HUD_LAYER, HudLayer::render);
    event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, WARNING_LAYER, WarningLayer::render);
    event.registerBelow(VanillaGuiLayers.HOTBAR, HERB_ALERT_LAYER, HerbLayer::render);
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

    event.registerItem(deferToArmorModel, ModItems.BEETLE_HELMET, ModItems.BEETLE_BOOTS, ModItems.BEETLE_CHESTPLATE, ModItems.BEETLE_LEGGINGS, ModItems.ANTLER_HAT);
  }

  @SubscribeEvent
  public static void onRecipeCategories(RegisterRecipeBookCategoriesEvent event) {
    event.registerRecipeCategoryFinder(ModRecipes.PYRE.get(), (o) -> RecipeBookCategories.UNKNOWN);
    event.registerRecipeCategoryFinder(ModRecipes.MORTAR.get(), (o) -> RecipeBookCategories.UNKNOWN);
    event.registerRecipeCategoryFinder(ModRecipes.GROVE.get(), (o) -> RecipeBookCategories.UNKNOWN);
    event.registerRecipeCategoryFinder(ModRecipes.KNIFE.get(), (o) -> RecipeBookCategories.UNKNOWN);
    event.registerRecipeCategoryFinder(ModRecipes.RUNIC_BLOCK.get(), (o) -> RecipeBookCategories.UNKNOWN);
    event.registerRecipeCategoryFinder(ModRecipes.RUNIC_ENTITY.get(), (o) -> RecipeBookCategories.UNKNOWN);
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
    event.registerSpriteSet(ModParticles.AIR_BUBBLE.get(), AirBubbleParticle.Provider::new);
    event.registerSpriteSet(ModParticles.DISARM.get(), DisarmParticle.Provider::new);

    event.registerSpecial(ModParticles.DISARM_EMITTER.get(), new EntityEmitter.DisarmProvider());
    event.registerSpecial(ModParticles.SYLVAN_LIGHT_EMITTER.get(), new SylvanLightEmitter.Provider());
  }

  @SubscribeEvent
  public static void onRegisterScreens(RegisterMenuScreensEvent event) {
    event.register(ModContainers.HERB_POUCH.get(), HerbPouchScreen::new);
    event.register(ModContainers.SYLVAN_POUCH.get(), SylvanPouchScreen::new);
    event.register(ModContainers.APOTHECARY_POUCH.get(), ApothecaryPouchScreen::new);
    event.register(ModContainers.COMPONENT_POUCH.get(), ComponentPouchScreen::new);
    event.register(ModContainers.QUIVER.get(), QuiverScreen::new);
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
