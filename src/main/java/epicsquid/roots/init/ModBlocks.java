package epicsquid.roots.init;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.roots.Roots;
import epicsquid.roots.block.BlockMortar;
import epicsquid.roots.tileentity.TileEntityMortar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class ModBlocks {

  // All block

  /**
   * Register all block
   */
  public static void registerBlocks(@Nonnull RegisterContentEvent event) {
    event.addBlock(new BlockMortar(Material.ROCK, SoundType.STONE, 1.4f, "mortar", TileEntityMortar.class)).setCreativeTab(Roots.tab).setLightOpacity(0);
  }
}
