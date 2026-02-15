package mysticmods.roots.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.init.ModLoot;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.Set;

public class FillHorns extends LootItemConditionalFunction {
  public static final MapCodec<FillHorns> CODEC = RecordCodecBuilder.mapCodec(
      p_298087_ -> commonFields(p_298087_)
          .apply(p_298087_, FillHorns::new)
  );

  public FillHorns(List<LootItemCondition> predicates) {
    super(predicates);
  }

  @Override
  public LootItemFunctionType<FillHorns> getType() {
    return ModLoot.FILL_HORNS.get();
  }

  @Override
  public Set<LootContextParam<?>> getReferencedContextParams() {
    return ImmutableSet.of(LootContextParams.THIS_ENTITY);
  }

  /**
   * Called to perform the actual action of this function, after conditions have been checked.
   */
  @Override
  public ItemStack run(ItemStack stack, LootContext context) {
    if (stack.is(Items.GOAT_HORN) && context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Goat goat) {
      ItemStack horns = goat.createHorn();
      stack.set(DataComponents.INSTRUMENT, horns.get(DataComponents.INSTRUMENT));
    }

    return stack;
  }

  public static LootItemConditionalFunction.Builder<?> fillHorns() {
    return simpleBuilder(FillHorns::new);
  }
}
