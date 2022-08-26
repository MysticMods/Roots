package epicsquid.roots.network;

import epicsquid.roots.container.IInvalidatingContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
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

  public static class MessageHolder extends ClientMessageHandler<MessageInvalidateContainer> {
    @SideOnly(Side.CLIENT)
    @Override
    protected void handleMessage(final MessageInvalidateContainer message, final MessageContext ctx) {
      EntityPlayer player = Minecraft.getMinecraft().player;
      if (player.openContainer instanceof IInvalidatingContainer) {
        ((IInvalidatingContainer) player.openContainer).invalidate();
      }
    }
  }
}