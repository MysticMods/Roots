/*
package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.SpellShatter;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageShatterBurstFX implements IMessage {
  private double srcX = 0, srcY = 0, srcZ = 0;
  private double posX = 0, posY = 0, posZ = 0;

  public MessageShatterBurstFX() {
    super();
  }

  public MessageShatterBurstFX(double sx, double sy, double sz, double x, double y, double z) {
    super();
    this.srcX = sx;
    this.srcY = sy;
    this.srcZ = sz;
    this.posX = x;
    this.posY = y;
    this.posZ = z;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    srcX = buf.readDouble();
    srcY = buf.readDouble();
    srcZ = buf.readDouble();
    posX = buf.readDouble();
    posY = buf.readDouble();
    posZ = buf.readDouble();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(srcX);
    buf.writeDouble(srcY);
    buf.writeDouble(srcZ);
    buf.writeDouble(posX);
    buf.writeDouble(posY);
    buf.writeDouble(posZ);
  }

  public static float getColorCycle(float ticks) {
    return (MathHelper.sin((float) Math.toRadians(ticks)) + 1.0f) / 2.0f;
  }

  public static class MessageHolder implements IMessageHandler<MessageShatterBurstFX, IMessage> {
    @OnlyIn(Dist.CLIENT)
    @Override
    public IMessage onMessage(final MessageShatterBurstFX message, final MessageContext ctx) {
      World world = Minecraft.getInstance().world;
      for (float i = 0; i < 40; i++) {
        double x = (1.0f - i / 40.0f) * message.srcX + (i / 40.0f) * message.posX;
        double y = (1.0f - i / 40.0f) * message.srcY + (i / 40.0f) * message.posY;
        double z = (1.0f - i / 40.0f) * message.srcZ + (i / 40.0f) * message.posZ;
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleGlow(world, (float) x, (float) y, (float) z, 0, 0, 0, SpellShatter.instance.getRed1() * 255.0f,
              SpellShatter.instance.getGreen1() * 255.0f, SpellShatter.instance.getBlue1() * 255.0f, 0.25f * (i / 40.0f), 2.5f, 24);
        } else {
          ParticleUtil.spawnParticleGlow(world, (float) x, (float) y, (float) z, 0, 0, 0, SpellShatter.instance.getRed2() * 255.0f,
              SpellShatter.instance.getGreen2() * 255.0f, SpellShatter.instance.getBlue2() * 255.0f, 0.25f * (i / 40.0f), 2.5f, 24);
        }
      }
      for (int k = 0; k < 20; k++) {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.25f * (Util.rand.nextFloat() - 0.5f),
              0.25f * (Util.rand.nextFloat() - 0.5f), 0.25f * (Util.rand.nextFloat() - 0.5f), SpellShatter.instance.getRed1() * 255.0f,
              SpellShatter.instance.getGreen1() * 255.0f, SpellShatter.instance.getBlue1() * 255.0f, 0.25f, 5f, 12);
        } else {
          ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.25f * (Util.rand.nextFloat() - 0.5f),
              0.25f * (Util.rand.nextFloat() - 0.5f), 0.25f * (Util.rand.nextFloat() - 0.5f), SpellShatter.instance.getRed2() * 255.0f,
              SpellShatter.instance.getGreen2() * 255.0f, SpellShatter.instance.getBlue2() * 255.0f, 0.25f, 5f, 12);
        }
      }
      return null;
    }
  }

}*/
