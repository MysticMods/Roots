package epicsquid.roots.network.fx;

import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class MessageCreatureSummonedFX implements IMessage {
  private Vector3d position;
  private float height;

  public MessageCreatureSummonedFX() {
    super();
  }

  public MessageCreatureSummonedFX(Entity entity) {
    super();
    this.position = entity.getPositionVector();
    this.height = entity.height;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.position = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    this.height = buf.readFloat();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(this.position.x);
    buf.writeDouble(this.position.y);
    buf.writeDouble(this.position.z);
    buf.writeFloat(this.height);
  }

  public static class MessageHolder extends ClientMessageHandler<MessageCreatureSummonedFX> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessageCreatureSummonedFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (int i = 0; i < 100; i++) {
        ClientProxy.particleRenderer
            .spawnParticle(world, ParticleGlitter.class, message.position.x, message.position.y + 0.9f, message.position.z,
                Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1),
                Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1),
                Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), 120,
                100f / 255f,
                65f / 255f + Util.rand.nextDouble() * 0.05,
                210f / 255f,
                194f / 255f,
                Util.rand.nextDouble() + 0.5,
                Util.rand.nextDouble() * 2);
      }
    }
  }

}