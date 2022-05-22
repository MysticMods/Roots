package mysticmods.roots.api.reference;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import noobanidus.libs.noobutil.util.VoxelUtil;

public interface Shapes {
  VoxelShape FEY_LIGHT = Block.box(6, 6, 6, 10, 10, 10);
  VoxelShape GROVE_CRAFTER = VoxelUtil.multiOr(Block.box(0.5, 8, 0.5,15.5, 15, 15.5), Block.box(2, 0, 2,14, 8, 14));
  VoxelShape RITUAL_PEDESTAL = VoxelUtil.multiOr(Block.box(3, 2, 3,13, 4, 13), Block.box(3, 13, 3,13, 15, 13), Block.box(3, 4, 6,6, 13, 10), Block.box(10, 4, 6,13, 13, 10), Block.box(6, 4, 3,10, 13, 6), Block.box(6, 4, 10,10, 13, 13), Block.box(2, 0, 2,14, 2, 14), Block.box(2, 15, 2,14, 17, 14));
  VoxelShape GROVE_STONE_BOTTOM = Block.box(3, 0, 4,13, 16, 12);
  VoxelShape GROVE_STONE_MIDDLE = GROVE_STONE_BOTTOM;
  VoxelShape GROVE_STONE_TOP = VoxelUtil.multiOr(Block.box(3, 0, 4,13, 3, 12), Block.box(10, 3, 4,13, 8, 12), Block.box(3, 8, 4,13, 11, 12), Block.box(3, 3, 4,6, 8, 12));
  VoxelShape IMBUER = VoxelUtil.multiOr(Block.box(5, 0, 5, 11, 2, 11), Block.box(7, 0, 2, 9, 2, 4), Block.box(7, 2, 3, 9, 4, 5), Block.box(7, 4, 4, 9, 6, 6), Block.box(7, 4, 10, 9, 6, 12), Block.box(7, 2, 11, 9, 4, 13), Block.box(7, 0, 12, 9, 2, 14), Block.box(12, 0, 7, 14, 2, 9), Block.box(11, 2, 7, 13, 4, 9), Block.box(10, 4, 7, 12, 6, 9), Block.box(4, 4, 7, 6, 6, 9), Block.box(3, 2, 7, 5, 4, 9), Block.box(2, 0, 7, 4, 2, 9));
  VoxelShape INCENSE_BURNER = VoxelUtil.multiOr(Block.box(3, 2, 3,13, 4, 13), Block.box(3, 13, 3,13, 15, 13), Block.box(3, 4, 6,6, 13, 10), Block.box(10, 4, 6,13, 13, 10), Block.box(6, 4, 3,10, 13, 6), Block.box(6, 4, 10,10, 13, 13), Block.box(2, 0, 2,14, 2, 14), Block.box(2, 15, 2,14, 17, 5), Block.box(2, 15, 11,14, 17, 14), Block.box(2, 15, 5,5, 17, 11), Block.box(11, 15, 5,14, 17, 11), Block.box(5, 15, 5,11, 15.5, 11));
  VoxelShape MORTAR = VoxelUtil.multiOr(Block.box(5, 0, 5,11, 1, 11), Block.box(4, 1, 11,12, 8, 12), Block.box(4, 1, 4,12, 8, 5), Block.box(4, 1, 5,5, 8, 11), Block.box(11, 1, 5,12, 8, 11));
  VoxelShape RUNIC_CRAFTER = VoxelUtil.multiOr(Block.box(5, 1, 5, 11, 3, 11), Block.box(1, 0, 1, 15, 1, 15), Block.box(6, 3, 6, 10, 9, 10), Block.box(6, 9, 6, 10, 12, 10), Block.box(13, 7, 7, 17, 10, 11), Block.box(9, 6, 0, 13, 9, 4), Block.box(3, 8, -1, 7, 11, 3), Block.box(5, 8, 13, 9, 11, 17), Block.box(-1, 7, 7, 3, 10, 11), Block.box(5, 12, 5, 11, 13, 11), Block.box(7, 13, 7, 9, 14, 9));
  VoxelShape UNENDING_BOWL = VoxelUtil.multiOr(Block.box(3, 0, 3,13, 2, 13), Block.box(2, 2, 2,14, 6, 4), Block.box(2, 2, 12,14, 6, 14), Block.box(2, 2, 4,4, 6, 12), Block.box(12, 2, 4,14, 6, 12), Block.box(4, 5, 4,12, 5, 12));
  VoxelShape PYRE = VoxelUtil.multiOr(Block.box(5, 0, 5,11, 3, 11), Block.box(3, 0, 5,5, 2, 11), Block.box(11, 0, 5,13, 2, 11), Block.box(4, 0, 11,12, 2, 13), Block.box(4, 0, 3,12, 2, 5), Block.box(3, 0, 1,6, 3, 4), Block.box(1, 0, 3,4, 3, 6), Block.box(6, 0, 0,10, 4, 3), Block.box(6, 0, 13,10, 4, 16), Block.box(0, 0, 6,3, 4, 10), Block.box(13, 0, 6,16, 4, 10), Block.box(3, 0, 12,6, 3, 15), Block.box(1, 0, 10,4, 3, 13), Block.box(12, 0, 10,15, 3, 13), Block.box(10, 0, 12,13, 3, 15), Block.box(12, 0, 3,15, 3, 6), Block.box(10, 0, 1,13, 3, 4));
  VoxelShape GROVE_PEDESTAL = VoxelUtil.multiOr(Block.box(5, 0, 5,11, 1, 11), Block.box(5, 7, 5,11, 8, 11), Block.box(6, 1, 6,10, 7, 10), Block.box(4, 8, 4,12, 9, 12));
}
