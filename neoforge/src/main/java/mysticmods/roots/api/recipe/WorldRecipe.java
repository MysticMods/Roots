package mysticmods.roots.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.function.Function;

public abstract class WorldRecipe<W extends IWorldCrafting> extends RootsRecipe<ItemStackHandler, W> implements IWorldRecipe<W> {
  protected BlockState outputState;
  protected Condition condition;

  public WorldRecipe() {
    super();
  }

  public void setOutputState(BlockState outputState) {
    this.outputState = outputState;
  }

  public BlockState getOutputState() {
    return this.outputState;
  }

  public Condition getCondition() {
    return condition;
  }

  public void setCondition(Condition condition) {
    this.condition = condition;
  }

  @Override
  public boolean matches(W pContainer, Level pLevel) {
    return getCondition().test(pContainer.getBlockPos(), pLevel);
  }

  public BlockState modifyState(W pContainer, BlockState state) {
    return state;
  }

  @Override
  public ItemStack assemble(W pInv, HolderLookup.Provider provider) {
    Level level = pInv.getLevel();
    if (level == null) {
      throw new IllegalStateException("Cannot assemble recipe without a world!");
    }
    if (!level.isClientSide()) {
      BlockPos pos = pInv.getBlockPos();
      BlockState newState = modifyState(pInv, level.getBlockState(pos));
      level.setBlock(pos, newState, 11);
      Player player = pInv.getPlayer();
      if (player != null) {
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(pInv.getPlayer(), newState));
        for (Grant grant : getGrants()) {
          grant.grant((ServerPlayer) player);
        }
      }
    }

    return getResultItem(provider).copy();
  }

  public enum Shift implements Function<BlockPos, BlockPos>, StringRepresentable {
    NONE(null),
    ABOVE(Direction.UP),
    BELOW(Direction.DOWN),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    EAST(Direction.EAST),
    WEST(Direction.WEST);

    public static final Codec<Shift> CODEC = StringRepresentable.fromEnum(Shift::values);

    private final Direction offset;

    Shift(Direction offset) {
      this.offset = offset;
    }

    @Override
    public BlockPos apply(BlockPos blockPos) {
      return offset == null ? blockPos : blockPos.relative(offset);
    }

    @Override
    public String getSerializedName() {
      return this.name().toLowerCase(Locale.ROOT);
    }
  }

  public static class Condition implements BiPredicate<BlockPos, Level> {
    private final Shift shift;
    private final RuleTest test;

    public static final Codec<Condition> CODEC = RecordCodecBuilder.create((codec) -> codec.group(Shift.CODEC.fieldOf("shift").forGetter((condition) -> condition.shift), RuleTest.CODEC.fieldOf("test").forGetter((condition) -> condition.test)).apply(codec, Condition::new));

    public Condition(Shift shift, RuleTest test) {
      this.shift = shift;
      this.test = test;
    }

    public Condition(RuleTest test) {
      this(Shift.NONE, test);
    }

    @Override
    public boolean test(BlockPos blockPos, Level level) {
      BlockPos pos = shift.apply(blockPos);
      BlockState stateAt = level.getBlockState(pos);
      return test.test(stateAt, level.getRandom());
    }
  }
}
