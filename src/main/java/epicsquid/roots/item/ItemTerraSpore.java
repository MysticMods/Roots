package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.config.MossConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class ItemTerraSpore extends ItemBase {

  private Random random;

  public ItemTerraSpore(@Nonnull String name) {
    super(name);
    random = new Random();
  }

  @Override
  @Nonnull
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    IBlockState state = world.getBlockState(pos);
    IBlockState mossified = MossConfig.mossConversion(state);
    if (mossified != null && isWaterAround(pos, world)) {
      world.setBlockState(pos, mossified);

      if (world.isRemote) {
        for (int i = 0; i < 50; i++) {
          ClientProxy.particleRenderer.spawnParticle(world, ParticleGlitter.class, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1),
              random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), 120, 0.607, 0.698 + random.nextDouble() * 0.05, 0.306, 1, random.nextDouble() + 0.5, random.nextDouble() * 2);
        }
      }

      if (!player.isCreative()) {
        player.getHeldItem(hand).shrink(1);
      }
    }
    return EnumActionResult.SUCCESS;
  }

  private boolean isWaterAround(BlockPos pos, World world) {
    for (EnumFacing dir : EnumFacing.HORIZONTALS) {
      Block block = world.getBlockState(pos.offset(dir)).getBlock();
      if (GeneralConfig.getWaterBlocks().contains(block)) {
        return true;
      }
    }
    return false;
  }
}
