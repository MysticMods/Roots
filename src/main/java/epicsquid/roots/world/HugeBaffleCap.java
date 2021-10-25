package epicsquid.roots.world;

import epicsquid.mysticallib.world.StructureData;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class HugeBaffleCap {

  private StructureData data;

  public HugeBaffleCap() {
    this.data = new StructureData() {
      @Override
      public void generateIn(@Nonnull World world, int x, int y, int z, @Nonnull Rotation rotation, @Nonnull Mirror doMirror, boolean replaceWithAir, boolean force) {
        super.generateIn(world, x - 2, y, z - 2, rotation, doMirror, replaceWithAir, force);
      }
    };

    // SIDE VIEW
    // . S S S .
    // S . S . S
    // S . S . S
    // . . S . .
    // . . S . .

    data.addLayer(new String[]{"AAAAA", "AAAAA", "AASAA", "AAAAA", "AAAAA"}, 0);
    data.addLayer(new String[]{"AAAAA", "AAAAA", "AASAA", "AAAAA", "AAAAA"}, 1);
    data.addLayer(new String[]{"ATTTA", "TAAAT", "TASAT", "TAAAT", "ATTTA"}, 2);
    data.addLayer(new String[]{"ATTTA", "TAAAT", "TASAT", "TAAAT", "ATTTA"}, 3);
    data.addLayer(new String[]{"AAAAA", "ATTTA", "ATTTA", "ATTTA", "AAAAA"}, 4);

    data.addBlock("S", ModBlocks.baffle_cap_huge_stem.getDefaultState());
    data.addBlock("T", ModBlocks.baffle_cap_huge_top.getDefaultState());
    data.addBlock("A", Blocks.AIR.getDefaultState());
  }

  @Nonnull
  public StructureData getData() {
    return data;
  }
}
