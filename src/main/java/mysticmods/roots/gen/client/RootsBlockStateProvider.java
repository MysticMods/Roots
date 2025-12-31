package mysticmods.roots.gen.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.block.*;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.mixin.accessor.AccessorMixinCropBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class RootsBlockStateProvider extends BlockStateProvider {
  public RootsBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, RootsAPI.MODID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    simpleBlock(ModBlocks.THATCH.get());
    simpleBlock(ModBlocks.RUNESTONE.get());
    simpleBlock(ModBlocks.MOSSY_RUNESTONE.get());
    simpleBlock(ModBlocks.CHISELED_RUNESTONE.get());
    simpleBlock(ModBlocks.RUNESTONE_BRICK.get());
    simpleBlock(ModBlocks.RUNESTONE_TILE.get());
    simpleBlock(ModBlocks.RUNED_OBSIDIAN.get());
    simpleBlock(ModBlocks.RUNED_BRICK.get());
    simpleBlock(ModBlocks.RUNED_TILE.get());
    simpleBlock(ModBlocks.CHISELED_RUNED_OBSIDIAN.get());
    simpleBlock(ModBlocks.SILVER_ORE.get());
    simpleBlock(ModBlocks.DEEPSLATE_SILVER_ORE.get());
    simpleBlock(ModBlocks.GRANITE_QUARTZ_ORE.get());
    simpleBlock(ModBlocks.RAW_SILVER_BLOCK.get());
    simpleBlock(ModBlocks.SILVER_BLOCK.get());
    logBlock(ModBlocks.WILDWOOD_LOG.get());
    logBlock(ModBlocks.STRIPPED_WILDWOOD_LOG.get());
    axisBlock(ModBlocks.WILDWOOD_WOOD.get(), RootsAPI.rl("block/wildwood_log"), RootsAPI.rl("block/wildwood_log"));
    axisBlock(ModBlocks.STRIPPED_WILDWOOD_WOOD.get(), RootsAPI.rl("block/stripped_wildwood_log"), RootsAPI.rl("block/stripped_wildwood_log"));
    simpleBlock(ModBlocks.WILDWOOD_PLANKS.get());
    ModelFile crop = models().getExistingFile(ResourceLocation.withDefaultNamespace("block/cross"));
    getVariantBuilder(ModBlocks.WILDWOOD_SAPLING.get())
        .forAllStates(state -> {
          ModelFile stage = models().getBuilder("block/wildwood_sapling")
              .parent(crop)
              .texture("cross", modLoc("block/wildwood_sapling")).renderType("cutout");
          return ConfiguredModel.builder().modelFile(stage).build();
        });
    getVariantBuilder(ModBlocks.STONEPETAL.get()).partialState()
        .setModels(new ConfiguredModel(models().cross("stonepetal", blockTexture(ModBlocks.STONEPETAL.get()))
            .renderType("cutout")));
    // TODO: Render type
    simpleBlock(ModBlocks.WILDWOOD_LEAVES.get());
    stairsBlock(ModBlocks.RUNESTONE_STAIRS.get(), blockTexture(ModBlocks.RUNESTONE.get()));
    stairsBlock(ModBlocks.RUNESTONE_BRICK_STAIRS.get(), blockTexture(ModBlocks.RUNESTONE_BRICK.get()));
    stairsBlock(ModBlocks.MOSSY_RUNESTONE_STAIRS.get(), blockTexture(ModBlocks.MOSSY_RUNESTONE.get()));
    stairsBlock(ModBlocks.RUNESTONE_TILE_STAIRS.get(), blockTexture(ModBlocks.RUNESTONE_TILE.get()));
    stairsBlock(ModBlocks.RUNED_STAIRS.get(), blockTexture(ModBlocks.RUNED_OBSIDIAN.get()));
    stairsBlock(ModBlocks.RUNED_BRICK_STAIRS.get(), blockTexture(ModBlocks.RUNED_BRICK.get()));
    stairsBlock(ModBlocks.RUNED_TILE_STAIRS.get(), blockTexture(ModBlocks.RUNED_TILE.get()));
    stairsBlock(ModBlocks.WILDWOOD_STAIRS.get(), blockTexture(ModBlocks.WILDWOOD_PLANKS.get()));
    slabBlock(ModBlocks.RUNESTONE_SLAB.get(), blockTexture(ModBlocks.RUNESTONE.get()), blockTexture(ModBlocks.RUNESTONE.get()));
    slabBlock(ModBlocks.RUNESTONE_BRICK_SLAB.get(), blockTexture(ModBlocks.RUNESTONE_BRICK.get()), blockTexture(ModBlocks.RUNESTONE_BRICK.get()));
    slabBlock(ModBlocks.MOSSY_RUNESTONE_SLAB.get(), blockTexture(ModBlocks.MOSSY_RUNESTONE.get()), blockTexture(ModBlocks.MOSSY_RUNESTONE.get()));
    slabBlock(ModBlocks.RUNESTONE_TILE_SLAB.get(), blockTexture(ModBlocks.RUNESTONE_TILE.get()), blockTexture(ModBlocks.RUNESTONE_TILE.get()));
    slabBlock(ModBlocks.RUNED_SLAB.get(), blockTexture(ModBlocks.RUNED_OBSIDIAN.get()), blockTexture(ModBlocks.RUNED_OBSIDIAN.get()));
    slabBlock(ModBlocks.RUNED_BRICK_SLAB.get(), blockTexture(ModBlocks.RUNED_BRICK.get()), blockTexture(ModBlocks.RUNED_BRICK.get()));
    slabBlock(ModBlocks.RUNED_TILE_SLAB.get(), blockTexture(ModBlocks.RUNED_TILE.get()), blockTexture(ModBlocks.RUNED_TILE.get()));
    slabBlock(ModBlocks.WILDWOOD_SLAB.get(), blockTexture(ModBlocks.WILDWOOD_PLANKS.get()), blockTexture(ModBlocks.WILDWOOD_PLANKS.get()));
    fenceBlock(ModBlocks.WILDWOOD_FENCE.get(), blockTexture(ModBlocks.WILDWOOD_PLANKS.get()));
    fenceGateBlock(ModBlocks.WILDWOOD_GATE.get(), blockTexture(ModBlocks.WILDWOOD_PLANKS.get()));
    buttonBlock(ModBlocks.RUNESTONE_BUTTON.get(), blockTexture(ModBlocks.RUNESTONE.get()));
    buttonBlock(ModBlocks.RUNESTONE_BRICK_BUTTON.get(), blockTexture(ModBlocks.RUNESTONE_BRICK.get()));
    buttonBlock(ModBlocks.MOSSY_RUNESTONE_BUTTON.get(), blockTexture(ModBlocks.MOSSY_RUNESTONE.get()));
    buttonBlock(ModBlocks.RUNESTONE_TILE_BUTTON.get(), blockTexture(ModBlocks.RUNESTONE_TILE.get()));
    buttonBlock(ModBlocks.RUNED_BUTTON.get(), blockTexture(ModBlocks.RUNED_OBSIDIAN.get()));
    buttonBlock(ModBlocks.RUNED_BRICK_BUTTON.get(), blockTexture(ModBlocks.RUNED_BRICK.get()));
    buttonBlock(ModBlocks.RUNED_TILE_BUTTON.get(), blockTexture(ModBlocks.RUNED_TILE.get()));
    buttonBlock(ModBlocks.WILDWOOD_BUTTON.get(), blockTexture(ModBlocks.WILDWOOD_PLANKS.get()));
    pressurePlateBlock(ModBlocks.RUNESTONE_PRESSURE_PLATE.get(), blockTexture(ModBlocks.RUNESTONE.get()));
    pressurePlateBlock(ModBlocks.RUNESTONE_BRICK_PRESSURE_PLATE.get(), blockTexture(ModBlocks.RUNESTONE_BRICK.get()));
    pressurePlateBlock(ModBlocks.MOSSY_RUNESTONE_PRESSURE_PLATE.get(), blockTexture(ModBlocks.MOSSY_RUNESTONE.get()));
    pressurePlateBlock(ModBlocks.RUNESTONE_TILE_PRESSURE_PLATE.get(), blockTexture(ModBlocks.RUNESTONE_TILE.get()));
    pressurePlateBlock(ModBlocks.RUNED_PRESSURE_PLATE.get(), blockTexture(ModBlocks.RUNED_OBSIDIAN.get()));
    pressurePlateBlock(ModBlocks.RUNED_BRICK_PRESSURE_PLATE.get(), blockTexture(ModBlocks.RUNED_BRICK.get()));
    pressurePlateBlock(ModBlocks.RUNED_TILE_PRESSURE_PLATE.get(), blockTexture(ModBlocks.RUNED_TILE.get()));
    pressurePlateBlock(ModBlocks.WILDWOOD_PRESSURE_PLATE.get(), blockTexture(ModBlocks.WILDWOOD_PLANKS.get()));
    doorBlockWithRenderType(ModBlocks.WILDWOOD_DOOR.get(), "wildwood", modLoc("block/wildwood_door_bottom"), modLoc("block/wildwood_door_top"), "translucent");
    trapdoorBlockWithRenderType(ModBlocks.WILDWOOD_TRAPDOOR.get(), "wildwood", modLoc("block/wildwood_trapdoor"), true, "translucent");
    horizontalBlock(ModBlocks.WILDWOOD_LADDER.get(), models().withExistingParent("wildwood_ladder", mcLoc("block/ladder"))
        .texture("texture", modLoc("block/wildwood_ladder")).texture("particle", modLoc("block/wildwood_ladder"))
        .renderType("cutout"));
    wallBlock(ModBlocks.RUNESTONE_WALL.get(), blockTexture(ModBlocks.RUNESTONE.get()));
    wallBlock(ModBlocks.RUNESTONE_BRICK_WALL.get(), blockTexture(ModBlocks.RUNESTONE_BRICK.get()));
    wallBlock(ModBlocks.MOSSY_RUNESTONE_WALL.get(), blockTexture(ModBlocks.MOSSY_RUNESTONE.get()));
    wallBlock(ModBlocks.RUNESTONE_TILE_WALL.get(), blockTexture(ModBlocks.RUNESTONE_TILE.get()));
    wallBlock(ModBlocks.RUNED_WALL.get(), blockTexture(ModBlocks.RUNED_OBSIDIAN.get()));
    wallBlock(ModBlocks.RUNED_BRICK_WALL.get(), blockTexture(ModBlocks.RUNED_BRICK.get()));
    wallBlock(ModBlocks.RUNED_TILE_WALL.get(), blockTexture(ModBlocks.RUNED_TILE.get()));
    simpleBlock(ModBlocks.ELEMENTAL_SOIL.get());
    ModelFile aqueousPillar = models().cubeBottomTop(ModBlocks.AQUEOUS_SOIL.getKey().location()
        .getPath(), modLoc("block/water_soil_side"), modLoc("block/elemental_soil"), modLoc("block/water_soil_top"));
    getVariantBuilder(ModBlocks.AQUEOUS_SOIL.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(aqueousPillar).build());
    ModelFile caelicPillar = models().cubeBottomTop(ModBlocks.CAELIC_SOIL.getKey().location()
        .getPath(), modLoc("block/air_soil_side"), modLoc("block/elemental_soil"), modLoc("block/air_soil_top"));
    getVariantBuilder(ModBlocks.CAELIC_SOIL.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(caelicPillar).build());
    ModelFile terranPillar = models().cubeBottomTop(ModBlocks.TERRAN_SOIL.getKey().location()
        .getPath(), modLoc("block/earth_soil_side"), modLoc("block/elemental_soil"), modLoc("block/earth_soil_top"));
    getVariantBuilder(ModBlocks.TERRAN_SOIL.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(terranPillar).build());
    ModelFile fieryPillar = models().cubeBottomTop(ModBlocks.MAGMATIC_SOIL.getKey().location()
        .getPath(), modLoc("block/fire_soil_side"), modLoc("block/elemental_soil"), modLoc("block/fire_soil_top"));
    getVariantBuilder(ModBlocks.MAGMATIC_SOIL.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(fieryPillar).build());
    ModelFile ritualPedestal = models().withExistingParent("ritual_pedestal", modLoc("block/complex/ritual_pedestal"));
    getVariantBuilder(ModBlocks.RITUAL_PEDESTAL.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(ritualPedestal).build());
    ModelFile reinforcedRitualPedestal = models().withExistingParent("reinforced_ritual_pedestal", modLoc("block/complex/reinforced_ritual_pedestal"));
    getVariantBuilder(ModBlocks.REINFORCED_RITUAL_PEDESTAL.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(reinforcedRitualPedestal).build());
    ModelFile existingModel = models().withExistingParent("enchanted_turf", mcLoc("block/grass_block"))
        .renderType("cutout");
    getVariantBuilder(ModBlocks.ENCHANTED_TURF.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(existingModel).build());
    ModelFile existingGroveCrafterActive = models().withExistingParent("grove_crafter_active", modLoc("block/complex/grove_crafter_active"));
    ModelFile existingGroveCrafterInactive = models().withExistingParent("grove_crafter_inactive", modLoc("block/complex/grove_crafter_inactive"));
    getVariantBuilder(ModBlocks.GROVE_CRAFTER.get()).forAllStates(state -> {
      if (state.getValue(StateProperties.ACTIVE)) {
        return ConfiguredModel.builder().modelFile(existingGroveCrafterActive).build();
      } else {
        return ConfiguredModel.builder().modelFile(existingGroveCrafterInactive).build();
      }
    });
    ModelFile grovePedestal = models().withExistingParent("grove_pedestal", modLoc("block/complex/grove_pedestal"));
    getVariantBuilder(ModBlocks.GROVE_PEDESTAL.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(grovePedestal).build());
    ModelFile wildwoodPedestal = models().withExistingParent("wildwood_pedestal", modLoc("block/complex/wildwood_pedestal"));
    getVariantBuilder(ModBlocks.WILDWOOD_PEDESTAL.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(wildwoodPedestal).build());
    ModelFile displayPedestal = models().withExistingParent("display_pedestal", modLoc("block/complex/grove_pedestal"));
    getVariantBuilder(ModBlocks.DISPLAY_PEDESTAL.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(displayPedestal).build());
    /*    ModelFile growthAmplifier = models().withExistingParent("growth_amplifier", modLoc("block/complex/grove_amplifier"));*/
/*    getVariantBuilder(ModBlocks.GROWTH_AMPLIFIER.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(growthAmplifier).build());*/
    // Wild roots are existing
    simpleBlock(ModBlocks.CREEPING_GROVE_MOSS.get(), models().singleTexture(ModBlocks.CREEPING_GROVE_MOSS.getKey()
            .location().getPath(), mcLoc("block/carpet"), "wool", modLoc("block/creeping_grove_moss"))
        .renderType("cutout"));
    ModelFile hangingGroveMoss = models().withExistingParent("hanging_grove_moss", modLoc("block/complex/hanging_grove_moss"))
        .renderType("cutout");
    getVariantBuilder(ModBlocks.HANGING_GROVE_MOSS.get())
        .forAllStates(state -> {
          Direction dir = state.getValue(HangingGroveMossBlock.FACING);
          return ConfiguredModel.builder()
              .modelFile(hangingGroveMoss)
              .rotationY(dir.getAxis().isVertical() ? 0 : (int) dir.toYRot())
              .build();
        });

    // Bafflecap mushroom block
    ModelFile modelInside = models().withExistingParent("bafflecap_block_inside", mcLoc("block/template_single_face"))
        .texture("texture", modLoc("block/bafflecap_block_inside"));
    models().cubeAll("bafflecap_block_inventory", modLoc("block/bafflecap_block_outside"));
    ModelFile modelOutside = models().withExistingParent("bafflecap_block_outside", mcLoc("block/template_single_face"))
        .texture("texture", modLoc("block/bafflecap_block_outside")).renderType("cutout");
    getMultipartBuilder(ModBlocks.BAFFLECAP_BLOCK.get()).part().modelFile(modelInside).addModel()
        .condition(HugeMushroomBlock.NORTH, false).end();
    getMultipartBuilder(ModBlocks.BAFFLECAP_BLOCK.get()).part().modelFile(modelOutside).addModel()
        .condition(HugeMushroomBlock.NORTH, true).end();
    getMultipartBuilder(ModBlocks.BAFFLECAP_BLOCK.get()).part().modelFile(modelInside).uvLock(true).rotationY(180)
        .addModel().condition(HugeMushroomBlock.SOUTH, false).end();
    getMultipartBuilder(ModBlocks.BAFFLECAP_BLOCK.get()).part().modelFile(modelOutside).uvLock(true).rotationY(180)
        .addModel().condition(HugeMushroomBlock.SOUTH, true).end();
    getMultipartBuilder(ModBlocks.BAFFLECAP_BLOCK.get()).part().modelFile(modelInside).uvLock(true).rotationY(270)
        .addModel().condition(HugeMushroomBlock.WEST, false).end();
    getMultipartBuilder(ModBlocks.BAFFLECAP_BLOCK.get()).part().modelFile(modelOutside).uvLock(true).rotationY(270)
        .addModel().condition(HugeMushroomBlock.WEST, true).end();
    getMultipartBuilder(ModBlocks.BAFFLECAP_BLOCK.get()).part().modelFile(modelInside).uvLock(true).rotationY(90)
        .addModel().condition(HugeMushroomBlock.EAST, false).end();
    getMultipartBuilder(ModBlocks.BAFFLECAP_BLOCK.get()).part().modelFile(modelOutside).uvLock(true).rotationY(90)
        .addModel().condition(HugeMushroomBlock.EAST, true).end();
    getMultipartBuilder(ModBlocks.BAFFLECAP_BLOCK.get()).part().modelFile(modelInside).uvLock(true).rotationX(270)
        .addModel().condition(HugeMushroomBlock.UP, false).end();
    getMultipartBuilder(ModBlocks.BAFFLECAP_BLOCK.get()).part().modelFile(modelOutside).uvLock(true).rotationX(270)
        .addModel().condition(HugeMushroomBlock.UP, true).end();
    getMultipartBuilder(ModBlocks.BAFFLECAP_BLOCK.get()).part().modelFile(modelInside).uvLock(true).rotationX(90)
        .addModel().condition(HugeMushroomBlock.DOWN, false).end();
    getMultipartBuilder(ModBlocks.BAFFLECAP_BLOCK.get()).part().modelFile(modelOutside).uvLock(true).rotationX(90)
        .addModel().condition(HugeMushroomBlock.DOWN, true).end();

    ModelFile incenseBurner = models().withExistingParent("incense_burner", modLoc("block/complex/incense_burner"));
    getVariantBuilder(ModBlocks.INCENSE_BURNER.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(incenseBurner).build());

/*    ModelFile stoneAltar = models().withExistingParent("stone_altar", modLoc("block/complex/stone_altar"));
    getVariantBuilder(ModBlocks.STONE_ALTAR.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(stoneAltar).build());*/

    ModelFile mortar = models().withExistingParent("mortar", modLoc("block/complex/mortar"));
    getVariantBuilder(ModBlocks.MORTAR.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(mortar)
        .build());

    ModelFile pyre = models().withExistingParent("pyre", modLoc("block/complex/pyre")).renderType("cutout");
    ModelFile pyreLit = models().withExistingParent("pyre_lit", modLoc("block/complex/pyre_lit")).renderType("cutout");
    getVariantBuilder(ModBlocks.PYRE.get()).forAllStates(state -> {
      if (state.getValue(PyreBlock.ACTIVE)) {
        return ConfiguredModel.builder().modelFile(pyreLit).build();
      } else {
        return ConfiguredModel.builder().modelFile(pyre).build();
      }
    });

    ModelFile soulPyre = models().withExistingParent("soul_pyre", modLoc("block/complex/pyre")).renderType("cutout");
    ModelFile soulPyreLit = models().withExistingParent("soul_pyre_lit", modLoc("block/complex/soul_pyre_lit"))
        .renderType("cutout");
    getVariantBuilder(ModBlocks.SOUL_PYRE.get()).forAllStates(state -> {
      if (state.getValue(PyreBlock.ACTIVE)) {
        return ConfiguredModel.builder().modelFile(soulPyreLit).build();
      } else {
        return ConfiguredModel.builder().modelFile(soulPyre).build();
      }
    });

    // TODO: Lit vs unlit
    ModelFile reinforcedPyre = models().withExistingParent("reinforced_pyre", modLoc("block/complex/reinforced_pyre"))
        .renderType("cutout");
    ModelFile reinforcedPyreLit = models().withExistingParent("reinforced_pyre_lit", modLoc("block/complex/reinforced_pyre_lit"))
        .renderType("cutout");
    getVariantBuilder(ModBlocks.REINFORCED_PYRE.get()).forAllStates(state -> {
      if (state.getValue(PyreBlock.ACTIVE)) {
        return ConfiguredModel.builder().modelFile(reinforcedPyreLit).build();
      } else {
        return ConfiguredModel.builder().modelFile(reinforcedPyre).build();
      }
    });

    ModelFile soulReinforcedPyre = models().withExistingParent("reinforced_soul_pyre", modLoc("block/complex/reinforced_pyre"))
        .renderType("cutout");
    ModelFile soulReinforcedPyreLit = models().withExistingParent("reinforced_soul_pyre_lit", modLoc("block/complex/reinforced_soul_pyre_lit"))
        .renderType("cutout");
    getVariantBuilder(ModBlocks.REINFORCED_SOUL_PYRE.get()).forAllStates(state -> {
      if (state.getValue(PyreBlock.ACTIVE)) {
        return ConfiguredModel.builder().modelFile(soulReinforcedPyreLit).build();
      } else {
        return ConfiguredModel.builder().modelFile(soulReinforcedPyre).build();
      }
    });

    ModelFile decorativePyre = models().withExistingParent("decorative_pyre", modLoc("block/complex/pyre_lit"))
        .renderType("cutout");
    getVariantBuilder(ModBlocks.DECORATIVE_PYRE.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(decorativePyre).build());

    ModelFile decorativeSoulPyre = models().withExistingParent("decorative_soul_pyre", modLoc("block/complex/soul_pyre_lit"))
        .renderType("cutout");
    getVariantBuilder(ModBlocks.DECORATIVE_SOUL_PYRE.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(decorativeSoulPyre).build());

    ModelFile unendingBowl = models().withExistingParent("unending_bowl", modLoc("block/complex/unending_bowl"));
    getVariantBuilder(ModBlocks.UNENDING_BOWL.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(unendingBowl).build());

    getVariantBuilder(ModBlocks.BAFFLECAP.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(models().getBuilder("block/bafflecap").parent(crop).texture("cross", modLoc("block/bafflecap"))
            .renderType("cutout"))
        .build());

    ModelFile sylvanLight = models().getExistingFile(modLoc("block/sylvan_light"));

    getVariantBuilder(ModBlocks.SYLVAN_LIGHT.get()).forAllStates(state -> ConfiguredModel.builder()
        .modelFile(sylvanLight)
        .build());

    crop(ModBlocks.WILDROOT_CROP, false);
    crop(ModBlocks.CLOUD_BERRY_CROP, false);
    crop(ModBlocks.DEWGONIA_CROP, false);
    crop(ModBlocks.INFERNO_BULB_CROP, true);
    crop(ModBlocks.STALICRIPE_CROP, false);
    crop(ModBlocks.MOONGLOW_CROP, false);
    crop(ModBlocks.PERESKIA_CROP, true);
    crop(ModBlocks.SPIRITLEAF_CROP, false);
    crop(ModBlocks.WILDEWHEET_CROP, false);
    crop(ModBlocks.AUBERGINE_CROP, false);

    ModelFile fungalTransmuter = models().withExistingParent("fungal_transmuter", modLoc("block/complex/fungal_transmuter"))
        .renderType("solid")
        .texture("bottom", modLoc("block/fungal_transmuter_bottom"))
        .texture("top", modLoc("block/fungal_transmuter_top"))
        .texture("side", modLoc("block/fungal_transmuter_side"))
        .texture("particle", modLoc("block/fungal_transmuter_bottom"));
    getVariantBuilder(ModBlocks.FUNGAL_TRANSMUTER.get())
        .forAllStates(state -> {
          Direction dir = state.getValue(FungalTransmuterBlock.FACING);
          return ConfiguredModel.builder()
              .modelFile(fungalTransmuter)
              .rotationY(dir.getAxis().isVertical() ? 0 : (int) dir.toYRot())
              .build();
        });

    ModelFile cropcrop = models().withExistingParent("wild_aubergine", ResourceLocation.withDefaultNamespace("block/crop"))
        .renderType("cutout").texture("crop", modLoc("block/wild_aubergine")).renderType("cutout");
    getVariantBuilder(ModBlocks.WILD_AUBERGINE.get())
        .forAllStates(state ->
            ConfiguredModel.builder().modelFile(cropcrop).build());
    simpleBlock(ModBlocks.POTTED_BAFFLECAP.get(), models().withExistingParent("potted_bafflecap", mcLoc("block/flower_pot_cross"))
        .texture("plant", modLoc("block/bafflecap")).renderType("cutout"));
    simpleBlock(ModBlocks.POTTED_STONEPETAL.get(), models().withExistingParent("potted_stonepetal", mcLoc("block/flower_pot_cross"))
        .texture("plant", modLoc("block/stonepetal")).renderType("cutout"));
    simpleBlock(ModBlocks.POTTED_WILDWOOD_SAPLING.get(), models().withExistingParent("potted_wildwood_sapling", mcLoc("block/flower_pot_cross"))
        .texture("plant", modLoc("block/wildwood_sapling")).renderType("cutout"));

    groveStone(ModBlocks.PRIMAL_GROVE_STONE);
    groveStone(ModBlocks.WILD_GROVE_STONE);
    groveStone(ModBlocks.FAIRY_GROVE_STONE);
    groveStone(ModBlocks.TWILIGHT_GROVE_STONE);
    groveStone(ModBlocks.ELEMENTAL_GROVE_STONE);
    groveStone(ModBlocks.SPROUTING_GROVE_STONE);
    groveStone(ModBlocks.FUNGAL_GROVE_STONE);

    fairyHut(ModBlocks.RED_FAIRY_HUT);
    fairyHut(ModBlocks.WARPED_FAIRY_HUT);
    fairyHut(ModBlocks.BAFFLECAP_FAIRY_HUT);
    fairyHut(ModBlocks.CRIMSON_FAIRY_HUT);
    fairyHut(ModBlocks.BROWN_FAIRY_HUT);

  }

  private void fairyHut(Holder<Block> holder) {
    getVariantBuilder(holder.value())
        .forAllStates(state -> {
          String partName = state.getValue(FairyHutBlock.HALF).getSerializedName();
          String material = holder.getKey().location().getPath().split("_")[0];
          String name = material + "_fairy_hut_" + partName;
          BlockModelBuilder model = models().withExistingParent(name, modLoc("block/complex/house_" + partName));

          ResourceLocation texture = modLoc("block/" + material + "_fairy_house");

          model.texture("4", texture);
          // TODO: Fix particle texture
          model.texture("particle", texture);

          if (state.getValue(FairyHutBlock.HALF) == DoubleBlockHalf.LOWER) {
            models().withExistingParent(material + "_fairy_hut_inventory", modLoc("block/complex/house_full"))
                .texture("4", texture).texture("particle", texture);
          }

          Direction dir = state.getValue(FairyHutBlock.FACING);
          return ConfiguredModel.builder()
              .modelFile(model)
              .rotationX(0)
              .rotationY(dir.getAxis().isVertical() ? 0 : (int) (dir.toYRot() - 90) % 360)
              .build();
        });
  }

  private void groveStone(Holder<Block> holder) {
    getVariantBuilder(holder.value())
        .forAllStates(state -> {
          boolean valid = state.getValue(StateProperties.ACTIVE);
          String partName = state.getValue(GroveStoneBlock.PART).getSerializedName();
          String groveType = ((GroveStoneBlock) state.getBlock()).getGrove().getKey().location().getPath();
          String name = groveType + "_grove_stone_" + partName;
          if (valid) {
            name = name + "_valid";
          }
          BlockModelBuilder model = models().withExistingParent(name, modLoc("block/complex/grove_stone_" + partName));

          ResourceLocation active;

          if (groveType.equals("primal")) {
            active = modLoc("block/ob_stone_active");
            models().withExistingParent("primal_grove_stone_inventory", modLoc("block/complex/grove_stone_full"));
          } else {
            active = modLoc("block/ob_stone_active_" + groveType);
            models().withExistingParent(groveType + "_grove_stone_inventory", modLoc("block/complex/grove_stone_full"))
                .texture("monolith", active).texture("particle", active);
          }

          if (valid) {
            model.texture("monolith", active);
            model.texture("particle", active);
          }

          Direction dir = state.getValue(GroveStoneBlock.FACING);
          return ConfiguredModel.builder()
              .modelFile(model)
              .rotationX(0)
              .rotationY(dir.getAxis().isVertical() ? 0 : (int) dir.toYRot() % 360)
              .build();
        });
  }

  private void crop(Holder<Block> holder, boolean cross) {
    ModelFile crop = models().getExistingFile(ResourceLocation.withDefaultNamespace(cross ? "block/cross" : "block/crop"));
    String prefix = holder.getKey().location().getPath().replace("crop", "");
    getVariantBuilder(holder.value())
        .forAllStates(state -> {
          String file = prefix + state.getValue(((AccessorMixinCropBlock) state.getBlock()).rootsCallGetAgeProperty());
          ModelFile stege = models().getBuilder(file)
              .parent(crop)
              .renderType("cutout")
              .texture(cross ? "cross" : "crop", modLoc("block/crops/" + file));
          return ConfiguredModel.builder().modelFile(stege).build();
        });
  }
}
