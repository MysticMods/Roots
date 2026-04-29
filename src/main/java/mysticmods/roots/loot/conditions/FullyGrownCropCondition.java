package mysticmods.roots.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.growth.HarvestRecord;
import mysticmods.roots.init.ModLoot;
import mysticmods.roots.util.HarvestUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public record FullyGrownCropCondition() implements LootItemCondition {
  public static final FullyGrownCropCondition INSTANCE = new FullyGrownCropCondition();
  public static final MapCodec<FullyGrownCropCondition> CODEC = MapCodec.unit(INSTANCE);

  @Override
  public Set<LootContextParam<?>> getReferencedContextParams() {
    return ImmutableSet.of(LootContextParams.BLOCK_STATE, LootContextParams.ORIGIN);
  }

  @Override
  public LootItemConditionType getType() {
    return ModLoot.LOOT_ITEM_FULLY_GROWN_CROP_CONDITION_TYPE.get();
  }

  @Override
  public boolean test(LootContext lootContext) {
    BlockState state = lootContext.getParamOrNull(LootContextParams.BLOCK_STATE);
    if (state == null) {
      return false;
    }

    Vec3 origin = lootContext.getParamOrNull(LootContextParams.ORIGIN);
    if (origin == null) {
      return false;
    }
    BlockPos pos = BlockPos.containing(origin);

    Entity entity = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
    Player player;
    if (entity instanceof Player player2) {
      player = player2;
    } else {
      player = null;
    }

    HarvestRecord record = HarvestUtil.getRecord(lootContext.getLevel(), pos, state, player);
    if (record == null) {
      return false;
    }

    return record.canHarvest(lootContext.getLevel(), pos, state, player);
  }

  public static FullyGrownCropCondition fullyGrownCrop() {
    return INSTANCE;
  }
}
