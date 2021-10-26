package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellPetalShell;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class MessagePetalShellBurstFX extends ModifierPacket implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;

  public MessagePetalShellBurstFX() {
    super();
  }

  public MessagePetalShellBurstFX(double x, double y, double z, ModifierSnapshot info) {
    super(info);
    this.posX = x;
    this.posY = y;
    this.posZ = z;
  }

  public MessagePetalShellBurstFX(double x, double y, double z, StaffModifierInstanceList info) {
    super(info);
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

  @OnlyIn(Dist.CLIENT)
  public static float[] getColours(boolean first, final MessagePetalShellBurstFX message) {
    if (first) {
      if (message.has(SpellPetalShell.COLOUR)) {
        return SpellPetalShell.mossFirst;
      } else {
        return SpellPetalShell.instance.getFirstColours(0.5f);
      }
    } else {
      if (message.has(SpellPetalShell.COLOUR)) {
        return SpellPetalShell.mossSecond;
      } else {
        return SpellPetalShell.instance.getSecondColours(0.5f);
      }
    }
  }

  public static class MessageHolder extends ClientMessageHandler<MessagePetalShellBurstFX> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessagePetalShellBurstFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (int k = 0; k < 10; k++) {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticlePetal(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), getColours(true, message), 7.5f, 24);
        } else {
          ParticleUtil.spawnParticlePetal(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), getColours(false, message), 7.5f, 24);
        }
      }
      for (float k = 0; k < 360; k += Util.rand.nextInt(12)) {
        if (Util.rand.nextBoolean()) {
          if (Util.rand.nextBoolean()) {
            ParticleUtil.spawnParticlePetal(world, (float) message.posX + 1.35f * (float) Math.sin(Math.toRadians(k)), (float) message.posY, (float) message.posZ + 1.35f * (float) Math.cos(Math.toRadians(k)), 0, 0, 0, getColours(true, message), 1.25f + 5.0f * Util.rand.nextFloat(), 40);
          } else {
            ParticleUtil.spawnParticlePetal(world, (float) message.posX + 1.35f * (float) Math.sin(Math.toRadians(k)), (float) message.posY, (float) message.posZ + 1.35f * (float) Math.cos(Math.toRadians(k)), 0, 0, 0, getColours(false, message), 1.25f + 5.0f * Util.rand.nextFloat(), 40);
          }
        }
      }
    }
  }
}