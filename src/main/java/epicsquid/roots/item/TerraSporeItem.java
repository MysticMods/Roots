package epicsquid.roots.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class TerraSporeItem extends Item {

  private Random random;

  public TerraSporeItem(Properties properties) {
    super(properties);
  }

/*  public TerraSporeItem(@Nonnull String name) {
    super(name);
    random = new Random();
  }*/

  @Override
  @Nonnull
  public ActionResultType onItemUse(ItemUseContext context) {
/*    PlayerEntity player, World
  } world, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {*/
    World world = context.getWorld();
    PlayerEntity player = context.getPlayer();
    BlockPos pos = context.getPos();
    BlockState state = world.getBlockState(pos);
    BlockState mossified = Blocks.AIR.getDefaultState(); // MossConfig.mossConversion(state);
    Hand hand = context.getHand();
    if (mossified != null && isWaterAround(pos, world)) {
      world.setBlockState(pos, mossified);

      if (world.isRemote) {
        for (int i = 0; i < 50; i++) {
          // TODO: Particles
/*          ClientProxy.particleRenderer.spawnParticle(world, Util.getLowercaseClassName(ParticleGlitter.class), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1),
              random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), 120, 0.607, 0.698 + random.nextDouble() * 0.05, 0.306, 1, random.nextDouble() + 0.5, random.nextDouble() * 2);*/
        }
      }

      if (!player.isCreative()) {
        player.getHeldItem(hand).shrink(1);
      }
    }
    return ActionResultType.SUCCESS;
  }

  private boolean isWaterAround(BlockPos pos, World world) {
    for (Direction dir : Direction.values()) {
      if (dir == Direction.UP || dir == Direction.DOWN) {
        continue;
      }
      if (world.getBlockState(pos.offset(dir)).getBlock() == Blocks.WATER) {
        return true;
      }
    }
    return false;
  }
}
