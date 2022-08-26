package epicsquid.roots.network.fx;

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

public class MessageGroveTickFX implements IMessage {
  public List<BlockPos> affectedBlocks = new ArrayList<>();

  public MessageGroveTickFX() {
  }

  public MessageGroveTickFX(List<BlockPos> affectedBlocks) {
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

  public static class MessageHolder implements IMessageHandler<MessageGroveTickFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageGroveTickFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (BlockPos pos : message.affectedBlocks) {
        for (int k = 0; k < 30; k++) {
          ParticleUtil.spawnParticleGlow(world, (float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f, 0.025f * (Util.rand.nextFloat() - 0.3f), 0.025f * (Util.rand.nextFloat() - 0.3f), 0.025f * (Util.rand.nextFloat() - 0.3f), 36f, 119f, 52f, 0.5f, 2.5f, 200);
        }
      }
      return null;
    }
  }
}
