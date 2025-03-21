package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.util.MagnetismUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class MagnetismSpell extends TwoRadiusSpell {
  public MagnetismSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0x996117, 0xe62222);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.MAGNETISM_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.MAGNETISM_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.MAGNETISM_RADIUS_ZX;
  }

  @Override
  public void initialize(Holder<Spell> holder) {

  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    int pulled = MagnetismUtil.pull(pLevel, pPlayer.blockPosition(), radiusZX, radiusY, radiusZX);
    if (pulled == 0) {
      costs.noCharge();
      return 0;
    } else {
      costs.operations(pulled);
      return cooldown;
    }

  }

  @Override
  public int getMaximumOperations() {
    return 100;
  }
}
