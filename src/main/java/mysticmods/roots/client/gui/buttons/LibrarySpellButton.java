package mysticmods.roots.client.gui.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.RenderUtil;
import mysticmods.roots.client.gui.SpellSupplier;
import mysticmods.roots.client.gui.screen.StaffScreen;
import net.minecraft.resources.ResourceLocation;

public class LibrarySpellButton extends TypedButton<Spell, SpellSupplier<Spell>, StaffScreen> {
  private final boolean transparent;
  public LibrarySpellButton(StaffScreen parentScreen, SpellSupplier<Spell> spellSupplier, int id, int pX, int pY, boolean transparent) {
    super(parentScreen, spellSupplier, id, pX, pY, 16, 16, parentScreen::buttonClicked);
    this.transparent = transparent;
  }

  @Override
  public boolean isTransparent() {
    return this.transparent;
  }

  public Spell getSpell() {
    return spellSupplier.get();
  }

  private static final ResourceLocation highlight = new ResourceLocation(RootsAPI.MODID, "textures/gui/library_spell_slot_highlight.png");

  @Override
  public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
    super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    if (parentScreen.isSelected(this) && visible) {
      StaffScreen.drawFromTexture(highlight, x - 1, y - 1, 0, 0, 18, 18, 18, 18, pPoseStack);
    }
  }
}
