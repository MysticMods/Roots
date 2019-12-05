package epicsquid.roots.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;

@SuppressWarnings("deprecation")
public class FeyLightBlock extends Block {
  public FeyLightBlock(Properties properties) {
    super(properties);
  }
/*  public FeyLightBlock(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
    this.setLightLevel(1.0f);
    this.setLightOpacity(0);

    // This prevents this from being registered as an itemblock
    this.setItemBlock(null);
  }*/

  // TODO: Voxel

  @Override
  public BlockRenderType getRenderType(final BlockState state) {
    return BlockRenderType.INVISIBLE;
  }

  // TODO: Visual tick

/*  @OnlyIn(Dist.CLIENT)
  @Override
  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    List<Float> reds = new ArrayList<Float>();
    List<Float> greens = new ArrayList<Float>();
    List<Float> blues = new ArrayList<Float>();
    reds.add(177f);
    reds.add(255f);
    reds.add(255f);
    reds.add(219f);
    reds.add(122f);
    greens.add(255f);
    greens.add(223f);
    greens.add(163f);
    greens.add(179f);
    greens.add(144f);
    blues.add(117f);
    blues.add(163f);
    blues.add(255f);
    blues.add(255f);
    blues.add(255f);

    int ind = Util.rand.nextInt(5);

    float r = reds.get(ind);
    float g = greens.get(ind);
    float b = blues.get(ind);
    for (int i = 0; i < 2; i++) {
      ParticleUtil.spawnParticleGlow(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, (random.nextFloat() - 0.5f) * 0.003f, 0f, (random.nextFloat() - 0.5f) * 0.003f, r, g, b, 0.25f, 3.0f, 240);
    }
  }*/

  // TODO: Loot table

/*  @Override
  public Item getItemDropped(BlockState state, Random rand, int fortune) {
    return Items.AIR;
  }*/
}

