package epicsquid.roots.block.groves;

import epicsquid.mysticallib.particle.particles.ParticleLeafArc;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockTwilightGroveStone extends BlockGroveStone {
  public BlockTwilightGroveStone(@Nonnull String name) {
    super(name);
  }

  @Override
  public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
    this.updateTick(world, pos, state, random);

    /*if (!GeneralConfig.EnableGroveStoneEnvironment) return;

    if (world.isRemote) return;

    if (!state.getValue(VALID)) return;

    if (random.nextInt(GeneralConfig.GroveStoneChance) == 1) {
      int effectsCount = 1 + random.nextInt(1);

      List<BlockPos> positions = Util.getBlocksWithinRadius(world, pos.down(), 4, 5, 4, (p) -> {
        if (world.isAirBlock(p.up())) {
          IBlockState s = world.getBlockState(p);
          return s.getMaterial() == Material.GRASS;
        }
        return false;
      });

      Collections.shuffle(positions);

      for (BlockPos p : positions) {
        if (effectsCount <= 0) break;

        IBlockState s = world.getBlockState(p);
        if (s.getMaterial() == Material.GRASS) {
          switch (random.nextInt(50)) {
            case 0:
              Blocks.DOUBLE_PLANT.placeAt(world, p.up(), BlockDoublePlant.EnumPlantType.ROSE, 3);
              break;
            case 1:
              Blocks.DOUBLE_PLANT.placeAt(world, p.up(), BlockDoublePlant.EnumPlantType.SUNFLOWER, 3);
              break;
            case 2:
            case 3:
              Blocks.DOUBLE_PLANT.placeAt(world, p.up(), BlockDoublePlant.EnumPlantType.GRASS, 3);
              break;
            case 4:
              Blocks.DOUBLE_PLANT.placeAt(world, p.up(), BlockDoublePlant.EnumPlantType.PAEONIA, 3);
              break;
            case 5:
              Blocks.DOUBLE_PLANT.placeAt(world, p.up(), BlockDoublePlant.EnumPlantType.SYRINGA, 3);
              break;
            default:
              world.setBlockState(p.up(), Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), 3);
              break;
          }
          MessageOvergrowthEffectFX message = new MessageOvergrowthEffectFX(p.getX() + 0.5, p.getY() + 0.3, p.getZ() + 0.5);
          PacketHandler.sendToAllTracking(message, world, p.up());
          effectsCount--;
        }
      }
    }*/
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (stateIn.getValue(VALID)) {
      for (int i = -2; i <= 2; ++i) {
        for (int j = -2; j <= 2; ++j) {
          if (i > -2 && i < 2 && j == -1) {
            j = 2;
          }
          if (rand.nextInt(32) == 0) {
            for (int k = 0; k <= 1; ++k) {
              if (!worldIn.isAirBlock(pos.add(i / 2, 0, j / 2))) {
                break;
              }
              ClientProxy.particleRenderer.spawnParticle(worldIn, Util.getLowercaseClassName(ParticleLeafArc.class), (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, (i + rand.nextDouble() - 0.05) * 0.04, -0.0001, (j + rand.nextFloat() - 0.05) * 0.04,
                  100, 90 / 255.0, 98 / 255.0, (145 / 255.0) + rand.nextDouble() * 0.05, 1, rand.nextDouble() + 0.5, rand.nextDouble() * 2);
            }
          }
        }
      }
    }
  }
}
