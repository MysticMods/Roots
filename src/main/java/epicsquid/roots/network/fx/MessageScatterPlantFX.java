package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellScatter;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class MessageScatterPlantFX implements IMessage {

  private List<BlockPos> affectedBlocks;

  @SuppressWarnings("unused")
  public MessageScatterPlantFX() {
  }

  public MessageScatterPlantFX(List<BlockPos> affectedBlocks) {
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

  public static class Handler implements IMessageHandler<MessageScatterPlantFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(MessageScatterPlantFX message, MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (BlockPos pos : message.affectedBlocks) {
        for (int k = 0; k < 2 + Util.rand.nextInt(3); k++) {
          ParticleUtil.spawnParticleGlow(world, (float) pos.getX() + Util.rand.nextFloat(), (float) pos.getY() + Util.rand.nextFloat(), (float) pos.getZ() + Util.rand.nextFloat(), 0, 0.125f * (Util.rand.nextFloat() - 0.5f), 0, SpellScatter.instance.getRed2() * 255.0f, SpellScatter.instance.getGreen2() * 255.0f, SpellScatter.instance.getBlue2() * 255.0f, 0.5f, 5f, 100);
        }
      }
      return null;
    }
  }
}
