package mysticmods.roots.block;

import mysticmods.roots.init.ModBlocks;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import noobanidus.libs.noobutil.block.BaseBlocks;

/**
 * These classes are specifically designed to be wither- and ender-dragon-proof
 * along with a high resistance to explosion damage.
 */
@SuppressWarnings("deprecation")
public class RunedObsidianBlocks {
  public static class Block extends net.minecraft.world.level.block.Block {
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

  public static class Stairs extends StairBlock {
    public Stairs(Properties properties) {
      super(() -> ModBlocks.RUNED_OBSIDIAN.getDefaultState(), properties);
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

  public static class Fence extends FenceBlock {
    public Fence(Properties properties) {
      super(properties);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }

  public static class Gate extends FenceGateBlock {
    public Gate(Properties properties) {
      super(properties);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }
}
