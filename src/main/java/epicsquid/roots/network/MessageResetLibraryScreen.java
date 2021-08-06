package epicsquid.roots.network;

import epicsquid.roots.container.ContainerLibrary;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageResetLibraryScreen implements IMessage {
  public MessageResetLibraryScreen() {
  }

  @Override
  public void fromBytes(ByteBuf buf) {
  }

  @Override
  public void toBytes(ByteBuf buf) {
  }

  public static class MessageHolder extends ServerMessageHandler<MessageResetLibraryScreen> {
    @Override
    protected void handleMessage(MessageResetLibraryScreen message, MessageContext ctx) {
      Container container = ctx.getServerHandler().player.openContainer;
      if (container instanceof ContainerLibrary) {
        ((ContainerLibrary) container).setSelectSpell();
        ((ContainerLibrary) container).reset();
      }
    }
  }
}
