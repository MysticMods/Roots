package epicsquid.roots.init;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.block.*;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.roots.Roots;
import epicsquid.roots.api.CustomPlantType;
import epicsquid.roots.block.*;
import epicsquid.roots.block.runes.BlockTrample;
import epicsquid.roots.block.runes.BlockWildwoodRune;
import epicsquid.roots.tileentity.*;
import epicsquid.roots.util.EnumElementalSoilType;
import epicsquid.roots.world.HugeBaffleCap;
import epicsquid.roots.world.tree.WorldGenBigWildwoodTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.common.EnumPlantType;

import javax.annotation.Nonnull;

public class ModBlocks {

  // All block
  public static Block mortar, bonfire,imbuer, structure_marker, offertory_plate, unending_bowl;
  public static Block incense_burner, wildwood_rune;

  public static BlockCropBase moonglow, aubergine, pereskia, wildroot, spirit_herb,wildewheet, cloud_berry, infernal_bulb, dewgonia, stalicripe;

  // Runestones
  public static Block runestone, runestone_brick, runestone_brick_alt, chiseled_runestone, wildwood_log, wildwood_planks, wildwood_leaves, wildwood_sapling, baffle_cap_huge_stem, baffle_cap_huge_top,
      baffle_cap_mushroom, trample_rune;

  //Elemental Soil
  public static Block elemental_soil_fire, elemental_soil_water, elemental_soil_air, elemental_soil_earth, elemental_soil;


  // Decoration
  public static Block runestone_slab, runestone_double_slab, runestone_stairs, runestone_wall, runestone_button, runestone_pressure_plate;
  public static Block runestone_brick_slab, runestone_brick_double_slab, runestone_brick_stairs, runestone_brick_wall, runestone_brick_button, runestone_brick_pressure_plate;
  public static Block runestone_brick_alt_slab, runestone_brick_alt_double_slab, runestone_brick_alt_stairs, runestone_brick_alt_wall, runestone_brick_alt_button, runestone_brick_alt_pressure_plate;

  // Wildwood
  public static Block wildwood_slab, wildwood_double_slab, wildwood_stairs, wildwood_wall;
  public static Block wildwood_door, wildwood_trapdoor;
  public static Block wildwood_button, wildwood_pressure_plate, wildwood_fence, wildwood_ladder;

  /**
   * Register all block
   */
  public static void registerBlocks(@Nonnull RegisterContentEvent event) {
    // Roots
    event.addBlock(moonglow = new BlockMoonglowCrop("moonglow_crop", EnumPlantType.Crop));
    event.addBlock(aubergine = new BlockAubergineCrop("aubergine_crop", EnumPlantType.Crop));
    event.addBlock(pereskia = new BlockPereskiaCrop("pereskia_crop", EnumPlantType.Crop));
    event.addBlock(wildroot = new BlockWildrootCrop("wildroot_crop", EnumPlantType.Crop));
    event.addBlock(spirit_herb = new BlockSpiritHerbCrop("spirit_herb_crop", EnumPlantType.Crop));
    event.addBlock(baffle_cap_huge_stem = new BlockHugeMushroomBase(Material.WOOD, SoundType.WOOD, 0.8f, "baffle_cap_huge_stem").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addBlock(baffle_cap_huge_top = new BlockHugeMushroomBase(Material.WOOD, SoundType.WOOD, 0.8f, "baffle_cap_huge_top").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addBlock(baffle_cap_mushroom = new BlockMushroomBase("baffle_cap_mushroom", new HugeBaffleCap().getData()));
    ((BlockHugeMushroomBase) baffle_cap_huge_stem).setSmallBlock(baffle_cap_mushroom);
    ((BlockHugeMushroomBase) baffle_cap_huge_top).setSmallBlock(baffle_cap_mushroom);
    event.addBlock(wildewheet = new BlockWildewheetCrop("wildewheet_crop", EnumPlantType.Crop));
    event.addBlock(cloud_berry = new BlockCloudBerryCrop("cloud_berry_crop", CustomPlantType.ELEMENT_AIR));
    event.addBlock(infernal_bulb = new BlockInfernalBulbCrop("infernal_bulb_crop", CustomPlantType.ELEMENT_FIRE));
    // TODO 1.13 make the dewgonia work only underwater
    event.addBlock(dewgonia = new BlockDewgoniaCrop("dewgonia_crop", CustomPlantType.ELEMENT_WATER));
    event.addBlock(stalicripe = new BlockStalicripeCrop("stalicripe_crop", CustomPlantType.ELEMENT_EARTH));
    event.addBlock(elemental_soil = new BlockBase(Material.GROUND, SoundType.GROUND, 0.8f, "elemental_soil")
    {
      @Override
      public Item getItemBlock() {
        return ModItems.item_block_elemental_soil;
      }
    }

            .setModelCustom(true).setCreativeTab(Roots.tab));

    event.addBlock(elemental_soil_air = new BlockElementalSoil(Material.GROUND, SoundType.GROUND, "elemental_soil_air", EnumElementalSoilType.AIR).setModelCustom(false).setCreativeTab(Roots.tab));
    event.addBlock(elemental_soil_water = new BlockElementalSoil(Material.GROUND, SoundType.GROUND, "elemental_soil_water", EnumElementalSoilType.WATER).setModelCustom(false).setCreativeTab(Roots.tab));
    event.addBlock(elemental_soil_fire = new BlockElementalSoil(Material.GROUND, SoundType.GROUND, "elemental_soil_fire", EnumElementalSoilType.FIRE).setModelCustom(false).setCreativeTab(Roots.tab));
    event.addBlock(elemental_soil_earth = new BlockElementalSoil(Material.GROUND, SoundType.GROUND, "elemental_soil_earth", EnumElementalSoilType.EARTH).setModelCustom(false).setCreativeTab(Roots.tab));

    // Post registration block setup
    ((BlockMushroomBase) baffle_cap_mushroom).setItemBlock(new ItemBlock(baffle_cap_mushroom).setRegistryName(LibRegistry.getActiveModid(), "baffle_cap_mushroom"));

    //Runestones
    event.addBlock(runestone = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone").setModelCustom(true)).setCreativeTab(Roots.tab);
    event.addBlock(runestone_brick = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_brick").setModelCustom(true)).setCreativeTab(Roots.tab);
    event.addBlock(runestone_brick_alt = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_brick_alt").setModelCustom(true)).setCreativeTab(Roots.tab);
    event.addBlock(chiseled_runestone = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "chiseled_runestone").setModelCustom(true)).setCreativeTab(Roots.tab);
    event.addBlock(trample_rune = new BlockTrample(Material.WATER, SoundType.METAL, 1.4f, "runestone_trample").setModelCustom(true)).setCreativeTab(Roots.tab);

    event.addBlock(wildwood_leaves = new BlockLeavesBase(0.8f, "wildwood_leaves", () -> new ItemStack(ModItems.wildroot), 50).setModelCustom(true).setOpacity(false).setCreativeTab(Roots.tab));
    event.addBlock(wildwood_log = new BlockLogBase("wildwood_log").setCreativeTab(Roots.tab));
    event.addBlock(wildwood_planks = new BlockBase(Material.WOOD, SoundType.WOOD, 2.0f, "wildwood_planks").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addBlock(wildwood_sapling = new BlockSaplingBase("wildwood_sapling", () -> new WorldGenBigWildwoodTree(true)).setModelCustom(false).setCreativeTab(Roots.tab));

    event.addBlock(wildwood_door = new BlockDoorBase(wildwood_planks, SoundType.WOOD, 2.0f, "wildwood_door").setModelCustom(true).setLayer(BlockRenderLayer.TRANSLUCENT).setCreativeTab(Roots.tab));
    event.addBlock(wildwood_trapdoor = new BlockTrapDoorBase(wildwood_planks, SoundType.WOOD, 2.0f, "wildwood_trapdoor").setModelCustom(true).setLayer(BlockRenderLayer.TRANSLUCENT).setCreativeTab(Roots.tab));
    event.addBlock(wildwood_fence = new BlockFenceBase(wildwood_planks, SoundType.WOOD, 2.0f, "wildwood_fence").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addBlock(wildwood_ladder = new BlockLadderBase(wildwood_planks, 2.0f, "wildwood_ladder").setModelCustom(true).setCreativeTab(Roots.tab));

    Variants wildwood = variants(event, wildwood_planks, "wildwood", SoundType.WOOD, Material.WOOD);
    wildwood_slab = wildwood.slab;
    wildwood_double_slab = wildwood.double_slab;
    wildwood_stairs = wildwood.stairs;
    wildwood_wall = wildwood.wall;
    wildwood_button = wildwood.button;
    wildwood_pressure_plate = wildwood.pressure_plate;

    //Decoration
    Variants runes = variants(event, runestone, "runestone", SoundType.STONE, Material.ROCK);
    runestone_slab = runes.slab;
    runestone_double_slab = runes.double_slab;
    runestone_stairs = runes.stairs;
    runestone_wall = runes.wall;
    runestone_button = runes.button;
    runestone_pressure_plate = runes.pressure_plate;

    runes = variants(event, runestone_brick, "runestone_brick", SoundType.STONE, Material.ROCK);
    runestone_brick_slab = runes.slab;
    runestone_brick_double_slab = runes.double_slab;
    runestone_brick_stairs = runes.stairs;
    runestone_brick_wall = runes.wall;
    runestone_brick_button = runes.button;
    runestone_brick_pressure_plate = runes.pressure_plate;

    runes = variants(event, runestone_brick_alt, "runestone_brick_alt", SoundType.STONE, Material.ROCK);
    runestone_brick_alt_slab = runes.slab;
    runestone_brick_alt_double_slab = runes.double_slab;
    runestone_brick_alt_stairs = runes.stairs;
    runestone_brick_alt_wall = runes.wall;
    runestone_brick_alt_button = runes.button;
    runestone_brick_alt_pressure_plate = runes.pressure_plate;

    event.addBlock(structure_marker = new BlockStructureMarker());
    event.addBlock(mortar = new BlockMortar(Material.ROCK, SoundType.STONE, 1.4f, "mortar", TileEntityMortar.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(bonfire = new BlockBonfire(Material.WOOD, SoundType.WOOD, 1.4f, "bonfire", TileEntityBonfire.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(imbuer = new BlockImbuer(Material.WOOD, SoundType.WOOD, 1.4f, "imbuer", TileEntityImbuer.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(offertory_plate = new BlockOffertoryPlate(Material.ROCK, SoundType.STONE, 1.4f, "offertory_plate", TileEntityOffertoryPlate.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(unending_bowl = new BlockUnendingBowl(Material.ROCK, SoundType.STONE, 1.4f, "unending_bowl", TileEntityUnendingBowl.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(incense_burner = new BlockIncenseBurner(Material.ROCK, SoundType.STONE, 1.4f, "incense_burner", TileEntityIncenseBurner.class)).setCreativeTab(Roots.tab).setLightOpacity(0);

    //Runes
    event.addBlock(wildwood_rune = new BlockWildwoodRune(Material.WOOD, SoundType.WOOD, 1.4f, "wildwood_rune", TileEntityWildrootRune.class)).setCreativeTab(Roots.tab);

  }

  private static Variants variants(RegisterContentEvent event, Block base, String name, SoundType sound, Material material) {
    Block[] slabs = new Block[2];
    Block stairs;
    Block wall;
    Block button;
    Block pressure_plate;
    LibRegistry.addSlabPair(material, sound, 1.7f, name, base.getDefaultState(), slabs, true,
        base.getCreativeTab());
    event.addBlock(stairs = new BlockStairsBase(base.getDefaultState(), sound, 1.7f, name + "_stairs").setModelCustom(true)
        .setCreativeTab(base.getCreativeTab()));
    event.addBlock(
        wall = new BlockWallBase(base, sound, 1.7f, name + "_wall").setModelCustom(true).setCreativeTab(base.getCreativeTab()));
    if (material.equals(Material.ROCK)) {
      event.addBlock(button = new BlockButtonStoneBase(base, sound, 1.7f, name + "_button").setModelCustom(true).setCreativeTab(Roots.tab));
      event.addBlock(pressure_plate = new BlockPressurePlateBase(base, BlockPressurePlate.Sensitivity.MOBS, sound, 1.7f, name + "_pressure_plate").setModelCustom(true).setCreativeTab(Roots.tab));
    } else {
      event.addBlock(button = new BlockButtonWoodBase(base, sound, 1.7f, name + "_button").setModelCustom(true).setCreativeTab(Roots.tab));
      event.addBlock(pressure_plate = new BlockPressurePlateBase(base, BlockPressurePlate.Sensitivity.EVERYTHING, sound, 1.7f, name + "_pressure_plate").setModelCustom(true).setCreativeTab(Roots.tab));
    }
    return new Variants(slabs, stairs, wall, button, pressure_plate);
  }

  private static class Variants {
    public Block stairs;
    public Block wall;
    public Block slab;
    public Block double_slab;
    public Block button;
    public Block pressure_plate;

    public Variants (Block[] slabs, Block stairs, Block wall, Block button, Block pressure_plate) {
      this.slab = slabs[0];
      this.double_slab = slabs[1];
      this.stairs = stairs;
      this.wall = wall;
      this.button = button;
      this.pressure_plate = pressure_plate;
    }
  }
}
