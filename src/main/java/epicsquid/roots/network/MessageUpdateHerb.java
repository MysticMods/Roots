package epicsquid.roots.network;

import epicsquid.roots.api.Herb;
import epicsquid.roots.client.hud.RenderHerbHUD;
import epicsquid.roots.init.HerbRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class MessageUpdateHerb implements IMessage {
  private Herb herb = null;
  private double amount = 0;

  public MessageUpdateHerb() {
  }

  public MessageUpdateHerb(Herb herb, double amount) {
    this.herb = herb;
    this.amount = amount;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.herb = HerbRegistry.getHerb(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
    this.amount = buf.readDouble();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeUTF8String(buf, herb.getRegistryName().toString());
    buf.writeDouble(amount);
  }

  public static class MessageHolder extends ClientMessageHandler<MessageUpdateHerb> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessageUpdateHerb message, final MessageContext ctx) {
      Minecraft mc = Minecraft.getMinecraft();
      //noinspection ConstantConditions
      if (mc == null || mc.player == null) {
        return;
      }
      RenderHerbHUD.INSTANCE.resolveSlots(mc.player, message.herb, message.amount);
    }
  }
}