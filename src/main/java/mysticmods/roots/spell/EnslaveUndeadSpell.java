package mysticmods.roots.spell;

import mysticmods.roots.api.herb.ChargeType;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EnslaveUndeadSpell extends Spell {
  public EnslaveUndeadSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, ChargeType.INSTANCE, 0xcc6d0e, 0x9e1827);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.SUMMON_UNDEAD_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
  }

  @Override
  public int cast(Level Plevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    return cooldown;
  }
}
