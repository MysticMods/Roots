package mysticmods.roots.effect;

import mysticmods.roots.network.client.ClientboundLightDrifterSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class LightDrifterEffect extends SimpleEffect{
  public LightDrifterEffect(MobEffectCategory category, int color, boolean hiddenByDefault) {
    super(category, color, hiddenByDefault);
  }

  @Override
  public boolean onEffectExpired(LivingEntity entity, int amplifier) {
    if (entity instanceof ServerPlayer player) {
      PacketDistributor.sendToPlayer(player, new ClientboundLightDrifterSyncPacket(-1));
    }
    return super.onEffectExpired(entity, amplifier);
  }

  @Override
  public boolean onEffectRemoved(LivingEntity entity, int amplifier) {
    if (entity instanceof ServerPlayer player) {
      PacketDistributor.sendToPlayer(player, new ClientboundLightDrifterSyncPacket(-1));
    }
    return super.onEffectRemoved(entity, amplifier);
  }
}
