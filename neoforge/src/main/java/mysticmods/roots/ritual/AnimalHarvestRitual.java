package mysticmods.roots.ritual;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AnimalHarvestRitual extends Ritual {
  private int count, glowDuration, lootingValue;
  private float lootingChance;

  private final Set<EntityType<?>> emptyLoot = new ObjectLinkedOpenHashSet<>();
  private final Set<EntityType<?>> normalLoot = new ObjectLinkedOpenHashSet<>();

  @Override
  public void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration) {
    if (duration % getInterval() == 0) {
      List<LivingEntity> entities = blockEntity.getLevel().getEntitiesOfClass(LivingEntity.class, getAABB().move(blockEntity.getBlockPos()), EntitySelector.NO_SPECTATORS.and((o) -> o.getType().is(RootsTags.Entities.ANIMAL_HARVEST) && !emptyLoot.contains(o.getType())));
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

    // TODO:
/*    JsonObject pool = LootTables.serialize(table).getAsJsonObject();
    if (pool.get("pools") == null) {
      emptyLoot.add(entity.getType());
      return false;
    } else {
      normalLoot.add(entity.getType());
      return true;
    }*/
    return false;
  }

  protected List<ItemStack> getDrops(LivingEntity entity) {
/*    ResourceKey<LootTable> resourcelocation = entity.getLootTable();
    LootTable loottable = entity.level().getServer().reloadableRegistries().getLootTable(resourcelocation);
    if (!checkEntity(loottable, entity)) {
      return Collections.emptyList();
    }
    DamageSource pDamageSource = DamageSource;
    LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) entity.level())).withRandom(entity.getRandom()).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.DAMAGE_SOURCE, pDamageSource).withOptionalParameter(LootContextParams.KILLER_ENTITY, pDamageSource.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, pDamageSource.getDirectEntity());
    lootcontext$builder = lootcontext$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, FakePlayerFactory.get((ServerLevel) entity.level, FakePlayerUtil.ROOTS));
    if (entity.getRandom().nextFloat() < lootingChance) {
      lootcontext$builder = lootcontext$builder.withLuck(lootingValue);
    }
    LootContext ctx = lootcontext$builder.create(LootContextParamSets.ENTITY);
    return loottable.getRandomItems(ctx);*/
    return Collections.emptyList();
  }

  @Override
  public void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration) {

  }

  @Override
  public void initialize() {
/*    count = ModRituals.ANIMAL_HARVEST_COUNT.getValue();
    glowDuration = ModRituals.ANIMAL_HARVEST_GLOW_DURATION.getValue();
    lootingValue = ModRituals.ANIMAL_HARVEST_LOOTING_VALUE.getValue();
    lootingChance = ModRituals.ANIMAL_HARVEST_LOOTING_CHANCE.getValue();*/
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
