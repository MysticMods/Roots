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

  private double x;
  private double y;
  private double z;

  public MessageDisarmFX() {
  }

  public MessageDisarmFX(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    x = buf.readDouble();
    y = buf.readDouble();
    z = buf.readDouble();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(x);
    buf.writeDouble(y);
    buf.writeDouble(z);
  }

  public static class Handler implements IMessageHandler<MessageDisarmFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(MessageDisarmFX message, MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;

      ParticleUtil.spawnParticleSmoke(world, (float) message.x + 0.5F, (float) message.y + 3, (float) message.z + 0.5F,
          0, 0, 0, SpellDisarm.instance.getRed1(), 0, 0, 1.0f, 6.0f + 6.0f * Util.rand.nextFloat(), 60, false);

      return null;
    }

  }
}
