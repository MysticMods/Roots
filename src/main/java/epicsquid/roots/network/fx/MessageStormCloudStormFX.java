package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellStormCloud;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageStormCloudStormFX extends ModifierPacket implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;

  private static final float[] color1 = new float[]{78 / 255.0f, 189 / 255.0f, 191 / 255.0f, 0.75f};
  private static final float[] color2 = new float[]{18 / 255.0f, 90 / 255.0f, 138 / 255.0f, 0.75f};

  private static final float[] color3 = new float[]{107/255.0f, 252/255.0f, 3/255.0f, 0.75f};
  private static final float[] color4 = new float[]{42/255.0f, 189/255.0f, 93/255.0f, 0.75f};

  public MessageStormCloudStormFX() {
    super();
  }

  public MessageStormCloudStormFX(Entity entity, ModifierSnapshot modifiers) {
    this(entity.posX, entity.posY, entity.posZ, modifiers);
  }

  public MessageStormCloudStormFX(double x, double y, double z, ModifierSnapshot modifiers) {
    super(modifiers);
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

  public static class MessageHolder implements IMessageHandler<MessageStormCloudStormFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageStormCloudStormFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (float i = 0; i < 360; i += Util.rand.nextInt(50)) {
        double r = Math.toRadians(i);
        float x = (float) message.posX + (0.75f + Util.rand.nextFloat() - 0.5f) * (float) Math.sin(r);
        float y = (float) message.posY + 0.7f + 1.5f;
        float z = (float) message.posZ + (0.75f + Util.rand.nextFloat() - 0.5f) * (float) Math.cos(r);
        float vx = 0.0625f * (float) Math.cos(r);
        float vz = 0.025f * (float) Math.sin(r);
        if (Util.rand.nextBoolean()) {
          vx *= -1;
          vz *= -1;
        }
        float[] color = color1;
        if (Util.rand.nextBoolean()) {
          color = color2;
        }
        if (message.hasRand(SpellStormCloud.POISON, 4)) {
          color = Util.rand.nextBoolean() ? color3 : color4;
        }
        ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0, vz, color, 3f + Util.rand.nextFloat() * 4f, 80, false);
      }
      return null;
    }
  }

}