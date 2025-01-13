package mysticmods.roots.block;

import mysticmods.roots.init.ModBlocks;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;

/**
 * These classes are specifically designed to be wither- and ender-dragon-proof
 * along with a high resistance to explosion damage.
 */
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

  public static class Button extends ButtonBlock {
    public Button(BlockSetType p_273462_, int p_273212_, Properties p_273290_) {
      super(p_273462_, p_273212_, p_273290_);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }

  public static class PressurePlate extends PressurePlateBlock {
    public PressurePlate(BlockSetType p_273284_, Properties p_273571_) {
      super(p_273284_, p_273571_);
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
      super(ModBlocks.RUNED_OBSIDIAN.get().defaultBlockState(), properties);
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

  public static class Fence extends FenceBlock {
    public Fence(Properties properties) {
      super(properties);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }

  // TODO:
  public static class Gate extends FenceGateBlock {
    public Gate(WoodType p_273340_, Properties p_273352_) {
      super(p_273340_, p_273352_);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
    }
  }
}
