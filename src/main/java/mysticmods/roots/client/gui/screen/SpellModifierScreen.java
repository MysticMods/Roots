package mysticmods.roots.client.gui.screen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.client.ModifierWidget;
import mysticmods.roots.api.datacomponent.SpellSlot;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.modifier.ModifierTree;
import mysticmods.roots.api.modifier.ModifierTrees;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.modifier.SpellModifierSet;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstanceData;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class SpellModifierScreen extends RootsScreen {
  private ModifierTab<Spell, SpellModifier> tab = null;
  private final InteractionHand hand;
  private final int inventorySlot, spellSlot;

  public SpellModifierScreen(InteractionHand hand, int inventorySlot, int spellSlot) {
    super(Component.empty());
    this.hand = hand;
    this.inventorySlot = inventorySlot;
    this.spellSlot = spellSlot;
    this.width = 256;
    this.height = 192;
  }

  @Nullable
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

  @Nullable
  private ModifierTree<Spell, SpellModifier>.Instance getInstance () {
    SpellStorage storage = getStorage();
    if (storage == null) {
      return null;
    }
    SpellSlot data = storage.getSpell(spellSlot);
    if (data == null) {
      return null;
    }
    var tree = ModifierTrees.getSpell(data.getSpell());
    return tree.instance(data.getEnabledModifiers());
  }

  @Override
  protected void init() {
    super.init();

    var instance = getInstance();
    if (instance == null) {
      return;
    }

    this.tab = new ModifierTab<>(instance);
  }

  @Override
  public void drawForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    super.drawForeground(graphics, mouseX, mouseY, partialTicks);

    for (ModifierWidget<Spell, SpellModifier> widget : tab.roots()) {
      widget.drawConnectivity(graphics, 0, 0, true);
      widget.drawConnectivity(graphics, 0, 0, false);
      widget.draw(graphics, 0, 0); //guiLeft, guiTop/*, mouseX, mouseY/ partialTicks*/);
    }
  }

  @Override
  public void drawTooltip(GuiGraphics guiGraphics, int x, int y) {
    super.drawTooltip(guiGraphics, x, y);

    for (ModifierWidget<Spell, SpellModifier> widget : tab.roots()) {
      if (widget.isMouseOver(0, 0, x, y)) {
        widget.drawHover(guiGraphics, x, y, 0, 0, 0);
      }
    }
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

  public static void open (StaffScreen staffScreen, int slot) {
    open(staffScreen.hand, staffScreen.inventorySlot, slot);
  }

  public static void open(@Nullable InteractionHand hand, int inventorySlot, int spellSlot) {
    RootsClientHooks.popAndStopUsingItem(new SpellModifierScreen(hand, inventorySlot, spellSlot));
  }
}
