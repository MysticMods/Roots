package epicsquid.roots.block;

import epicsquid.roots.spell.SpellFeyLight;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.util.SoundEvents;
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
    this.setDefaultState(this.getDefaultState().with(DECAY, 0));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer.Builder(this).add(DECAY).build();
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateFromMeta(int meta) {
    return this.getDefaultState().with(DECAY, meta);
  }

  @Override
  public int getMetaFromState(BlockState state) {
    return state.get(DECAY);
  }

  @Override
  public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
    super.updateTick(worldIn, pos, state, rand);

    if (!worldIn.isAreaLoaded(pos, 1)) {
      return;
    }

    if (!worldIn.isRemote) {
      if (state.get(DECAY) == 15) {
        // TODO: FIX SOUND EFFECTS
        worldIn.playSound(null, pos, SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.PLAYERS, 0.25f, 1);
        worldIn.setBlockToAir(pos);
      } else {
        worldIn.setBlockState(pos, state.with(DECAY, Math.min(state.get(DECAY) + 1, 15)));
        worldIn.scheduleUpdate(pos, this, SpellFeyLight.instance.nextTick());
      }
    }
  }
}
