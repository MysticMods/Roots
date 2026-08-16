package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.spell.*;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.screen.DesaturateScreenFXPacket;
import mysticmods.roots.network.client.fx.HealFXPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class DesaturateSpell extends Spell {
  private float multiplier;

  public DesaturateSpell(Properties properties) {
    super(properties);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.DESATURATE_COOLDOWN;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.DESATURATE_MULTIPLIER);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.multiplier = properties.get(ModSpells.DESATURATE_MULTIPLIER);
  }

  @Override
  public SpellCastResult cast(Level Plevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    if (!pPlayer.isHurt()) {
      costs.noCharge();
      return SpellCastResult.nothing();
    }

    FoodData stats = pPlayer.getFoodData();
    final int originalFood = stats.getFoodLevel();
    int food = originalFood;
    if (food <= 1) {
      costs.noCharge();
      return SpellCastResult.nothing();
    }

    float originalHealth = pPlayer.getHealth();

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
      return SpellCastResult.nothing();
    }

    pPlayer.heal(healed);
    stats.setFoodLevel(food);
    stats.setSaturation(Math.min(stats.getExhaustionLevel(), food));
    PacketDistributor.sendToPlayer((ServerPlayer) pPlayer, new DesaturateScreenFXPacket(originalHealth, pPlayer.getHealth(), originalFood, food));
    PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new HealFXPacket(pPlayer.getId(), healed));
    return SpellCastResult.success(cooldown);
  }
}
