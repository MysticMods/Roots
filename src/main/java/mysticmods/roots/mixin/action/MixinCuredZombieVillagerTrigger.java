package mysticmods.roots.mixin.action;

import mysticmods.roots.action.CureVillagerAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CuredZombieVillagerTrigger.class)
public class MixinCuredZombieVillagerTrigger {
  @Inject(method = "trigger", at = @At("HEAD"))
  private void RootsTriggerCureZombieVillager(ServerPlayer player, Zombie zombie, Villager villager, CallbackInfo ci) {
    CureVillagerAction.Context context = new CureVillagerAction.Context(player.serverLevel(), player, villager, zombie);
    ModActions.CURE_VILLAGER.get().accept(context);

  }
}
