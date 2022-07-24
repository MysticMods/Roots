package mysticmods.roots.ritual;

import mysticmods.roots.RootsTags;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.Rituals;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.FakePlayerUtil;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import noobanidus.libs.noobutil.util.ItemUtil;

import java.util.List;

public class AnimalHarvestRitual extends Ritual {
  private int count, glowDuration, lootingValue;
  private float lootingChance;

  @Override
  public void functionalTick(PyreBlockEntity blockEntity, int duration) {
    if (duration % getInterval() == 0) {
      ITag<EntityType<?>> harvestTag = ForgeRegistries.ENTITIES.tags().getTag(RootsTags.Entities.ANIMAL_HARVEST);
      List<LivingEntity> entities = blockEntity.getLevel().getEntitiesOfClass(LivingEntity.class, getAABB().move(blockEntity.getBlockPos()), EntitySelector.NO_SPECTATORS.and((o) -> harvestTag.contains(o.getType())));
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

  protected List<ItemStack> getDrops(LivingEntity entity) {
    ResourceLocation resourcelocation = entity.getLootTable();
    LootTable loottable = entity.getLevel().getServer().getLootTables().get(resourcelocation);
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
    duration = ModRituals.ANIMAL_HARVEST_DURATION.get().getValue();
    interval = ModRituals.ANIMAL_HARVEST_INTERVAL.get().getValue();
    count = ModRituals.ANIMAL_HARVEST_COUNT.get().getValue();
    radiusXZ = ModRituals.ANIMAL_HARVEST_RADIUS_XZ.get().getValue();
    radiusY = ModRituals.ANIMAL_HARVEST_RADIUS_Y.get().getValue();
    glowDuration = ModRituals.ANIMAL_HARVEST_GLOW_DURATION.get().getValue();
    lootingValue = ModRituals.ANIMAL_HARVEST_LOOTING_VALUE.get().getValue();
    lootingChance = ModRituals.ANIMAL_HARVEST_LOOTING_CHANCE.get().getValue();
  }
}
