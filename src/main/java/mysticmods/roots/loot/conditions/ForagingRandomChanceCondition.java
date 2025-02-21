package mysticmods.roots.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModConditions;
import mysticmods.roots.init.ModLoot;
import mysticmods.roots.util.ForagingUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.Set;

public record ForagingRandomChanceCondition(NumberProvider chance) implements LootItemCondition {
  public static final MapCodec<ForagingRandomChanceCondition> CODEC = RecordCodecBuilder.mapCodec(
      p_344719_ -> p_344719_.group(NumberProviders.CODEC.fieldOf("chance").forGetter(ForagingRandomChanceCondition::chance))
          .apply(p_344719_, ForagingRandomChanceCondition::new)
  );

  @Override
  public Set<LootContextParam<?>> getReferencedContextParams() {
    return ImmutableSet.of(LootContextParams.TOOL);
  }

  @Override
  public LootItemConditionType getType() {
    return ModLoot.FORAGING_RANDOM_CHANCE.get();
  }

  public boolean test(LootContext context) {
    ItemStack itemstack = context.getParamOrNull(LootContextParams.TOOL);
    if (itemstack == null || !itemstack.canPerformAction(RootsAPI.FORAGE)) {
      return false;
    }
    float baseChance = this.chance.getFloat(context);
    int foraging = ForagingUtil.getForagingValue(itemstack);

    float maxMultiplier = 3.0f;

    float modifiedChance = Math.min(baseChance * (1 + (foraging / 6.0f) * (maxMultiplier - 1)), 0.9f);

    return context.getRandom().nextFloat() < modifiedChance;
  }

  public static LootItemCondition.Builder randomChance(float chance) {
    return () -> new ForagingRandomChanceCondition(ConstantValue.exactly(chance));
  }

  public static LootItemCondition.Builder randomChance(NumberProvider chance) {
    return () -> new ForagingRandomChanceCondition(chance);
  }
}
