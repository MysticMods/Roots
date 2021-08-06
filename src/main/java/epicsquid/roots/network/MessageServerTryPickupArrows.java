package epicsquid.roots.network;

import epicsquid.roots.item.ItemQuiver;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageServerTryPickupArrows implements IMessage {
  public MessageServerTryPickupArrows() {
  }

  @Override
  public void fromBytes(ByteBuf buf) {
  }

  @Override
  public void toBytes(ByteBuf buf) {
  }

  public static class MessageHolder extends ServerMessageHandler<MessageServerTryPickupArrows> {
    @Override
    protected void handleMessage(MessageServerTryPickupArrows message, MessageContext ctx) {
      EntityPlayerMP player = ctx.getServerHandler().player;
      ItemQuiver.tryPickupArrows(player);
    }
  }
}
