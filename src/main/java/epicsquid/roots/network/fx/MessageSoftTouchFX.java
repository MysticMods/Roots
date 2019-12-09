package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellSoftTouch;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSoftTouchFX implements IMessage {

  private int x;
  private int y;
  private int z;

  public MessageSoftTouchFX() { }

  public MessageSoftTouchFX(int x, int y, int z) {
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

  public static class Handler implements IMessageHandler<MessageSoftTouchFX, IMessage>
  {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(MessageSoftTouchFX message, MessageContext ctx) {
      for (int i = 0; i < 10; i++)
      {
        if (Util.rand.nextBoolean())
        {
          ParticleUtil.spawnParticleGlow(Minecraft.getMinecraft().world, message.x + ((float) Math.random()), message.y + ((float) Math.random()), message.z + ((float) Math.random()),
                  0, 0, 0,
                  SpellSoftTouch.instance.getRed1(), SpellSoftTouch.instance.getGreen1(), SpellSoftTouch.instance.getBlue1(),
                  0.75F, 1F, 80);
        }
        else
        {
          ParticleUtil.spawnParticleGlow(Minecraft.getMinecraft().world, message.x + ((float) Math.random()), message.y + ((float) Math.random()), message.z + ((float) Math.random()),
                  0, 0, 0,
                  SpellSoftTouch.instance.getRed2(), SpellSoftTouch.instance.getGreen2(), SpellSoftTouch.instance.getBlue2(),
                  0.75F, 1F, 80);
        }
      }
      return null;
    }
  }
}
