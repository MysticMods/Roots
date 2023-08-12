package mysticmods.roots.effect;

import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
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
        pLevel.getEntities(player, extension.getAABB().move(player.position())).forEach(entity -> {
          // TODO: Better utility for detecting hostiles, confer pacifist checks
          // TODO: Tags for auto-exclusion, auto-inclusion
          if (entity instanceof LivingEntity mob && (entity instanceof Enemy enemy || entity instanceof NeutralMob neutral && neutral.isAngryAt(player))) {
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
