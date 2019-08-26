package epicsquid.roots.network.fx;

import java.util.Random;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellGeas;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageGeasRingFX implements IMessage {
  private static Random random = new Random();
  private double posX = 0, posY = 0, posZ = 0;

  public MessageGeasRingFX() {
    super();
  }

  public MessageGeasRingFX(double x, double y, double z) {
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

  public static class MessageHolder implements IMessageHandler<MessageGeasRingFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageGeasRingFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (int k = 0; k < 20; k++) {
        if (random.nextBoolean()) {
          ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat() - 0.5f), SpellGeas.instance.getRed1() * 255.0f, SpellGeas.instance.getGreen1() * 255.0f, SpellGeas.instance.getBlue1() * 255.0f, 0.75f, 7.5f, 24);
        } else {
          ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat() - 0.5f), SpellGeas.instance.getRed2() * 255.0f, SpellGeas.instance.getGreen2() * 255.0f, SpellGeas.instance.getBlue2() * 255.0f, 0.75f, 7.5f, 24);
        }
      }
      for (float k = 0; k < 360; k += random.nextInt(9)) {
        if (random.nextBoolean()) {
          if (random.nextBoolean()) {
            ParticleUtil.spawnParticleGlow(world, (float) message.posX + 1.15f * (float) Math.sin(Math.toRadians(k)), (float) message.posY, (float) message.posZ + 1.15f * (float) Math.cos(Math.toRadians(k)), 0, 0, 0, SpellGeas.instance.getRed1(), SpellGeas.instance.getGreen1(), SpellGeas.instance.getBlue1(), 0.75f, 1.25f + 5.0f * random.nextFloat(), 40);
          } else {
            ParticleUtil.spawnParticleGlow(world, (float) message.posX + 1.15f * (float) Math.sin(Math.toRadians(k)), (float) message.posY, (float) message.posZ + 1.15f * (float) Math.cos(Math.toRadians(k)), 0, 0, 0, SpellGeas.instance.getRed2(), SpellGeas.instance.getGreen2(), SpellGeas.instance.getBlue2(), 0.75f, 1.25f + 5.0f * random.nextFloat(), 40);
          }
        }
      }
      return null;
    }
  }

}