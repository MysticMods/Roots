package mysticmods.roots.advancements;

import com.google.gson.JsonElement;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import noobanidus.libs.noobutil.advancement.IGenericPredicate;

import javax.annotation.Nullable;

// TODO: Actually implement this and test that it works
public class KillPredicate implements IGenericPredicate<LivingDeathEvent> {
  @Override
  public boolean test(ServerPlayer player, LivingDeathEvent event) {
    return Registries.ENTITY_REGISTRY.get().tags().getTag(RootsTags.Entities.PACIFIST).contains(event.getEntity().getType());
  }

  @Override
  public KillPredicate deserialize(@Nullable JsonElement element) {
    return new KillPredicate();
  }
}