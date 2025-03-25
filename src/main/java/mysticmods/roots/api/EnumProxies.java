package mysticmods.roots.api;

import com.google.common.base.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageEffects;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class EnumProxies {
  public static final EnumProxy<DamageEffects> ROOTS_DRAINING = new EnumProxy<>(DamageEffects.class, "roots:draining", (Supplier<SoundEvent>) () -> SoundEvents.PLAYER_HURT);
}
