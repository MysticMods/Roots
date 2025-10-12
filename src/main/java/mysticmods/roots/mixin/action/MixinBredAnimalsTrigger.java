package mysticmods.roots.mixin.action;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mysticmods.roots.action.BredAnimalAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BredAnimalsTrigger.class)
public class MixinBredAnimalsTrigger {
  @WrapMethod(method = "trigger")
  public void RootsTriggerAnimalBreeding(ServerPlayer player, Animal parent, Animal partner, AgeableMob child, Operation<Void> original) {
    original.call(player, parent, partner, child);
    BredAnimalAction.Context context = new BredAnimalAction.Context(player.serverLevel(), player, child, parent, partner);
    ModActions.BRED_ANIMAL.get().accept(context);
  }
}
