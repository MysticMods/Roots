package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
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

import java.util.List;

public class NoSpell extends Spell {

  public NoSpell(ChatFormatting color, List<Cost> defaultCosts) {
    super(Type.INSTANT, ChatFormatting.BLACK, List.of(), 0x0c0c0c, 0x0c0c0c);
  }

  @Override
  public List<PropertyHolder<?>> getProperties() {
    return List.of();
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return null;
  }

  @Override
  protected void initializeProperties(Holder<Spell> holder) {
  }

  @Override
  public void initialize(Holder<Spell> holder) {
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    return 0;
  }
}
