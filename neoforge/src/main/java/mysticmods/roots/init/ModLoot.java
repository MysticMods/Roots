package mysticmods.roots.init;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.loot.modifiers.AddGrassDropsModifier;
import mysticmods.roots.loot.predicates.HasHornsCondition;
import mysticmods.roots.loot.predicates.LootItemBlockTagCondition;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLoot {
  private static final DeferredRegister<LootItemConditionType> LOOT_ITEM_CONDITIONS = DeferredRegister.create(Registry.LOOT_ITEM_REGISTRY, RootsAPI.MODID);

  private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RootsAPI.MODID);

  public static final RegistryObject<LootItemConditionType> LOOT_ITEM_BLOCK_TAG_CONDITION_TYPE = LOOT_ITEM_CONDITIONS.register("block_tag", () -> new LootItemConditionType(new LootItemBlockTagCondition.Serializer()));

  public static final RegistryObject<LootItemConditionType> HAS_HORNS = LOOT_ITEM_CONDITIONS.register("has_horns", () -> new LootItemConditionType(new HasHornsCondition.HornSerializer()));

  public static final RegistryObject<Codec<AddGrassDropsModifier>> ADD_GRASS_DROPS_MODIFIER = GLOBAL_LOOT_MODIFIERS.register("add_grass_drops", AddGrassDropsModifier.CODEC);

  public static void register(IEventBus bus) {
    LOOT_ITEM_CONDITIONS.register(bus);
    GLOBAL_LOOT_MODIFIERS.register(bus);
  }
}
