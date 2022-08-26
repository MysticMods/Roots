package epicsquid.roots.block;

import epicsquid.roots.spell.SpellFeyLight;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockDecayingFeyLight extends BlockNormalFeyLight {
  public static final PropertyInteger DECAY = PropertyInteger.create("decay", 0, 15);

  public BlockDecayingFeyLight(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
    setTickRandomly(true);
    this.setDefaultState(this.getDefaultState().withProperty(DECAY, 0));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer.Builder(this).add(DECAY).build();
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(DECAY, meta);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(DECAY);
  }

  @Override
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    super.updateTick(worldIn, pos, state, rand);

    if (!worldIn.isAreaLoaded(pos, 1)) {
      return;
    }

    if (!worldIn.isRemote) {
      if (state.getValue(DECAY) == 15) {
        // TODO: FIX SOUND EFFECTS
        worldIn.playSound(null, pos, SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.PLAYERS, 0.25f, 1);
        worldIn.setBlockToAir(pos);
      } else {
        worldIn.setBlockState(pos, state.withProperty(DECAY, Math.min(state.getValue(DECAY) + 1, 15)));
        worldIn.scheduleUpdate(pos, this, SpellFeyLight.instance.nextTick());
      }
    }
  }
}
