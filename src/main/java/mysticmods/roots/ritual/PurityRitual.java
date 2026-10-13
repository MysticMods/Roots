package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.ritual.IRitualInstance;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.mixin.accessor.AccessorMixinZombieVillager;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PurityRitual extends Ritual {
  private boolean convertZombies;
  private int conversionAddition, potionCount;

  public PurityRitual(Properties properties) {
    super(properties);
  }

  @Override
  public boolean requiresCache(IRitualInstance iRitualInstance) {
    return true;
  }

  @Override
  protected void functionalTick(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (pCache == null && requiresCache(instance)) {
      RootsAPI.LOG.error("Ritual {} requires a PositionCache but none was provided. This will cause the ritual to not function correctly.", getOrCreateDescriptionId());
      return;
    }

    List<LivingEntity> entities = pLevel.getEntitiesOfClass(LivingEntity.class, pCache.getAABB());
    List<MobEffectInstance> toRemove = new ArrayList<>();
    for (LivingEntity entity : entities) {
      if (entity.getType().is(RootsTags.Entities.ZOMBIE_VILLAGERS) && !(entity.getType()
          .is(RootsTags.Entities.ZOMBIE_VILLAGERS_EXCLUDE)) && entity instanceof ZombieVillager zombie) {
        if (zombie.isConverting()) {
          entity.extinguishFire();
          if (convertZombies) {
            int conversionTime = ((AccessorMixinZombieVillager) zombie).roots$GetVillagerConversionTime();
            ((AccessorMixinZombieVillager) zombie).roots$SetVillagerConversionTime(conversionTime - conversionAddition);
          }
        }
      } else {
        if (duration % instance.getInterval() == 0) {
          for (MobEffectInstance effect : entity.getActiveEffects()) {
            if (effect.isInfiniteDuration()) {
              continue;
            }

            if (effect.getEffect().is(RootsTags.MobEffects.PURITY_FORCE_EXCLUDE)) {
              continue;
            }

            if (effect.getEffect().is(RootsTags.MobEffects.PURITY_FORCE_INCLUDE) || !effect.getEffect().value()
                .isBeneficial()) {
              toRemove.add(effect);
            }
          }
          if (!toRemove.isEmpty()) {
            for (int i = 0; i < potionCount; i++) {
              if (toRemove.isEmpty()) {
                break;
              }
              Holder<MobEffect> effectToRemove = toRemove.get(randomSource.nextInt(toRemove.size())).getEffect();
              entity.removeEffect(effectToRemove);
            }
          }
          toRemove.clear();
        }
      }
    }
  }

  @Override
  protected void initialize(Holder<Ritual> holder) {
    var properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    this.conversionAddition = properties.get(ModRituals.PURITY_CONVERSION_ADDITION);
    this.potionCount = properties.get(ModRituals.PURITY_POTION_COUNT);
    this.convertZombies = properties.get(ModRituals.PURITY_CONVERT_ZOMBIES);
  }
}
