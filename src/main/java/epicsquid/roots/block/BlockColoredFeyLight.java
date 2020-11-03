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
          new float[]{224 / 255.0f, 162 / 255.0f, 201 / 255.0f},
          new float[]{201 / 255.0f, 125 / 255.0f, 191 / 255.0f},
          new float[]{235 / 255.0f, 190 / 255.0f, 218 / 255.0f},
          new float[]{161 / 255.0f, 143 / 255.0f, 189 / 255.0f},
          new float[]{218 / 255.0f, 182 / 255.0f, 227 / 255.0f}
      },
      new float[][]{ // Yellow
          new float[]{230 / 255.0f, 229 / 255.0f, 177 / 255.0f},
          new float[]{247 / 255.0f, 240 / 255.0f, 183 / 255.0f},
          new float[]{245 / 255.0f, 225 / 255.0f, 166 / 255.0f},
          new float[]{241 / 255.0f, 245 / 255.0f, 166 / 255.0f},
          new float[]{252 / 255.0f, 247 / 255.0f, 197 / 255.0f}
      },
      new float[][]{ // Purple
          new float[]{219 / 255.0f, 159 / 255.0f, 227 / 255.0f},
          new float[]{225 / 255.0f, 182 / 255.0f, 242 / 255.0f},
          new float[]{212 / 255.0f, 162 / 255.0f, 245 / 255.0f},
          new float[]{172 / 255.0f, 126 / 255.0f, 189 / 255.0f},
          new float[]{219 / 255.0f, 149 / 255.0f, 217 / 255.0f}
      },
      new float[][]{ // Green
          new float[]{187 / 255.0f, 235 / 255.0f, 164 / 255.0f},
          new float[]{214 / 255.0f, 245 / 255.0f, 186 / 255.0f},
          new float[]{177 / 255.0f, 252 / 255.0f, 177 / 255.0f},
          new float[]{179 / 255.0f, 217 / 255.0f, 163 / 255.0f},
          new float[]{218 / 255.0f, 240 / 255.0f, 189 / 255.0f}
      },
      new float[][]{ // Red
          new float[]{237 / 255.0f, 190 / 255.0f, 187 / 255.0f},
          new float[]{247 / 255.0f, 192 / 255.0f, 181 / 255.0f},
          new float[]{245 / 255.0f, 203 / 255.0f, 203 / 255.0f},
          new float[]{217 / 255.0f, 177 / 255.0f, 171 / 255.0f},
          new float[]{242 / 255.0f, 194 / 255.0f, 177 / 255.0f}
      },
      new float[][]{ // Brown
          new float[]{240 / 255.0f, 154 / 255.0f, 122 / 255.0f},
          new float[]{240 / 255.0f, 171 / 255.0f, 122 / 255.0f},
          new float[]{242 / 255.0f, 194 / 255.0f, 130 / 255.0f},
          new float[]{247 / 255.0f, 194 / 255.0f, 163 / 255.0f},
          new float[]{247 / 255.0f, 185 / 255.0f, 134 / 255.0f}
      },
      new float[][]{ // Blue
          new float[]{153 / 255.0f, 197 / 255.0f, 242 / 255.0f},
          new float[]{184 / 255.0f, 237 / 255.0f, 247 / 255.0f},
          new float[]{176 / 255.0f, 247 / 255.0f, 245 / 255.0f},
          new float[]{196 / 255.0f, 224 / 255.0f, 245 / 255.0f},
          new float[]{222 / 255.0f, 246 / 255.0f, 255 / 255.0f}
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
