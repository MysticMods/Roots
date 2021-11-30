package mysticmods.roots.blocks;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noobanidus.libs.particleslib.client.particle.Particles;
import noobanidus.libs.particleslib.init.ModParticles;

import java.util.Random;

public class FeyLightBlock extends Block {
  public static BooleanProperty DECAYING = BooleanProperty.create("decaying");
  public static IntegerProperty DECAY = IntegerProperty.create("decay", 0, 15);
  public static BooleanProperty COLORED = BooleanProperty.create("colored");
  public static EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

  private static int[][] UNCOLORED = {
      {177, 255, 255, 219, 122},
      {255, 223, 163, 179, 144},
      {117, 163, 255, 255, 255}
  };

  public FeyLightBlock(Properties builder) {
    super(builder);
    this.registerDefaultState(this.defaultBlockState().setValue(DECAYING, false).setValue(DECAY, 0).setValue(COLORED, false).setValue(COLOR, DyeColor.WHITE));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(DECAYING, DECAY, COLORED, COLOR);
  }

  @Override
  public void animateTick(BlockState pState, World pLevel, BlockPos pPos, Random pRand) {
    super.animateTick(pState, pLevel, pPos, pRand);

    DyeColor color = null;

    if (pState.getValue(COLORED)) {
      color = pState.getValue(COLOR);
    }

    float r, g, b;

/*    if (color == null) {*/
      int index = pRand.nextInt(5);
      r = UNCOLORED[0][index] / 255.0f;
      g = UNCOLORED[1][index] / 255.0f;
      b = UNCOLORED[2][index] / 255.0f;
/*    }*/

    // TODO: Handle additional colors
    for (int i = 0; i < 2; i++) {
      Particles.create(ModParticles.GLOW_PARTICLE)
          .setColor(r, g, b)
          .setScale(0.2f)
          .setAlpha(0.25f)
          .setLifetime(40)
          .disableGravity()
          .addVelocity((pRand.nextFloat() - 0.5f) * 0.003, 0f, (pRand.nextFloat() - 0.5f) * 0.003f)
          .spawn(pLevel, pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5);
    }
  }
}