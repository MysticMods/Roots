package mysticmods.roots.client.gui.screen.fake;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.client.ModifierTab;
import mysticmods.roots.api.client.ModifierWidget;
import mysticmods.roots.api.datacomponent.SpellSlot;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.modifier.IModifierNode;
import mysticmods.roots.api.modifier.ModifierTree;
import mysticmods.roots.api.modifier.ModifierTrees;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.KeyBindings;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.client.gui.screen.RootsScreen;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.server.ServerboundToggleSpellModifierPacket;
import mysticmods.roots.network.server.debug.ServerboundDebugScreenTick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;

public class SpellModifierScreen extends RootsScreen {
  private ModifierTab<Spell, SpellModifier> tab = null;
  private final InteractionHand hand;
  private final int inventorySlot, spellSlot;
  @Nullable
  private final StaffScreen parent;

  public SpellModifierScreen(@Nullable StaffScreen parent, InteractionHand hand, int inventorySlot, int spellSlot) {
    super(CommonComponents.EMPTY);
    this.parent = parent;
    this.hand = hand;
    this.inventorySlot = inventorySlot;
    this.spellSlot = spellSlot;
    this.width = 256;
    this.height = 192;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == KeyBindings.OPEN_POUCH.getKey().getValue()) {
      PacketDistributor.sendToServer(new ServerboundDebugScreenTick(hand, inventorySlot));
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
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
  private ModifierTree<Spell, SpellModifier>.Instance getInstance() {
    SpellStorage storage = getStorage();
    if (storage == null) {
      return null;
    }
    SpellSlot data = storage.getSpell(spellSlot);
    if (data == null) {
      return null;
    }
    var spell = data.getSpell();
    var tree = ModifierTrees.getSpell(spell);
    if (Minecraft.getInstance().player == null) {
      return null;
    }
    var granted = Minecraft.getInstance().player.getData(ModAttachments.GRANT_STORAGE).getSpellModifiers();
    var typeKey = spell.builtInRegistryHolder().getKey();
    RootsRegistries.SPELL_MODIFIERS.forEach(o -> {
      if (!o.is(RootsTags.SpellModifiers.REQUIRES_UNLOCK) && !o.is(RootsTags.SpellModifiers.RESTRICTED) && o.isFor(typeKey)) {
        granted.add(o);
      }
    });
    return tree.instance(data.getEnabledModifiers(), granted);
  }

  @Override
  protected void init() {
    super.init();
    updateTab();
  }

  public void updateTab() {
    var instance = getInstance();
    if (instance == null) {
      return;
    }

    this.tab = new ModifierTab<>(instance, SpellModifierWidget::new, leftPos, topPos);
    if (parent != null) {
      parent.validate();
    }
  }

  @Override
  public void drawForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    super.drawForeground(graphics, mouseX, mouseY, partialTicks);

    var widget = tab.root();
    widget.drawConnectivity(graphics, 0, 0, true);
    widget.drawConnectivity(graphics, 0, 0, false);
    widget.draw(graphics, 0, 0); //guiLeft, guiTop/*, mouseX, mouseY/ partialTicks*/);
  }

  @Override
  public void drawTooltip(GuiGraphics guiGraphics, int x, int y) {
    super.drawTooltip(guiGraphics, x, y);
    tab.drawTooltips(guiGraphics, x, y, width, height);
  }

  private static final ResourceLocation background = RootsAPI.rl("textures/gui/staff_gui_new.png");

  @Override
  public ResourceLocation getBackground() {
    // UNUSED
    return background;
  }

  public void drawBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, int uvW, int uvH, int maxW, int maxH) {
  }

  @Override
  public int getBackgroundWidth() {
    return 256;
  }

  @Override
  public int getBackgroundHeight() {
    return 192;
  }

  @Override
  public List<? extends GuiEventListener> children() {
    return tab.roots();
  }

  public void validate () {
    if (getStorage() == null) {
      this.onClose();
    } else if (this.parent != null) {
      this.parent.validate();
    }
  }

  public static void open(StaffScreen staffScreen, int slot) {
    RootsClientHooks.popAndStopUsingItem(new SpellModifierScreen(staffScreen, staffScreen.hand, staffScreen.inventorySlot, slot));
  }

  public class SpellModifierWidget extends ModifierWidget<Spell, SpellModifier> {
    public SpellModifierWidget(ModifierTab<Spell, SpellModifier> tab, IModifierNode<Spell, SpellModifier> node) {
      super(tab, node);
    }

    @Override
    protected void onClick(double mouseX, double mouseY, double button) {
      ServerboundToggleSpellModifierPacket packet = new ServerboundToggleSpellModifierPacket(hand, inventorySlot, spellSlot, tab.getTree()
          .getObject().value(), ModifierTree.getHolder(tab.getTree(), node.key()).value());
      PacketDistributor.sendToServer(packet);
    }
  }
}
