package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.api.spell.SpellStorage;
import mysticmods.roots.client.blockentity.GroveCrafterBlockEntityRenderer;
import mysticmods.roots.client.blockentity.MortarBlockEntityRenderer;
import mysticmods.roots.client.blockentity.PedestalBlockEntityRenderer;
import mysticmods.roots.client.blockentity.PyreBlockEntityRenderer;
import mysticmods.roots.client.model.*;
import mysticmods.roots.client.model.armor.AntlerHatModel;
import mysticmods.roots.client.model.armor.BeetleArmorModel;
import mysticmods.roots.client.render.*;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModItems;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
  @SubscribeEvent
  public static void clientSetup(FMLClientSetupEvent event) {
    ModelHolder.init();

    event.enqueueWork(() -> {
      RenderType cutout = RenderType.cutoutMipped();
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.FEY_LIGHT.get(), cutout);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.CREEPING_GROVE_MOSS.get(), cutout);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.BAFFLECAP.get(), cutout);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_AUBERGINE.get(), cutout);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_BAFFLECAP.get(), cutout);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_STONEPETAL.get(), cutout);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_WILDWOOD_SAPLING.get(), cutout);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.STONEPETAL.get(), cutout);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILDWOOD_SAPLING.get(), cutout);
      RenderType translucent = RenderType.translucent();
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILDWOOD_DOOR.get(), translucent);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILDWOOD_TRAPDOOR.get(), translucent);
      RenderType cutout2 = RenderType.cutout();
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.HANGING_GROVE_MOSS.get(), cutout2);
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILDWOOD_LADDER.get(), cutout2);
    });
  }

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(ModBlockEntities.PEDESTAL.get(), PedestalBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.MORTAR.get(), MortarBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.GROVE_CRAFTER.get(), GroveCrafterBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.PYRE.get(), PyreBlockEntityRenderer::new);
  }

  @SubscribeEvent
  public static void onColorHandlerBlock(RegisterColorHandlersEvent.Block event) {
    event.register((pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null ? BiomeColors.getAverageWaterColor(pLevel, pPos) : -1, ModBlocks.UNENDING_BOWL.get());
  }

  @SubscribeEvent
  public static void onColorHandlerItem(RegisterColorHandlersEvent.Item event) {
    // OVerworldBiome.NORMAL_WATER_COLOR
    event.register((stack, index) -> index == 1 ? 4159204 : -1, ModBlocks.UNENDING_BOWL.get());
    event.register((stack, index) -> {
      if (index != 0) {
        SpellStorage storage = SpellStorage.getOrCreate(stack);
        // TODO: Shouldn't be null but yeah
        if (storage == null) {
          return 0xbae38a; // Just a default bland colour
        }
        SpellInstance spell = storage.getSpell();
        if (spell == null) {
          return 0xbae38a; // Just a default bland colour
        }
        if (index == 1) {
          return spell.getSpell().getColor1();
        } else if (index == 2) {
          return spell.getSpell().getColor2();
        }
      }
      return -1;
    }, ModItems.STAFF.get());
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
  }

  @SubscribeEvent
  public static void registerLayers(EntityRenderersEvent.AddLayers event) {
/*    for (String skin : event.getSkins()) {
      LivingEntityRenderer<Player, PlayerModel<Player>> skinRenderer = event.getSkin(skin);
      if (skinRenderer != null) {
        skinRenderer.addLayer(new ShoulderRenderLayer<>(skinRenderer));
      }
    }*/
  }
}
