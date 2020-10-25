package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellAcidCloud;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageAcidCloudFX extends ModifierPacket implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;

  public MessageAcidCloudFX() {
    super();
  }

  public MessageAcidCloudFX(double x, double y, double z, StaffModifierInstanceList modifiers) {
    super(modifiers);
    this.posX = x;
    this.posY = y;
    this.posZ = z;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    posX = buf.readDouble();
    posY = buf.readDouble();
    posZ = buf.readDouble();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeDouble(posX);
    buf.writeDouble(posY);
    buf.writeDouble(posZ);
  }

  public static float getColorCycle(float ticks) {
    return (MathHelper.sin((float) Math.toRadians(ticks)) + 1.0f) / 2.0f;
  }

  private float[] getColor() {
    float[] list;
    // Default Acid
    if (Util.rand.nextBoolean()) {
      list = SpellAcidCloud.instance.getFirstColours(0.125f);
    } else {
      list = SpellAcidCloud.instance.getSecondColours(0.125f);
    }

    // Fire
    if (hasRand(SpellAcidCloud.FIRE, 3)) {
      if (Util.rand.nextBoolean()) {
        list = new float[]{209.0f / 255, 54.0f / 255, 15.0f / 255, 0.125f};
      } else {
        list = new float[]{245.0f / 255, 158.0f / 255, 66.0f / 255, 0.125f};
      }
    }

    // Physical Damage
    if (hasRand(SpellAcidCloud.PHYSICAL, 5)) {
      if (Util.rand.nextBoolean()) {
        list = new float[]{181.0f / 255, 175.0f / 255, 158.0f / 255, 0.125f};
      } else {
        list = new float[]{224.0f / 255, 213.0f / 255, 182.0f / 255, 0.125f};
      }
    }

    if (has(SpellAcidCloud.HEALING)) {
      if (Util.rand.nextBoolean()) {
        list = new float[]{235.0f / 255, 215.0f / 255, 63.0f / 255, 0.125f};
      } else {
        list = new float[]{237.0f / 255, 217.0f / 255, 168.0f / 255, 0.125f};
      }
    }

    return list;
  }

  public static class MessageHolder implements IMessageHandler<MessageAcidCloudFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageAcidCloudFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      boolean radius = message.has(SpellAcidCloud.RADIUS);
      for (int q = 0; q < (radius ? 2 : 1); q++) {
        float mod = q == 0 ? SpellAcidCloud.instance.radius / 2.0f : (SpellAcidCloud.instance.radius + SpellAcidCloud.instance.radius_boost) / 2.0f;
        for (float i = 0; i < 360; i += Util.rand.nextInt(40)) {
          float x = (float) message.posX + (1.5f * Util.rand.nextFloat() + mod) * (float) Math.sin(Math.toRadians(i));
          float y = (float) message.posY + (Util.rand.nextFloat() - 1.5f);
          float z = (float) message.posZ + (1.5f * Util.rand.nextFloat() + mod) * (float) Math.cos(Math.toRadians(i));
          float vx = 0.0625f * (float) Math.cos(Math.toRadians(i));
          float vz = 0.025f * (float) Math.sin(Math.toRadians(i));
          if (Util.rand.nextBoolean()) {
            vx *= -1;
            vz *= -1;
          }
          ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, message.getColor(), 10f + Util.rand.nextFloat() * 6f, 120, false);
        }
      }
      return null;
    }
  }
}