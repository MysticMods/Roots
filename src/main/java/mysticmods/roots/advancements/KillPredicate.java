package mysticmods.roots.advancements;

import com.google.gson.JsonElement;
import mysticmods.roots.api.RootsTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import noobanidus.libs.noobutil.advancement.IGenericPredicate;

import javax.annotation.Nullable;

// TODO: Actually implement this and test that it works
public class KillPredicate implements IGenericPredicate<LivingDeathEvent> {
  @Override
  public boolean test(ServerPlayer player, LivingDeathEvent event) {
    LivingEntity entity = event.getEntity();
    if (entity.getType().is(RootsTags.Entities.PACIFIST)) {
      if (entity instanceof TamableAnimal tamable) {
        if (tamable.isTame() && (tamable.getOwner() == null || !tamable.getOwner().equals(player))) {
          return false;
        }
      }
      if (entity instanceof NeutralMob neutral) {
        if (neutral.isAngryAt(player) && !neutral.isAngryAtAllPlayers(player.getLevel())) {
          return false;
        }
      }
      if (entity instanceof Chicken potentialJockey) {
        if (potentialJockey.isChickenJockey()) {
          return false;
        }
        for (Entity ridingEntity : potentialJockey.getPassengers()) {
          if (ridingEntity instanceof Enemy) {
            return false;
          }
        }
      }
      if (entity instanceof Rabbit potentialKiller) {
        if (potentialKiller.getRabbitType() == 99) {
          return false;
        }
      }
      return true;
    }

    return false;
  }

  @Override
  public KillPredicate deserialize(@Nullable JsonElement element) {
    return new KillPredicate();
  }
}