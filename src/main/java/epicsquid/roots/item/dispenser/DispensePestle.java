package epicsquid.roots.item.dispenser;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.tileentity.TileEntityMortar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DispensePestle implements IBehaviorDispenseItem {
  private static final DispensePestle INSTANCE = new DispensePestle();

  public static DispensePestle getInstance() {
    return INSTANCE;
  }

  private DispensePestle() {
  }

  @Override
  public ItemStack dispense(IBlockSource source, ItemStack stack) {
    World world = source.getWorld();
    EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
    BlockPos target = source.getBlockPos().offset(facing);
    IBlockState targetState = world.getBlockState(target);
    Block targetBlock = targetState.getBlock();

    if (targetBlock == ModBlocks.mortar) {
      TileEntity te = world.getTileEntity(target);
      if (te instanceof TileEntityMortar) {
        ((TileEntityMortar) te).handleCraft(source.getBlockPos());
      }
    }

    return stack;
  }
}
