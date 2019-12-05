package epicsquid.roots.world.village;

import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.LogBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructurePiece;
import net.minecraft.world.gen.feature.VillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Random;

public class ComponentDruidHut extends VillagePieces.Village {
  private Direction facing;

  public ComponentDruidHut() {
  }

  public ComponentDruidHut(VillagePieces.Start start, int type, Random random, MutableBoundingBox box, Direction facing) {
    super(start, type);
    this.boundingBox = box;
    this.facing = facing;

    if (type == 1) {
      boundingBox.offset(-(box.maxX - box.minX), 0, 0);
    } else if (type == 2) {
      boundingBox.offset(0, 0, -(box.maxZ - box.minZ));
    }
  }

  @Override
  public boolean addComponentParts(World world, Random random, MutableBoundingBox bb) {
    if (this.averageGroundLvl < 0) {
      this.averageGroundLvl = this.getAverageGroundLevel(world, bb);

      if (this.averageGroundLvl < 0) {
        return true;
      }

      this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 12 - 1, 0);
    }

    BlockState cobblestone = Blocks.COBBLESTONE.getDefaultState();
    BlockState stairs = ModBlocks.wildwood_stairs.getDefaultState(); // TODO: Facing
    BlockState door = ModBlocks.wildwood_door.getDefaultState(); // TODO: Facing
    BlockState wildwood_log_down = ModBlocks.wildwood_log.getDefaultState().withProperty(LogBlock.LOG_AXIS, LogBlock.EnumAxis.Y);
    BlockState planks = ModBlocks.wildwood_planks.getDefaultState();
    BlockState thatch = epicsquid.mysticalworld.init.ModBlocks.thatch.getDefaultState();
    BlockState runestone = ModBlocks.chiseled_runestone.getDefaultState();
    BlockState air = Blocks.AIR.getDefaultState();

    this.fillWithAir(world, bb, 0, 0, 0, 8, 0, 0);
    // YEP. Layer 1.
    this.setBlockState(world, air, 0, 0, 1, bb);
    this.setBlockState(world, air, 1, 0, 1, bb);
    this.setBlockState(world, air, 2, 0, 1, bb);
    this.setBlockState(world, cobblestone, 3, 0, 1, bb);
    this.setBlockState(world, cobblestone, 4, 0, 1, bb);
    this.setBlockState(world, cobblestone, 5, 0, 1, bb);
    this.setBlockState(world, air, 6, 0, 1, bb);
    this.setBlockState(world, air, 7, 0, 1, bb);
    this.setBlockState(world, air, 8, 0, 1, bb);
    // Layer 2
    this.setBlockState(world, air, 0, 0, 2, bb);
    this.setBlockState(world, air, 1, 0, 2, bb);
    this.setBlockState(world, cobblestone, 2, 0, 2, bb);
    this.setBlockState(world, planks, 3, 0, 2, bb);
    this.setBlockState(world, planks, 4, 0, 2, bb);
    this.setBlockState(world, planks, 5, 0, 2, bb);
    this.setBlockState(world, cobblestone, 6, 0, 2, bb);
    this.setBlockState(world, air, 7, 0, 2, bb);
    this.setBlockState(world, air, 8, 0, 2, bb);
    // Layer 3
    this.setBlockState(world, air, 0, 0, 3, bb);
    this.setBlockState(world, cobblestone, 2, 0, 3, bb);
    this.setBlockState(world, planks, 3, 0, 3, bb);
    this.setBlockState(world, planks, 4, 0, 3, bb);
    this.setBlockState(world, planks, 5, 0, 3, bb);
    this.setBlockState(world, planks, 6, 0, 3, bb);
    this.setBlockState(world, planks, 7, 0, 3, bb);
    this.setBlockState(world, cobblestone, 8, 0, 3, bb);
    this.setBlockState(world, air, 9, 0, 3, bb);
    // Layer 4
    this.setBlockState(world, air, 0, 0, 4, bb);
    this.setBlockState(world, cobblestone, 2, 0, 4, bb);
    this.setBlockState(world, planks, 3, 0, 4, bb);
    this.setBlockState(world, planks, 4, 0, 4, bb);
    this.setBlockState(world, runestone, 5, 0, 4, bb);
    this.setBlockState(world, planks, 6, 0, 4, bb);
    this.setBlockState(world, planks, 7, 0, 4, bb);
    this.setBlockState(world, cobblestone, 8, 0, 4, bb);
    this.setBlockState(world, air, 9, 0, 4, bb);
    // Layer 5
    this.setBlockState(world, air, 0, 0, 5, bb);
    this.setBlockState(world, cobblestone, 2, 0, 5, bb);
    this.setBlockState(world, planks, 3, 0, 5, bb);
    this.setBlockState(world, planks, 4, 0, 5, bb);
    this.setBlockState(world, planks, 5, 0, 5, bb);
    this.setBlockState(world, planks, 6, 0, 5, bb);
    this.setBlockState(world, planks, 7, 0, 5, bb);
    this.setBlockState(world, cobblestone, 8, 0, 5, bb);
    this.setBlockState(world, air, 9, 0, 5, bb);
    // Layer 6
    this.setBlockState(world, air, 0, 0, 6, bb);
    this.setBlockState(world, air, 1, 0, 6, bb);
    this.setBlockState(world, cobblestone, 2, 0, 6, bb);
    this.setBlockState(world, planks, 3, 0, 6, bb);
    this.setBlockState(world, planks, 4, 0, 6, bb);
    this.setBlockState(world, planks, 5, 0, 6, bb);
    this.setBlockState(world, cobblestone, 6, 0, 6, bb);
    this.setBlockState(world, air, 7, 0, 6, bb);
    this.setBlockState(world, air, 8, 0, 6, bb);
    // Layer 7
    this.setBlockState(world, air, 0, 0, 7, bb);
    this.setBlockState(world, air, 1, 0, 7, bb);
    this.setBlockState(world, air, 2, 0, 7, bb);
    this.setBlockState(world, cobblestone, 3, 0, 7, bb);
    this.setBlockState(world, cobblestone, 4, 0, 7, bb);
    this.setBlockState(world, cobblestone, 5, 0, 7, bb);
    this.setBlockState(world, air, 6, 0, 7, bb);
    this.setBlockState(world, air, 7, 0, 7, bb);
    this.setBlockState(world, air, 8, 0, 7, bb);
    // Layer 8
    this.fillWithAir(world, bb, 0, 0, 8, 8, 0, 8);

    return true;
  }

  public static ComponentDruidHut create(VillagePieces.PieceWeight piece, VillagePieces.Start start, List<StructurePiece> pieces, Random random, int structMinX, int structMinY, int structMinZ, Direction facing, int type) {
    MutableBoundingBox box = MutableBoundingBox.getComponentToAddBoundingBox(structMinX, structMinY, structMinZ, 0, 0, 0, 10, 7, 9, facing);
    return (canVillageGoDeeper(box) && StructurePiece.findIntersecting(pieces, box) == null) ? new ComponentDruidHut(start, type, random, box, facing) : null;
  }

  public static class CreationHandler implements VillagerRegistry.IVillageCreationHandler {

    @Override
    public VillagePieces.PieceWeight getVillagePieceWeight(Random random, int i) {
      return new VillagePieces.PieceWeight(ComponentDruidHut.class, 500, 1);
    }

    @Override
    public Class<?> getComponentClass() {
      return ComponentDruidHut.class;
    }

    @Override
    public VillagePieces.Village buildComponent(VillagePieces.PieceWeight villagePiece, VillagePieces.Start startPiece, List<StructurePiece> pieces, Random random, int p1, int p2, int p3, Direction facing, int p5) {
      return ComponentDruidHut.create(villagePiece, startPiece, pieces, random, p1, p2, p3, facing, p5);
    }
  }
}
