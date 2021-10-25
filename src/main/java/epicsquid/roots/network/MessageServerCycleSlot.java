package epicsquid.roots.network;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemStaff;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageServerCycleSlot implements IMessage {
  public MessageServerCycleSlot() {
  }

  @Override
  public void fromBytes(ByteBuf buf) {
  }

  @Override
  public void toBytes(ByteBuf buf) {
  }

  public static class MessageHolder extends ServerMessageHandler<MessageServerCycleSlot> {
    @Override
    protected void handleMessage(MessageServerCycleSlot message, MessageContext ctx) {
      ServerPlayerEntity player = ctx.getServerHandler().player;
      ItemStack stack = player.getHeldItemMainhand();
      ItemStack staff = ItemStack.EMPTY;
      if (stack.getItem() == ModItems.staff) {
        staff = stack;
      } else {
        stack = player.getHeldItemOffhand();
        if (stack.getItem() == ModItems.staff) {
          staff = stack;
        }
      }
      if (!staff.isEmpty()) {
        StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
        if (capability != null) {
          ((ItemStaff) ModItems.staff).nextSlot(player.world, player, stack, capability);
        }
      } else {
        player.sendStatusMessage(new TranslationTextComponent("roots.message.no_staff"), true);
      }
    }
  }
}
