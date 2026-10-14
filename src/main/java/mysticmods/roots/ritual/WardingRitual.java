package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.ritual.IRitualInstance;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

// TODO: More
public class WardingRitual extends Ritual {
  private int potionDuration, potionAmplifier;

  public WardingRitual(Properties properties) {
    super(properties);
  }

  @Override
  public boolean requiresCache(IRitualInstance iRitualInstance) {
    return true;
  }

  @Override
  protected void functionalTick(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    assert pCache != null;

    if (duration % instance.getInterval() == 0) {
      List<LivingEntity> entities = pLevel.getEntitiesOfClass(LivingEntity.class, pCache.getAABB());
      for (LivingEntity entity : entities) {
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, potionDuration, potionAmplifier, false, false), blockEntity.getLastPlayer());
      }
    }
  }

  @Override
  protected void initialize(Holder<Ritual> holder) {
    var properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    assert properties != null;
    this.potionAmplifier = properties.get(ModRituals.WARDING_POTION_AMPLIFIER);
    this.potionDuration = properties.get(ModRituals.WARDING_POTION_DURATION);
  }
}
