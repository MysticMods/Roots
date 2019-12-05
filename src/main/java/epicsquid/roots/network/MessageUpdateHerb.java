package epicsquid.roots.network;

import epicsquid.roots.api.Herb;
import epicsquid.roots.event.handlers.ClientTickHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.util.PowderInventoryUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageUpdateHerb implements IMessage {
  private Herb herb = null;

  public MessageUpdateHerb() {
  }

  public MessageUpdateHerb(Herb herb) {
    this.herb = herb;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.herb = HerbRegistry.getHerb(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeUTF8String(buf, herb.getRegistryName().toString());
  }

  public static class MessageHolder implements IMessageHandler<MessageUpdateHerb, IMessage> {
    @OnlyIn(Dist.CLIENT)
    @Override
    public IMessage onMessage(final MessageUpdateHerb message, final MessageContext ctx) {
      ClientTickHandler.addRunnable(() -> PowderInventoryUtil.resolveSlots(Minecraft.getMinecraft().player, message.herb));
      return null;
    }
  }
}