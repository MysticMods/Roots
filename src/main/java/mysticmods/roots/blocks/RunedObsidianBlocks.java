package mysticmods.roots.blocks;

import mysticmods.roots.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.PushReaction;
import noobanidus.libs.noobutil.block.BaseBlocks;

/**
 * These classes are specifically designed to be wither- and ender-dragon-proof
 * along with a high resistance to explosion damage.
 */
@SuppressWarnings("deprecation")
public class RunedObsidianBlocks {
  public static class Block extends net.minecraft.block.Block {
    public Block(Properties p_i48440_1_) {
      super(p_i48440_1_);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }

  public static class Button extends BaseBlocks.StoneButtonBlock {
    public Button(Properties properties) {
      super(properties);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }

  public static class PressurePlate extends BaseBlocks.PressurePlateBlock {
    public PressurePlate(Sensitivity sensitivityIn, Properties propertiesIn) {
      super(sensitivityIn, propertiesIn);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }

  public static class Slab extends SlabBlock {
    public Slab(Properties p_i48331_1_) {
      super(p_i48331_1_);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }

  public static class Stairs extends StairsBlock {
    public Stairs(Properties properties) {
      // TODO
      super(() -> ModBlocks.Decoration.RunedObsidian.RUNED_OBSIDIAN.getDefaultState(), properties);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }

  public static class Wall extends WallBlock {
    public Wall(Properties p_i48301_1_) {
      super(p_i48301_1_);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }

  public static class NarrowPost extends BaseBlocks.NarrowPostBlock {
    public NarrowPost(Properties properties) {
      super(properties);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }

  public static class WidePost extends BaseBlocks.WidePostBlock {
    public WidePost(Properties properties) {
      super(properties);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }
}
