package mysticmods.roots.ritual;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.FakePlayerUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.util.FakePlayerFactory;
import noobanidus.libs.noobutil.util.ItemUtil;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AnimalHarvestRitual extends Ritual {
  private int count, glowDuration, lootingValue;
  private float lootingChance;

  private final Set<EntityType<?>> emptyLoot = new ObjectLinkedOpenHashSet<>();
  private final Set<EntityType<?>> normalLoot = new ObjectLinkedOpenHashSet<>();

  @Override
  public void functionalTick(PyreBlockEntity blockEntity, int duration) {
    if (duration % getInterval() == 0) {
      List<LivingEntity> entities = blockEntity.getLevel().getEntitiesOfClass(LivingEntity.class, getAABB().move(blockEntity.getBlockPos()), EntitySelector.NO_SPECTATORS.and((o) -> o.getType().is(RootsTags.Entities.ANIMAL_HARVEST) && !emptyLoot.contains(o.getType())));
      if (entities.isEmpty()) {
        return;
      }
      for (int i = 0; i < count; i++) {
        LivingEntity entity = entities.get(blockEntity.getRandom().nextInt(entities.size()));
        for (ItemStack stack : getDrops(entity)) {
          ItemUtil.Spawn.spawnItem(blockEntity.getLevel(), entity.blockPosition(), stack);
        }
        if (glowDuration > 0) {
          entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, glowDuration, 0, false, false));
        }
      }
    }
  }

  private boolean checkEntity (LootTable table, LivingEntity entity) {
    if (normalLoot.contains(entity.getType())) {
      return true;
    } else if (emptyLoot.contains(entity.getType())) {
      return false;
    }

    if (table == LootTable.EMPTY) {
      emptyLoot.add(entity.getType());
      return false;
    }

    JsonObject pool = LootTables.serialize(table).getAsJsonObject();
    if (pool.get("pools") == null) {
      emptyLoot.add(entity.getType());
      return false;
    } else {
      normalLoot.add(entity.getType());
      return true;
    }
  }

  protected List<ItemStack> getDrops(LivingEntity entity) {
    ResourceLocation resourcelocation = entity.getLootTable();
    LootTable loottable = entity.getLevel().getServer().getLootTables().get(resourcelocation);
    if (!checkEntity(loottable, entity)) {
      return Collections.emptyList();
    }
    DamageSource pDamageSource = DamageSource.GENERIC;
    LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) entity.level)).withRandom(entity.getRandom()).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.DAMAGE_SOURCE, pDamageSource).withOptionalParameter(LootContextParams.KILLER_ENTITY, pDamageSource.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, pDamageSource.getDirectEntity());
    lootcontext$builder = lootcontext$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, FakePlayerFactory.get((ServerLevel) entity.level, FakePlayerUtil.ROOTS));
    if (entity.getRandom().nextFloat() < lootingChance) {
      lootcontext$builder = lootcontext$builder.withLuck(lootingValue);
    }
    LootContext ctx = lootcontext$builder.create(LootContextParamSets.ENTITY);
    return loottable.getRandomItems(ctx);
  }

  @Override
  public void animationTick(PyreBlockEntity blockEntity, int duration) {

  }

  @Override
  public void initialize() {
    count = ModRituals.ANIMAL_HARVEST_COUNT.get().getValue();
    glowDuration = ModRituals.ANIMAL_HARVEST_GLOW_DURATION.get().getValue();
    lootingValue = ModRituals.ANIMAL_HARVEST_LOOTING_VALUE.get().getValue();
    lootingChance = ModRituals.ANIMAL_HARVEST_LOOTING_CHANCE.get().getValue();
  }

  @Override
  protected RitualProperty<Integer> getDurationProperty() {
    return ModRituals.ANIMAL_HARVEST_DURATION.get();
  }

  @Override
  protected RitualProperty<Integer> getRadiusXZProperty() {
    return ModRituals.ANIMAL_HARVEST_RADIUS_XZ.get();
  }

  @Override
  protected RitualProperty<Integer> getRadiusYProperty() {
    return ModRituals.ANIMAL_HARVEST_RADIUS_Y.get();
  }

  @Override
  protected RitualProperty<Integer> getIntervalProperty() {
    return ModRituals.ANIMAL_HARVEST_INTERVAL.get();
  }
}
