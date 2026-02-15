package mysticmods.roots.loot.conditions;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.init.ModLoot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record DoubleBlockPreventionCondition() implements LootItemCondition {
  private static final DoubleBlockPreventionCondition INSTANCE = new DoubleBlockPreventionCondition();
  public static final MapCodec<DoubleBlockPreventionCondition> CODEC = MapCodec.unit(() -> INSTANCE);

  public static DoubleBlockPreventionCondition prevention() {
    return INSTANCE;
  }

  @Override
  public LootItemConditionType getType() {
    return ModLoot.DOUBLE_PREVENTION.get();
  }

  // TODO: API this?
  @Override
  public boolean test(LootContext lootContext) {
    BlockPos position = BlockPos.containing(lootContext.getParam(LootContextParams.ORIGIN));
    BlockState state = lootContext.getParam(LootContextParams.BLOCK_STATE);
    if (state.hasProperty(DoublePlantBlock.HALF)) {
      boolean isUpper = state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER;
      return !lootContext.getLevel().getBlockState(isUpper ? position.below() : position.above()).isAir();
    } else {
      return false;
    }
  }
}
