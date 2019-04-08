package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellDisarm;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageDisarmFX implements IMessage {

  private int x;
  private int y;
  private int z;

  public MessageDisarmFX() { }

  public MessageDisarmFX(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    x = buf.readInt();
    y = buf.readInt();
    z = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(x);
    buf.writeInt(y);
    buf.writeInt(z);
  }

  public static class Handler implements IMessageHandler<MessageDisarmFX, IMessage>
  {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(MessageDisarmFX message, MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;

      ParticleUtil.spawnParticleSmoke(
              world, (float) message.x, (float) message.y + 2,(float) message.z,
              (float) message.x, (float)message.y + 4, (float) message.z,
              SpellDisarm.instance.getRed1(), 0, 0, 1.0f,
              6.0f + 6.0f * Util.rand.nextFloat(), 40, false);

      return null;
    }

  }
}
