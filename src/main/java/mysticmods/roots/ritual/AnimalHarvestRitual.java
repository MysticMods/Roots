package mysticmods.roots.ritual;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.mixin.accessor.AccessorMixinLootTable;
import mysticmods.roots.network.client.fx.AnimalHarvestFXPacket;
import mysticmods.roots.util.FakePlayerUtil;
import mysticmods.roots.util.ItemUtil;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.*;

public class AnimalHarvestRitual extends Ritual {
  private int count, glowDuration, lootingValue, itemStackCountLimit, itemStackLimit, maxTries;
  private float lootingChance;

  private final Set<EntityType<?>> emptyLoot = new ObjectOpenHashSet<>();
  private final Set<EntityType<?>> normalLoot = new ObjectOpenHashSet<>();

  private final Map<EntityType<?>, List<LootTable>> additionalLootTables = new HashMap<>();
  // TODO: Handle reload
  private boolean checkedAdditionalLootTables = false;

  @Override
  public void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (pCache == null && requiresCache()) {
      RootsAPI.LOG.error("Ritual {} requires a PositionCache but none was provided. This will cause the ritual to not function correctly.", getOrCreateDescriptionId());
      return;
    }

    // TODO: "First tick"
    FakePlayerUtil.buildItems(pLevel, randomSource);
    if (!checkedAdditionalLootTables) {
      buildAdditionalLootTables(pLevel.getServer().reloadableRegistries());
      checkedAdditionalLootTables = true;
    }

    if (duration % getInterval() == 0) {
      List<LivingEntity> entities = blockEntity.getLevel()
          .getEntitiesOfClass(LivingEntity.class, getAABB().move(blockEntity.getBlockPos()), EntitySelector.NO_SPECTATORS.and(Entity::isAlive)
              .and((o) -> o.getType().is(RootsTags.Entities.ANIMAL_HARVEST) && !o.getType()
                  .is(RootsTags.Entities.ANIMAL_HARVEST_EXCLUDE) && !emptyLoot.contains(o.getType())));
      if (entities.isEmpty()) {
        return;
      }
      int tries = maxTries;
      int i = 0;
      while (i < count) {
        if (tries < 0) {
          break;
        }
        tries--;
        LivingEntity entity = entities.get(blockEntity.getRandom().nextInt(entities.size()));
        List<ItemStack> result = getDrops(entity, randomSource);
        if (!result.isEmpty()) {
          i++;
          for (ItemStack stack : result) {
            ItemUtil.Spawn.spawnItem(blockEntity.getLevel(), entity.blockPosition(), stack);
          }
          if (glowDuration > 0) {
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, glowDuration, 0, false, false), blockEntity.getLastPlayer());
          }
          AnimalHarvestFXPacket packet = new AnimalHarvestFXPacket(entity.getId());
          PacketDistributor.sendToPlayersTrackingEntity(entity, packet);
        }
      }
    }
  }

  public void reset () {
    this.emptyLoot.clear();
    this.normalLoot.clear();
    this.additionalLootTables.clear();
  }

  private boolean checkEntity(LootTable table, List<LootTable> additionalTables, LivingEntity entity) {
    boolean result = checkEntity(table, entity);

    if (additionalTables != null && !additionalTables.isEmpty()) {
      for (LootTable additional : additionalTables) {
        if (checkEntity(additional, entity) && !result) {
          result = true;
        }
      }
    }

    return result;
  }

  private boolean checkEntity(LootTable table, LivingEntity entity) {
    if (normalLoot.contains(entity.getType()) || additionalLootTables.containsKey(entity.getType())) {
      return true;
    } else if (emptyLoot.contains(entity.getType())) {
      return false;
    }

    if (table == LootTable.EMPTY) {
      emptyLoot.add(entity.getType());
      return false;
    }

    List<LootPool> pools = ((AccessorMixinLootTable) table).roots$GetPools();
    if (pools.isEmpty()) {
      emptyLoot.add(entity.getType());
      return false;
    } else {
      normalLoot.add(entity.getType());
      return true;
    }
  }

  protected void buildAdditionalLootTables(ReloadableServerRegistries.Holder registryLookup) {
    for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
      var additional = type.builtInRegistryHolder().getData(DataMaps.ADDITIONAL_ANIMAL_HARVEST_LOOT_TABLES);
      if (additional != null && !additional.isEmpty()) {
        List<LootTable> additionals = new ArrayList<>();
        for (ResourceKey<LootTable> key : additional) {
          var table = registryLookup.getLootTable(key);
          if (table != null && table != LootTable.EMPTY) {
            additionals.add(table);
          }
        }
        if (!additionals.isEmpty()) {
          additionalLootTables.put(type, additionals);
        }
      }
    }
  }

  protected List<ItemStack> getDrops(LivingEntity entity, RandomSource pRandom) {
    ResourceKey<LootTable> resourcelocation = entity.getLootTable();
    LootTable loottable = entity.level().getServer().reloadableRegistries().getLootTable(resourcelocation);

    List<LootTable> additionalTables = additionalLootTables.getOrDefault(entity.getType(), Collections.emptyList());

    if (!checkEntity(loottable, additionalTables, entity)) {
      return Collections.emptyList();
    }
    DamageSources pDamageSources = entity.damageSources();
    FakePlayer fakePlayer = FakePlayerFactory.get((ServerLevel) entity.level(), FakePlayerUtil.ROOTS);
    if (pRandom.nextFloat() < lootingChance) {
      fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, switch (lootingValue) {
        case 2 -> FakePlayerUtil.LOOTING_II_ITEM;
        case 3 -> FakePlayerUtil.LOOTING_III_ITEM;
        default -> FakePlayerUtil.LOOTING_I_ITEM;
      });
    } else {
      fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    }
    DamageSource pDamageSource = pDamageSources.playerAttack(fakePlayer);
    LootParams.Builder lootParamsBuilder = new LootParams.Builder((ServerLevel) entity.level()).withParameter(LootContextParams.ORIGIN, entity.position())
        .withParameter(LootContextParams.THIS_ENTITY, entity)
        .withParameter(LootContextParams.DAMAGE_SOURCE, pDamageSource)
        .withParameter(LootContextParams.ATTACKING_ENTITY, fakePlayer)
        .withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, fakePlayer)
        .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, fakePlayer);

    List<ItemStack> randomItems = loottable.getRandomItems(lootParamsBuilder.create(LootContextParamSets.ENTITY));
    for (LootTable additionalTable : additionalTables) {
      if (additionalTable == LootTable.EMPTY) {
        continue;
      }
      randomItems.addAll(additionalTable.getRandomItems(lootParamsBuilder.create(LootContextParamSets.ENTITY)));
    }
    if (randomItems.isEmpty() && !entity.getType().equals(EntityType.GOAT)) {
      RootsAPI.LOG.error("Generated empty loot for entity {}.", entity);
    }
    if (itemStackCountLimit != -1) {
      for (ItemStack stack : randomItems) {
        if (stack.getCount() > itemStackCountLimit) {
          stack.setCount(itemStackCountLimit);
        }
      }
    }
    randomItems.removeIf(ItemStack::isEmpty);
    // TODO: Should this be < or <=
    if (itemStackLimit == -1 || randomItems.size() <= itemStackLimit) {
      return randomItems;
    }

    List<ItemStack> result = new ArrayList<>();
    for (int i = 0; i < itemStackLimit; i++) {
      if (randomItems.isEmpty()) {
        break;
      }

      result.add(randomItems.remove(pRandom.nextInt(randomItems.size())));
    }

    return result;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModRituals.ANIMAL_HARVEST_COUNT);
    properties.add(ModRituals.ANIMAL_HARVEST_GLOW_DURATION);
    properties.add(ModRituals.ANIMAL_HARVEST_LOOTING_VALUE);
    properties.add(ModRituals.ANIMAL_HARVEST_LOOTING_CHANCE);
    properties.add(ModRituals.ANIMAL_HARVEST_STACK_COUNT_LIMIT);
    properties.add(ModRituals.ANIMAL_HARVEST_STACK_LIMIT);
    properties.add(ModRituals.ANIMAL_HARVEST_MAX_TRIES);
  }

  @Override
  public void initialize(Holder<Ritual> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    count = properties.get(ModRituals.ANIMAL_HARVEST_COUNT);
    maxTries = properties.get(ModRituals.ANIMAL_HARVEST_MAX_TRIES);
    glowDuration = properties.get(ModRituals.ANIMAL_HARVEST_GLOW_DURATION);
    lootingValue = properties.get(ModRituals.ANIMAL_HARVEST_LOOTING_VALUE);
    lootingChance = properties.get(ModRituals.ANIMAL_HARVEST_LOOTING_CHANCE);
    itemStackCountLimit = properties.get(ModRituals.ANIMAL_HARVEST_STACK_COUNT_LIMIT);
    itemStackLimit = properties.get(ModRituals.ANIMAL_HARVEST_STACK_LIMIT);
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.ANIMAL_HARVEST_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.ANIMAL_HARVEST_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.ANIMAL_HARVEST_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.ANIMAL_HARVEST_INTERVAL;
  }
}
