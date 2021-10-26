package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

import java.util.UUID;

public class MessageDandelionCastFX implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;
  private UUID id = null;

  public MessageDandelionCastFX() {
    super();
  }

  public MessageDandelionCastFX(UUID id, double x, double y, double z) {
    super();
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    this.id = id;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    posX = buf.readDouble();
    posY = buf.readDouble();
    posZ = buf.readDouble();
    id = new UUID(buf.readLong(), buf.readLong());
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(posX);
    buf.writeDouble(posY);
    buf.writeDouble(posZ);
    buf.writeLong(id.getMostSignificantBits());
    buf.writeLong(id.getLeastSignificantBits());
  }

  public static class MessageHolder extends ClientMessageHandler<MessageDandelionCastFX> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessageDandelionCastFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      PlayerEntity player = world.getPlayerEntityByUUID(message.id);
      if (player != null) {
        for (int k = 0; k < 40; k++) {
          ParticleUtil.spawnParticleSmoke(world, (float) player.posX, (float) player.posY + player.getEyeHeight(), (float) player.posZ, (float) player.getLookVec().x + (Util.rand.nextFloat() - 0.5f), (float) player.getLookVec().y + (Util.rand.nextFloat() - 0.5f), (float) player.getLookVec().z + (Util.rand.nextFloat() - 0.5f), 0.65f, 0.65f, 0.65f, 0.15f, 12f, 40, false);
        }
      }
    }
  }
}