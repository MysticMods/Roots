package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellIcedTouch;
import epicsquid.roots.spell.SpellThaw;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageIcedTouchThawFX implements IMessage {

  private float x;
  private float y;
  private float z;
  private boolean isThaw;

  public MessageIcedTouchThawFX() {
  }

  public MessageIcedTouchThawFX(float x, float y, float z, boolean isThaw) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.isThaw = isThaw;
  }

  @Override
  public void fromBytes(ByteBuf byteBuf) {
    x = byteBuf.readFloat();
    y = byteBuf.readFloat();
    z = byteBuf.readFloat();
    isThaw = byteBuf.readBoolean();
  }

  @Override
  public void toBytes(ByteBuf byteBuf) {
    byteBuf.writeFloat(x);
    byteBuf.writeFloat(y);
    byteBuf.writeFloat(z);
    byteBuf.writeBoolean(isThaw);
  }

  public static class Handler implements IMessageHandler<MessageIcedTouchThawFX, IMessage> {

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(MessageIcedTouchThawFX message, MessageContext context) {
      World world = Minecraft.getMinecraft().world;
      if (message.isThaw) {
        for (int i = 0; i < 4; i++) {
            ParticleUtil.spawnParticleFiery(world, message.x + 0.45F, message.y + 0.9F, message.z + 0.45F, 0, 0.01F, 0, SpellThaw.instance.getRed2(),  SpellThaw.instance.getGreen2(),  SpellThaw.instance.getBlue2(), 0.30F, 10F, 40);
        }
      }
      else {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleSmoke(world, message.x + 0.5F, message.y + 0.9F, message.z + 0.5F, 0, 0.01F, 0, SpellIcedTouch.instance.getRed1(), SpellIcedTouch.instance.getBlue1(), SpellIcedTouch.instance.getGreen1(), 0.30F, 15F, 40, true);
        } else {
          ParticleUtil.spawnParticleSmoke(world, message.x + 0.5F, message.y + 0.9F, message.z + 0.5F, 0, 0.01F, 0, SpellIcedTouch.instance.getRed2(), SpellIcedTouch.instance.getBlue2(), SpellIcedTouch.instance.getGreen2(), 0.30F, 15F, 40, true);
        }
      }
      return null;
    }
  }
}
