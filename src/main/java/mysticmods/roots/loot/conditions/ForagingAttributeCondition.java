package mysticmods.roots.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModAttributes;
import mysticmods.roots.init.ModLoot;
import mysticmods.roots.util.EnchantmentUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public record ForagingAttributeCondition() implements LootItemCondition {
  private static final ForagingAttributeCondition INSTANCE = new ForagingAttributeCondition();
  public static final MapCodec<ForagingAttributeCondition> CODEC = MapCodec.unit(INSTANCE);

  @Override
  public Set<LootContextParam<?>> getReferencedContextParams() {
    return ImmutableSet.of(LootContextParams.TOOL);
  }

  @Override
  public LootItemConditionType getType() {
    return ModLoot.FORAGING_RANDOM_CHANCE.get();
  }

  @Override
  public boolean test(LootContext context) {
    ItemStack itemstack = context.getParamOrNull(LootContextParams.TOOL);
    if (itemstack == null || !itemstack.canPerformAction(RootsAPI.FORAGE)) {
      return false;
    }

    double baseChance;

    if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player pPlayer) {
      baseChance = pPlayer.getAttributeValue(ModAttributes.FORAGING);
    } else {
      baseChance = EnchantmentUtil.getForagingValue(itemstack);
    }

    return context.getRandom().nextFloat() < baseChance;
  }

  public static ForagingAttributeCondition instance() {
    return INSTANCE;
  }
}
