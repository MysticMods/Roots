package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.util.MagnetismUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class MagnetismSpell extends TwoRadiusSpell {
  public MagnetismSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.MAGNETISM_COOLDOWN.get();
  }

  @Override
  public SpellProperty<Integer> getRadiusYProperty() {
    return ModSpells.MAGNETISM_RADIUS_Y.get();
  }

  @Override
  public SpellProperty<Integer> getRadiusZXProperty() {
    return ModSpells.MAGNETISM_RADIUS_ZX.get();
  }

  @Override
  public void initialize() {

  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    if (MagnetismUtil.pull(pLevel, pPlayer.blockPosition(), radiusZX, radiusY, radiusZX) == 0) {
      costs.noCharge();
    }
  }
}
