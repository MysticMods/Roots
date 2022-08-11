package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class FakeSpell extends SpellBase {
	public static FakeSpell INSTANCE = new FakeSpell();
	
	public FakeSpell() {
		super(new ResourceLocation(Roots.MODID, "fake_spell"), TextFormatting.DARK_BLUE, 42 / 255.f, 69 / 255.f, 53 / 255.f, 68 / 255.f, 78 / 255.f, 88 / 255.f);
	}
	
	@Override
	public boolean cast(EntityPlayer caster, StaffModifierInstanceList info, int ticks) {
		return false;
	}
	
	@Override
	public void doFinalise() {
	}
	
	@Override
	public void init() {
	}
}
