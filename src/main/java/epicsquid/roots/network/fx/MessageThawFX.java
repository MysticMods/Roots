/*
package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.unused.SpellSummersThaw;
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

public class MessageThawFX implements IMessage {
  public List<BlockPos> affectedBlocks = new ArrayList<>();

  public MessageThawFX() {
  }

  public MessageThawFX(List<BlockPos> affectedBlocks) {
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

  public static class MessageHolder extends ClientMessageHandler<MessageThawFX> {
    @SideOnly(Side.CLIENT)
    @Override
    protected void handleMessage(final MessageThawFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (BlockPos pos : message.affectedBlocks) {
        for (int k = 0; k < 2 + Util.rand.nextInt(2); k++) {
          for (int i = 0; i < 4; i++) {
            ParticleUtil.spawnParticleFiery(world, pos.getX() + 0.45F, pos.getY() + 0.9F, pos.getZ() + 0.45F, 0, 0.01F, 0, SpellSummersThaw.instance.getRed2(), SpellSummersThaw.instance.getGreen2(), SpellSummersThaw.instance.getBlue2(), 0.30F, 10F, 40);
          }
        }
      }
    }
  }
}
*/
