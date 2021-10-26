package mysticmods.roots.blocks;

import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import mysticmods.roots.init.ModBlocks;
import net.minecraft.block.*;
import noobanidus.libs.noobutil.block.BaseBlocks;

import java.util.function.Supplier;

/**
 * These classes are specifically designed to be wither- and ender-dragon-proof
 * along with a high resistance to explosion damage.
 */
public class RunedObsidianBlocks {
  public static NonNullUnaryOperator<AbstractBlock.Properties> RUNED_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OBSIDIAN);

  public static class Block extends net.minecraft.block.Block {
    public Block(Properties p_i48440_1_) {
      super(p_i48440_1_);
    }
  }

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
    public Stairs(Properties properties) {
      // TODO
      super(() -> null, properties);
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
