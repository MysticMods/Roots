package epicsquid.roots.modifiers;

import epicsquid.roots.client.gui.GuiImposer;
import epicsquid.roots.container.ContainerImposer;
import epicsquid.roots.container.slots.SlotImposerSpellInfo;
import epicsquid.roots.spell.info.StaffSpellInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModifierContext {
  @SuppressWarnings("ConstantConditions")
  public static StaffSpellInfo getHoveredSpellInfo() {
    Minecraft mc = Minecraft.getMinecraft();
    if (mc == null || mc.player == null) {
      return StaffSpellInfo.EMPTY;
    }

    PlayerEntity player = mc.player;
    if (!(player.openContainer instanceof ContainerImposer)) {
      return StaffSpellInfo.EMPTY;
    }

    if (!(mc.currentScreen instanceof GuiImposer)) {
      return StaffSpellInfo.EMPTY;
    }

    ContainerImposer container = (ContainerImposer) player.openContainer;
    if (!container.isSelectSpell()) {
      return StaffSpellInfo.EMPTY;
    }

    GuiImposer screen = (GuiImposer) mc.currentScreen;

    Slot slot = screen.getSlotUnderMouse();

    if (!(slot instanceof SlotImposerSpellInfo)) {
      return StaffSpellInfo.EMPTY;
    }

    SlotImposerSpellInfo slot2 = (SlotImposerSpellInfo) slot;
    StaffSpellInfo info = slot2.getInfo();
    if (info == null) {
      return StaffSpellInfo.EMPTY;
    }
    return info;
  }
}
