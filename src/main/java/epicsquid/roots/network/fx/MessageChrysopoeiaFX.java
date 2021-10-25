package epicsquid.roots.network.fx;

import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellChrysopoeia;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
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

  public MessageChrysopoeiaFX(PlayerEntity player) {
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
  public static class Handler extends ClientMessageHandler<MessageChrysopoeiaFX> {
    @SideOnly(Side.CLIENT)
    @Override
    protected void handleMessage(MessageChrysopoeiaFX message, MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      Entity entity = world.getEntityByID(message.entityId);
      if (entity instanceof PlayerEntity) {
        PlayerEntity player = (PlayerEntity) entity;
        float height = player.height;
        float increment = 0.08f;

        for (int i = 0; i <= 36; i++) {
          float tx = (float) entity.posX + 0.75f * (float) Math.sin(Math.toRadians(i * 10));
          float ty = (float) entity.posY + (height / 2 - 0.6f) + ((i > 18) ? (increment * 36) - increment * i : increment * i);
          float tz = (float) entity.posZ + 0.75f * (float) Math.cos(Math.toRadians(i * 10));
          ParticleUtil.spawnParticlePetal(world, tx, ty, tz, 0, -0.01f, 0, SpellChrysopoeia.instance.getSecondColours(1.0f), 2.0f, 360);
          ty = (float) entity.posY - height + 0.6f + ((i < 18) ? (increment * 36) - increment * i : increment * i);
          ParticleUtil.spawnParticlePetal(world, tx, ty, tz, 0, -0.01f, 0, SpellChrysopoeia.instance.getSecondColours(1.0f), 2.0f, 360);
          ty = (float) entity.posY + (height / 2);
          ParticleUtil.spawnParticlePetal(world, tx, ty, tz, 0, -0.01f, 0, SpellChrysopoeia.instance.getSecondColours(1.0f), 2.0f, 360);
        }
      }
    }
  }
}
