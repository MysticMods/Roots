package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.ModifierCores;
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

import java.util.ArrayList;
import java.util.List;

public class MessageAcidCloudFX extends ModifierPacket implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;

  public MessageAcidCloudFX() {
    super(null);
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
          if (!message.has(SpellAcidCloud.FIRE) || Util.rand.nextBoolean()) {
            if (Util.rand.nextBoolean()) {
              ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, SpellAcidCloud.instance.getRed1(), SpellAcidCloud.instance.getGreen1(), SpellAcidCloud.instance.getBlue1(), 0.125f, 10f + Util.rand.nextFloat() * 6f, 120, false);
            } else {
              ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, SpellAcidCloud.instance.getRed2(), SpellAcidCloud.instance.getGreen2(), SpellAcidCloud.instance.getBlue2(), 0.125f, 10f + Util.rand.nextFloat() * 6f, 120, false);
            }
          } else {
            if (Util.rand.nextBoolean()) {
              ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, 209.0f / 255, 54.0f / 255, 15.0f / 255, 0.125f, 10f + Util.rand.nextFloat() * 6f, 120, false);
            } else {
              ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, 245.0f / 255, 158.0f / 255, 66.0f / 255, 0.125f, 10f + Util.rand.nextFloat() * 6f, 120, false);
            }
          }
        }
      }
      return null;
    }
  }
}