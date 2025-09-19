package mysticmods.roots.loot.conditions;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.init.ModLoot;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public record WaterloggedBlockCondition() implements LootItemCondition {
  private static final WaterloggedBlockCondition INSTANCE = new WaterloggedBlockCondition();

  public static final MapCodec<WaterloggedBlockCondition> CODEC = MapCodec.unit(INSTANCE);

  @Override
  public LootItemConditionType getType() {
    return ModLoot.WATERLOGGED_BLOCK.get();
  }

  @Override
  public Set<LootContextParam<?>> getReferencedContextParams() {
    return Set.of(LootContextParams.BLOCK_STATE);
  }

  @Override
  public boolean test(LootContext context) {
    BlockState blockstate = context.getParamOrNull(LootContextParams.BLOCK_STATE);
    return blockstate != null && (blockstate.hasProperty(BlockStateProperties.WATERLOGGED) && blockstate.getValue(BlockStateProperties.WATERLOGGED));
  }

  public static LootItemCondition.Builder waterlogged() {
    return () -> INSTANCE;
  }
}
