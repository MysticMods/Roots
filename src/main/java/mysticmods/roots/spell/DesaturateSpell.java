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
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class DesaturateSpell extends Spell {
  private float multiplier;

  public DesaturateSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0xb8e82a, 0xbe20a8);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.DESATURATE_COOLDOWN.get();
  }

  @Override
  public void initialize() {
    this.multiplier = ModSpells.DESATURATE_MULTIPLIER.get().getValue();
  }

  @Override
  public void cast(Level Plevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    if (!pPlayer.isHurt()) {
      costs.noCharge();
      return;
    }

    FoodData stats = pPlayer.getFoodData();
    int food = stats.getFoodLevel();
    if (food <= 1) {
      costs.noCharge();
      return;
    }

    float missing = pPlayer.getMaxHealth() - pPlayer.getHealth();
    float healed = 0;
    int required = (int) Math.ceil(missing);

    for (int i = 0; i <= required; i++) {
      if (food > 1) {
        food--;
        healed += multiplier;
      } else {
        break;
      }
    }

    if (healed == 0) {
      costs.noCharge();
      return;
    }

    pPlayer.heal(healed);
    stats.setFoodLevel(food);
    stats.setSaturation(Math.min(stats.saturationLevel, food));
  }
}
