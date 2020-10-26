package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
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

  public static class MessageHolder extends ClientMessageHandler<MessageShatterBurstFX> {
    @SideOnly(Side.CLIENT)
    @Override
    protected void handleMessage(final MessageShatterBurstFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      float scale = 40f;
      for (float i = 0; i < scale; i++) {
        double x = (1.0f - i / scale) * message.srcX + (i / scale) * message.posX;
        double y = (1.0f - i / scale) * message.srcY + (i / scale) * message.posY;
        double z = (1.0f - i / scale) * message.srcZ + (i / scale) * message.posZ;
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleGlow(world, (float) x, (float) y, (float) z, 0, 0, 0, SpellShatter.instance.getFirstColours(0.25f * (i / scale)), 2.5f, 24);
        } else {
          ParticleUtil.spawnParticleGlow(world, (float) x, (float) y, (float) z, 0, 0, 0, SpellShatter.instance.getSecondColours(0.25f * (i / scale)), 2.5f, 24);
        }
      }
      for (int k = 0; k < 20; k++) {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.25f * (Util.rand.nextFloat() - 0.5f), 0.25f * (Util.rand.nextFloat() - 0.5f), 0.25f * (Util.rand.nextFloat() - 0.5f), SpellShatter.instance.getFirstColours(0.25f), 5f, 12);
        } else {
          ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.25f * (Util.rand.nextFloat() - 0.5f), 0.25f * (Util.rand.nextFloat() - 0.5f), 0.25f * (Util.rand.nextFloat() - 0.5f), SpellShatter.instance.getSecondColours(0.25f), 5f, 12);
        }
      }
    }
  }

}