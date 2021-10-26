package epicsquid.roots.network.fx;

import epicsquid.roots.network.ClientMessageHandler;
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
import net.minecraftforge.fml.relauncher.OnlyIn;

import java.util.Random;

public class MessageGeasFX implements IMessage {
  private static Random random = new Random();
  private double posX = 0, posY = 0, posZ = 0;

  public MessageGeasFX() {
    super();
  }

  public MessageGeasFX(double x, double y, double z) {
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

  public static class MessageHolder extends ClientMessageHandler<MessageGeasFX> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessageGeasFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (float i = 0; i < 360; i += random.nextInt(40)) {
        double yaw = Math.toRadians(i);
        float x = (float) message.posX + (0.25f * random.nextFloat()) * (float) Math.sin(yaw);
        float y = (float) message.posY;
        float z = (float) message.posZ + (0.25f * random.nextFloat()) * (float) Math.cos(yaw);
        if (random.nextBoolean()) {
          ParticleUtil.spawnParticleGlow(world, x, y, z, 0, 0.125f * (random.nextFloat() - 0.5f), 0, SpellGeas.instance.getFirstColours(0.75f), 2f + random.nextFloat() * 2f, 20);
        } else {
          ParticleUtil.spawnParticleGlow(world, x, y, z, 0, 0.125f * (random.nextFloat() - 0.5f), 0, SpellGeas.instance.getSecondColours(0.75f), 2f + random.nextFloat() * 2f, 20);
        }
      }
    }
  }
}