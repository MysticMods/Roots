package epicsquid.roots.advancements;

import com.google.gson.JsonElement;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import noobanidus.libs.noobutil.advancement.IGenericPredicate;

import javax.annotation.Nullable;

public class KillPredicate implements IGenericPredicate<LivingDeathEvent> {
  @Override
  public boolean test(ServerPlayerEntity player, LivingDeathEvent event) {
    return true; // Actual logic for this handled in DeathEventHandler
  }

  @Override
  public KillPredicate deserialize(@Nullable JsonElement element) {
    return new KillPredicate();
  }
}