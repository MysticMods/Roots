package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class AcidCloudSpell extends TwoRadiusSpell {
  private float damage;
  private int count;

  public AcidCloudSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.CONTINUOUS, color, costs);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.ACID_CLOUD_COOLDOWN.get();
  }

  @Override
  public SpellProperty<Integer> getRadiusYProperty() {
    return ModSpells.ACID_CLOUD_RADIUS_Y.get();
  }

  @Override
  public SpellProperty<Integer> getRadiusZXProperty() {
    return ModSpells.ACID_CLOUD_RADIUS_ZX.get();
  }

  @Override
  public void initialize() {
  }

  @Override
  public void cast(Level Plevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {

  }
}
