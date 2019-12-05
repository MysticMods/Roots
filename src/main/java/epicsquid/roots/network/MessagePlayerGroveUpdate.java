/*package epicsquid.roots.network;

import java.util.UUID;

import epicsquid.roots.capability.grove.PlayerGroveCapabilityProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessagePlayerGroveUpdate implements IMessage {
  private UUID id = null;
  private CompoundNBT tag = new CompoundNBT();

  public MessagePlayerGroveUpdate() {

  }

  public MessagePlayerGroveUpdate(UUID id, CompoundNBT tag) {
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

  public static class MessageHolder implements IMessageHandler<MessagePlayerGroveUpdate, IMessage> {
    @OnlyIn(Dist.CLIENT)
    @Override
    public IMessage onMessage(final MessagePlayerGroveUpdate message, final MessageContext ctx) {
      if (message != null) {
        PlayerEntity player = Minecraft.getMinecraft().player;
        if (player != null) {
          if (player.hasCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null)) {
            player.getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null).setData(message.tag);
          }
        }
      }

      return null;
    }
  }
}*/
