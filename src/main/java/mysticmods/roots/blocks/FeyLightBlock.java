package mysticmods.roots.blocks;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;

public class FeyLightBlock extends Block {
  public static BooleanProperty DECAYING = BooleanProperty.create("decaying");
  public static IntegerProperty DECAY = IntegerProperty.create("decay", 0, 15);
  public static BooleanProperty COLORED = BooleanProperty.create("colored");
  public static EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

  public FeyLightBlock(Properties builder) {
    super(builder);
    this.registerDefaultState(this.defaultBlockState().setValue(DECAYING, false).setValue(DECAY, 0).setValue(COLORED, false).setValue(COLOR, DyeColor.WHITE));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(DECAYING, DECAY, COLORED, COLOR);
  }
}