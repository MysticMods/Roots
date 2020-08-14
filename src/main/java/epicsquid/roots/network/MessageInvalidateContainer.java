package epicsquid.roots.network;

import epicsquid.roots.container.IInvalidatingContainer;
import epicsquid.roots.event.ClientTickHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageInvalidateContainer implements IMessage {
  public MessageInvalidateContainer() {
  }

  @Override
  public void fromBytes(ByteBuf buf) {
  }

  @Override
  public void toBytes(ByteBuf buf) {
  }

  public static class MessageHolder implements IMessageHandler<MessageInvalidateContainer, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageInvalidateContainer message, final MessageContext ctx) {
      ClientTickHandler.addRunnable(() -> {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player.openContainer instanceof IInvalidatingContainer) {
          ((IInvalidatingContainer) player.openContainer).invalidate();
        }
      });
      return null;
    }
  }
}