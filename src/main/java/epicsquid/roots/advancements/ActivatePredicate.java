package epicsquid.roots.advancements;

import com.google.gson.JsonElement;
import epicsquid.mysticallib.advancement.IGenericPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;

import javax.annotation.Nullable;

public class ActivatePredicate implements IGenericPredicate<Void> {
  @Override
  public boolean test(ServerPlayerEntity player, Void condition) {
    return true;
  }

  @Override
  public IGenericPredicate<Void> deserialize(@Nullable JsonElement element) {
    return new ActivatePredicate();
  }
}
