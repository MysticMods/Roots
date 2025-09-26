package mysticmods.roots.client;

import mysticmods.roots.entity.other.LightDrifterEntity;
import mysticmods.roots.network.server.ServerboundMoveLightDrifterPacket;
import mysticmods.roots.util.LightDrifterUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClientLightDrifterUtil {
  public static void syncPosition(LocalPlayer player) {
    LightDrifterEntity entity = LightDrifterUtil.getLightDrifterEntity(player);
    if (entity == null) {
      return;
    }
    double d4 = entity.getX() - entity.xLast;
    double d0 = entity.getY() - entity.yLast1;
    double d1 = entity.getZ() - entity.zLast;
    double d2 = entity.getYRot() - entity.yRotLast;
    double d3 = entity.getXRot() - entity.xRotLast;
    entity.positionReminder++;
    boolean flag1 = Mth.lengthSquared(d4, d0, d1) > Mth.square(2.0E-4) || entity.positionReminder >= 20;
    boolean flag2 = d2 != 0.0 || d3 != 0.0;
    if (flag1 && flag2) {
      PacketDistributor.sendToServer(new ServerboundMoveLightDrifterPacket.PosRot(entity.getId(), entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot(), entity.onGround()));
    } else if (flag1) {
      PacketDistributor.sendToServer(new ServerboundMoveLightDrifterPacket.Pos(entity.getId(), entity.getX(), entity.getY(), entity.getZ(), entity.onGround()));
    } else if (flag2) {
      PacketDistributor.sendToServer(new ServerboundMoveLightDrifterPacket.Rot(entity.getId(), entity.getYRot(), entity.getXRot(), entity.onGround()));
    }

    if (flag1) {
      entity.xLast = entity.getX();
      entity.yLast1 = entity.getY();
      entity.zLast = entity.getZ();
      entity.positionReminder = 0;
    }

    if (flag2) {
      entity.yRotLast = entity.getYRot();
      entity.xRotLast = entity.getXRot();
    }
  }

  public static void serverAiStep(LocalPlayer player) {
    LightDrifterEntity entity = LightDrifterUtil.getLightDrifterEntity(player);
    if (entity == null) {
      return;
    }

    entity.xxa = player.input.leftImpulse;
    entity.zza = player.input.forwardImpulse;
    entity.yBobO = entity.yBob;
    entity.xBobO = entity.xBob;
    entity.xBob = entity.xBob + (entity.getXRot() - entity.xBob) * 0.5F;
    entity.yBob = entity.yBob + (entity.getYRot() - entity.yBob) * 0.5F;
  }

  public static void aiStep(LocalPlayer player) {
    LightDrifterEntity entity = LightDrifterUtil.getLightDrifterEntity(player);
    if (entity == null) {
      return;
    }

    int j = 0;
    if (player.input.shiftKeyDown) {
      j--;
    }

    if (player.input.jumping) {
      j++;
    }

    if (j != 0) {
      // TODO: Is this sufficient to sync
      entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, (float) j * 0.05f * 3.0F, 0.0));
    }
  }
}
