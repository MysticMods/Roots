package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.instance.staff.ISnapshot;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRoseThorns;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageRoseThornsBurstFX extends ModifierPacket implements IMessage {
  private double posX;
  private double posY;
  private double posZ;


  public MessageRoseThornsBurstFX() {
    super();
  }

  public MessageRoseThornsBurstFX(double posX, double posY, double posZ, ISnapshot snapshot) {
    super(snapshot);
    this.posX = posX;
    this.posY = posY;
    this.posZ = posZ;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    this.posX = buf.readDouble();
    this.posY = buf.readDouble();
    this.posZ = buf.readDouble();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeDouble(this.posX);
    buf.writeDouble(this.posY);
    buf.writeDouble(this.posZ);
  }

  public static class MessageHolder implements IMessageHandler<MessageRoseThornsBurstFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageRoseThornsBurstFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (int i = 0; i < 30; i++) {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleThorn(world, (float) message.posX + 0.25f * (Util.rand.nextFloat() - 0.5f), (float) message.posY + 0.25f * (Util.rand.nextFloat() - 0.5f), (float) message.posZ + 0.25f * (Util.rand.nextFloat() - 0.5f), 0.375f * Util.rand.nextFloat() - 0.1875f, 0.1f + 0.125f * Util.rand.nextFloat(), 0.375f * Util.rand.nextFloat() - 0.1875f, SpellRoseThorns.instance.getFirstColours(0.5f), 4.0f, 24, Util.rand.nextBoolean());
        } else {
          ParticleUtil.spawnParticleThorn(world, (float) message.posX + 0.25f * (Util.rand.nextFloat() - 0.5f), (float) message.posY + 0.25f * (Util.rand.nextFloat() - 0.5f), (float) message.posZ + 0.25f * (Util.rand.nextFloat() - 0.5f), 0.375f * Util.rand.nextFloat() - 0.1875f, 0.1f + 0.125f * Util.rand.nextFloat(), 0.375f * Util.rand.nextFloat() - 0.1875f, SpellRoseThorns.instance.getSecondColours(0.5f), 4.0f, 24, Util.rand.nextBoolean());
        }
      }
      return null;
    }
  }
}
