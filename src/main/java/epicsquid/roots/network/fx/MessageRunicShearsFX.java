package epicsquid.roots.network.fx;

import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class MessageRunicShearsFX implements IMessage {
  private int entityId;

  public MessageRunicShearsFX() {
    super();
  }

  public MessageRunicShearsFX(Entity entity) {
    super();
    this.entityId = entity.getEntityId();
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.entityId = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.entityId);
  }

  @SideOnly(Side.CLIENT)
  @Nullable
  public Entity getEntity(World world) {
    return world.getEntityByID(this.entityId);
  }

  public static class MessageHolder extends ClientMessageHandler<MessageRunicShearsFX> {
    @SideOnly(Side.CLIENT)
    @Override
    protected void handleMessage(final MessageRunicShearsFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      Entity entity = message.getEntity(world);
      if (entity == null) {
        return;
      }
      for (int i = 0; i < 50; i++) {
        ClientProxy.particleRenderer
            .spawnParticle(world, ParticleGlitter.class, entity.posX, entity.posY + 0.9f, entity.posZ,
                Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1),
                Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), 120,
                100f / 255f,
                15f / 255f + Util.rand.nextDouble() * 0.05,
                120f / 255f,
                104f / 255f,
                Util.rand.nextDouble() + 0.5,
                Util.rand.nextDouble() * 2);
      }
    }
  }

}