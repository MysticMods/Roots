package epicsquid.roots.world;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class RootsTemplate extends Template {

  /*
  @Override
  public void addBlocksToWorld(World worldIn, BlockPos pos, @Nullable ITemplateProcessor templateProcessor, PlacementSettings placementIn, int flags) {
    if ((!this.blocks.isEmpty() || !placementIn.getIgnoreEntities() && !this.entities.isEmpty()) && this.size.getX() >= 1 && this.size.getY() >= 1
        && this.size.getZ() >= 1) {
      Block block = placementIn.getReplacedBlock();
      StructureBoundingBox structureboundingbox = placementIn.getBoundingBox();

      for (Template.BlockInfo template$blockinfo : this.blocks) {
        BlockPos blockpos = transformedBlockPos(placementIn, template$blockinfo.pos).add(pos);
        Template.BlockInfo template$blockinfo1 =
            templateProcessor != null ? templateProcessor.processBlock(worldIn, blockpos, template$blockinfo) : template$blockinfo;

        if (template$blockinfo1 != null) {
          Block block1 = template$blockinfo1.blockState.getBlock();

          if ((block == null || block != block1) && (!placementIn.getIgnoreStructureBlock() || block1 != Blocks.STRUCTURE_BLOCK) && (structureboundingbox == null || structureboundingbox.isVecInside(blockpos))) {
            IBlockState iblockstate = template$blockinfo1.blockState.withMirror(placementIn.getMirror());
            IBlockState iblockstate1 = iblockstate.withRotation(placementIn.getRotation());

            if (template$blockinfo1.tileentityData != null) {
              TileEntity tileentity = worldIn.getTileEntity(blockpos);

              if (tileentity != null) {
                if (tileentity instanceof IInventory) {
                  ((IInventory) tileentity).clear();
                }

                worldIn.setBlockState(blockpos, Blocks.BARRIER.getDefaultState(), 4);
              }
            }

            if (worldIn.setBlockState(blockpos, iblockstate1, flags) && template$blockinfo1.tileentityData != null) {
              TileEntity tileentity2 = worldIn.getTileEntity(blockpos);

              if (tileentity2 != null) {
                template$blockinfo1.tileentityData.setInteger("x", blockpos.getX());
                template$blockinfo1.tileentityData.setInteger("y", blockpos.getY());
                template$blockinfo1.tileentityData.setInteger("z", blockpos.getZ());
                tileentity2.readFromNBT(template$blockinfo1.tileentityData);
                tileentity2.mirror(placementIn.getMirror());
                tileentity2.rotate(placementIn.getRotation());
              }
            }
          }
        }
      }

      for (Template.BlockInfo template$blockinfo2 : this.blocks) {
        if (block == null || block != template$blockinfo2.blockState.getBlock()) {
          BlockPos blockpos1 = transformedBlockPos(placementIn, template$blockinfo2.pos).add(pos);

          if (structureboundingbox == null || structureboundingbox.isVecInside(blockpos1)) {
            worldIn.notifyNeighborsRespectDebug(blockpos1, template$blockinfo2.blockState.getBlock(), false);

            if (template$blockinfo2.tileentityData != null) {
              TileEntity tileentity1 = worldIn.getTileEntity(blockpos1);

              if (tileentity1 != null) {
                tileentity1.markDirty();
              }
            }
          }
        }
      }

      if (!placementIn.getIgnoreEntities()) {
        this.addEntitiesToWorld(worldIn, pos, placementIn.getMirror(), placementIn.getRotation(), structureboundingbox);
      }
    }
  }
   */
}
