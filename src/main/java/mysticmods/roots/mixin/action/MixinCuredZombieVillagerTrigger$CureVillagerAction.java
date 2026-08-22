package mysticmods.roots.mixin.action;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mysticmods.roots.action.CureVillagerAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CuredZombieVillagerTrigger.class)
public class MixinCuredZombieVillagerTrigger$CureVillagerAction {
  @WrapMethod(method = "trigger")
  private void roots$TriggerCureZombieVillager(ServerPlayer player, Zombie zombie, Villager villager, Operation<Void> original) {
    original.call(player, zombie, villager);
    if (ModActions.CURE_VILLAGER.get().shouldTest()) {
      CureVillagerAction.Context context = new CureVillagerAction.Context(player.serverLevel(), player, villager, zombie);
      ModActions.CURE_VILLAGER.get().accept(context);
    }
  }
}
