package epicsquid.roots.spell;

import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class FakeSpellRunicDust extends SpellBase {
  public FakeSpellRunicDust() {
    super("fake_spell", TextFormatting.DARK_BLUE, 42 / 255.f, 69 / 255.f, 53 / 255.f, 68 / 255.f, 78 / 255.f, 88 / 255.f);
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
    return false;
  }
}
