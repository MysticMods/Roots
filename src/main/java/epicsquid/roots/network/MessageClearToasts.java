package epicsquid.roots.network;

import epicsquid.roots.event.handlers.ClientTickHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageClearToasts implements IMessage {
  public MessageClearToasts() {
  }

  @Override
  public void fromBytes(ByteBuf buf) {
  }

  @Override
  public void toBytes(ByteBuf buf) {
  }

  public static class MessageHolder implements IMessageHandler<MessageClearToasts, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageClearToasts message, final MessageContext ctx) {
      ClientTickHandler.addRunnable(() -> Minecraft.getMinecraft().getToastGui().clear(), 20);
      return null;
    }
  }
}