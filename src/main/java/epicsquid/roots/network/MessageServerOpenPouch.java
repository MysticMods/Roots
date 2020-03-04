package epicsquid.roots.network;

import epicsquid.roots.GuiHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.item.ItemPouch;
import epicsquid.roots.util.ServerHerbUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageServerOpenPouch implements IMessage {
  public MessageServerOpenPouch() {
  }

  @Override
  public void fromBytes(ByteBuf buf) {
  }

  @Override
  public void toBytes(ByteBuf buf) {
  }

  public static class MessageHolder implements IMessageHandler<MessageServerOpenPouch, IMessage> {

    @Override
    public IMessage onMessage(MessageServerOpenPouch message, MessageContext ctx) {
      FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(message, ctx));

      return null;
    }

    private void handleMessage(MessageServerOpenPouch message, MessageContext ctx) {
      EntityPlayerMP player = ctx.getServerHandler().player;
      ItemStack pouch = ServerHerbUtil.getFirstPouch(player);
      if (!pouch.isEmpty()) {
        player.openGui(Roots.getInstance(), GuiHandler.POUCH_ID, player.world, 0, 0, 0);
      }
    }
  }
}
