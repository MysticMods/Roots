package mysticmods.roots.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.init.ModLoot;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public class LootItemBlockTagCondition implements LootItemCondition {
  public static final MapCodec<LootItemBlockTagCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(Registries.BLOCK)
      .fieldOf("tag").forGetter(LootItemBlockTagCondition::getTag)).apply(instance, LootItemBlockTagCondition::new));
  private final TagKey<Block> tag;

  protected LootItemBlockTagCondition(TagKey<Block> tag) {
    this.tag = tag;
  }

  public static LootItemBlockTagCondition tag(TagKey<Block> tag) {
    return new LootItemBlockTagCondition(tag);
  }

  @Override
  public Set<LootContextParam<?>> getReferencedContextParams() {
    return ImmutableSet.of(LootContextParams.BLOCK_STATE);
  }

  public TagKey<Block> getTag() {
    return tag;
  }

  @Override
  public LootItemConditionType getType() {
    return ModLoot.LOOT_ITEM_BLOCK_TAG_CONDITION_TYPE.get();
  }

  @Override
  public boolean test(LootContext lootContext) {
    BlockState blockstate = lootContext.getParamOrNull(LootContextParams.BLOCK_STATE);
    return blockstate != null && blockstate.is(this.tag);
  }

  public static class Builder implements LootItemCondition.Builder {
    private final TagKey<Block> block;

    public Builder(TagKey<Block> pBlock) {
      this.block = pBlock;
    }

    @Override
    public LootItemCondition build() {
      return new LootItemBlockTagCondition(this.block);
    }
  }
}
