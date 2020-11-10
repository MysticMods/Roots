package epicsquid.roots.block;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockColoredFeyLight extends BlockFeyLight {
  private static float[][][] colors = new float[][][]{
      new float[][]{ // Pink
          new float[]{186 / 255.0f, 87 / 255.0f, 161 / 166.0f},
          new float[]{196 / 255.0f, 53 / 255.0f, 168 / 255.0f},
          new float[]{156 / 255.0f, 98 / 255.0f, 179 / 255.0f},
          new float[]{184 / 255.0f, 53 / 255.0f, 118 / 255.0f},
          new float[]{207 / 255.0f, 107 / 255.0f, 157 / 255.0f}
      },
      new float[][]{ // Yellow
          new float[]{194 / 255.0f, 192 / 255.0f, 66 / 255.0f},
          new float[]{199 / 255.0f, 164 / 255.0f, 50 / 255.0f},
          new float[]{240 / 255.0f, 196 / 255.0f, 36 / 255.0f},
          new float[]{242 / 255.0f, 211 / 255.0f, 72 / 255.0f},
          new float[]{245 / 255.0f, 227 / 255.0f, 108 / 255.0f}
      },
      new float[][]{ // Purple
          new float[]{117 / 255.0f, 60 / 255.0f, 222 / 255.0f},
          new float[]{143 / 255.0f, 87 / 255.0f, 186 / 255.0f},
          new float[]{162 / 255.0f, 114 / 255.0f, 176 / 255.0f},
          new float[]{139 / 255.0f, 76 / 255.0f, 217 / 255.0f},
          new float[]{170 / 255.0f, 122 / 255.0f, 230 / 255.0f}
      },
      new float[][]{ // Green
          new float[]{58 / 255.0f, 161 / 255.0f, 66 / 255.0f},
          new float[]{54 / 255.0f, 199 / 255.0f, 97 / 255.0f},
          new float[]{113 / 255.0f, 212 / 255.0f, 89 / 255.0f},
          new float[]{80 / 255.0f, 148 / 255.0f, 80 / 255.0f},
          new float[]{84 / 255.0f, 222 / 255.0f, 84 / 255.0f}
      },
      new float[][]{ // Red
          new float[]{222 / 255.0f, 40 / 255.0f, 40 / 255.0f},
          new float[]{232 / 255.0f, 88 / 255.0f, 88 / 255.0f},
          new float[]{186 / 255.0f, 87 / 255.0f, 111 / 255.0f},
          new float[]{196 / 255.0f, 59 / 255.0f, 59 / 255.0f},
          new float[]{186 / 255.0f, 11 / 255.0f, 11 / 255.0f}
      },
      new float[][]{ // Brown
          new float[]{186 / 255.0f, 81 / 255.0f, 11 / 255.0f},
          new float[]{166 / 255.0f, 103 / 255.0f, 61 / 255.0f},
          new float[]{240 / 255.0f, 163 / 255.0f, 70 / 255.0f},
          new float[]{184 / 255.0f, 76 / 255.0f, 40 / 255.0f},
          new float[]{217 / 255.0f, 67 / 255.0f, 17 / 255.0f}
      },
      new float[][]{ // Blue
          new float[]{17 / 255.0f, 177 / 255.0f, 217 / 255.0f},
          new float[]{54 / 255.0f, 133 / 255.0f, 153 / 255.0f},
          new float[]{14 / 255.0f, 100 / 255.0f, 176 / 255.0f},
          new float[]{74 / 255.0f, 139 / 255.0f, 224 / 255.0f},
          new float[]{42 / 255.0f, 68 / 255.0f,  212/ 255.0f}
      }
  };

  // PINK, YELLOW, PURPLE, GREEN, RED, BROWN, BLUE

  public static final PropertyInteger COLOR = PropertyInteger.create("color", 0, 6);

  public BlockColoredFeyLight(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
    setTickRandomly(true);
    this.setDefaultState(this.getDefaultState().withProperty(COLOR, 0));
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(COLOR, meta);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(COLOR);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer.Builder(this).add(COLOR).build();
  }

  @Override
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
    int col = state.getValue(COLOR);
    // SANITY CHECK THIS
    float[][] color = colors[col];

    int ind = Util.rand.nextInt(5);

    float r = color[ind][0];
    float g = color[ind][1];
    float b = color[ind][2];
    for (int i = 0; i < 2; i++) {
      ParticleUtil.spawnParticleGlow(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, (random.nextFloat() - 0.5f) * 0.003f, 0f, (random.nextFloat() - 0.5f) * 0.003f, r, g, b, 0.25f, 3.0f, 240);
    }
  }
}
