package epicsquid.roots.network;

import epicsquid.roots.item.QuiverItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
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

  public static class MessageHolder implements IMessageHandler<MessageServerTryPickupArrows, IMessage> {

    @Override
    public IMessage onMessage(MessageServerTryPickupArrows message, MessageContext ctx) {
      FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(message, ctx));
      return null;
    }

    private void handleMessage(MessageServerTryPickupArrows message, MessageContext ctx) {
      ServerPlayerEntity player = ctx.getServerHandler().player;
      QuiverItem.tryPickupArrows(player);
    }
  }
}
