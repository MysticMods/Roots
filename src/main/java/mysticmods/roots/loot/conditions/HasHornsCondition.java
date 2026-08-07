package mysticmods.roots.loot.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.entity.DeerEntity;
import mysticmods.roots.init.ModLoot;
import mysticmods.roots.mixin.accessor.AccessorMixinGoat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record HasHornsCondition(boolean inverse) implements LootItemCondition {
  public static final MapCodec<HasHornsCondition> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(Codec.BOOL.fieldOf("inverse").forGetter(HasHornsCondition::inverse))
          .apply(instance, HasHornsCondition::new));

  @Override
  public boolean test(LootContext lootContext) {
    boolean flag;
    Entity looted = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
    if (looted instanceof DeerEntity deer) {
      flag = deer.getEntityData().get(DeerEntity.hasHorns);
    } else if (looted instanceof Goat goat) {
      flag = goat.getEntityData().get(AccessorMixinGoat.roots$getDataHasLeftHorn()) || goat.getEntityData()
          .get(AccessorMixinGoat.roots$getDataHasRightHorn());
    } else {
      flag = false;
    }
    return flag == !this.inverse;
  }

  @Override
  public LootItemConditionType getType() {
    return ModLoot.HAS_HORNS.get();
  }

  private static final HasHornsCondition INSTANCE = new HasHornsCondition(false);

  public static Builder builder() {
    return () -> INSTANCE;
  }
}

