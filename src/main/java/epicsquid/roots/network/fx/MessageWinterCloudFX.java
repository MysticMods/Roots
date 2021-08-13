package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellStormCloud;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageWinterCloudFX implements IMessage {

  private float x;
  private float y;
  private float z;

  public MessageWinterCloudFX() {
  }

  public MessageWinterCloudFX(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public void fromBytes(ByteBuf byteBuf) {
    x = byteBuf.readFloat();
    y = byteBuf.readFloat();
    z = byteBuf.readFloat();
  }

  @Override
  public void toBytes(ByteBuf byteBuf) {
    byteBuf.writeFloat(x);
    byteBuf.writeFloat(y);
    byteBuf.writeFloat(z);
  }

  public static class Handler extends ClientMessageHandler<MessageWinterCloudFX> {
    @Override
    @SideOnly(Side.CLIENT)
    protected void handleMessage(MessageWinterCloudFX message, MessageContext context) {
      World world = Minecraft.getMinecraft().world;
      if (Util.rand.nextBoolean()) {
        ParticleUtil.spawnParticleSmoke(world, message.x + 0.5F, message.y + 0.9F, message.z + 0.5F, 0, 0.01F, 0, SpellStormCloud.instance.getRed1(), SpellStormCloud.instance.getBlue1(), SpellStormCloud.instance.getGreen1(), 0.30F, 15F, 40, true);
      } else {
        ParticleUtil.spawnParticleSmoke(world, message.x + 0.5F, message.y + 0.9F, message.z + 0.5F, 0, 0.01F, 0, SpellStormCloud.instance.getRed2(), SpellStormCloud.instance.getBlue2(), SpellStormCloud.instance.getGreen2(), 0.30F, 15F, 40, true);
      }
    }
  }
}
