package epicsquid.roots.world.village;

import epicsquid.roots.Roots;
import javafx.geometry.BoundingBox;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Random;

public class ComponentDruidHut extends StructureVillagePieces.Village {
  private final ResourceLocation structure = new ResourceLocation(Roots.MODID, "druidhut");

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
    if (averageGroundLvl < 0) {
      averageGroundLvl = getAverageGroundLevel(world, bb);
      if (averageGroundLvl < 0) {
        return true;
      }
      boundingBox.offset(0, averageGroundLvl - boundingBox.maxY + 10 - 1, 0);
    }

    fillWithAir(world, boundingBox, 0, 0, 0, 10, 7, 9);

    MinecraftServer minecraftServer = world.getMinecraftServer();
    TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
    Template template = templateManager.getTemplate(minecraftServer, structure);

    PlacementSettings placementSettings = new PlacementSettings();

    placementSettings.setBoundingBox(boundingBox);
    Rotation rotation = Rotation.NONE;
    switch (facing) {
      case EAST:
        break;
      case WEST:
        rotation = Rotation.CLOCKWISE_180;
        break;
      case SOUTH:
        rotation = Rotation.COUNTERCLOCKWISE_90;
        break;
      case NORTH:
        rotation = Rotation.CLOCKWISE_90;
        break;
    }
    placementSettings.setRotation(rotation);
    placementSettings.setIntegrity(1);
    placementSettings.setRandom(random);

    BlockPos pos = new BlockPos(boundingBox.minX, boundingBox.minY, boundingBox.minZ); //.down(4).offset(facing.getOpposite(), 4);

    template.addBlocksToWorld(world, pos, placementSettings);
    return false;
  }

  public static ComponentDruidHut create(StructureVillagePieces.PieceWeight piece, StructureVillagePieces.Start start, List<StructureComponent> pieces, Random random, int structMinX, int structMinY, int structMinZ, EnumFacing facing, int type) {
    StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(structMinX, structMinY, structMinZ, 0, 0, 0, 10, 7, 9, facing);
    if (canVillageGoDeeper(box) && StructureComponent.findIntersecting(pieces, box) == null) {
      return new ComponentDruidHut(start, type, random, box, facing);
    }

    return null;
  }

  public static class CreationHandler implements VillagerRegistry.IVillageCreationHandler {

    @Override
    public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i) {
      return new StructureVillagePieces.PieceWeight(ComponentDruidHut.class, 5, MathHelper.getInt(random, i, 1 + i));
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
