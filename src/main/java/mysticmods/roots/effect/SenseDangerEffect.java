package mysticmods.roots.effect;

import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

// Does nothing for non-player mobs
public class SenseDangerEffect extends MobEffect {
  public SenseDangerEffect() {
    super(MobEffectCategory.BENEFICIAL, 0xc29155);
  }

  @Override
  public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
    super.applyEffectTick(pLivingEntity, pAmplifier);
    final Level pLevel = pLivingEntity.getLevel();
    SnapshotHelper.applyPlayer(pLivingEntity, ModSerializers.EXTENSION.get(), (player, extension) -> {
        pLevel.getEntities(player, extension.getAABB()).forEach(entity -> {
          if (entity instanceof Mob mob) {
              mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40, 0, false, false));
          }
        });
    });
  }

  @Override
  public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
    return true;
  }
}
