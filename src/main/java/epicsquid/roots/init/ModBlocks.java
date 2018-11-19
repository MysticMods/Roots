package epicsquid.roots.init;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.block.BlockBase;
import epicsquid.mysticallib.block.BlockStairsBase;
import epicsquid.mysticallib.block.BlockWallBase;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.roots.Roots;
import epicsquid.roots.block.BlockBonfire;
import epicsquid.roots.block.BlockImbuer;
import epicsquid.roots.block.BlockMortar;
import epicsquid.roots.block.BlockOffertoryPlate;
import epicsquid.roots.block.BlockRunestone;
import epicsquid.roots.block.BlockStructureMarker;
import epicsquid.roots.block.BlockUnendingBowl;
import epicsquid.roots.tileentity.TileEntityBonfire;
import epicsquid.roots.tileentity.TileEntityImbuer;
import epicsquid.roots.tileentity.TileEntityMortar;
import epicsquid.roots.tileentity.TileEntityOffertoryPlate;
import epicsquid.roots.tileentity.TileEntityUnendingBowl;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class ModBlocks {

  // All block
  public static Block mortar, bonfire, runestone_brick, chiseled_runestone, imbuer, structure_marker, offertory_plate, unending_bowl;

  //Runestones
  public static Block runestone, runestone_wild, runestone_natural, runestone_mystic, runestone_fungal, runestone_forbidden, runestone_fairy;

  //Decoration
  public static Block runestone_slab, runestone_double_slab, runestone_stairs, runestone_wall;
  public static Block runestone_brick_slab, runestone_brick_double_slab, runestone_brick_stairs, runestone_brick_wall;

  /**
   * Register all block
   */
  public static void registerBlocks(@Nonnull RegisterContentEvent event) {
    event.addBlock(mortar = new BlockMortar(Material.ROCK, SoundType.STONE, 1.4f, "mortar", TileEntityMortar.class)).setCreativeTab(Roots.tab)
        .setLightOpacity(0);
    event.addBlock(bonfire = new BlockBonfire(Material.WOOD, SoundType.WOOD, 1.4f, "bonfire", TileEntityBonfire.class)).setCreativeTab(Roots.tab)
        .setLightOpacity(0);
    event.addBlock(imbuer = new BlockImbuer(Material.WOOD, SoundType.WOOD, 1.4f, "imbuer", TileEntityImbuer.class)).setCreativeTab(Roots.tab)
        .setLightOpacity(0);
    event.addBlock(runestone = new BlockRunestone(Material.ROCK, SoundType.METAL, 1.4f, "runestone")).setCreativeTab(Roots.tab);
    event.addBlock(runestone_wild = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_wild")).setCreativeTab(Roots.tab);
    event.addBlock(runestone_natural = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_natural")).setCreativeTab(Roots.tab);
    event.addBlock(runestone_mystic = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_mystic")).setCreativeTab(Roots.tab);
    event.addBlock(runestone_fungal = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_fungal")).setCreativeTab(Roots.tab);
    event.addBlock(runestone_forbidden = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_forbidden")).setCreativeTab(Roots.tab);
    event.addBlock(runestone_fairy = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_fairy")).setCreativeTab(Roots.tab);
    event.addBlock(runestone_brick = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_brick")).setCreativeTab(Roots.tab);
    event.addBlock(chiseled_runestone = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "chiseled_runestone")).setCreativeTab(Roots.tab);
    event.addBlock(structure_marker = new BlockStructureMarker());
    event.addBlock(offertory_plate = new BlockOffertoryPlate(Material.ROCK, SoundType.STONE, 1.4f, "offertory_plate", TileEntityOffertoryPlate.class))
        .setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(unending_bowl = new BlockUnendingBowl(Material.ROCK, SoundType.STONE, 1.4f, "unending_bowl", TileEntityUnendingBowl.class))
        .setCreativeTab(Roots.tab).setLightOpacity(0);

    //Decoration
    variants(event, runestone, "runestone", runestone_slab, runestone_double_slab, runestone_stairs, runestone_wall);
    variants(event, runestone_brick, "runestone_brick", runestone_brick_slab, runestone_brick_double_slab, runestone_brick_stairs, runestone_brick_wall);

  }

  private static void variants(RegisterContentEvent event, Block base, String name, Block... refs) {
    LibRegistry.addSlabPair(Material.ROCK, SoundType.STONE, 1.7f, name, base.getDefaultState(), new Block[] { refs[0], refs[1] }, true,
        base.getCreativeTabToDisplayOn());
    event.addBlock(refs[2] = new BlockStairsBase(base.getDefaultState(), SoundType.STONE, 1.7f, name + "_stairs").setModelCustom(true)
        .setCreativeTab(base.getCreativeTabToDisplayOn()));
    event.addBlock(
        refs[3] = new BlockWallBase(base, SoundType.STONE, 1.7f, name + "_wall").setModelCustom(true).setCreativeTab(base.getCreativeTabToDisplayOn()));
  }
}
