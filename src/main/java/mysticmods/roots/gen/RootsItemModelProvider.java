package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class RootsItemModelProvider extends ItemModelProvider {
  public RootsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    simpleBlockItem(ModBlocks.THATCH.get());
    simpleBlockItem(ModBlocks.RUNESTONE.get());
    simpleBlockItem(ModBlocks.MOSSY_RUNESTONE.get());
    simpleBlockItem(ModBlocks.CHISELED_RUNESTONE.get());
    simpleBlockItem(ModBlocks.RUNESTONE_BRICK.get());
    simpleBlockItem(ModBlocks.RUNESTONE_TILE.get());
    simpleBlockItem(ModBlocks.RUNED_OBSIDIAN.get());
    simpleBlockItem(ModBlocks.RUNED_BRICK.get());
    simpleBlockItem(ModBlocks.RUNED_TILE.get());
    simpleBlockItem(ModBlocks.CHISELED_RUNED_OBSIDIAN.get());
    simpleBlockItem(ModBlocks.SILVER_ORE.get());
    simpleBlockItem(ModBlocks.DEEPSLATE_SILVER_ORE.get());
    simpleBlockItem(ModBlocks.GRANITE_QUARTZ_ORE.get());
    simpleBlockItem(ModBlocks.RAW_SILVER_BLOCK.get());
    simpleBlockItem(ModBlocks.SILVER_BLOCK.get());
    simpleBlockItem(ModBlocks.WILDWOOD_LOG.get());
    simpleBlockItem(ModBlocks.STRIPPED_WILDWOOD_LOG.get());
    simpleBlockItem(ModBlocks.WILDWOOD_WOOD.get());
    simpleBlockItem(ModBlocks.STRIPPED_WILDWOOD_WOOD.get());
    simpleBlockItem(ModBlocks.WILDWOOD_PLANKS.get());
    basicItem(ModItems.WILDWOOD_SAPLING.get());
    basicItem(ModItems.STONEPETAL.get());
    simpleBlockItem(ModBlocks.WILDWOOD_LEAVES.get());
    simpleBlockItem(ModBlocks.RUNED_WILDWOOD_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_SPRUCE_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_BIRCH_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_JUNGLE_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_ACACIA_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_DARK_OAK_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_CRIMSON_STEM.get());
    simpleBlockItem(ModBlocks.RUNED_WARPED_STEM.get());
    simpleBlockItem(ModBlocks.RUNED_MANGROVE_LOG.get());
    simpleBlockItem(ModBlocks.RUNESTONE_STAIRS.get());
    simpleBlockItem(ModBlocks.RUNESTONE_BRICK_STAIRS.get());
    simpleBlockItem(ModBlocks.RUNESTONE_TILE_STAIRS.get());
    simpleBlockItem(ModBlocks.MOSSY_RUNESTONE_STAIRS.get());
    simpleBlockItem(ModBlocks.RUNED_STAIRS.get());
    simpleBlockItem(ModBlocks.RUNED_BRICK_STAIRS.get());
    simpleBlockItem(ModBlocks.RUNED_TILE_STAIRS.get());
    simpleBlockItem(ModBlocks.WILDWOOD_STAIRS.get());
    simpleBlockItem(ModBlocks.RUNESTONE_SLAB.get());
    simpleBlockItem(ModBlocks.RUNESTONE_BRICK_SLAB.get());
    simpleBlockItem(ModBlocks.RUNESTONE_TILE_SLAB.get());
    simpleBlockItem(ModBlocks.MOSSY_RUNESTONE_SLAB.get());
    simpleBlockItem(ModBlocks.RUNED_SLAB.get());
    simpleBlockItem(ModBlocks.RUNED_BRICK_SLAB.get());
    simpleBlockItem(ModBlocks.RUNED_TILE_SLAB.get());
    simpleBlockItem(ModBlocks.WILDWOOD_SLAB.get());
    fenceInventory(ModBlocks.WILDWOOD_FENCE.getKey().location().getPath(), modLoc("block/" + ModBlocks.WILDWOOD_PLANKS.getKey().location().getPath()));
    simpleBlockItem(ModBlocks.WILDWOOD_GATE.get());
    buttonInventory(ModBlocks.RUNESTONE.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNESTONE.getKey().location().getPath()));
    buttonInventory(ModBlocks.RUNESTONE_BRICK.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNESTONE_BRICK.getKey().location().getPath()));
    buttonInventory(ModBlocks.MOSSY_RUNESTONE.getKey().location().getPath(), modLoc("block/" + ModBlocks.MOSSY_RUNESTONE.getKey().location().getPath()));
    buttonInventory(ModBlocks.RUNESTONE_TILE.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNESTONE_TILE.getKey().location().getPath()));
    buttonInventory(ModBlocks.RUNED_OBSIDIAN.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNED_OBSIDIAN.getKey().location().getPath()));
    buttonInventory(ModBlocks.RUNED_BRICK.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNED_BRICK.getKey().location().getPath()));
    buttonInventory(ModBlocks.RUNED_TILE.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNED_TILE.getKey().location().getPath()));
    buttonInventory(ModBlocks.WILDWOOD_BUTTON.getKey().location().getPath(), modLoc("block/" + ModBlocks.WILDWOOD_PLANKS.getKey().location().getPath()));
    simpleBlockItem(ModBlocks.RUNESTONE_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.RUNESTONE_BRICK_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.MOSSY_RUNESTONE_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.RUNESTONE_TILE_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.RUNED_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.RUNED_BRICK_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.RUNED_TILE_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.WILDWOOD_PRESSURE_PLATE.get());
    // TODO: Does htis work?
    basicItem(ModItems.WILDWOOD_DOOR.get());
    basicItem(ModItems.WILDWOOD_TRAPDOOR.get());
    basicItem(ModItems.WILDWOOD_LADDER.get());
    wallInventory(ModBlocks.RUNESTONE_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNESTONE.getKey().location().getPath()));
    wallInventory(ModBlocks.RUNESTONE_BRICK_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNESTONE_BRICK.getKey().location().getPath()));
    wallInventory(ModBlocks.MOSSY_RUNESTONE_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.MOSSY_RUNESTONE.getKey().location().getPath()));
    wallInventory(ModBlocks.RUNESTONE_TILE_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNESTONE_TILE.getKey().location().getPath()));
    wallInventory(ModBlocks.RUNED_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNED_OBSIDIAN.getKey().location().getPath()));
    wallInventory(ModBlocks.RUNED_BRICK_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNED_BRICK.getKey().location().getPath()));
    wallInventory(ModBlocks.RUNED_TILE_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNED_TILE.getKey().location().getPath()));
    simpleBlockItem(ModBlocks.ELEMENTAL_SOIL.get());
    simpleBlockItem(ModBlocks.AQUEOUS_SOIL.get());
    simpleBlockItem(ModBlocks.CAELIC_SOIL.get());
    simpleBlockItem(ModBlocks.MAGMATIC_SOIL.get());
    simpleBlockItem(ModBlocks.TERRAN_SOIL.get());
    withExistingParent(ModBlocks.RITUAL_PEDESTAL.getKey().location().getPath(), modLoc("block/complex/ritual_pedestal"));
    withExistingParent(ModBlocks.REINFORCED_RITUAL_PEDESTAL.getKey().location().getPath(), modLoc("block/complex/reinforced_ritual_pedestal"));
    withExistingParent(ModBlocks.GROVE_CRAFTER.getKey().location().getPath(), modLoc("block/complex/grove_crafter"));
    withExistingParent(ModBlocks.GROVE_PEDESTAL.getKey().location().getPath(), modLoc("block/complex/grove_pedestal"));
    withExistingParent(ModBlocks.WILDWOOD_PEDESTAL.getKey().location().getPath(), modLoc("block/complex/wildwood_pedestal"));
    withExistingParent(ModBlocks.DISPLAY_PEDESTAL.getKey().location().getPath(), modLoc("block/complex/grove_pedestal"));
    // Wild roots are existing
    simpleBlockItem(ModBlocks.CREEPING_GROVE_MOSS.get());
    simpleBlockItem(ModBlocks.CREEPING_GROVE_MOSS.get());
    // TODO: is this correct?
    simpleBlockItem(ModBlocks.BAFFLECAP_BLOCK.get());
    // Is THIS correct? TODO
    withExistingParent(ModBlocks.PRIMAL_GROVE_STONE.getKey().location().getPath(), modLoc("block/primal_grove_stone_inventory"));
    simpleBlockItem(ModBlocks.INCENSE_BURNER.get());
    simpleBlockItem(ModBlocks.MORTAR.get());
    simpleBlockItem(ModBlocks.PYRE.get());
    simpleBlockItem(ModBlocks.REINFORCED_PYRE.get());
    simpleBlockItem(ModBlocks.DECORATIVE_PYRE.get());
    simpleBlockItem(ModBlocks.UNENDING_BOWL.get());

    // Crops have no item models

  }
}
