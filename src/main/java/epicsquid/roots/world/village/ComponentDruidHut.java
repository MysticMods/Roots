package epicsquid.roots.world.village;

import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Random;

public class ComponentDruidHut extends StructureVillagePieces.Village {
  private EnumFacing facing;

  public ComponentDruidHut() {
  }

  public ComponentDruidHut(StructureVillagePieces.Start start, int type, Random random, StructureBoundingBox box, EnumFacing facing) {
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
  public boolean addComponentParts(World world, Random random, StructureBoundingBox bb) {
    if (this.averageGroundLvl < 0) {
      this.averageGroundLvl = this.getAverageGroundLevel(world, bb);

      if (this.averageGroundLvl < 0) {
        return true;
      }

      this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 12 - 1, 0);
    }

    IBlockState cobblestone = Blocks.COBBLESTONE.getDefaultState();
    IBlockState stairs = ModBlocks.wildwood_stairs.getDefaultState(); // TODO: Facing
    IBlockState door = ModBlocks.wildwood_door.getDefaultState(); // TODO: Facing
    IBlockState wildwood_log_down = ModBlocks.wildwood_log.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
    IBlockState planks = ModBlocks.wildwood_planks.getDefaultState();
    IBlockState thatch = epicsquid.mysticalworld.init.ModBlocks.thatch.getDefaultState();
    IBlockState runestone = ModBlocks.chiseled_runestone.getDefaultState();
    IBlockState air = Blocks.AIR.getDefaultState();

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

  public static ComponentDruidHut create(StructureVillagePieces.PieceWeight piece, StructureVillagePieces.Start start, List<StructureComponent> pieces, Random random, int structMinX, int structMinY, int structMinZ, EnumFacing facing, int type) {
    StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(structMinX, structMinY, structMinZ, 0, 0, 0, 10, 7, 9, facing);
    return (canVillageGoDeeper(box) && StructureComponent.findIntersecting(pieces, box) == null) ? new ComponentDruidHut(start, type, random, box, facing) : null;
  }

  public static class CreationHandler implements VillagerRegistry.IVillageCreationHandler {

    @Override
    public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i) {
      return new StructureVillagePieces.PieceWeight(ComponentDruidHut.class, 500, 1);
    }

    @Override
    public Class<?> getComponentClass() {
      return ComponentDruidHut.class;
    }

    @Override
    public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
      return ComponentDruidHut.create(villagePiece, startPiece, pieces, random, p1, p2, p3, facing, p5);
    }
  }
}
