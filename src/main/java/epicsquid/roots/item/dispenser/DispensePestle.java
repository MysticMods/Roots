package epicsquid.roots.item.dispenser;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.tileentity.TileEntityMortar;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.BlockState;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DispensePestle implements IDispenseItemBehavior {
  private static final DispensePestle INSTANCE = new DispensePestle();

  public static DispensePestle getInstance() {
    return INSTANCE;
  }

  private DispensePestle() {
  }

  @Override
  public ItemStack dispense(IBlockSource source, ItemStack stack) {
    World world = source.getWorld();
    Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
    BlockPos target = source.getBlockPos().offset(facing);
    BlockState targetState = world.getBlockState(target);
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
