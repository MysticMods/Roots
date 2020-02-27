package epicsquid.roots.network.fx;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellChrysopoeia;
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

public class MessageChrysopoeiaFX implements IMessage {

  private int entityId;

  @SuppressWarnings("unused")
  public MessageChrysopoeiaFX() {
  }

  public MessageChrysopoeiaFX(int id) {
    this.entityId = id;
  }

  public MessageChrysopoeiaFX(EntityPlayer player) {
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
  public static class Handler implements IMessageHandler<MessageChrysopoeiaFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(MessageChrysopoeiaFX message, MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      Entity entity = world.getEntityByID(message.entityId);
      if (entity instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) entity;
        float height = player.height;
        float increment = 0.05f;

        for (int i = 5; i <= 36 + 5; i++) {
          float tx = (float) entity.posX + 0.75f * (float) Math.sin(Math.toRadians(i * 10));
          float ty = (float) entity.posY + height + 0.6f - increment * i;
          float tz = (float) entity.posZ + 0.75f * (float) Math.cos(Math.toRadians(i * 10));
          ParticleUtil.spawnParticlePetal(world, tx, ty, tz, 0, -0.01f, 0, SpellChrysopoeia.instance.getRed2(), SpellChrysopoeia.instance.getGreen2(), SpellChrysopoeia.instance.getBlue2(), 1.0f, 2.0f, 360);
        }
        for (int i = 5; i <= 36 + 5; i++) {
          float tx = (float) entity.posX + 0.75f * (float) Math.cos(Math.toRadians(i * 10));
          float ty = (float) entity.posY + height + 0.6f - increment * i;
          float tz = (float) entity.posZ + 0.75f * (float) Math.sin(Math.toRadians(i * 10));
          ParticleUtil.spawnParticlePetal(world, tx, ty, tz, 0, -0.01f, 0, SpellChrysopoeia.instance.getRed2(), SpellChrysopoeia.instance.getGreen2(), SpellChrysopoeia.instance.getBlue2(), 1.0f, 2.0f, 360);
        }
        for (int i = 36 + 5; i >= 5; i--) {
          float tx = (float) entity.posX + 0.75f * (float) Math.sin(Math.toRadians(i * 10));
          float ty = (float) entity.posY + 0.1f + (increment * 36) - increment * i;
          float tz = (float) entity.posZ + 0.75f * (float) Math.cos(Math.toRadians(i * 10));
          ParticleUtil.spawnParticlePetal(world, tx, ty, tz, 0, -0.01f, 0, SpellChrysopoeia.instance.getRed2(), SpellChrysopoeia.instance.getGreen2(), SpellChrysopoeia.instance.getBlue2(), 1.0f, 2.0f, 360);
        }
        for (int i = 36 + 5; i >= 5; i--) {
          float tx = (float) entity.posX + 0.75f * (float) Math.cos(Math.toRadians(i * 10));
          float ty = (float) entity.posY + 0.1f + (increment * 36) - increment * i;
          float tz = (float) entity.posZ + 0.75f * (float) Math.sin(Math.toRadians(i * 10));
          ParticleUtil.spawnParticlePetal(world, tx, ty, tz, 0, -0.01f, 0, SpellChrysopoeia.instance.getRed2(), SpellChrysopoeia.instance.getGreen2(), SpellChrysopoeia.instance.getBlue2(), 1.0f, 2.0f, 360);
        }
      }

      return null;
    }
  }
}
