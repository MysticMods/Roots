package epicsquid.roots.network;

import java.util.UUID;

import epicsquid.roots.capability.playerdata.PlayerDataCapabilityProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessagePlayerDataUpdate implements IMessage {
  private UUID id = null;
  private NBTTagCompound tag = new NBTTagCompound();

  public MessagePlayerDataUpdate() {
    //
  }

  public MessagePlayerDataUpdate(UUID id, NBTTagCompound tag) {
    this.tag = tag;
    this.id = id;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    id = new UUID(buf.readLong(), buf.readLong());
    tag = ByteBufUtils.readTag(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeLong(id.getMostSignificantBits());
    buf.writeLong(id.getLeastSignificantBits());
    ByteBufUtils.writeTag(buf, tag);
  }

  public static class MessageHolder implements IMessageHandler<MessagePlayerDataUpdate, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessagePlayerDataUpdate message, final MessageContext ctx) {
      if (message != null) {
        World w = Minecraft.getMinecraft().world;
        if (w != null) {
          if (w.getPlayerEntityByUUID(message.id) != null) {
            EntityPlayer player = w.getPlayerEntityByUUID(message.id);
            if (player != null && player.hasCapability(PlayerDataCapabilityProvider.PLAYER_DATA_CAPABILITY, null)) {
              player.getCapability(PlayerDataCapabilityProvider.PLAYER_DATA_CAPABILITY, null).setData(message.tag);
            }
          }
        }
      }
      return null;
    }
  }
}