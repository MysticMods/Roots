package epicsquid.roots.network.fx;

import epicsquid.roots.particle.ParticleUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageScatterPlantFX implements IMessage {

  private int x, y, z;

  public MessageScatterPlantFX() { }

  public MessageScatterPlantFX(int x, int y, int z) {
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

  public static class Handler implements IMessageHandler<MessageScatterPlantFX, IMessage>
  {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(MessageScatterPlantFX message, MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
        for (int x = 0; x < 40; x++)
          ParticleUtil.spawnParticlePetal(world, message.x, message.y, message.z, message.x, message.y, message.z, 71F / 255F, 132F / 255F, 30F / 255F, 0.75F, 30F, 60);
        return null;
    }
  }
}
