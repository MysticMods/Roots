package mysticmods.roots.api.reference;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import noobanidus.libs.noobutil.util.VoxelUtil;

public interface Shapes {
  VoxelShape FEY_LIGHT = Block.box(6, 6, 6, 10, 10, 10);
  VoxelShape GROVE_CRAFTER = VoxelUtil.multiOr(Block.box(0, 8, 0, 16, 15, 16), Block.box(2, 0, 2, 14, 8, 14));
  VoxelShape RITUAL_PEDESTAL = VoxelUtil.multiOr(Block.box(3, 12, 3, 13, 14, 13), Block.box(6, 4, 3, 10, 12, 6), Block.box(6, 4, 10, 10, 12, 13), Block.box(3, 4, 6, 6, 12, 10), Block.box(10, 4, 6, 13, 12, 10), Block.box(2, 14, 2, 14, 16, 14), Block.box(3, 2, 3, 13, 4, 13), Block.box(2, 0, 2, 14, 2, 14));
  VoxelShape GROVE_STONE_BOTTOM = Block.box(3, 0, 4, 13, 16, 12);
  VoxelShape GROVE_STONE_MIDDLE = GROVE_STONE_BOTTOM;
  VoxelShape GROVE_STONE_TOP = VoxelUtil.multiOr(Block.box(3, 0, 4, 13, 3, 12), Block.box(10, 3, 4, 13, 8, 12), Block.box(3, 8, 4, 13, 11, 12), Block.box(3, 3, 4, 6, 8, 12));
  VoxelShape INCENSE_BURNER = VoxelUtil.multiOr(Block.box(3, 12, 3, 13, 14, 13), Block.box(6, 4, 3, 10, 12, 6), Block.box(6, 4, 10, 10, 12, 13), Block.box(3, 4, 6, 6, 12, 10), Block.box(10, 4, 6, 13, 12, 10), Block.box(11, 14, 2, 14, 16, 14), Block.box(2, 14, 2, 5, 16, 14), Block.box(5, 14, 2, 11, 16, 5), Block.box(5, 14, 11, 11, 16, 14), Block.box(3, 2, 3, 13, 4, 13), Block.box(2, 0, 2, 14, 2, 14), Block.box(5, 14, 5, 11, 14.5, 11));
  VoxelShape MORTAR = VoxelUtil.multiOr(Block.box(5, 0, 5, 11, 1, 11), Block.box(4, 1, 11, 12, 8, 12), Block.box(4, 1, 4, 12, 8, 5), Block.box(4, 1, 5, 5, 8, 11), Block.box(11, 1, 5, 12, 8, 11));
  VoxelShape RUNIC_CRAFTER = VoxelUtil.multiOr(Block.box(5, 1, 5, 11, 3, 11), Block.box(1, 0, 1, 15, 1, 15), Block.box(6, 3, 6, 10, 12, 10), Block.box(2, 8, 0, 6, 11, 4), Block.box(9, 7, 1, 13, 10, 5), Block.box(5, 8, 12, 9, 11, 16), Block.box(0, 7, 7, 4, 10, 11), Block.box(12, 6, 7, 16, 9, 11), Block.box(5, 12, 5, 11, 13, 11), Block.box(6, 13, 6, 10, 14, 10));
  VoxelShape UNENDING_BOWL = VoxelUtil.multiOr(Block.box(3, 0, 3, 13, 2, 13), Block.box(2, 2, 2, 14, 6, 4), Block.box(2, 2, 12, 14, 6, 14), Block.box(2, 2, 4, 4, 6, 12), Block.box(12, 2, 4, 14, 6, 12), Block.box(4, 5, 4, 12, 5, 12));
  VoxelShape PYRE = VoxelUtil.multiOr(Block.box(3, 0, 3,13, 1, 13), Block.box(5, 3, 4,7, 5, 12), Block.box(9, 3, 4,11, 5, 12), Block.box(4, 1, 5,12, 3, 7), Block.box(4, 1, 9,12, 3, 11), Block.box(3, 0, 0,13, 3, 3), Block.box(3, 0, 13,13, 3, 16), Block.box(13, 0, 3,16, 3, 13), Block.box(0, 0, 3,3, 3, 13));
  VoxelShape GROVE_PEDESTAL = VoxelUtil.multiOr(Block.box(5, 0, 5, 11, 1, 11), Block.box(5, 7, 5, 11, 8, 11), Block.box(6, 1, 6, 10, 7, 10), Block.box(4, 8, 4, 12, 9, 12));
}
