package epicsquid.roots.network;

import epicsquid.roots.util.QuiverInventoryUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageServerOpenQuiver implements IMessage {
  public MessageServerOpenQuiver() {
  }

  @Override
  public void fromBytes(ByteBuf buf) {
  }

  @Override
  public void toBytes(ByteBuf buf) {
  }

  public static class MessageHolder implements IMessageHandler<MessageServerOpenQuiver, IMessage> {

    @Override
    public IMessage onMessage(MessageServerOpenQuiver message, MessageContext ctx) {
      FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(message, ctx));

      return null;
    }

    private void handleMessage(MessageServerOpenQuiver message, MessageContext ctx) {
      ServerPlayerEntity player = ctx.getServerHandler().player;
      ItemStack quiver = QuiverInventoryUtil.getQuiver(player);
      if (!quiver.isEmpty()) {
        quiver.getItem().onItemRightClick(player.world, player, Hand.MAIN_HAND);
      }
    }
  }
}
