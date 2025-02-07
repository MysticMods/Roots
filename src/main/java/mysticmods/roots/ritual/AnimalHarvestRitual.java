package mysticmods.roots.ritual;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.mixin.AccessorMixinLootTable;
import mysticmods.roots.util.FakePlayerUtil;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AnimalHarvestRitual extends Ritual {
  private int count, glowDuration, lootingValue;
  private float lootingChance;

  private final Set<EntityType<?>> emptyLoot = new ObjectLinkedOpenHashSet<>();
  private final Set<EntityType<?>> normalLoot = new ObjectLinkedOpenHashSet<>();

  @Override
  public void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (duration % getInterval() == 0) {
      List<LivingEntity> entities = blockEntity.getLevel()
          .getEntitiesOfClass(LivingEntity.class, getAABB().move(blockEntity.getBlockPos()), EntitySelector.NO_SPECTATORS.and(Entity::isAlive)
              .and((o) -> o.getType().is(RootsTags.Entities.ANIMAL_HARVEST) && !emptyLoot.contains(o.getType())));
      if (entities.isEmpty()) {
        return;
      }
      for (int i = 0; i < count; i++) {
        if (entities.isEmpty()) {
          break;
        }
        LivingEntity entity = entities.remove(blockEntity.getRandom().nextInt(entities.size()));
        for (ItemStack stack : getDrops(entity)) {
          ItemUtil.Spawn.spawnItem(blockEntity.getLevel(), entity.blockPosition(), stack);
        }
        if (glowDuration > 0) {
          entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, glowDuration, 0, false, false));
        }
      }
    }
  }

  private boolean checkEntity(LootTable table, LivingEntity entity) {
    if (normalLoot.contains(entity.getType())) {
      return true;
    } else if (emptyLoot.contains(entity.getType())) {
      return false;
    }

    if (table == LootTable.EMPTY) {
      emptyLoot.add(entity.getType());
      return false;
    }

    List<LootPool> pools = ((AccessorMixinLootTable) table).getPools();
    if (pools.isEmpty()) {
      emptyLoot.add(entity.getType());
      return false;
    } else {
      normalLoot.add(entity.getType());
      return true;
    }
  }

  protected List<ItemStack> getDrops(LivingEntity entity) {
    ResourceKey<LootTable> resourcelocation = entity.getLootTable();
    LootTable loottable = entity.level().getServer().reloadableRegistries().getLootTable(resourcelocation);
    if (!checkEntity(loottable, entity)) {
      return Collections.emptyList();
    }
    DamageSources pDamageSources = entity.damageSources();
    FakePlayer fakePlayer = FakePlayerFactory.get((ServerLevel) entity.level(), FakePlayerUtil.ROOTS);
    DamageSource pDamageSource = pDamageSources.playerAttack(fakePlayer);
    LootParams.Builder lootParamsBuilder = new LootParams.Builder((ServerLevel) entity.level()).withParameter(LootContextParams.ORIGIN, entity.position())
        .withParameter(LootContextParams.THIS_ENTITY, entity)
        .withParameter(LootContextParams.DAMAGE_SOURCE, pDamageSource)
        .withParameter(LootContextParams.ATTACKING_ENTITY, fakePlayer)
        .withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, fakePlayer)
        .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, fakePlayer);


    if (entity.getRandom().nextFloat() < lootingChance) {
      lootParamsBuilder.withLuck(lootingValue);
    }

    return loottable.getRandomItems(lootParamsBuilder.create(LootContextParamSets.ENTITY));
  }

  @Override
  public void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModRituals.ANIMAL_HARVEST_COUNT);
    properties.add(ModRituals.ANIMAL_HARVEST_GLOW_DURATION);
    properties.add(ModRituals.ANIMAL_HARVEST_LOOTING_VALUE);
    properties.add(ModRituals.ANIMAL_HARVEST_LOOTING_CHANCE);
  }

  @Override
  public void initialize(Holder<Ritual> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    count = properties.get(ModRituals.ANIMAL_HARVEST_COUNT);
    glowDuration = properties.get(ModRituals.ANIMAL_HARVEST_GLOW_DURATION);
    lootingValue = properties.get(ModRituals.ANIMAL_HARVEST_LOOTING_VALUE);
    lootingChance = properties.get(ModRituals.ANIMAL_HARVEST_LOOTING_CHANCE);
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
