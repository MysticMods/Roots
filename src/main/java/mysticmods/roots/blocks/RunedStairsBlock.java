package mysticmods.roots.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

import java.util.function.Supplier;

public class RunedStairsBlock extends StairsBlock {
    public RunedStairsBlock(Supplier<BlockState> state, Properties builder) {
        super(state, builder);
    }
}
