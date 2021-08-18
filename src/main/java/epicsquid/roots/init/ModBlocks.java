package epicsquid.roots.init;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.block.*;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.roots.Roots;
import epicsquid.roots.api.CustomPlantType;
import epicsquid.roots.block.*;
import epicsquid.roots.block.groves.BlockGroveStone;
import epicsquid.roots.tileentity.*;
import epicsquid.roots.util.EnumElementalSoilType;
import epicsquid.roots.world.HugeBaffleCap;
import epicsquid.roots.world.tree.WorldGenBigWildwoodTree;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ModBlocks {

  // All block
  public static Block mortar, pyre, imbuer, imposer, catalyst_plate, unending_bowl, reinforced_pyre, reinforced_catalyst_plate;
  public static Block wildroot_rune;
  public static Block grove_stone, fey_crafter, runic_crafter;

  public static Block fey_fire, fake_water, fake_lava;

  public static BlockCropBase moonglow, pereskia, wildroot, spirit_herb, wildewheet, cloud_berry, infernal_bulb, dewgonia, stalicripe;

  // Runestones
  public static Block runestone, runestone_brick, runestone_brick_alt, chiseled_runestone, wildwood_log, wildwood_planks, wildwood_leaves, wildwood_sapling, baffle_cap_huge_stem, baffle_cap_huge_top,
      baffle_cap_mushroom, runestone_trample;

  // Runed wood
  public static Block runed_acacia, runed_birch, runed_dark_oak, runed_jungle, runed_oak, runed_spruce, runed_wildwood;

  //Elemental Soil
  public static Block elemental_soil_fire, elemental_soil_water, elemental_soil_air, elemental_soil_earth, elemental_soil;

  // Decoration
  public static Block runestone_slab, runestone_double_slab, runestone_stairs, runestone_wall, runestone_button, runestone_pressure_plate;
  public static Block runestone_brick_slab, runestone_brick_double_slab, runestone_brick_stairs, runestone_brick_wall, runestone_brick_button, runestone_brick_pressure_plate;
  public static Block runestone_brick_alt_slab, runestone_brick_alt_double_slab, runestone_brick_alt_stairs, runestone_brick_alt_wall, runestone_brick_alt_button, runestone_brick_alt_pressure_plate;

  public static Block runed_obsidian, chiseled_runed_obsidian, runed_obsidian_brick, runed_obsidian_brick_alt;

  public static Block runed_obsidian_slab, runed_obsidian_double_slab, runed_obsidian_stairs, runed_obsidian_wall, runed_obsidian_button, runed_obsidian_pressure_plate;
  public static Block runed_obsidian_brick_slab, runed_obsidian_brick_double_slab, runed_obsidian_brick_stairs, runed_obsidian_brick_wall, runed_obsidian_brick_button, runed_obsidian_brick_pressure_plate;
  public static Block runed_obsidian_brick_alt_slab, runed_obsidian_brick_alt_double_slab, runed_obsidian_brick_alt_stairs, runed_obsidian_brick_alt_wall, runed_obsidian_brick_alt_button, runed_obsidian_brick_alt_pressure_plate;


  // Wildwood
  public static Block wildwood_slab, wildwood_double_slab, wildwood_stairs, wildwood_wall;
  public static Block wildwood_door, wildwood_trapdoor;
  public static Block wildwood_button, wildwood_pressure_plate, wildwood_fence, wildwood_fence_gate, wildwood_ladder;

  public static Block fey_light, fey_colored_light, fey_decaying_light, decorative_pyre;

  // Lists of blcoks
  public static List<Block> runestoneBlocks;
  public static List<Block> runedObsidianBlocks;
  public static List<Block> runedWoodBlocks;

  /**
   * Register all block
   */
  public static void registerBlocks(@Nonnull RegisterContentEvent event) {
    // Roots
    event.addBlock(moonglow = new BlockMoonglowCrop("moonglow_crop", EnumPlantType.Crop));
    event.addBlock(pereskia = new BlockPereskiaCrop("pereskia_crop", EnumPlantType.Crop));
    event.addBlock(wildroot = new BlockWildrootCrop("wildroot_crop", EnumPlantType.Crop));
    event.addBlock(spirit_herb = new BlockSpiritHerbCrop("spirit_herb_crop", EnumPlantType.Crop));
    event.addBlock(baffle_cap_huge_stem = new BlockHugeMushroomBase(Material.WOOD, SoundType.WOOD, 0.8f, "baffle_cap_huge_stem").setCreativeTab(Roots.tab));
    event.addBlock(baffle_cap_huge_top = new BlockHugeMushroomBase(Material.WOOD, SoundType.WOOD, 0.8f, "baffle_cap_huge_top").setCreativeTab(Roots.tab));
    event.addBlock(baffle_cap_mushroom = new BlockMushroomBase("baffle_cap_mushroom", new HugeBaffleCap().getData()));
    ((BlockHugeMushroomBase) baffle_cap_huge_stem).setSmallBlock(baffle_cap_mushroom);
    ((BlockHugeMushroomBase) baffle_cap_huge_top).setSmallBlock(baffle_cap_mushroom);
    event.addBlock(wildewheet = new BlockWildewheetCrop("wildewheet_crop", EnumPlantType.Crop));
    event.addBlock(cloud_berry = new BlockCloudBerryCrop("cloud_berry_crop", CustomPlantType.ELEMENT_AIR));
    event.addBlock(infernal_bulb = new BlockInfernalBulbCrop("infernal_bulb_crop", CustomPlantType.ELEMENT_FIRE));
    // TODO 1.13 make the dewgonia work only underwater
    event.addBlock(dewgonia = new BlockDewgoniaCrop("dewgonia_crop", CustomPlantType.ELEMENT_WATER));
    event.addBlock(stalicripe = new BlockStalicripeCrop("stalicripe_crop", CustomPlantType.ELEMENT_EARTH));
    BlockElementalSoil.SOIL_INIT = EnumElementalSoilType.BASE;
    event.addBlock(elemental_soil = new BlockElementalSoil(Material.GROUND, SoundType.GROUND, "elemental_soil", EnumElementalSoilType.BASE).setCreativeTab(Roots.tab));
    BlockElementalSoil.SOIL_INIT = EnumElementalSoilType.AIR;
    event.addBlock(elemental_soil_air = new BlockElementalSoil(Material.GROUND, SoundType.GROUND, "elemental_soil_air", EnumElementalSoilType.AIR).setModelCustom(false).setCreativeTab(Roots.tab));
    BlockElementalSoil.SOIL_INIT = EnumElementalSoilType.WATER;
    event.addBlock(elemental_soil_water = new BlockElementalSoil(Material.GROUND, SoundType.GROUND, "elemental_soil_water", EnumElementalSoilType.WATER).setModelCustom(false).setCreativeTab(Roots.tab));
    BlockElementalSoil.SOIL_INIT = EnumElementalSoilType.FIRE;
    event.addBlock(elemental_soil_fire = new BlockElementalSoil(Material.GROUND, SoundType.GROUND, "elemental_soil_fire", EnumElementalSoilType.FIRE).setModelCustom(false).setCreativeTab(Roots.tab));
    BlockElementalSoil.SOIL_INIT = EnumElementalSoilType.EARTH;
    event.addBlock(elemental_soil_earth = new BlockElementalSoil(Material.GROUND, SoundType.GROUND, "elemental_soil_earth", EnumElementalSoilType.EARTH).setModelCustom(false).setCreativeTab(Roots.tab));

    // Post registration block setup
    ((BlockMushroomBase) baffle_cap_mushroom).setItemBlock(new ItemBlock(baffle_cap_mushroom).setRegistryName(LibRegistry.getActiveModid(), "baffle_cap_mushroom"));

    //Runestones
    event.addBlock(runestone = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone")).setCreativeTab(Roots.tab);
    event.addBlock(runestone_brick = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_brick")).setCreativeTab(Roots.tab);
    event.addBlock(runestone_brick_alt = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_brick_alt")).setCreativeTab(Roots.tab);
    event.addBlock(chiseled_runestone = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "chiseled_runestone")).setCreativeTab(Roots.tab);
    event.addBlock(runed_obsidian = new BlockRunedObsidian(Material.ROCK, SoundType.METAL, 8.5f, "runed_obsidian")).setCreativeTab(Roots.tab);
    event.addBlock(runed_obsidian_brick = new BlockRunedObsidian(Material.ROCK, SoundType.METAL, 8.5f, "runed_obsidian_brick")).setCreativeTab(Roots.tab);
    event.addBlock(runed_obsidian_brick_alt = new BlockRunedObsidian(Material.ROCK, SoundType.METAL, 8.5f, "runed_obsidian_brick_alt")).setCreativeTab(Roots.tab);
    event.addBlock(chiseled_runed_obsidian = new BlockRunedObsidian(Material.ROCK, SoundType.METAL, 8.5f, "chiseled_runed_obsidian")).setCreativeTab(Roots.tab);
    event.addBlock(runestone_trample = new BlockTrample(Material.ROCK, SoundType.METAL, 1.4f, "runestone_trample", TileEntityTrample.class)).setCreativeTab(Roots.tab);

    event.addBlock(runed_acacia = new BlockLogBase("runed_acacia")).setCreativeTab(Roots.tab);
    event.addBlock(runed_dark_oak = new BlockLogBase("runed_dark_oak")).setCreativeTab(Roots.tab);
    event.addBlock(runed_oak = new BlockLogBase("runed_oak")).setCreativeTab(Roots.tab);
    event.addBlock(runed_birch = new BlockLogBase("runed_birch")).setCreativeTab(Roots.tab);
    event.addBlock(runed_jungle = new BlockLogBase("runed_jungle")).setCreativeTab(Roots.tab);
    event.addBlock(runed_spruce = new BlockLogBase("runed_spruce")).setCreativeTab(Roots.tab);
    event.addBlock(runed_wildwood = new BlockLogBase("runed_wildwood")).setCreativeTab(Roots.tab);

    runestoneBlocks = Arrays.asList(runestone, runestone_brick, runestone_brick_alt, chiseled_runestone);
    runedObsidianBlocks = Arrays.asList(runed_obsidian, runed_obsidian_brick, runed_obsidian_brick_alt, chiseled_runed_obsidian);
    runedWoodBlocks = Arrays.asList(runed_acacia, runed_dark_oak, runed_oak, runed_birch, runed_jungle, runed_spruce, runed_wildwood);

    event.addBlock(wildwood_leaves = new BlockLeavesBase(0.8f, "wildwood_leaves", () -> new ItemStack(ModItems.wildroot), 50).setFlammable(true).setCreativeTab(Roots.tab));
    event.addBlock(wildwood_log = new BlockLogBase("wildwood_log").setCreativeTab(Roots.tab));
    event.addBlock(wildwood_planks = new BlockBase(Material.WOOD, SoundType.WOOD, 2.0f, "wildwood_planks").setCreativeTab(Roots.tab));
    event.addBlock(wildwood_sapling = new BlockSaplingBase("wildwood_sapling", () -> new WorldGenBigWildwoodTree(true)).setModelCustom(false).setCreativeTab(Roots.tab));

    event.addBlock(wildwood_door = new BlockDoorBase(wildwood_planks, SoundType.WOOD, 2.0f, "wildwood_door").setLayer(BlockRenderLayer.TRANSLUCENT).setCreativeTab(Roots.tab));
    event.addBlock(wildwood_trapdoor = new BlockTrapDoorBase(wildwood_planks, SoundType.WOOD, 2.0f, "wildwood_trapdoor").setLayer(BlockRenderLayer.TRANSLUCENT).setCreativeTab(Roots.tab));
    event.addBlock(wildwood_fence = new BlockFenceBase(wildwood_planks, SoundType.WOOD, 2.0f, "wildwood_fence").setCreativeTab(Roots.tab));
    event.addBlock(wildwood_fence_gate = new BlockFenceGateBase(wildwood_planks, SoundType.WOOD, 2.0f, "wildwood_fence_gate").setCreativeTab(Roots.tab));
    event.addBlock(wildwood_ladder = new BlockLadderBase(wildwood_planks, 2.0f, "wildwood_ladder").setCreativeTab(Roots.tab));

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

    runes = variantsObsidian(event, runed_obsidian, "runed_obsidian", SoundType.STONE, Material.ROCK);
    runed_obsidian_slab = runes.slab;
    runed_obsidian_double_slab = runes.double_slab;
    runed_obsidian_stairs = runes.stairs;
    runed_obsidian_wall = runes.wall;
    runed_obsidian_button = runes.button;
    runed_obsidian_pressure_plate = runes.pressure_plate;

    runes = variantsObsidian(event, runed_obsidian_brick, "runed_obsidian_brick", SoundType.STONE, Material.ROCK);
    runed_obsidian_brick_slab = runes.slab;
    runed_obsidian_brick_double_slab = runes.double_slab;
    runed_obsidian_brick_stairs = runes.stairs;
    runed_obsidian_brick_wall = runes.wall;
    runed_obsidian_brick_button = runes.button;
    runed_obsidian_brick_pressure_plate = runes.pressure_plate;

    runes = variants(event, runed_obsidian_brick_alt, "runed_obsidian_brick_alt", SoundType.STONE, Material.ROCK);
    runed_obsidian_brick_alt_slab = runes.slab;
    runed_obsidian_brick_alt_double_slab = runes.double_slab;
    runed_obsidian_brick_alt_stairs = runes.stairs;
    runed_obsidian_brick_alt_wall = runes.wall;
    runed_obsidian_brick_alt_button = runes.button;
    runed_obsidian_brick_alt_pressure_plate = runes.pressure_plate;

    //event.addBlock(structure_marker = new BlockStructureMarker());
    event.addBlock(mortar = new BlockMortar(Material.ROCK, SoundType.STONE, 1.4f, "mortar", TileEntityMortar.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(pyre = new BlockPyre(Material.WOOD, SoundType.WOOD, 1.4f, "pyre", TileEntityPyre.class, false)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(reinforced_pyre = new BlockReinforcedPyre(Material.ROCK, SoundType.STONE, 8.4f, "reinforced_pyre", TileEntityPyre.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(decorative_pyre = new BlockDecorativePyre(Material.WOOD, SoundType.WOOD, 1.4f, "decorative_pyre", TileEntityDecorativePyre.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(imbuer = new BlockImbuer(Material.WOOD, SoundType.WOOD, 1.4f, "imbuer", TileEntityImbuer.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(imposer = new BlockImposer(Material.WOOD, SoundType.WOOD, 1.4f, "imposer", TileEntityImposer.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(catalyst_plate = new BlockCatalystPlate(Material.ROCK, SoundType.STONE, 1.4f, "catalyst_plate", TileEntityCatalystPlate.class, false)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(reinforced_catalyst_plate = new BlockReinforcedCatalystPlate(Material.ROCK, SoundType.STONE, 4.4f, "reinforced_catalyst_plate", TileEntityCatalystPlate.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(unending_bowl = new BlockUnendingBowl(Material.ROCK, SoundType.STONE, 1.4f, "unending_bowl", TileEntityUnendingBowl.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    //event.addBlock(incense_burner = new BlockIncenseBurner(Material.ROCK, SoundType.STONE, 1.4f, "incense_burner", TileEntityIncenseBurner.class)).setCreativeTab(Roots.tab).setLightOpacity(0);

    //Runes
    event.addBlock(wildroot_rune = new BlockWildwoodRune(Material.WOOD, SoundType.WOOD, 1.4f, "wildroot_rune")).setCreativeTab(Roots.tab);

    // Grove Stones
    event.addBlock(grove_stone = new BlockGroveStone("grove_stone")).setCreativeTab(Roots.tab);

    event.addBlock(fey_crafter = new BlockFeyCrafter(Material.WOOD, SoundType.WOOD, 2.5f, "fey_crafter", TileEntityFeyCrafter.class)).setCreativeTab(Roots.tab);
    event.addBlock(runic_crafter = new BlockFeyCrafter(Material.ROCK, SoundType.STONE, 4.5f, "runic_crafter", TileEntityRunicCrafter.class)).setCreativeTab(Roots.tab);

    event.addBlock(fey_light = new BlockNormalFeyLight(Material.SNOW, SoundType.SNOW, 0.0f, "fey_light"));
    event.addBlock(fey_colored_light = new BlockColoredFeyLight(Material.SNOW, SoundType.SNOW, 0.0f, "fey_colored_light"));
    event.addBlock(fey_decaying_light = new BlockDecayingFeyLight(Material.SNOW, SoundType.SNOW, 0.0f, "fey_decaying_light"));
    event.addBlock(fey_fire = new BlockWildFire("fey_fire")).setCreativeTab(Roots.tab);
    event.addBlock(fake_water = new BlockFakeLiquid(Material.WATER, SoundType.SNOW, 0, "fake_water")).setCreativeTab(Roots.tab);
    event.addBlock(fake_lava = new BlockFakeLiquid(Material.LAVA, SoundType.SNOW, 0, "fake_lava")).setCreativeTab(Roots.tab);

    ModItems.baffle_cap = ((BlockMushroomBase) baffle_cap_mushroom).getItemBlock();
  }

  private static Variants variantsObsidian(RegisterContentEvent event, Block base, String name, SoundType sound, Material material) {
    Block[] slabs = new Block[2];
    Block stairs;
    Block wall;
    Block button;
    Block pressure_plate;
    LibRegistry.addSlabPair(material, sound, 8.5f, name, base.getDefaultState(), slabs, Roots.tab, BlockRunedSlab::new);
    event.addBlock(stairs = new BlockRunedStairs(base.getDefaultState(), sound, 8.5f, name + "_stairs").setCreativeTab(Roots.tab));
    event.addBlock(wall = new BlockRunedWall(base, sound, 8.5f, name + "_wall").setCreativeTab(Roots.tab));
    event.addBlock(button = new BlockRunedButton(base, sound, 8.5f, name + "_button").setCreativeTab(Roots.tab));
    event.addBlock(pressure_plate = new BlockRunedPressurePlate(base, BlockPressurePlateBase.PressurePlateType.MOBS, sound, 8.5f, name + "_pressure_plate").setCreativeTab(Roots.tab));
    return new Variants(slabs, stairs, wall, button, pressure_plate);
  }

  private static Variants variants(RegisterContentEvent event, Block base, String name, SoundType sound, Material material) {
    Block[] slabs = new Block[2];
    Block stairs;
    Block wall;
    Block button;
    Block pressure_plate;
    LibRegistry.addSlabPair(material, sound, 1.7f, name, base.getDefaultState(), slabs,
        Roots.tab);
    event.addBlock(stairs = new BlockStairsBase(base.getDefaultState(), sound, 1.7f, name + "_stairs").setCreativeTab(Roots.tab));
    event.addBlock(wall = new BlockWallBase(base, sound, 1.7f, name + "_wall").setCreativeTab(Roots.tab));
    if (material.equals(Material.ROCK)) {
      event.addBlock(button = new BlockButtonStoneBase(base, sound, 1.7f, name + "_button").setCreativeTab(Roots.tab));
      event.addBlock(pressure_plate = new BlockPressurePlateBase(base, BlockPressurePlateBase.PressurePlateType.MOBS, sound, 1.7f, name + "_pressure_plate").setCreativeTab(Roots.tab));
    } else {
      event.addBlock(button = new BlockButtonWoodBase(base, sound, 1.7f, name + "_button").setCreativeTab(Roots.tab));
      event.addBlock(pressure_plate = new BlockPressurePlateBase(base, BlockPressurePlateBase.PressurePlateType.ALL, sound, 1.7f, name + "_pressure_plate").setCreativeTab(Roots.tab));
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

    public Variants(Block[] slabs, Block stairs, Block wall, Block button, Block pressure_plate) {
      this.slab = slabs[0];
      this.double_slab = slabs[1];
      this.stairs = stairs;
      this.wall = wall;
      this.button = button;
      this.pressure_plate = pressure_plate;
    }
  }
}
