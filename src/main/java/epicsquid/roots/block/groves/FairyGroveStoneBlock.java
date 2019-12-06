package epicsquid.roots.block.groves;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

@SuppressWarnings("deprecation")
public class FairyGroveStoneBlock extends GroveStoneBlock {
  public FairyGroveStoneBlock(Properties properties) {
    super(properties);
  }

  @Override
  public void randomTick(World world, BlockPos pos, BlockState state, Random random) {
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

/*  @Override
  @OnlyIn(Dist.CLIENT)
  public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
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
              ClientProxy.particleRenderer.spawnParticle(worldIn, Util.getLowercaseClassName(ParticleLeafArc.class), (double) pos.getX() + 0.5D, (double) pos.getY() + 1.0D, (double) pos.getZ() + 0.5D, (i + rand.nextDouble() - 0.05) * 0.04, -0.0001, (j + rand.nextFloat() - 0.05) * 0.04,
                  100, 1, 0.54 + rand.nextDouble() * 0.05, 0.76 + rand.nextDouble() * 0.05, 1, rand.nextDouble() + 0.5, rand.nextDouble() * 2);
            }
          }
        }
      }
    }*/
}