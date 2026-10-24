package mysticmods.roots.event.setup;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.block.ExtraSkullTypes;
import mysticmods.roots.api.client.RootsClientAPI;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.RenderUtil;
import mysticmods.roots.client.RootsRenderTypes;
import mysticmods.roots.client.RootsShaders;
import mysticmods.roots.client.blockentity.*;
import mysticmods.roots.client.gui.layer.*;
import mysticmods.roots.client.gui.screen.*;
import mysticmods.roots.client.item.WildwoodChestItemRenderer;
import mysticmods.roots.client.layer.AquaBubbleRenderLayer;
import mysticmods.roots.client.layer.DandelionWindsRenderLayer;
import mysticmods.roots.client.model.*;
import mysticmods.roots.client.model.armor.AntlerHatModel;
import mysticmods.roots.client.model.armor.ArmorModel;
import mysticmods.roots.client.model.armor.BeetleArmorModel;
import mysticmods.roots.client.particle.bolt.BoltRenderer;
import mysticmods.roots.client.particle.bolt.RenderPreset;
import mysticmods.roots.client.particle.screen.*;
import mysticmods.roots.client.particle.world.*;
import mysticmods.roots.client.particle.world.block.*;
import mysticmods.roots.client.particle.world.channel.ChannelCastParticle;
import mysticmods.roots.client.particle.world.channel.ChannelJauntParticle;
import mysticmods.roots.client.particle.world.channel.ChannelNoCastParticle;
import mysticmods.roots.client.particle.world.channel.ChannelTargetCastParticle;
import mysticmods.roots.client.particle.world.emitter.EntityEmitter;
import mysticmods.roots.client.particle.world.emitter.SylvanLightEmitter;
import mysticmods.roots.client.particle.world.entity.RoseThornsParticle;
import mysticmods.roots.client.particle.world.entity.SproutPortalParticle;
import mysticmods.roots.client.particle.world.ritual.AnimalHarvestParticle;
import mysticmods.roots.client.particle.world.ritual.MeteorParticle;
import mysticmods.roots.client.particle.world.spell.*;
import mysticmods.roots.client.render.*;
import mysticmods.roots.init.*;
import mysticmods.roots.item.CastingItem;
import mysticmods.roots.item.GramaryItem;
import mysticmods.roots.item.util.DyeableWithDefault;
import mysticmods.roots.mixin.accessor.AccessorMixinOverworldBiomes;
import mysticmods.roots.mixin.client.accessor.AccessorMixinEntityRenderer;
import mysticmods.roots.mixin.client.accessor.AccessorMixinTextureAtlas;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PiglinHeadModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT)
public class ClientSetup {
  public static final ResourceLocation SPELL_PREDICATE = RootsAPI.rl("spell_predicate");
  public static final ResourceLocation STRING_COLOR = RootsAPI.rl("string_color");
  public static final ResourceLocation UNDYED = RootsAPI.rl("undyed");
  public static final BlockColor GRASS =
      (p_276237_, p_276238_, p_276239_, p_276240_) -> p_276238_ != null && p_276239_ != null
          ? BiomeColors.getAverageGrassColor(p_276238_, p_276239_)
          : GrassColor.getDefaultColor();
  public static final ResourceLocation WARNING_OVERLAY = RootsAPI.rl("warning");
  public static final ResourceLocation HERB_ALERT_OVERLAY = RootsAPI.rl("herb_alert");
  public static final ResourceLocation REPUTATION_ALERT_OVERLAY = RootsAPI.rl("reputation_alert");
  public static final ResourceLocation HUD_OVERLAY = RootsAPI.rl("hud");
  public static final ResourceLocation AQUA_BUBBLE_OVERLAY = RootsAPI.rl("aqua_bubble");
  public static final ResourceLocation LIGHT_DRIFTER_OVERLAY = RootsAPI.rl("light_drifter");
  public static final ResourceLocation CANCEL_EFFECT_OVERLAY = RootsAPI.rl("cancel_effect");
  public static final ResourceLocation GIFT_BOX_KEY = RootsAPI.rl("item/gift_box");
  public static final ModelResourceLocation GIFT_BOX_LOCATION = new ModelResourceLocation(GIFT_BOX_KEY, "standalone");
  public static final ResourceLocation GEAS_KEY = RootsAPI.rl("item/geas");
  public static final ModelResourceLocation GEAS_LOCATION = new ModelResourceLocation(GEAS_KEY, "standalone");
  public static final ResourceLocation NO_GROVE_STONE_KEY = RootsAPI.rl("item/no_grove_stone");
  public static final ModelResourceLocation NO_GROVE_STONE_LOCATION = new ModelResourceLocation(NO_GROVE_STONE_KEY, "standalone");
  public static BakedModel GIFT_BOX_MODEL;
  public static BakedModel GEAS_MODEL;
  public static BakedModel NO_GROVE_STONE_MODEL;
  private static Vec2 BLOCKS_ATLAS_SIZE = null;

  @SuppressWarnings("deprecation")
  @SubscribeEvent
  public static void init(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      ItemPropertyFunction func = (stack, level, entity, seed) -> {
        DyeableWithDefault dyeable = stack.get(ModAttachments.DYEABLE);
        if (dyeable == DyeableWithDefault.DEFAULT) {
          return 0;
        }
        return 1;
      };
      ItemProperties.register(ModItems.APOTHECARY_POUCH.get(), UNDYED, func);
      ItemProperties.register(ModItems.COMPONENT_POUCH.get(), UNDYED, func);
      ItemProperties.register(ModItems.HERB_POUCH.get(), UNDYED, func);

      ItemPropertyFunction func2 = (stack, level, entity, seed) -> {
        DyeableWithDefault dyeable = stack.get(ModAttachments.DYEABLE);
        if (dyeable == DyeableWithDefault.DEFAULT || dyeable == null || dyeable.color() == null) {
          return -1;
        }
        return dyeable.color().getId() / 16f;
      };

      ItemProperties.register(ModItems.SYLVAN_POUCH.get(), STRING_COLOR, func2);

      ItemPropertyFunction func3 = (stack, level, entity, seed) -> {
        var spell = CastingItem.getCurrentSpell(level, entity, stack);
        if (spell != null) {
          return spell.getIconPredicate();
        }

        return -1;
      };

      ItemProperties.register(ModItems.SPELL_GROWTH_INFUSION.get(), SPELL_PREDICATE, func3);

      Map<SkullBlock.Type, ResourceLocation> SKIN_LAYERS_BY_TYPE = Util.make(Maps.newHashMap(), m -> {
        m.put(ExtraSkullTypes.BOGGED, ResourceLocation.withDefaultNamespace("textures/entity/skeleton/bogged_overlay.png"));
        m.put(ExtraSkullTypes.STRAY, ResourceLocation.withDefaultNamespace("textures/entity/skeleton/stray_overlay.png"));
        m.put(ExtraSkullTypes.DROWNED, ResourceLocation.withDefaultNamespace("textures/entity/zombie/drowned_outer_layer.png"));
      });

      SkullBlockRenderer.SKIN_BY_TYPE.putAll(Util.make(Maps.newHashMap(), m -> {
        // Additional layers
        m.put(ExtraSkullTypes.BOGGED, ResourceLocation.withDefaultNamespace("textures/entity/skeleton/bogged.png"));
        m.put(ExtraSkullTypes.STRAY, ResourceLocation.withDefaultNamespace("textures/entity/skeleton/stray.png"));
        m.put(ExtraSkullTypes.DROWNED, ResourceLocation.withDefaultNamespace("textures/entity/zombie/drowned.png"));
        m.put(ExtraSkullTypes.HUSK, ResourceLocation.withDefaultNamespace("textures/entity/zombie/husk.png"));
        m.put(ExtraSkullTypes.PIGLIN_BRUTE, ResourceLocation.withDefaultNamespace("textures/entity/piglin/piglin_brute.png"));
        m.put(ExtraSkullTypes.ZOMBIFIED_PIGLIN, ResourceLocation.withDefaultNamespace("textures/entity/piglin/zombified_piglin.png"));
        m.put(ExtraSkullTypes.ZOMBIE_VILLAGER, ResourceLocation.withDefaultNamespace("textures/entity/zombie_villager/zombie_villager.png"));
        m.put(ExtraSkullTypes.ENDERMAN, ResourceLocation.withDefaultNamespace("textures/entity/enderman/enderman.png"));
        m.put(ExtraSkullTypes.WANDERING_TRADER, ResourceLocation.withDefaultNamespace("textures/entity/wandering_trader.png"));
        m.put(ExtraSkullTypes.VILLAGER, ResourceLocation.withDefaultNamespace("textures/entity/villager/villager.png"));
        m.put(ExtraSkullTypes.WITCH, ResourceLocation.withDefaultNamespace("textures/entity/witch.png"));
        m.put(ExtraSkullTypes.PILLAGER, ResourceLocation.withDefaultNamespace("textures/entity/illager/pillager.png"));
        m.put(ExtraSkullTypes.ILLUSIONER, ResourceLocation.withDefaultNamespace("textures/entity/illager/illusioner.png"));
        m.put(ExtraSkullTypes.IRON_GOLEM, ResourceLocation.withDefaultNamespace("textures/entity/iron_golem/iron_golem.png"));
      }));
    });
  }


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
    event.register((stack, index) -> index == 1 ? AccessorMixinOverworldBiomes.roots$GetNormalWaterColor() : -1, ModBlocks.UNENDING_BOWL.get());
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
          return FastColor.ARGB32.opaque(spell.getColor1());
        } else if (index == 2) {
          return FastColor.ARGB32.opaque(spell.getColor2());
        }
      }
      return -1;
    }, ModItems.STAFF.get(), ModItems.CREATIVE_STAFF.get());
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
    event.registerLayerDefinition(ModelHolder.DANDELION_WINDS, DandelionWindsModel::createWindsLayer);

    Supplier<LayerDefinition> humanoid = SkullModel::createHumanoidHeadLayer;
    ;
    Supplier<LayerDefinition> mob = SkullModel::createMobHeadLayer;

    event.registerLayerDefinition(ModelHolder.HUSK_HEAD, humanoid);
    event.registerLayerDefinition(ModelHolder.DROWNED_HEAD, humanoid);
    event.registerLayerDefinition(ModelHolder.ZOMBIFIED_PIGLIN_HEAD, humanoid);
    event.registerLayerDefinition(ModelHolder.BOGGED_HEAD, mob);
    event.registerLayerDefinition(ModelHolder.STRAY_HEAD, mob);
    event.registerLayerDefinition(ModelHolder.ENDERMAN_HEAD, mob);
    event.registerLayerDefinition(ModelHolder.WITCH_HEAD, ClientSetup::createWitchHead);
    event.registerLayerDefinition(ModelHolder.PILLAGER_HEAD, ClientSetup::createPillagerHead);
    event.registerLayerDefinition(ModelHolder.ILLUSIONER_HEAD, ClientSetup::createPillagerHead);
    event.registerLayerDefinition(ModelHolder.VILLAGER_HEAD, ClientSetup::createVillagerHead);
    event.registerLayerDefinition(ModelHolder.ZOMBIE_VILLAGER_HEAD, ClientSetup::createVillagerHead);
    event.registerLayerDefinition(ModelHolder.WANDERING_TRADER_HEAD, ClientSetup::createVillagerHead);
    event.registerLayerDefinition(ModelHolder.IRON_GOLEM_HEAD, ClientSetup::createIronGolemHead);
  }

  // TODO: Iron golem head model
  private static LayerDefinition createIronGolemHead() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    partdefinition.addOrReplaceChild(
        "head",
        CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F).texOffs(24, 0)
            .addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F),
        PartPose.offset(0.0F, -7.0F, -2.0F)
    );
    return LayerDefinition.create(meshdefinition, 128, 128);
  }

  // TODO: Villager head model
  private static LayerDefinition createVillagerHead() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    float f = 0.5F;
    PartDefinition partdefinition1 = partdefinition.addOrReplaceChild(
        "head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.ZERO
    );
    PartDefinition partdefinition2 = partdefinition1.addOrReplaceChild(
        "hat", CubeListBuilder.create().texOffs(32, 0)
            .addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.51F)), PartPose.ZERO
    );
    partdefinition2.addOrReplaceChild(
        "hat_rim",
        CubeListBuilder.create().texOffs(30, 47).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F),
        PartPose.rotation((float) (-Math.PI / 2), 0.0F, 0.0F)
    );
    partdefinition1.addOrReplaceChild(
        "nose", CubeListBuilder.create().texOffs(24, 0)
            .addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, -2.0F, 0.0F)
    );
    return LayerDefinition.create(meshdefinition, 64, 64);
  }

  // TODO: Pillager Head Model
  private static LayerDefinition createPillagerHead() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    PartDefinition partdefinition1 = partdefinition.addOrReplaceChild(
        "head", CubeListBuilder.create().texOffs(0, 0)
            .addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
    );
    partdefinition1.addOrReplaceChild(
        "hat", CubeListBuilder.create().texOffs(32, 0)
            .addBox(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, new CubeDeformation(0.45F)), PartPose.ZERO
    );
    partdefinition1.addOrReplaceChild(
        "nose", CubeListBuilder.create().texOffs(24, 0)
            .addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, -2.0F, 0.0F)
    );
    return LayerDefinition.create(meshdefinition, 64, 64);
  }

  // TODO: Move to Witch Head Model
  private static LayerDefinition createWitchHead() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    PartDefinition partdefinition1 = partdefinition.addOrReplaceChild(
        "head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.ZERO
    );
    PartDefinition partdefinition2 = partdefinition1.addOrReplaceChild(
        "hat", CubeListBuilder.create().texOffs(0, 64)
            .addBox(0.0F, 0.0F, 0.0F, 10.0F, 2.0F, 10.0F), PartPose.offset(-5.0F, -10.03125F, -5.0F)
    );
    PartDefinition partdefinition3 = partdefinition2.addOrReplaceChild(
        "hat2",
        CubeListBuilder.create().texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 7.0F, 4.0F, 7.0F),
        PartPose.offsetAndRotation(1.75F, -4.0F, 2.0F, -0.05235988F, 0.0F, 0.02617994F)
    );
    PartDefinition partdefinition4 = partdefinition3.addOrReplaceChild(
        "hat3",
        CubeListBuilder.create().texOffs(0, 87).addBox(0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F),
        PartPose.offsetAndRotation(1.75F, -4.0F, 2.0F, -0.10471976F, 0.0F, 0.05235988F)
    );
    partdefinition4.addOrReplaceChild(
        "hat4",
        CubeListBuilder.create().texOffs(0, 95).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.25F)),
        PartPose.offsetAndRotation(1.75F, -2.0F, 2.0F, (float) (-Math.PI / 15), 0.0F, 0.10471976F)
    );
    partdefinition1.addOrReplaceChild(
        "nose", CubeListBuilder.create().texOffs(24, 0)
            .addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, -2.0F, 0.0F)
    );
    PartDefinition partdefinition5 = partdefinition1.getChild("nose");
    partdefinition5.addOrReplaceChild(
        "mole",
        CubeListBuilder.create().texOffs(0, 0)
            .addBox(0.0F, 3.0F, -6.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
        PartPose.offset(0.0F, -2.0F, 0.0F)
    );
    return LayerDefinition.create(meshdefinition, 64, 128);
  }

  @SubscribeEvent
  public static void registerGuiLayers(RegisterGuiLayersEvent event) {
    event.registerBelow(VanillaGuiLayers.CROSSHAIR, HUD_OVERLAY, HudOverlay::render);
    event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, WARNING_OVERLAY, WarningOverlay::render);
    event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, AQUA_BUBBLE_OVERLAY, AquaBubbleOverlay::render);
    event.registerBelow(VanillaGuiLayers.HOTBAR, HERB_ALERT_OVERLAY, HerbOverlay::render);
    event.registerBelow(VanillaGuiLayers.HOTBAR, REPUTATION_ALERT_OVERLAY, ReputationOverlay::render);
    event.registerBelow(VanillaGuiLayers.OVERLAY_MESSAGE, LIGHT_DRIFTER_OVERLAY, LightDrifterPositionOverlay::render);
    event.registerBelow(VanillaGuiLayers.OVERLAY_MESSAGE, CANCEL_EFFECT_OVERLAY, CancelEffectOverlay::render);
  }

  @SubscribeEvent
  public static void registerLayers(EntityRenderersEvent.AddLayers event) {
    PlayerRenderer render = event.getSkin(PlayerSkin.Model.WIDE);
    if (render != null) {
      render.addLayer(new AquaBubbleRenderLayer(render));
      render.addLayer(new DandelionWindsRenderLayer<>(render, event.getEntityModels()));
    }
    render = event.getSkin(PlayerSkin.Model.SLIM);
    if (render != null) {
      render.addLayer(new AquaBubbleRenderLayer(render));
      render.addLayer(new DandelionWindsRenderLayer<>(render, event.getEntityModels()));
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

  @SubscribeEvent
  public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
    event.registerShader(new ShaderInstance(event.getResourceProvider(), RootsShaders.LOW_DISCARD_PARTICLE_SHADER_LOCATION, DefaultVertexFormat.PARTICLE), (s) -> RootsShaders.LOW_DISCARD_PARTICLE_SHADER = s);
    event.registerShader(new ShaderInstance(event.getResourceProvider(), RootsShaders.CARDIOID_PARTICLE_SHADER_LOCATION, RootsRenderTypes.CARDIOID_PARTICLE), (s) -> RootsShaders.CARDIOID_PARTICLE_SHADER = s);
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
    event.registerSpriteSet(ModParticles.RANK.get(), EmptyProvider::new);
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
    ScreenParticleEngine.register(ModParticles.RANK.get(), new RankScreenParticle.Provider());
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
    event.register(ModContainers.TRANSMUTER.get(), TransmuterScreen::new);
  }

  @SubscribeEvent
  public static void onRegisterGeometry(ModelEvent.RegisterAdditional event) {
    event.register(GIFT_BOX_LOCATION);
    event.register(GEAS_LOCATION);
    event.register(NO_GROVE_STONE_LOCATION);
  }

  @SubscribeEvent
  public static void onBakeModels(ModelEvent.BakingCompleted event) {
    GIFT_BOX_MODEL = event.getModels().get(GIFT_BOX_LOCATION);
    GEAS_MODEL = event.getModels().get(GEAS_LOCATION);
    NO_GROVE_STONE_MODEL = event.getModels().get(NO_GROVE_STONE_LOCATION);
  }

  @SubscribeEvent
  public static void onAtlasCreation(RegisterMaterialAtlasesEvent event) {
    event.register(RootsAPI.OVERLAYS_ATLAS_FILE, RootsAPI.OVERLAYS_ATLAS);
  }

  public static Vec2 getBlocksAtlasSize() {
    if (BLOCKS_ATLAS_SIZE == null) {
      RootsAPI.LOG.error("Blocks atlas size not initialized, fetching from Minecraft instance.");
      TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
      BLOCKS_ATLAS_SIZE = new Vec2(((AccessorMixinTextureAtlas) atlas).roots$GetWidth(), ((AccessorMixinTextureAtlas) atlas).roots$GetHeight());
    }
    return BLOCKS_ATLAS_SIZE;
  }

  @SubscribeEvent
  public static void onRenderNameTag(RenderNameTagEvent event) {
    if (event.getEntity().getType()
        .equals(EntityType.PLAYER) && event.getEntity() instanceof Player player && player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      event.setCanRender(TriState.TRUE);
    }
  }

  @SubscribeEvent
  public static void onLevelDestroyed(LevelEvent.Unload event) {
    RenderUtil.clearEntityMap();
  }

  @SubscribeEvent
  public static void onClientLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
    RenderUtil.clearEntityMap();
  }

  @SubscribeEvent
  public static void onTextureStitched(TextureAtlasStitchedEvent event) {
    TextureAtlas atlas = event.getAtlas();
    if (atlas.location().equals(TextureAtlas.LOCATION_BLOCKS)) {
      BLOCKS_ATLAS_SIZE = new Vec2(((AccessorMixinTextureAtlas) atlas).roots$GetWidth(), ((AccessorMixinTextureAtlas) atlas).roots$GetHeight());
    }
  }

  @SubscribeEvent
  public static void onRecipeUpdate(RecipesUpdatedEvent event) {
    ResolvedRecipes.reset();
  }

  @SubscribeEvent
  public static void onEntityRender(RenderLivingEvent.Post<?, ?> event) {
    if (RootsClientAPI.isGramaryMode(GramaryItem.GramaryMode.ENTITY_INFORMATION) && event.getEntity().getType()
        .is(RootsTags.Entities.SHOULD_RENDER_HUD)) {
    }

    if (event.getEntity().getData(ModAttachments.HAS_GEAS)) {
      LivingEntityRenderer<?, ?> renderer = event.getRenderer();
      LivingEntity entity = event.getEntity();
      PoseStack poseStack = event.getPoseStack();
      MultiBufferSource buffer = event.getMultiBufferSource();
      float partialTicks = event.getPartialTick();
      Vec3 vec3 = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(partialTicks));
      if (vec3 != null) {
        poseStack.pushPose();
        float ticks = RenderTickHandler.getClientTicks() + partialTicks;
        float bobbingOffset = Mth.sin(ticks * 0.1f) * 0.05f;
        float pulse = 0.05f * Mth.sin(ticks * 0.1f) + 0.95f;
        poseStack.translate(0f, entity.getBbHeight() + entity.getBbHeight() * 0.2 + bobbingOffset, 0);
        poseStack.mulPose(((AccessorMixinEntityRenderer) renderer).roots$GetEntityRenderDispatcher()
            .cameraOrientation());
        poseStack.scale(0.3f * pulse, 0.3f * pulse, 0.3f * pulse);
        poseStack.translate(-0.5f, 0, -0.5f);
        // Re: #1289 this should only execute if the entity has geas
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
            poseStack.last(),
            buffer.getBuffer(Sheets.translucentItemSheet()),
            null,
            GEAS_MODEL,
            1,
            1,
            1,
            LightTexture.FULL_SKY,
            OverlayTexture.NO_OVERLAY,
            ModelData.EMPTY,
            Sheets.translucentItemSheet());
        poseStack.popPose();
      }
    }
  }

  @SubscribeEvent
  public static void onSkullModels(EntityRenderersEvent.CreateSkullModels event) {
    EntityModelSet models = event.getEntityModelSet();

    event.registerSkullModel(ExtraSkullTypes.PIGLIN_BRUTE, new PiglinHeadModel(models.bakeLayer(ModelLayers.PIGLIN_HEAD)));
  }
}
