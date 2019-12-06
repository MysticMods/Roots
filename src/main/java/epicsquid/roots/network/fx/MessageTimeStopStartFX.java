/*
package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.SpellTimeStop;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageTimeStopStartFX implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;

  public MessageTimeStopStartFX() {
    super();
  }

  public MessageTimeStopStartFX(double x, double y, double z) {
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

  public static class MessageHolder implements IMessageHandler<MessageTimeStopStartFX, IMessage> {
    @OnlyIn(Dist.CLIENT)
    @Override
    public IMessage onMessage(final MessageTimeStopStartFX message, final MessageContext ctx) {
      World world = Minecraft.getInstance().world;
      for (float k = 0; k < 360; k += Util.rand.nextInt(4)) {
        if (Util.rand.nextBoolean()) {
          if (Util.rand.nextBoolean()) {
            ParticleUtil.spawnParticleGlow(world, (float) message.posX + 0.5f * (float) Math.sin(Math.toRadians(k)), (float) message.posY,
                (float) message.posZ + 0.5f * (float) Math.cos(Math.toRadians(k)), Util.rand.nextFloat() * 0.1875f * (float) Math.sin(Math.toRadians(k)),
                Util.rand.nextFloat() * -0.0625f, Util.rand.nextFloat() * 0.1875f * (float) Math.cos(Math.toRadians(k)), SpellTimeStop.instance.getRed1(),
                SpellTimeStop.instance.getGreen1(), SpellTimeStop.instance.getBlue1(), 0.5f, 2.5f + 7.0f * Util.rand.nextFloat(), 40);
          } else {
            ParticleUtil.spawnParticleGlow(world, (float) message.posX + 0.5f * (float) Math.sin(Math.toRadians(k)), (float) message.posY,
                (float) message.posZ + 0.5f * (float) Math.cos(Math.toRadians(k)), Util.rand.nextFloat() * 0.1875f * (float) Math.sin(Math.toRadians(k)),
                Util.rand.nextFloat() * -0.0625f, Util.rand.nextFloat() * 0.1875f * (float) Math.cos(Math.toRadians(k)), SpellTimeStop.instance.getRed2(),
                SpellTimeStop.instance.getGreen2(), SpellTimeStop.instance.getBlue2(), 0.5f, 2.5f + 7.0f * Util.rand.nextFloat(), 40);
          }
        }
      }
      return null;
    }
  }

}*/
