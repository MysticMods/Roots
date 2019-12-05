package epicsquid.roots.item.dispenser;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.config.MossConfig;
import epicsquid.roots.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.common.util.FakePlayerFactory;

public class KnifeDispenser implements IDispenseItemBehavior {
  private static final KnifeDispenser INSTANCE = new KnifeDispenser();

  public static KnifeDispenser getInstance() {
    return INSTANCE;
  }

  private KnifeDispenser() {
  }

  @Override
  public ItemStack dispense(IBlockSource source, ItemStack stack) {
    World world = source.getWorld();
    Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
    BlockPos target = source.getBlockPos().offset(facing);
    BlockState targetState = world.getBlockState(target);
    BlockState result = MossConfig.scrapeResult(targetState);

    if (result != null) {
      world.setBlockState(target, result);
      world.scheduleBlockUpdate(target, result.getBlock(), 1, result.getBlock().tickRate(world));
      ItemUtil.spawnItem(world, source.getBlockPos().add(0, 1, 0), new ItemStack(ModItems.terra_moss));
      stack.damageItem(1, FakePlayerFactory.getMinecraft((ServerWorld) world));
      source.getWorld().playSound(null, source.getBlockPos(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
    }

    return stack;
  }
}
