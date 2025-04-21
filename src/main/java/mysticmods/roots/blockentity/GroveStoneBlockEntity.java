package mysticmods.roots.blockentity;

import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GroveStoneBlockEntity extends BaseBlockEntity implements ServerTickBlockEntity {
  public GroveStoneBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public GroveStoneBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    this(ModBlockEntities.GROVE_STONE.get(), pWorldPosition, pBlockState);
  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {

  }

  // TODO: Resetting the bounds
  @Override
  public int getRadiusX() {
    return switch (getBlockState().getValue(GroveStoneBlock.RANK)) {
      case 1 -> ConfigManager.RANK_1_GROVE_BOUNDS_ZX.getAsInt();
      case 2 -> ConfigManager.RANK_2_GROVE_BOUNDS_ZX.getAsInt();
      case 3 -> ConfigManager.RANK_3_GROVE_BOUNDS_ZX.getAsInt();
      case 4 -> ConfigManager.RANK_4_GROVE_BOUNDS_ZX.getAsInt();
      default -> 0;
    };
  }

  @Override
  public int getRadiusY() {
    return switch (getBlockState().getValue(GroveStoneBlock.RANK)) {
      case 1 -> ConfigManager.RANK_1_GROVE_BOUNDS_Y.getAsInt();
      case 2 -> ConfigManager.RANK_2_GROVE_BOUNDS_Y.getAsInt();
      case 3 -> ConfigManager.RANK_3_GROVE_BOUNDS_Y.getAsInt();
      case 4 -> ConfigManager.RANK_4_GROVE_BOUNDS_Y.getAsInt();
      default -> 0;
    };
  }

  @Override
  public int getRadiusZ() {
    return switch (getBlockState().getValue(GroveStoneBlock.RANK)) {
      case 1 -> ConfigManager.RANK_1_GROVE_BOUNDS_ZX.getAsInt();
      case 2 -> ConfigManager.RANK_2_GROVE_BOUNDS_ZX.getAsInt();
      case 3 -> ConfigManager.RANK_3_GROVE_BOUNDS_ZX.getAsInt();
      case 4 -> ConfigManager.RANK_4_GROVE_BOUNDS_ZX.getAsInt();
      default -> 0;
    };
  }
}
