package epicsquid.roots.network.fx;

import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class MessageItemGatheredFX implements IMessage {
  private List<BlockPos> positions = new ArrayList<>();

  public MessageItemGatheredFX() {
    super();
  }

  public MessageItemGatheredFX(List<BlockPos> positions) {
    super();
    this.positions = positions;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    int count = buf.readInt();
    this.positions = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      this.positions.add(BlockPos.fromLong(buf.readLong()));
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.positions.size());
    for (BlockPos pos : positions) {
      buf.writeLong(pos.toLong());
    }
  }

  public static class MessageHolder implements IMessageHandler<MessageItemGatheredFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageItemGatheredFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (BlockPos pos : message.positions) {
        for (int i = 0; i < 15; i++) {
          ClientProxy.particleRenderer
              .spawnParticle(world, ParticleGlitter.class, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                  Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1),
                  Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), 120,
                  161f / 255f,
                  18f / 255f + Util.rand.nextDouble() * 0.05,
                  23f / 255f,
                  255f / 255f,
                  Util.rand.nextDouble() + 0.5,
                  Util.rand.nextDouble() * 2);
        }
      }
      return null;
    }
  }

}