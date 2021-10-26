package epicsquid.roots.network.fx;

import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class MessageRunicShearsBlockFX implements IMessage {
  public BlockPos position = BlockPos.ORIGIN;

  public MessageRunicShearsBlockFX() {
  }

  public MessageRunicShearsBlockFX(BlockPos position) {
    this.position = position;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.position = BlockPos.fromLong(buf.readLong());
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeLong(this.position.toLong());
  }

  public static class MessageHolder extends ClientMessageHandler<MessageRunicShearsBlockFX> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessageRunicShearsBlockFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (int k = 0; k < 2 + Util.rand.nextInt(2); k++) {
        ClientProxy.particleRenderer.spawnParticle(world, ParticleGlitter.class, message.position.getX() + 0.5, message.position.getY() + 0.5, message.position.getZ() + 0.5, Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), 120, 0.855 + Util.rand.nextDouble() * 0.05, 0.710, 0.943 - Util.rand.nextDouble() * 0.05, 1, Util.rand.nextDouble() + 0.5, Util.rand.nextDouble() * 2);
      }
    }
  }
}
