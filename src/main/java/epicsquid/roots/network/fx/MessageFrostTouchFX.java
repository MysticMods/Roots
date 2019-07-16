package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellIcedTouch;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageFrostTouchFX implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;

  public MessageFrostTouchFX() {
    super();
  }

  public MessageFrostTouchFX(double x, double y, double z) {
    super();
    this.posX = x;
    this.posY = y;
    this.posZ = z;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    posX = buf.readDouble();
    posY = buf.readDouble();
    posZ = buf.readDouble();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(posX);
    buf.writeDouble(posY);
    buf.writeDouble(posZ);
  }

  public static float getColorCycle(float ticks) {
    return (MathHelper.sin((float) Math.toRadians(ticks)) + 1.0f) / 2.0f;
  }

  public static class MessageHolder implements IMessageHandler<MessageFrostTouchFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageFrostTouchFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (float i = 0; i < 360; i += Util.rand.nextInt(40)) {
        float x = (float) message.posX + (1.5f * Util.rand.nextFloat() + 2.0f) * (float) Math.sin(Math.toRadians(i));
        float y = (float) message.posY + (Util.rand.nextFloat() - 1.5f);
        float z = (float) message.posZ + (1.5f * Util.rand.nextFloat() + 2.0f) * (float) Math.cos(Math.toRadians(i));
        float vx = 0.0625f * (float) Math.cos(Math.toRadians(i));
        float vz = 0.025f * (float) Math.sin(Math.toRadians(i));
        if (Util.rand.nextBoolean()) {
          vx *= -1;
          vz *= -1;
        }
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, SpellIcedTouch.instance.getRed1(),
              SpellIcedTouch.instance.getGreen1(), SpellIcedTouch.instance.getBlue1(), 0.125f, 10f + Util.rand.nextFloat() * 6f, 120, false);
        } else {
          ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, SpellIcedTouch.instance.getRed2(),
              SpellIcedTouch.instance.getGreen2(), SpellIcedTouch.instance.getBlue2(), 0.125f, 10f + Util.rand.nextFloat() * 6f, 120, false);
        }
      }
      return null;
    }
  }

}