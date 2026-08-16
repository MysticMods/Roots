package mysticmods.roots.spell;

import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.spell.*;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.CastMagnetismFXPacket;
import mysticmods.roots.util.MagnetismUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class MagnetismSpell extends TwoRadiusSpell {
  public MagnetismSpell(Properties properties) {
    super(properties);
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
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    int pulled = MagnetismUtil.pull(pLevel, pPlayer.blockPosition(), radiusZX, radiusY, radiusZX);
    if (pulled == 0) {
      costs.noCharge();
      return SpellCastResult.nothing();
    } else {
      PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new CastMagnetismFXPacket(pPlayer.getId()));
      costs.operations(pulled);
      return SpellCastResult.success(pulled, cooldown * pulled);
    }
  }

  @Override
  public int getBaseMaximumOperations() {
    return 100;
  }

  @Override
  public Component[] createExtendedDescriptionComponents() {
    return new Component[]{
        Component.literal(String.valueOf(radiusZX)),
        Component.literal(String.valueOf(radiusY))
    };
  }

  // TODO: When modifiers
  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    return super.createModifierDescriptionComponents(spellModifier);
  }
}
