package mysticmods.roots.effect;

import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

// Does nothing for non-player mobs
public class SenseDangerEffect extends MobEffect {
  public SenseDangerEffect() {
    super(MobEffectCategory.BENEFICIAL, 0xc29155);
  }

  @Override
  public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
    super.applyEffectTick(pLivingEntity, pAmplifier);
    final Level pLevel = pLivingEntity.level();
    SnapshotHelper.applyLiving(pLivingEntity, ModSerializers.EXTENSION.get(), (player, extension) -> {
      pLevel.getEntities(player, extension.getAABB().move(player.position())).forEach(entity -> {
        // TODO: Better utility for detecting hostiles, confer pacifist checks
        // TODO: Tags for auto-exclusion, auto-inclusion
        if (entity instanceof LivingEntity mob && (entity instanceof Enemy enemy || entity instanceof NeutralMob neutral && player instanceof LivingEntity living && neutral.isAngryAt(living))) {
          mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40, 0, false, false), player);
        }
      });
    });
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
    return true;
  }
}
