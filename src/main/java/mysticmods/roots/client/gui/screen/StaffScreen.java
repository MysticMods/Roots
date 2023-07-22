package mysticmods.roots.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spell.SpellStorage;
import mysticmods.roots.client.gui.buttons.LibrarySpellButton;
import mysticmods.roots.client.gui.buttons.StaffSpellButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StaffScreen extends RootsScreen {
  private ItemStack stack;
  private final List<StaffSpellButton> staffSpellButtons = new ArrayList<>();
  protected StaffScreen(InteractionHand hand) {
    super(Component.literal(""));
    // TODO: Alternately suppress null possibility/constant conditions
    if (minecraft == null || minecraft.player == null) {
      throw new NullPointerException(minecraft == null ? "Minecraft is null?!?!" : "minecraft.player is null!!!");
    }
    this.stack = minecraft.player.getItemInHand(hand);
    if (this.stack.isEmpty()) {
      throw new IllegalStateException("Staff screen opened with empty item in hand " + hand);
    }
  }

  private SpellStorage cachedStorage = null;

  private SpellStorage getStorage () {
    if (cachedStorage == null) {
      cachedStorage = SpellStorage.fromItem(stack);
    }
    return cachedStorage;
  }

  @Override
  protected void init() {
    super.init();
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(0), 2, 33)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(1), 7, 9)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(2), 31, 4)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(3), 55, 9)));
    staffSpellButtons.add(addRenderableWidget(new StaffSpellButton(this, () -> getStorage().getSpell(4), 60, 33)));
  }

  public void onLibrarySpellClick (Button pButton) {
    LibrarySpellButton button = (LibrarySpellButton) pButton;
  }

  public void onStaffSpellClick (Button pButton) {
    StaffSpellButton button = (StaffSpellButton) pButton;
  }

  // TODO: "Slot" changes

  public static void open (InteractionHand hand) {
    Minecraft.getInstance().setScreen(new StaffScreen(hand));
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
