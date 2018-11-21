package epicsquid.roots.network.fx;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageLightDrifterSync implements IMessage {
  private UUID id = null;
  private boolean enable = false;
  private double x = 0, y = 0, z = 0;
  private int mode = 0;

  public MessageLightDrifterSync() {
    super();
  }

  public MessageLightDrifterSync(UUID id, double x, double y, double z, boolean enable, int mode) {
    super();
    this.id = id;
    this.enable = enable;
    this.x = x;
    this.y = y;
    this.z = z;
    this.mode = mode;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    x = buf.readDouble();
    y = buf.readDouble();
    z = buf.readDouble();
    enable = buf.readBoolean();
    id = new UUID(buf.readLong(), buf.readLong());
    mode = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(x);
    buf.writeDouble(y);
    buf.writeDouble(z);
    buf.writeBoolean(enable);
    buf.writeLong(id.getMostSignificantBits());
    buf.writeLong(id.getLeastSignificantBits());
    buf.writeInt(mode);
  }

  public static float getColorCycle(float ticks) {
    return (MathHelper.sin((float) Math.toRadians(ticks)) + 1.0f) / 2.0f;
  }

  public static class MessageHolder implements IMessageHandler<MessageLightDrifterSync, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageLightDrifterSync message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      EntityPlayer player = world.getPlayerEntityByUUID(message.id);
      if (player != null) {
        player.capabilities.disableDamage = message.enable;
        player.capabilities.allowFlying = message.enable;
        player.noClip = message.enable;
        player.capabilities.isFlying = message.enable;
        player.setGameType(GameType.getByID(message.mode));
        player.setPositionAndUpdate(message.x, message.y, message.z);
      }
      return null;
    }
  }

}