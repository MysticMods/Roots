package mysticmods.roots.effect;

import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.client.ClientboundLightDrifterSyncPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class LightDrifterEffect extends SimpleEffect{
  public LightDrifterEffect(MobEffectCategory category, int color, boolean hiddenByDefault) {
    super(category, color, hiddenByDefault);
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity instanceof ServerPlayer player) {
      var drifter = player.getData(ModAttachments.DRIFTER_SERVER_STORAGE);
      ServerLevel level = player.serverLevel();
      if (drifter.id() == null) {
        player.removeData(ModAttachments.DRIFTER_SERVER_STORAGE);
        return false;
      }
      var drifterEntity = level.getEntity(drifter.id());
      if (drifterEntity == null) {
        player.removeData(ModAttachments.DRIFTER_SERVER_STORAGE);
        return false;
      }
      drifter.setEntityId(drifterEntity.getId());
      player.setData(ModAttachments.DRIFTER_SERVER_STORAGE, drifter);
      PacketDistributor.sendToPlayer(player, new ClientboundLightDrifterSyncPacket(drifterEntity.getId()));
    }

    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    return true;
  }

  @Override
  public boolean onEffectExpired(LivingEntity entity, int amplifier) {
    if (entity instanceof ServerPlayer player) {
      player.removeData(ModAttachments.DRIFTER_SERVER_STORAGE);
      PacketDistributor.sendToPlayer(player, new ClientboundLightDrifterSyncPacket(-1));
    }
    return super.onEffectExpired(entity, amplifier);
  }

  @Override
  public boolean onEffectRemoved(LivingEntity entity, int amplifier) {
    if (entity instanceof ServerPlayer player) {
      player.removeData(ModAttachments.DRIFTER_SERVER_STORAGE);
      PacketDistributor.sendToPlayer(player, new ClientboundLightDrifterSyncPacket(-1));
    }
    return super.onEffectRemoved(entity, amplifier);
  }
}
