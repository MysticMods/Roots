package mysticmods.roots.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import noobanidus.libs.noobutil.block.BaseBlocks;

import java.util.function.Supplier;

public class RunedObsidianBlocks  {
    public static class Button extends BaseBlocks.StoneButtonBlock {
        public Button(Properties properties) {
            super(properties);
        }
    }

    public static class PressurePlate extends BaseBlocks.PressurePlateBlock {
        public PressurePlate(Sensitivity sensitivityIn, Properties propertiesIn) {
            super(sensitivityIn, propertiesIn);
        }
    }

    public static class Slab extends SlabBlock {
        public Slab(Properties p_i48331_1_) {
            super(p_i48331_1_);
        }
    }

    public static class Stairs extends StairsBlock {
        public Stairs(Supplier<BlockState> state, Properties properties) {
            super(state, properties);
        }
    }

    public static class Wall extends WallBlock {
        public Wall(Properties p_i48301_1_) {
            super(p_i48301_1_);
        }
    }

    public static class NarrowPost extends BaseBlocks.NarrowPostBlock {
        public NarrowPost(Properties properties) {
            super(properties);
        }
    }

    public static class WidePost extends BaseBlocks.WidePostBlock {
        public WidePost(Properties properties) {
            super(properties);
        }
    }
}
