package mysticmods.roots.effect;

import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

// Does nothing for non-player mobs
public class SkySoarerEffect extends MobEffect {
    public SkySoarerEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x03f0fc);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
        SnapshotHelper.applyPlayer(pLivingEntity, ModSerializers.SKY_SOARER.get(), (player, sky) -> {
            player.hasImpulse = true;
            player.hurtMarked = true;
            player.fallDistance = 0f;
            player.setDeltaMovement(player.getLookAngle().multiply(sky.getAmplifier(), sky.getAmplifier(), sky.getAmplifier()));
            // TODO: Handle while riding entities
        });
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        SnapshotHelper.applyPlayer(pLivingEntity, ModSerializers.SKY_SOARER.get(), (player, sky) -> {
            player.hasImpulse = true;
            player.hurtMarked = true;
            player.setDeltaMovement(sky.getOriginalMovement());
        });
    }
}
