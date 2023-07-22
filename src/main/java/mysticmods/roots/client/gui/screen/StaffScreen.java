package mysticmods.roots.client.gui.screen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.capability.GrantCapability;
import mysticmods.roots.api.spell.SpellStorage;
import mysticmods.roots.client.gui.buttons.LibrarySpellButton;
import mysticmods.roots.client.gui.buttons.StaffSpellButton;
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

  protected StaffScreen(InteractionHand hand) {
    super(Component.literal(""));
    this.hand = hand;
    // TODO: Alternately suppress null possibility/constant conditions
  }

  private SpellStorage cachedStorage = null;

  private SpellStorage getStorage() {
    if (cachedStorage == null) {
      cachedStorage = SpellStorage.fromItem(stack);
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
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(0), guiLeft + 2, guiTop + 33)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(1), guiLeft + 7, guiTop + 9)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(2), guiLeft + 31, guiTop + 4)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(3), guiLeft + 55, guiTop + 9)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(4), guiLeft + 60, guiTop + 33)));

    getMinecraft().player.getCapability(Capabilities.GRANT_CAPABILITY).resolve().ifPresentOrElse(this::createLibraryButtons, () -> {
      RootsAPI.LOG.error("Grant capability isn't present!");
    });
  }

  private void createLibraryButtons (GrantCapability grants) {
  }

  public void onLibrarySpellClick(Button pButton) {
    LibrarySpellButton button = (LibrarySpellButton) pButton;
  }

  public void onStaffSpellClick(Button pButton) {
    StaffSpellButton button = (StaffSpellButton) pButton;
  }

  // TODO: "Slot" changes

  public static void open(InteractionHand hand) {
    StaffScreen newScreen = new StaffScreen(hand);
    Minecraft.getInstance().setScreen(newScreen);
  }

  private static final ResourceLocation background = new ResourceLocation(RootsAPI.MODID, "textures/gui/staff_gui.png");

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
    return 256;
  }

  public ItemStack getStack() {
    return stack;
  }

  public void setStack(ItemStack stack) {
    this.stack = stack;
    cachedStorage = null;
  }
}
