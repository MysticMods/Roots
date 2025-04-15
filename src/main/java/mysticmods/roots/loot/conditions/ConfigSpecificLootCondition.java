package mysticmods.roots.loot.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModLoot;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record ConfigSpecificLootCondition(String name) implements LootItemCondition {
  public static final MapCodec<ConfigSpecificLootCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("name")
      .forGetter(ConfigSpecificLootCondition::name)).apply(instance, ConfigSpecificLootCondition::new));

  @Override
  public LootItemConditionType getType() {
    return ModLoot.LOOT_ITEM_CONFIG_SPECIFIC_CONDITION_TYPE.get();
  }

  @Override
  public boolean test(LootContext lootContext) {
    return switch (this.name) {
      case "aubergine" -> ConfigManager.DROP_AUBERGINE_SEEDS.getAsBoolean();
      case "wildroot" -> ConfigManager.DROP_WILDROOT.getAsBoolean();
      case "grove_spores" -> ConfigManager.DROP_GROVE_SPORES.getAsBoolean();
      default -> throw new IllegalStateException("Invalid value: " + this.name);
    };
  }
}
