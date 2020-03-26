package epicsquid.roots.network.fx;

import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
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

public class MessageRunicShearsAOEFX implements IMessage {
  public List<BlockPos> affectedBlocks = new ArrayList<>();

  public MessageRunicShearsAOEFX() {
  }

  public MessageRunicShearsAOEFX(List<BlockPos> affectedBlocks) {
    this.affectedBlocks = affectedBlocks;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    affectedBlocks.clear();
    int posCount = buf.readInt();
    for (int i = 0; i < posCount; i++) {
      affectedBlocks.add(BlockPos.fromLong(buf.readLong()));
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(affectedBlocks.size());
    for (BlockPos pos : affectedBlocks) {
      buf.writeLong(pos.toLong());
    }
  }

  public static class MessageHolder implements IMessageHandler<MessageRunicShearsAOEFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageRunicShearsAOEFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (BlockPos pos : message.affectedBlocks) {
        for (int k = 0; k < 2 + Util.rand.nextInt(2); k++) {
          ClientProxy.particleRenderer.spawnParticle(world, ParticleGlitter.class, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), 120, 0.855 + Util.rand.nextDouble() * 0.05, 0.710, 0.943 - Util.rand.nextDouble() * 0.05, 1, Util.rand.nextDouble() + 0.5, Util.rand.nextDouble() * 2);
        }
      }
      return null;
    }
  }
}
