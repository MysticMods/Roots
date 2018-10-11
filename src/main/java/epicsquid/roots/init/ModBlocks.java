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
import epicsquid.roots.block.BlockStructureMarker;
import epicsquid.roots.tileentity.TileEntityBonfire;
import epicsquid.roots.tileentity.TileEntityImbuer;
import epicsquid.roots.tileentity.TileEntityMortar;
import epicsquid.roots.tileentity.TileEntityOffertoryPlate;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class ModBlocks {

  // All block
  public static Block mortar, bonfire, runestone, runestone_brick, chiseled_runestone, imbuer, structure_marker, offertory_plate;
  public static Block runestone_slab, runestone_double_slab, runestone_stairs, runestone_wall;
  /**
   * Register all block
   */
  public static void registerBlocks(@Nonnull RegisterContentEvent event) {
    event.addBlock(mortar = new BlockMortar(Material.ROCK, SoundType.STONE, 1.4f, "mortar", TileEntityMortar.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(bonfire = new BlockBonfire(Material.WOOD, SoundType.WOOD, 1.4f, "bonfire", TileEntityBonfire.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(imbuer = new BlockImbuer(Material.WOOD, SoundType.WOOD, 1.4f, "imbuer", TileEntityImbuer.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
    event.addBlock(runestone = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone")).setCreativeTab(Roots.tab);
    variants(event, runestone, "runestone", runestone_slab, runestone_double_slab, runestone_stairs, runestone_wall);
    event.addBlock(runestone_brick = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "runestone_brick")).setCreativeTab(Roots.tab);
    event.addBlock(chiseled_runestone = new BlockBase(Material.ROCK, SoundType.METAL, 1.4f, "chiseled_runestone")).setCreativeTab(Roots.tab);
    event.addBlock(structure_marker = new BlockStructureMarker());
    event.addBlock(offertory_plate = new BlockOffertoryPlate(Material.ROCK, SoundType.STONE, 1.4f, "offertory_plate", TileEntityOffertoryPlate.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
  }

  private static void variants(RegisterContentEvent event, Block base, String name, Block... refs){
    event.addBlock(base);
    LibRegistry.addSlabPair(Material.ROCK, SoundType.STONE, 1.7f, name, base.getDefaultState(), new Block[]{refs[0], refs[1]}, true, base.getCreativeTabToDisplayOn());
    event.addBlock(refs[2] = new BlockStairsBase(base.getDefaultState(), SoundType.STONE, 1.7f, name+"_stairs").setModelCustom(true).setCreativeTab(base.getCreativeTabToDisplayOn()));
    event.addBlock(refs[3] = new BlockWallBase(base, SoundType.STONE, 1.7f, name+"_wall").setModelCustom(true).setCreativeTab(base.getCreativeTabToDisplayOn()));
  }
}
