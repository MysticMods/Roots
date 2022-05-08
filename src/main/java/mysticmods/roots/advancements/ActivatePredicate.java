package mysticmods.roots.advancements;

import com.google.gson.JsonElement;
import net.minecraft.server.level.ServerPlayer;
import noobanidus.libs.noobutil.advancement.IGenericPredicate;

import javax.annotation.Nullable;

public class ActivatePredicate implements IGenericPredicate<Void> {
  @Override
  public boolean test(ServerPlayer player, Void condition) {
    return true;
  }

  @Override
  public IGenericPredicate<Void> deserialize(@Nullable JsonElement element) {
    return new ActivatePredicate();
  }
}
