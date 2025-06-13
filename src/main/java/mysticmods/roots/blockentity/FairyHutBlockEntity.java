package mysticmods.roots.blockentity;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.grove.IGroveConsumer;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.block.FairyHutBlock;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FairyHutBlockEntity extends BaseBlockEntity implements ServerTickBlockEntity, ClientTickBlockEntity, IGroveConsumer {
  private boolean powered = false;

  public FairyHutBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.FAIRY_HUT.get(), pWorldPosition, pBlockState);
  }

  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    if (pState.getValue(FairyHutBlock.ACTIVE)) {
    }
  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    if (!pState.getValue(FairyHutBlock.ACTIVE) && powered) {
      pLevel.setBlock(pPos, pState.setValue(FairyHutBlock.ACTIVE, true), 3);
      BlockState aboveState = pLevel.getBlockState(pPos.above());
      pLevel.setBlock(pPos.above(), aboveState.setValue(FairyHutBlock.ACTIVE, true), 3);
    } else if (pState.getValue(FairyHutBlock.ACTIVE) && !powered) {
      pLevel.setBlock(pPos, pState.setValue(FairyHutBlock.ACTIVE, false), 3);
      BlockState aboveState = pLevel.getBlockState(pPos.above());
      if (aboveState.getBlock() instanceof FairyHutBlock) {
        pLevel.setBlock(pPos.above(), aboveState.setValue(FairyHutBlock.ACTIVE, false), 3);
      }
    }
  }

  @Override
  public boolean isBounded() {
    return false;
  }

  @Override
  public boolean isPowered() {
    return powered;
  }

  @Override
  public void markPowered(IGroveInstance grove, boolean powered) {
    if (this.powered != powered) {
      this.powered = powered;
      setChanged();
      updateViaState();
    }
  }

  @Override
  public int getRequiredPower(IGroveInstance grove) {
    if (grove.is(RootsTags.Groves.FAIRY)) {
      return 15;
    }

    return 0;
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    this.powered = tag.getBoolean("powered");
  }

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider lookup) {
    super.saveAdditional(pTag, lookup);
    pTag.putBoolean("powered", this.powered);
  }
}
