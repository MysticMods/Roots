package epicsquid.roots.network;

import epicsquid.roots.Roots;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class MessageHandler<T extends IMessage> implements IMessageHandler<T, IMessage> {
  protected static boolean DEBUG = true;

  @Override
  public abstract IMessage onMessage(final T message, final MessageContext ctx);

  protected void handleMessageInternal(final T message, final MessageContext ctx) {
    if (DEBUG) {
      try {
        handleMessage(message, ctx);
      } catch (Throwable t) {
        Roots.logger.info("Caught an exception handling message of type " + message.getClass().toGenericString() + ": ", t);
        throw t;
      }
    } else {
      handleMessage(message, ctx);
    }
  }

  protected abstract void handleMessage(final T message, final MessageContext ctx);
}
