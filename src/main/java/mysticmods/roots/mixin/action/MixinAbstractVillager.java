package mysticmods.roots.mixin.action;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mysticmods.roots.action.TradeVillagerAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractVillager.class)
public class MixinAbstractVillager {

  @WrapMethod(method = "notifyTrade")
  public void RootsNotifyTradeStart(MerchantOffer offer, Operation<Void> original) {
    int lastExperience = ((AbstractVillager) (Object) this).getVillagerXp();
    original.call(offer);
    if (lastExperience != -1) {
      int currentExperience = ((AbstractVillager) (Object) this).getVillagerXp();
      if (currentExperience > lastExperience) {
        //noinspection DataFlowIssue
        if (((AbstractVillager) (Object) this).getTradingPlayer() instanceof ServerPlayer player) {
          //noinspection DataFlowIssue
          TradeVillagerAction.Context context = new TradeVillagerAction.Context(player.serverLevel(), player, (AbstractVillager) (Object) this, offer);
          ModActions.TRADE_VILLAGER.get().accept(context);
        }
      }
    }
  }
}
