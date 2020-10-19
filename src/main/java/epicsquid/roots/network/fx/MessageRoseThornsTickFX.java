package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.instance.staff.ISnapshot;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRoseThorns;
import epicsquid.roots.spell.SpellWildfire;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageRoseThornsTickFX extends ModifierPacket implements IMessage {
  private double posX;
  private double posY;
  private double posZ;
  private boolean onGround;


  public MessageRoseThornsTickFX() {
    super();
  }

  public MessageRoseThornsTickFX(double posX, double posY, double posZ, boolean onGround, ISnapshot snapshot) {
    super(snapshot);
    this.posX = posX;
    this.posY = posY;
    this.posZ = posZ;
    this.onGround = onGround;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    this.posX = buf.readDouble();
    this.posY = buf.readDouble();
    this.posZ = buf.readDouble();
    this.onGround = buf.readBoolean();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeDouble(this.posX);
    buf.writeDouble(this.posY);
    buf.writeDouble(this.posZ);
    buf.writeBoolean(this.onGround);
  }

  public static class MessageHolder implements IMessageHandler<MessageRoseThornsTickFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageRoseThornsTickFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      float scale1 = message.has(SpellRoseThorns.BIGGER) ? 4.5f : 2.5f;
      float scale2 = message.has(SpellRoseThorns.BIGGER) ? 9f : 5f;
      if (message.onGround) {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleThorn(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getFirstColours(0.5f), scale1, 12, Util.rand.nextBoolean());
        } else {
          ParticleUtil.spawnParticleThorn(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getSecondColours(0.5f), scale1, 12, Util.rand.nextBoolean());
        }
      } else {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getFirstColours(0.5f), scale2, 12);
        } else {
          ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getSecondColours(0.5f), scale2, 12);
        }
      }
      return null;
    }
  }
}
