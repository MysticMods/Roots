package epicsquid.roots.advancements;

import com.google.gson.JsonElement;
import epicsquid.mysticallib.advancement.IGenericPredicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import javax.annotation.Nullable;

public class KillPredicate implements IGenericPredicate<LivingDeathEvent> {
  @Override
  public boolean test(EntityPlayerMP player, LivingDeathEvent event) {
    return true; // Actual logic for this handled in DeathEventHandler
  }

  @Override
  public KillPredicate deserialize(@Nullable JsonElement element) {
    return new KillPredicate();
  }
}