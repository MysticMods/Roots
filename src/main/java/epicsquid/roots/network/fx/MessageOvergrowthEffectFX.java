package epicsquid.roots.network.fx;

import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class MessageOvergrowthEffectFX implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;

  public MessageOvergrowthEffectFX() {
    super();
  }

  public MessageOvergrowthEffectFX(double x, double y, double z) {
    super();
    this.posX = x;
    this.posY = y;
    this.posZ = z;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    posX = buf.readDouble();
    posY = buf.readDouble();
    posZ = buf.readDouble();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(posX);
    buf.writeDouble(posY);
    buf.writeDouble(posZ);
  }

  public static class MessageHolder extends ClientMessageHandler<MessageOvergrowthEffectFX> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessageOvergrowthEffectFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (int i = 0; i < 50; i++) {
        ClientProxy.particleRenderer.spawnParticle(world, ParticleGlitter.class, message.posX + 0.5, message.posY + 0.5, message.posZ + 0.5, Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), 120, 0.607, 0.698 + Util.rand.nextDouble() * 0.05, 0.306, 1, Util.rand.nextDouble() + 0.5, Util.rand.nextDouble() * 2);
      }
    }
  }
}