package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellSanctuary;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSanctuaryRingFX extends ModifierPacket implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;

  public MessageSanctuaryRingFX() {
    super();
  }

  public MessageSanctuaryRingFX(double x, double y, double z, StaffModifierInstanceList mods) {
    super(mods);
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

  public static class MessageHolder extends ClientMessageHandler<MessageSanctuaryRingFX> {
    @SideOnly(Side.CLIENT)
    @Override
    protected void handleMessage(final MessageSanctuaryRingFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      float c;
      if (message.modifiers.has(SpellSanctuary.RADIUS)) {
        c = MathHelper.sqrt(1 + SpellSanctuary.instance.radius_x + SpellSanctuary.instance.radius_z + (2 * SpellSanctuary.instance.radius_boost));
      } else {
        c = MathHelper.sqrt(1 + SpellSanctuary.instance.radius_x + SpellSanctuary.instance.radius_z);
      }
      for (float k = 0; k < 360; k += Util.rand.nextInt(42)) {
        if (Util.rand.nextBoolean()) {
          if (Util.rand.nextBoolean()) {
            ParticleUtil.spawnParticlePetal(world, (float) message.posX + c * (float) Math.sin(Math.toRadians(k)), (float) message.posY, (float) message.posZ + c * (float) Math.cos(Math.toRadians(k)), 0, 0, 0, SpellSanctuary.instance.getFirstColours(0.5f), 1.25f + 5.0f * Util.rand.nextFloat(), 40);
          } else {
            ParticleUtil.spawnParticlePetal(world, (float) message.posX + c * (float) Math.sin(Math.toRadians(k)), (float) message.posY, (float) message.posZ + c * (float) Math.cos(Math.toRadians(k)), 0, 0, 0, SpellSanctuary.instance.getSecondColours(0.5f), 1.25f + 5.0f * Util.rand.nextFloat(), 40);
          }
        }
      }
    }
  }

}