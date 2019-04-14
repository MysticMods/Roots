package epicsquid.roots.init;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.block.*;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.roots.block.runes.BlockWildwoodRune;
import epicsquid.roots.util.EnumRunicSoilType;
import epicsquid.roots.Roots;
import epicsquid.roots.api.CustomPlantType;
import epicsquid.roots.block.*;
import epicsquid.roots.block.runes.BlockTrample;
import epicsquid.roots.tileentity.*;
import epicsquid.roots.world.HugeBaffleCap;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.EnumPlantType;

import javax.annotation.Nonnull;

public class ModBlocks {

  // All block
  public static Block mortar, bonfire,imbuer, structure_marker, offertory_plate, unending_bowl;
  public static Block incense_burner, wildwood_rune;

  public static BlockCropBase moonglow, aubergine, pereskia, wildroot, spirit_herb,wildewheet, cloud_berry, infernal_bulb, dewgonia, stalicripe;

  // Runestones
  public static Block runestone, runestone_brick, runestone_brick_alt, chiseled_runestone, wildwoodLog, wildwoodPlanks, wildwoodLeaves, baffle_cap_huge_stem, baffle_cap_huge_top,
      baffle_cap_mushroom, runic_soil_fire, runic_soil_water, runic_soil_air, runic_soil_earth, runic_soil, trample_rune;


  // Decoration
  public static Block runestone_slab, runestone_double_slab, runestone_stairs, runestone_wall;
  public static Block runestone_brick_slab, runestone_brick_double_slab, runestone_brick_stairs, runestone_brick_wall;
  public static Block runestone_brick_alt_slab, runestone_brick_alt_double_slab, runestone_brick_alt_stairs, runestone_brick_alt_wall;

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
    event.addBlock(runic_soil = new BlockBase(Material.GROUND, SoundType.GROUND, 0.8f, "runic_soil").setModelCustom(true).setCreativeTab(Roots.tab));

    event.addBlock(runic_soil_air = new BlockRunicSoil(Material.GROUND, SoundType.GROUND, "runic_soil_air", EnumRunicSoilType.AIR).setModelCustom(false).setCreativeTab(Roots.tab));
    event.addBlock(runic_soil_water = new BlockRunicSoil(Material.GROUND, SoundType.GROUND, "runic_soil_water", EnumRunicSoilType.WATER).setModelCustom(false).setCreativeTab(Roots.tab));
    event.addBlock(runic_soil_fire = new BlockRunicSoil(Material.GROUND, SoundType.GROUND, "runic_soil_fire", EnumRunicSoilType.FIRE).setModelCustom(false).setCreativeTab(Roots.tab));
    event.addBlock(runic_soil_earth = new BlockRunicSoil(Material.GROUND, SoundType.GROUND, "runic_soil_earth", EnumRunicSoilType.EARTH).setModelCustom(false).setCreativeTab(Roots.tab));

    // Post registration block setup
    ((BlockMushroomBase) baffle_cap_mushroom).setItemBlock(new ItemBlock(baffle_cap_mushroom).setRegistryName(LibRegistry.getActiveModid(), "baffle_cap_mushroom"));

    //Runestones
    event.addBlock(runestone = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone").setModelCustom(true)).setCreativeTab(Roots.tab);
    event.addBlock(runestone_brick = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_brick").setModelCustom(true)).setCreativeTab(Roots.tab);
    event.addBlock(runestone_brick_alt = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_brick_alt").setModelCustom(true)).setCreativeTab(Roots.tab);
    event.addBlock(chiseled_runestone = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "chiseled_runestone").setModelCustom(true)).setCreativeTab(Roots.tab);
    event.addBlock(trample_rune = new BlockTrample(Material.ROCK, SoundType.METAL, 1.4f, "runestone_trample").setModelCustom(true)).setCreativeTab(Roots.tab);

    event.addBlock(wildwoodLog = new BlockLogBase("wildwood_log").setCreativeTab(Roots.tab));
    event.addBlock(wildwoodPlanks = new BlockBase(Material.WOOD, SoundType.WOOD, 2.0f, "wildwood_planks").setModelCustom(true).setCreativeTab(Roots.tab));
    //      event.addBlock(wildwoodLeaves = new BlockBase(Material.LEAVES, SoundType.PLANT, 0.8f,"wildwood_leaves").setModelCustom(true).setOpacity(false).setCreativeTab(Roots.tab));

    //Decoration
    variants(event, runestone, "runestone", runestone_slab, runestone_double_slab, runestone_stairs, runestone_wall);
    variants(event, runestone_brick, "runestone_brick", runestone_brick_slab, runestone_brick_double_slab, runestone_brick_stairs, runestone_brick_wall);
    variants(event, runestone_brick_alt, "runestone_brick_alt", runestone_brick_alt_slab, runestone_brick_alt_double_slab, runestone_brick_alt_stairs, runestone_brick_alt_wall);


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

  private static void variants(RegisterContentEvent event, Block base, String name, Block... refs) {
    LibRegistry.addSlabPair(Material.ROCK, SoundType.STONE, 1.7f, name, base.getDefaultState(), new Block[] { refs[0], refs[1] }, true,
        base.getCreativeTab());
    event.addBlock(refs[2] = new BlockStairsBase(base.getDefaultState(), SoundType.STONE, 1.7f, name + "_stairs").setModelCustom(true)
        .setCreativeTab(base.getCreativeTab()));
    event.addBlock(
        refs[3] = new BlockWallBase(base, SoundType.STONE, 1.7f, name + "_wall").setModelCustom(true).setCreativeTab(base.getCreativeTab()));
  }
}
