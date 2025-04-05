package mysticmods.roots.init;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.loot.conditions.ForagingRandomChanceCondition;
import mysticmods.roots.loot.conditions.HasHornsCondition;
import mysticmods.roots.loot.conditions.LootItemBlockTagCondition;
import mysticmods.roots.loot.conditions.WaterloggedBlockCondition;
import mysticmods.roots.loot.modifiers.AddGrassDropsModifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;


public class ModLoot {
  private static final DeferredRegister<LootItemConditionType> LOOT_ITEM_CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, RootsAPI.MODID);
  public static final DeferredHolder<LootItemConditionType, LootItemConditionType> LOOT_ITEM_BLOCK_TAG_CONDITION_TYPE = LOOT_ITEM_CONDITIONS.register("block_tag", () -> new LootItemConditionType(LootItemBlockTagCondition.CODEC));
  public static final DeferredHolder<LootItemConditionType, LootItemConditionType> HAS_HORNS = LOOT_ITEM_CONDITIONS.register("has_horns", () -> new LootItemConditionType(HasHornsCondition.CODEC));
  public static final DeferredHolder<LootItemConditionType, LootItemConditionType> FORAGING_RANDOM_CHANCE = LOOT_ITEM_CONDITIONS.register("foraging_random_chance", () -> new LootItemConditionType(ForagingRandomChanceCondition.CODEC));
  public static final DeferredHolder<LootItemConditionType, LootItemConditionType> WATERLOGGED_BLOCK = LOOT_ITEM_CONDITIONS.register("waterlogged_block", () -> new LootItemConditionType(WaterloggedBlockCondition.CODEC));
  private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RootsAPI.MODID);
  public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AddGrassDropsModifier>> ADD_GRASS_DROPS_MODIFIER = GLOBAL_LOOT_MODIFIERS.register("add_grass_drops", () -> AddGrassDropsModifier.CODEC);

  public static void register(IEventBus bus) {
    LOOT_ITEM_CONDITIONS.register(bus);
    GLOBAL_LOOT_MODIFIERS.register(bus);
  }
}
