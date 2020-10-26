package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellLifeDrain;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class MessageLifeDrainAbsorbFX implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;
  private UUID id = null;

  public MessageLifeDrainAbsorbFX() {
    super();
  }

  public MessageLifeDrainAbsorbFX(UUID id, double x, double y, double z) {
    super();
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    this.id = id;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    posX = buf.readDouble();
    posY = buf.readDouble();
    posZ = buf.readDouble();
    id = new UUID(buf.readLong(), buf.readLong());
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(posX);
    buf.writeDouble(posY);
    buf.writeDouble(posZ);
    buf.writeLong(id.getMostSignificantBits());
    buf.writeLong(id.getLeastSignificantBits());
  }

  public static float getColorCycle(float ticks) {
    return (MathHelper.sin((float) Math.toRadians(ticks)) + 1.0f) / 2.0f;
  }

  public static class MessageHolder extends ClientMessageHandler<MessageLifeDrainAbsorbFX> {
    @SideOnly(Side.CLIENT)
    @Override
    protected void handleMessage(final MessageLifeDrainAbsorbFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      EntityPlayer player = world.getPlayerEntityByUUID(message.id);
      if (player != null) {
        Vec3d lookVec = player.getLookVec();
        for (int i = 0; i < 4; i++) {
          float x = (float) player.posX + (float) lookVec.x * (6.0f + Util.rand.nextFloat() * 9.0f) + 2.0f * (Util.rand.nextFloat() - 0.5f);
          float y = (float) player.posY + 1.0f + (float) lookVec.y * (6.0f + Util.rand.nextFloat() * 9.0f) + 2.0f * (Util.rand.nextFloat() - 0.5f);
          float z = (float) player.posZ + (float) lookVec.z * (6.0f + Util.rand.nextFloat() * 9.0f) + 2.0f * (Util.rand.nextFloat() - 0.5f);
          if (Util.rand.nextBoolean()) {
            ParticleUtil.spawnParticleLineGlow(world, x, y, z, (float) player.posX, (float) player.posY + 1.0f, (float) player.posZ, SpellLifeDrain.instance.getFirstColours(1.0f), 6.0f + 6.0f * Util.rand.nextFloat(), 40);
          } else {
            ParticleUtil.spawnParticleLineGlow(world, x, y, z, (float) player.posX, (float) player.posY + 1.0f, (float) player.posZ, SpellLifeDrain.instance.getSecondColours(1.0f), 6.0f + 6.0f * Util.rand.nextFloat(), 40);
          }
        }
      }
    }
  }
}