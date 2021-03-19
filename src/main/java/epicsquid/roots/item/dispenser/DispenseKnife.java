package epicsquid.roots.item.dispenser;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.config.MossConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.tileentity.TileEntityFeyCrafter;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class DispenseKnife implements IBehaviorDispenseItem {
  private static final DispenseKnife INSTANCE = new DispenseKnife();

  public static DispenseKnife getInstance() {
    return INSTANCE;
  }

  private DispenseKnife() {
  }

  @Override
  public ItemStack dispense(IBlockSource source, ItemStack stack) {
    World world = source.getWorld();
    EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
    BlockPos target = source.getBlockPos().offset(facing);
    IBlockState targetState = world.getBlockState(target);
    if (targetState.getBlock() == ModBlocks.fey_crafter || targetState.getBlock() == ModBlocks.runic_crafter) {
      TileEntity te = world.getTileEntity(target);
      if (te instanceof TileBase) {
        FakePlayer crafter = ModDamage.getFakePlayer(world, ModDamage.FEY_CRAFTER_FAKE_PLAYER);
        crafter.setHeldItem(EnumHand.MAIN_HAND, stack);
        ((TileBase)te).activate(world, target, targetState, crafter, EnumHand.MAIN_HAND, facing.getOpposite(), 0.5f, 0.5f, 0.5f);
      }
    } else {
      if (MossConfig.getBlacklistDimensions().contains(world.provider.getDimension())) {
        return stack;
      }

      IBlockState result = MossConfig.scrapeResult(targetState);

      if (result != null) {
        world.setBlockState(target, result);
        world.scheduleBlockUpdate(target, result.getBlock(), 1, result.getBlock().tickRate(world));
        ItemUtil.spawnItem(world, source.getBlockPos().add(0, 1, 0), new ItemStack(ModItems.terra_moss));
        stack.damageItem(1, FakePlayerFactory.getMinecraft((WorldServer) world));
        source.getWorld().playSound(null, source.getBlockPos(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
      }
    }

    return stack;
  }
}
