package epicsquid.roots.network.fx;

import epicsquid.roots.particle.ParticleUtil;
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
      ParticleUtil.spawnParticleGlow(Minecraft.getMinecraft().world, message.x, message.y, message.z, message.x, message.y, message.z,
              1, 1, 1, 0.75F, 1F, 20);
      return null;
    }
  }
}
