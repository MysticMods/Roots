package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.CastResult;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.HealFXPacket;
import mysticmods.roots.network.client.fx.screen.DesaturateScreenFXPacket;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
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
  public CastResult cast(Level Plevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    if (!pPlayer.isHurt()) {
      costs.noCharge();
      return CastResult.nothing();
    }

    FoodData stats = pPlayer.getFoodData();
    final int originalFood = stats.getFoodLevel();
    int food = originalFood;
    if (food <= 1) {
      costs.noCharge();
      return CastResult.nothing();
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
      return CastResult.nothing();
    }

    pPlayer.heal(healed);
    stats.setFoodLevel(food);
    stats.setSaturation(Math.min(stats.getExhaustionLevel(), food));
    PacketDistributor.sendToPlayer((ServerPlayer) pPlayer, new DesaturateScreenFXPacket(originalHealth, pPlayer.getHealth(), originalFood, food));
    PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new HealFXPacket(pPlayer.getId(), healed));
    return CastResult.success(cooldown);
  }

  @Override
  public Component[] createExtendedDescriptionComponents() {
    return new Component[]{
        Component.literal(String.format("%.1f", multiplier))
    };
  }

  // TODO: Modifiers
  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    return new Component[]{};
  }
}
