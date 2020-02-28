package epicsquid.roots.network.fx;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellDesaturate;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageDesaturationFX implements IMessage {

  private int entityId;

  @SuppressWarnings("unused")
  public MessageDesaturationFX() {
  }

  public MessageDesaturationFX(int id) {
    this.entityId = id;
  }

  public MessageDesaturationFX(EntityPlayer player) {
    this.entityId = player.getEntityId();
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.entityId = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.entityId);
  }

  @SuppressWarnings("Duplicates")
  public static class Handler implements IMessageHandler<MessageDesaturationFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(MessageDesaturationFX message, MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      Entity entity = world.getEntityByID(message.entityId);
      if (entity instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) entity;
        for (int i = 0; i <= 360; i += 16) {
          float tx = (float) entity.posX + 0.5f * (float) Math.sin(Math.toRadians(i));
          float ty = (float) entity.posY + (player.height / 2) + 0.5f;
          float tz = (float) entity.posZ + 0.5f * (float) Math.cos(Math.toRadians(i));
          ParticleUtil.spawnParticlePetal(world, tx, ty, tz, (float) -(Math.sin(Math.toRadians(i)) * 0.1f), -0.01f, (float) -(Math.cos(Math.toRadians(i)) * 0.1f), SpellDesaturate.instance.getRed1(), SpellDesaturate.instance.getGreen1(), SpellDesaturate.instance.getBlue1(), 1.0f, 3.0f, 90);
        }
      }

      return null;
    }
  }
}
