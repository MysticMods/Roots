package mysticmods.roots.client.gui.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.client.gui.screen.fake.StaffScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class StaffSpellButton extends SpellButton<ISpellInstance, StaffScreen> {

  private final Supplier<ItemStack> itemGetter;

  public StaffSpellButton(StaffScreen parentScreen, @NotNull Supplier<ISpellInstance> spellGetter, @NotNull Supplier<ItemStack> itemGetter, int id, int pX, int pY) {
    super(parentScreen, spellGetter, id, pX, pY, 16, 16, parentScreen::buttonClicked);
    this.itemGetter = itemGetter;
  }

  private static final ResourceLocation background = RootsAPI.rl("textures/gui/staff_spell_slot.png");
  private static final ResourceLocation highlight = RootsAPI.rl("textures/gui/staff_spell_slot_highlight.png");

  @Override
  public void setupTooltip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
    if (itemGetter.get() == null) {
      return;
    }
    parentScreen.fillTooltip(itemGetter.get());
  }

  @Override
  public void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
    int x1 = getX() - 2;
    int y1 = getY() - 2;
    graphics.blit(background, x1, y1, 0, 0, 20, 20, 20, 20);
    super.renderWidget(graphics, pMouseX, pMouseY, pPartialTick);
    if (parentScreen.isSelected(this) && visible) {
      int x = getX() - 1;
      int y = getY() - 1;
      graphics.blit(highlight, x, y, 0, 0, 18, 18, 18, 18);
    }
  }
}
