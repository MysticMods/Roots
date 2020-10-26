package epicsquid.roots.network;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class ServerMessageHandler<T extends IMessage> extends MessageHandler<T> {
  @Override
  public IMessage onMessage(T message, MessageContext ctx) {
    FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessageInternal(message, ctx));
    return null;
  }
}
