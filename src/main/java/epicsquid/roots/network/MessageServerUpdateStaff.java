package epicsquid.roots.network;

import epicsquid.roots.handler.SpellHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.SpellBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageServerUpdateStaff implements IMessage {
  private int opcode = -1;

  public MessageServerUpdateStaff() {
  }

  public MessageServerUpdateStaff(int opcode) {
    this.opcode = opcode;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.opcode = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.opcode);
  }

  public int getOpcode() {
    return opcode;
  }

  public static class MessageHolder implements IMessageHandler<MessageServerUpdateStaff, IMessage> {

    @Override
    public IMessage onMessage(MessageServerUpdateStaff message, MessageContext ctx) {
      FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(message, ctx));
      return null;
    }

    private void handleMessage(MessageServerUpdateStaff message, MessageContext ctx) {
      int opcode = message.getOpcode();
      EntityPlayerMP player = ctx.getServerHandler().player;
      ItemStack staff = null;
      for (ItemStack s : player.getHeldEquipment()) {
        if (s.getItem() == ModItems.staff) {
          staff = s;
          break;
        }
      }
      if (staff == null) return;

      SpellHandler handler = SpellHandler.fromStack(staff);
      if (opcode < 50) {
        int slot = opcode - 1;
        if (handler.getSpellInSlot(slot) != null) {
          handler.setSelectedSlot(slot);
        }
      } else if (opcode == 50) {
        handler.nextSlot();
      } else if (opcode == 60) {
        handler.previousSlot();
      } else {
        return;
      }

      SpellBase spell = handler.getSelectedSpell();
      player.sendMessage(new TextComponentTranslation("roots.info.staff.slot_and_spell", handler.getSelectedSlot() + 1, spell == null ? "none" : new TextComponentTranslation("roots.spell." + spell.getName() + ".name").setStyle(new Style().setColor(spell.getTextColor()).setBold(true))).setStyle(new Style().setColor(TextFormatting.GOLD)));
    }
  }
}
