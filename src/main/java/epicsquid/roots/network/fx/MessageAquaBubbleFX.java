package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.potion.PotionAquaBubble;
import epicsquid.roots.spell.SpellAcidCloud;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class MessageAquaBubbleFX implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;

  public MessageAquaBubbleFX() {
    super();
  }

  public MessageAquaBubbleFX(double x, double y, double z) {
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

  public static class MessageHolder extends ClientMessageHandler<MessageAquaBubbleFX> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessageAquaBubbleFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      PotionAquaBubble.doEffect(world, message.posX, message.posY, message.posZ);
    }
  }
}