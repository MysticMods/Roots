package mysticmods.roots.loot.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.entity.DeerEntity;
import mysticmods.roots.init.ModLoot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class HasHornsCondition implements LootItemCondition {
  public static final MapCodec<HasHornsCondition> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(Codec.BOOL.fieldOf("inverse").forGetter(HasHornsCondition::isInverse)).apply(instance, HasHornsCondition::new));

  private final boolean inverse;

  public HasHornsCondition(boolean inverseIn) {
    this.inverse = inverseIn;
  }

  public boolean isInverse() {
    return inverse;
  }

  @Override
  public boolean test(LootContext lootContext) {
    boolean flag;
    Entity looted = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
    if (looted instanceof DeerEntity deer) {
      flag = deer.getEntityData().get(DeerEntity.hasHorns);
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

