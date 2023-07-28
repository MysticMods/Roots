package mysticmods.roots.client.gui.screen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.capability.GrantCapability;
import mysticmods.roots.api.spell.LibrarySpell;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellStorage;
import mysticmods.roots.client.gui.buttons.LibrarySpellButton;
import mysticmods.roots.client.gui.buttons.StaffSpellButton;
import mysticmods.roots.network.Networking;
import mysticmods.roots.network.server.ServerBoundLibraryToStaffPacket;
import mysticmods.roots.network.server.ServerBoundSwapStaffSlotsPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StaffScreen extends RootsScreen {
  private final InteractionHand hand;
  private ItemStack stack;
  private final List<StaffSpellButton> staffSpellButtons = new ArrayList<>();
  private final List<LibrarySpellButton> librarySpellButtons = new ArrayList<>();
  private int selectedStaff = -1;
  private int selectedLibrary = -1;

  protected StaffScreen(InteractionHand hand) {
    super(Component.translatable("roots.gui.spell_library"));
    this.hand = hand;
    this.width = 256;
    this.height = 192;
  }

  private SpellStorage cachedStorage = null;

  private SpellStorage getStorage() {
    if (cachedStorage == null) {
      cachedStorage = SpellStorage.getOrCreate(stack);
    }
    return cachedStorage;
  }

  @Override
  protected void init() {
    super.init();
    this.stack = minecraft.player.getItemInHand(hand);
    if (this.stack.isEmpty()) {
      throw new IllegalStateException("Staff screen opened with empty item in hand " + hand);
    }
    int index = 0;
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(0), index++, guiLeft + 2, guiTop + 33)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(1), index++, guiLeft + 7, guiTop + 9)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(2), index++, guiLeft + 31, guiTop + 4)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(3), index++, guiLeft + 55, guiTop + 9)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(4), index++, guiLeft + 60, guiTop + 33)));

    getMinecraft().player.getCapability(Capabilities.GRANT_CAPABILITY).resolve().ifPresentOrElse(this::createLibraryButtons, () -> {
      RootsAPI.LOG.error("Grant capability isn't present!");
    });
  }

  private void createLibraryButtons(GrantCapability grants) {
    int index = 0;
    int offsetX = 98;
    int offsetY = 15;

    List<LibrarySpell> spellInfo = grants.getLibrarySpells();

    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 8; x++) {
        if (index < spellInfo.size()) {
          librarySpellButtons.add(addRenderableWidget(new LibrarySpellButton(this, spellInfo.get(index)::spell, index++, guiLeft + offsetX + x * 18, guiTop + offsetY + y * 18, !spellInfo.get(index).granted())));
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
        RootsAPI.LOG.info("Selected library spell " + selectedLibrary);
      } else {
        // Adding a new spell from the library
        // Code duplication?
        RootsAPI.LOG.info("Tried to insert library spell " + selectedLibrary + " into spell slot " + selectedStaff);
        ServerBoundLibraryToStaffPacket packet = new ServerBoundLibraryToStaffPacket(hand, selectedStaff, lButton.getSpell());
        Networking.sendToServer(packet);
        selectedLibrary = -1;
        selectedStaff = -1;
      }
    } else if (pButton instanceof StaffSpellButton sButton) {
      if (selectedStaff == sButton.getId()) {
        selectedStaff = -1;
        RootsAPI.LOG.info("Unselected staff spell " + selectedStaff);
      } else if (selectedStaff != -1) {
        // Swapping slots
        RootsAPI.LOG.info("Swapped staff slots " + selectedStaff + " and " + sButton.getId());
        ServerBoundSwapStaffSlotsPacket packet = new ServerBoundSwapStaffSlotsPacket(hand, selectedStaff, sButton.getId());
        Networking.sendToServer(packet);
        selectedStaff = -1;
      } else if (selectedLibrary != -1) {
        // Adding a new spell from the library
        // Code duplication?
        RootsAPI.LOG.info("Tried to insert library spell " + selectedLibrary + " into spell slot " + sButton.getId());
        LibrarySpellButton lButton = getSpellButton(selectedLibrary);
        if (lButton == null) {
          return;
        }
        ServerBoundLibraryToStaffPacket packet = new ServerBoundLibraryToStaffPacket(hand, sButton.getId(), lButton.getSpell());
        Networking.sendToServer(packet);
        selectedLibrary = -1;
        selectedStaff = -1;
      } else {
        selectedStaff = sButton.getId();
      }
    }
  }

  public boolean isSelected(Button pButton) {
    if (pButton instanceof LibrarySpellButton lButton) {
      return lButton.getId() == selectedLibrary;
    } else if (pButton instanceof StaffSpellButton sButton) {
      return sButton.getId() == selectedStaff;
    }
    return false;
  }

  // TODO: "Slot" changes

  public static void open(InteractionHand hand) {
    StaffScreen newScreen = new StaffScreen(hand);
    Minecraft.getInstance().setScreen(newScreen);
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

  public ItemStack getStack() {
    return stack;
  }

  public void setStack(ItemStack stack) {
    this.stack = stack;
    cachedStorage = null;
    RootsAPI.LOG.info("Updated stack");
  }
}
