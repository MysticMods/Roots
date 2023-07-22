package mysticmods.roots.client.gui.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.client.ItemCache;
import mysticmods.roots.client.RenderUtil;
import mysticmods.roots.client.gui.SpellSupplier;
import mysticmods.roots.client.gui.screen.StaffScreen;
import net.minecraft.client.gui.components.Button;

import java.util.function.Supplier;

public class StaffSpellButton extends TypedButton<SpellInstance, SpellSupplier<SpellInstance>, StaffScreen> {

  public StaffSpellButton(StaffScreen parentScreen, SpellSupplier<SpellInstance> spellGetter, int pX, int pY) {
    super(parentScreen, spellGetter, pX, pY, 16, 16, spellGetter.get().getStyledName(), parentScreen::onStaffSpellClick);
  }

  public SpellInstance getSpellInstance() {
    return spellSupplier.get();
  }
}
