package mysticmods.roots.mixin.action;

import mysticmods.roots.action.TradeVillagerAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractVillager.class)
public class MixinAbstractVillager {
  @Unique
  private int roots_1_21$lastExperience = -1;

  @Inject(method="notifyTrade",at=@At("HEAD"))
  public void RootsNotifyTradeStart (MerchantOffer offer, CallbackInfo ci) {
    this.roots_1_21$lastExperience = ((AbstractVillager) (Object) this).getVillagerXp();
  }

  @Inject(method="notifyTrade",at=@At("RETURN"))
  public void RootsNotifyTradeEnd (MerchantOffer offer, CallbackInfo ci) {
    if (this.roots_1_21$lastExperience != -1) {
      int currentExperience = ((AbstractVillager) (Object) this).getVillagerXp();
      if (currentExperience > this.roots_1_21$lastExperience) {
        //noinspection DataFlowIssue
        if (((AbstractVillager) (Object) this).getTradingPlayer() instanceof ServerPlayer player) {
          //noinspection DataFlowIssue
          TradeVillagerAction.Context context = new TradeVillagerAction.Context(player.serverLevel(), player, (AbstractVillager) (Object) this, offer);
          ModActions.TRADE_VILLAGER.get().accept(context);
        }
      }
    }
    this.roots_1_21$lastExperience = -1;
  }
}
