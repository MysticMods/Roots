package mysticmods.roots.client.gui.screen;

import com.mojang.blaze3d.platform.InputConstants;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.GrantStorage;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.LibrarySpell;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.client.gui.buttons.LibrarySpellButton;
import mysticmods.roots.client.gui.buttons.StaffSpellButton;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.server.ServerboundClearStaffSlotPacket;
import mysticmods.roots.network.server.ServerboundSetSpellPacket;
import mysticmods.roots.network.server.ServerboundSwapSpellsPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class StaffScreen extends RootsScreen {
  protected final InteractionHand hand;
  protected final int inventorySlot;
  private final List<StaffSpellButton> staffSpellButtons = new ArrayList<>();
  private final List<LibrarySpellButton> librarySpellButtons = new ArrayList<>();
  protected int selectedStaff = -1;
  protected int selectedLibrary = -1;

  protected StaffScreen(InteractionHand hand, int inventorySlot) {
    super(Component.translatable("roots.gui.spell_library"));
    this.hand = hand;
    this.inventorySlot = inventorySlot;
    this.width = 256;
    this.height = 192;
  }

  private SpellStorage getStorage() {
    Player player = getMinecraft().player;
    if (player == null) {
      return null;
    }
    ItemStack stack = hand == null ? player.getInventory().getItem(inventorySlot) : player.getItemInHand(hand);
    if (stack.isEmpty() || !stack.has(ModAttachments.SPELL_STORAGE)) {
      return null;
    }

    return stack.get(ModAttachments.SPELL_STORAGE);
  }

  private Supplier<ISpellInstance> staffSlot(final int index) {
    return () -> getStorage() == null ? null : getStorage().getSpell(index);
  }

  private Supplier<Spell> librarySlot(final int index) {
    return () -> {
      Player player = getMinecraft().player;
      if (player == null) {
        return null;
      }
      GrantStorage grants = player.getData(ModAttachments.GRANT_STORAGE);
      List<LibrarySpell> spellInfo = grants.getLibrarySpells();
      if (index < 0 || index >= spellInfo.size()) {
        return null;
      }
      return spellInfo.get(index).spell().value();
    };
  }

  @Override
  protected void init() {
    super.init();

    int index = 0;
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, staffSlot(0), index++, leftPos + 2, topPos + 33)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, staffSlot(1), index++, leftPos + 7, topPos + 9)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, staffSlot(2), index++, leftPos + 31, topPos + 4)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, staffSlot(3), index++, leftPos + 55, topPos + 9)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, staffSlot(4), index, leftPos + 60, topPos + 33)));

    if (getMinecraft().player != null) {
      createLibraryButtons(getMinecraft().player.getData(ModAttachments.GRANT_STORAGE));
    }
  }

  private void createLibraryButtons(GrantStorage grants) {
    int index = 0;
    int offsetX = 98;
    int offsetY = 15;

    List<LibrarySpell> spellInfo = grants.getLibrarySpells();

    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 8; x++) {
        if (index < spellInfo.size()) {
          librarySpellButtons.add(addRenderableWidget(new LibrarySpellButton(this, librarySlot(index), index, leftPos + offsetX + x * 18, topPos + offsetY + y * 18, !spellInfo.get(index)
              .granted())));
          index++;
        }
      }
    }
  }

  private LibrarySpellButton getSpellButton(int index) {
    if (index < 0 || index >= librarySpellButtons.size()) {
      return null;
    }
    return librarySpellButtons.get(index);
  }

  public void buttonClicked(Button pButton) {
    if (getStorage() == null) {
      RootsAPI.LOG.error("Staff screen opened with empty item in hand {}", hand);
      return;
    }
    if (pButton instanceof LibrarySpellButton lButton) {
      if (lButton.isTransparent()) {
        return;
      }

      if (selectedStaff == -1) {
        if (selectedLibrary == lButton.getId()) {
          selectedLibrary = -1;
        } else {
          selectedLibrary = lButton.getId();
        }
        RootsAPI.LOG.info("Selected library spell {}", selectedLibrary);
      } else {
        RootsAPI.LOG.info("Tried to insert library spell {} into spell slot {}", selectedLibrary, selectedStaff);
        Spell newSpell = lButton.getSpell();
        PacketDistributor.sendToServer(new ServerboundSetSpellPacket(hand, inventorySlot, selectedStaff, newSpell));
        selectedLibrary = -1;
        selectedStaff = -1;
      }
    } else if (pButton instanceof StaffSpellButton sButton) {
      if (selectedStaff == sButton.getId()) {
        selectedStaff = -1;
        RootsAPI.LOG.info("Unselected staff spell {}", selectedStaff);
      } else if (selectedStaff != -1) {
        // Swapping slots
        RootsAPI.LOG.info("Swapped staff slots {} and {}", selectedStaff, sButton.getId());
        PacketDistributor.sendToServer(new ServerboundSwapSpellsPacket(hand, inventorySlot, selectedStaff, sButton.getId()));
        selectedStaff = -1;
      } else if (selectedLibrary != -1) {
        RootsAPI.LOG.info("Tried to insert library spell {} into spell slot {}", selectedLibrary, sButton.getId());
        LibrarySpellButton lButton = getSpellButton(selectedLibrary);
        if (lButton == null) {
          return;
        }
        PacketDistributor.sendToServer(new ServerboundSetSpellPacket(hand, inventorySlot, sButton.getId(), lButton.getSpell()));
        selectedLibrary = -1;
        selectedStaff = -1;
      } else {
        selectedStaff = sButton.getId();
      }
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    // TODO: Keybind this
    if (keyCode == InputConstants.KEY_DELETE) {
      StaffSpellButton toDeleteButton = null;
      for (StaffSpellButton button : staffSpellButtons) {
        if (isMouseInRelativeRange(lastMouseX, lastMouseY, button.getX(), button.getY(), button.getWidth(), button.getHeight())) {
          toDeleteButton = button;
          break;
        }
      }
      if (toDeleteButton != null) {
        selectedLibrary = -1;
        selectedStaff = -1;
        int slot = toDeleteButton.getId();
        PacketDistributor.sendToServer(new ServerboundClearStaffSlotPacket(hand, inventorySlot, slot));
        return true;
      }
    }
    if (keyCode == InputConstants.KEY_INSERT) {
      StaffSpellButton toInsertButton = null;
      for (StaffSpellButton button : staffSpellButtons) {
        if (isMouseInRelativeRange(lastMouseX, lastMouseY, button.getX(), button.getY(), button.getWidth(), button.getHeight())) {
          toInsertButton = button;
          break;
        }
      }
      if (toInsertButton != null) {
        SpellModifierScreen.open(this, toInsertButton.getId());
        return true;
      }
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  public boolean isSelected(Button pButton) {
    if (pButton instanceof LibrarySpellButton lButton) {
      return lButton.getId() == selectedLibrary;
    } else if (pButton instanceof StaffSpellButton sButton) {
      return sButton.getId() == selectedStaff;
    }
    return false;
  }

  public static void open(@Nullable InteractionHand hand, int inventorySlot) {
    RootsClientHooks.stopUsingItem(new StaffScreen(hand, inventorySlot));
  }

  private static final ResourceLocation background = RootsAPI.rl("textures/gui/staff_gui_new.png");

  @Override
  public ResourceLocation getBackground() {
    return background;
  }

  @Override
  public int getBackgroundWidth() {
    return 256;
  }

  @Override
  public int getBackgroundHeight() {
    return 192;
  }
}
