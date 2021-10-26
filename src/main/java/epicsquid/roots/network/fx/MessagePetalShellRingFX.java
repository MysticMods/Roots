package epicsquid.roots.network.fx;

import epicsquid.roots.modifiers.instance.staff.ISnapshot;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellPetalShell;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class MessagePetalShellRingFX extends ModifierPacket implements IMessage {
  private int ticksExisted;
  private int amplifier;
  private double posX;
  private double posY;
  private double posZ;

  public MessagePetalShellRingFX() {
    super();
  }

  public MessagePetalShellRingFX(int ticks, double posX, double posY, double posZ, int amplifier, ISnapshot snapshot) {
    super(snapshot);
    this.ticksExisted = ticks;
    this.posX = posX;
    this.posY = posY;
    this.posZ = posZ;
    this.amplifier = amplifier;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    this.ticksExisted = buf.readInt();
    this.posX = buf.readDouble();
    this.posY = buf.readDouble();
    this.posZ = buf.readDouble();
    this.amplifier = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeInt(this.ticksExisted);
    buf.writeDouble(this.posX);
    buf.writeDouble(this.posY);
    buf.writeDouble(this.posZ);
    buf.writeInt(this.amplifier);
  }

  public static class MessageHolder extends ClientMessageHandler<MessagePetalShellRingFX> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessagePetalShellRingFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      int count = message.amplifier;
      int shells = SpellPetalShell.instance.maxShells - 1; // 3 - 1
      if (message.has(SpellPetalShell.CHARGES)) {
        shells += SpellPetalShell.instance.extraShells; // 2
      }
      float radius = 0.8f;
      float height = 1.0f;
      float anglePerShell = (float)(Math.PI * 2.0 / (count));
      float angleOffset   = (float)Math.toRadians(message.ticksExisted % 360);
      for (int i = 0; i <= shells; i++) {
        float tx = (float) message.posX + radius * (float) Math.sin(angleOffset + i * anglePerShell);
        float tz = (float) message.posZ + radius * (float) Math.cos(angleOffset + i * anglePerShell);
        float ty = (float) message.posY + height;
        ParticleUtil.spawnParticlePetal(world, tx, ty, tz, 0, 0, 0, message.has(SpellPetalShell.COLOUR) ? SpellPetalShell.mossFirst : SpellPetalShell.instance.getFirstColours(), 3.5f, 15);
        count--;
        if (count <= 0) {
          break;
        }
      }
/*      World world = Minecraft.getMinecraft().world;
      int count = message.amplifier;
      int shells = SpellPetalShell.instance.maxShells; // 3 - 1
      if (message.has(SpellPetalShell.CHARGES)) {
        shells += SpellPetalShell.instance.extraShells; // 2
      }
      for (float i = 0; i < 360; i += (360.0 / shells + 1)) {
        float ang = (float) (message.ticksExisted % 360);
        float tx = (float) message.posX + 0.8f * (float) Math.sin(Math.toRadians(2.0f * (i + ang)));
        float ty = (float) message.posY + 1.0f;
        float tz = (float) message.posZ + 0.8f * (float) Math.cos(Math.toRadians(2.0f * (i + ang)));
        ParticleUtil.spawnParticlePetal(world, tx, ty, tz, 0, 0, 0, message.has(SpellPetalShell.COLOUR) ? SpellPetalShell.mossFirst : SpellPetalShell.instance.getFirstColours(), 3.5f, 15);
        count--;
        if (count < 0) {
          break;
        }
      }*/
    }
  }
}
