package mysticmods.roots.integration.curios;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.client.ClientboundChangeTomeMode;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

public class CuriosEventHandler {
  @SubscribeEvent
  public void onCuriosChange(CurioChangeEvent event) {
    LivingEntity entity = event.getEntity();
    if (entity instanceof ServerPlayer player && event.getTo().is(RootsTags.Items.GRAMARIES)) {
      boolean changedMode = false;
      if (event.getFrom().is(RootsTags.Items.GRAMARIES)) {
        if (event.getFrom().get(ModAttachments.GRAMARY_MODE) != event.getTo().get(ModAttachments.GRAMARY_MODE)) {
          changedMode = true;
        }
      }
      PacketDistributor.sendToPlayer(player, new ClientboundChangeTomeMode(changedMode));
    }
  }
}
