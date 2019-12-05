package epicsquid.roots.item;

import java.util.Random;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.config.MossConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTerraSpore extends ItemBase {

  private Random random;

  public ItemTerraSpore(@Nonnull String name) {
    super(name);
    random = new Random();
  }

  @Override
  @Nonnull
  public ActionResultType onItemUse(PlayerEntity player, World world, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
    BlockState state = world.getBlockState(pos);
    BlockState mossified = MossConfig.mossConversion(state);
    if (mossified != null && isWaterAround(pos, world)) {
      world.setBlockState(pos, mossified);

      if (world.isRemote) {
        for (int i = 0; i < 50; i++) {
          ClientProxy.particleRenderer.spawnParticle(world, Util.getLowercaseClassName(ParticleGlitter.class), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1),
              random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), 120, 0.607, 0.698 + random.nextDouble() * 0.05, 0.306, 1, random.nextDouble() + 0.5, random.nextDouble() * 2);
        }
      }

      if (!player.isCreative()) {
        player.getHeldItem(hand).shrink(1);
      }
    }
    return ActionResultType.SUCCESS;
  }

  private boolean isWaterAround(BlockPos pos, World world) {
    for (Direction dir : Direction.HORIZONTALS) {
      if (world.getBlockState(pos.offset(dir)).getBlock() == Blocks.WATER) {
        return true;
      }
    }
    return false;
  }
}
