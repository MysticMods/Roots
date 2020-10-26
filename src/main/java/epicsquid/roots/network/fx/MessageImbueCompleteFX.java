package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.FakeSpell;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageImbueCompleteFX implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;
  private String spellName = "null";

  public MessageImbueCompleteFX() {
    super();
  }

  public MessageImbueCompleteFX(String name, double x, double y, double z) {
    super();
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    this.spellName = name;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    posX = buf.readDouble();
    posY = buf.readDouble();
    posZ = buf.readDouble();
    spellName = ByteBufUtils.readUTF8String(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(posX);
    buf.writeDouble(posY);
    buf.writeDouble(posZ);
    ByteBufUtils.writeUTF8String(buf, spellName);
  }

  public static float getColorCycle(float ticks) {
    return (MathHelper.sin((float) Math.toRadians(ticks)) + 1.0f) / 2.0f;
  }

  public static class MessageHolder extends ClientMessageHandler<MessageImbueCompleteFX> {
    @SideOnly(Side.CLIENT)
    @Override
    protected void handleMessage(final MessageImbueCompleteFX message, final MessageContext ctx) {
      SpellBase spell = (message.spellName.equals("fake_spell")) ? new FakeSpell() : SpellRegistry.getSpell(message.spellName);
      if (spell != null) {
        World world = Minecraft.getMinecraft().world;
        for (int k = 0; k < 40; k++) {
          if (Util.rand.nextBoolean()) {
            ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), spell.getFirstColours(0.5f), 2.5f, 48);
          } else {
            ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), spell.getSecondColours(0.5f), 2.5f, 48);
          }
        }
      }
    }
  }
}