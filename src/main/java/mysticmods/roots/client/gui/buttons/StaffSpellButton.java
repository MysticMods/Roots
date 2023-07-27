package mysticmods.roots.client.gui.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.client.ItemCache;
import mysticmods.roots.client.RenderUtil;
import mysticmods.roots.client.gui.SpellSupplier;
import mysticmods.roots.client.gui.screen.StaffScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class StaffSpellButton extends TypedButton<SpellInstance, SpellSupplier<SpellInstance>, StaffScreen> {

  public StaffSpellButton(StaffScreen parentScreen, SpellSupplier<SpellInstance> spellGetter, int id, int pX, int pY) {
    super(parentScreen, spellGetter, id, pX, pY, 16, 16, parentScreen::buttonClicked);
  }

  private static final ResourceLocation background = RootsAPI.rl("textures/gui/staff_spell_slot.png");
  private static final ResourceLocation highlight = RootsAPI.rl("textures/gui/staff_spell_slot_highlight.png");

  @Override
  public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
    StaffScreen.drawFromTexture(background, x - 2, y - 2, 0, 0, 20, 20, 20, 20, pPoseStack);
    super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    if (parentScreen.isSelected(this) && visible) {
      StaffScreen.drawFromTexture(highlight, x - 1, y - 1, 0, 0, 18, 18, 18, 18, pPoseStack);
    }
  }

  public SpellInstance getSpellInstance() {
    return spellSupplier.get();
  }
}
