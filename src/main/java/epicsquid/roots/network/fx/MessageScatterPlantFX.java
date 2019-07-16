package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellScatter;
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

  @SuppressWarnings("unused")
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
      for (int k = 0; k < 5; k++) {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleGlow(world, (float) message.x + Util.rand.nextFloat(), (float) message.y + Util.rand.nextFloat(),
                  (float) message.z + Util.rand.nextFloat(), 0, 0.125f * (Util.rand.nextFloat() - 0.5f),
                  0, SpellScatter.instance.getRed2() * 255.0f, SpellScatter.instance.getGreen2() * 255.0f,
                  SpellScatter.instance.getBlue2() * 255.0f, 0.5f, 5f, 100);
        }
      }
      return null;
    }
  }
}
