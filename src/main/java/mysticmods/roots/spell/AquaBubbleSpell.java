package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellCastResult;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.CastAquaBubbleFXPacket;
import mysticmods.roots.snapshot.AquaBubbleSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class AquaBubbleSpell extends Spell {
  private int duration;
  private float fire_reduction, lava_reduction;
  private int absorption;

  public AquaBubbleSpell(Properties properties) {
    super(properties);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.AQUA_BUBBLE_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.duration = properties.get(ModSpells.AQUA_BUBBLE_DURATION);
    this.absorption = properties.get(ModSpells.AQUA_BUBBLE_ABSORPTION);
    this.fire_reduction = properties.get(ModSpells.AQUA_BUBBLE_FIRE_REDUCTION);
    this.lava_reduction = properties.get(ModSpells.AQUA_BUBBLE_LAVA_REDUCTION);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.AQUA_BUBBLE_DURATION);
    properties.add(ModSpells.AQUA_BUBBLE_ABSORPTION);
    properties.add(ModSpells.AQUA_BUBBLE_FIRE_REDUCTION);
    properties.add(ModSpells.AQUA_BUBBLE_LAVA_REDUCTION);
  }

  @Override
  public Component[] createExtendedDescriptionComponents() {
    double absorb = ((absorption + 1) * 4.0) / 2.0;

    return new Component[]{
        Component.literal(String.format("%.1f", duration / 20.0)),
        Component.literal(String.valueOf(duration)),
        Component.literal(String.format("%.1f", absorb)),
        Component.literal(String.format("%.1f", 1.0 - lava_reduction)),
        Component.literal(String.format("%.1f", 1.0 - fire_reduction))
    };
  }

  // TODO: No current modifiers
  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    return new Component[]{};
  }

  @Override
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
/*    if (pPlayer.hasEffect(ModEffects.AQUA_BUBBLE)) {
      AquaBubbleSnapshot snapshot = SnapshotHelper.getSnapshot(pPlayer, ModSerializers.AQUA_BUBBLE.get());
      if (snapshot != null) {
        var absorb = pPlayer.getEffect(MobEffects.ABSORPTION);
        if (absorb == null || absorb.getAmplifier() < snapshot.getAbsorption()) {
          // Can-reapply
          pPlayer.removeEffect(ModEffects.AQUA_BUBBLE);
          pPlayer.removeEffect(MobEffects.ABSORPTION);
        }


      }
    }*/

/*    if (pPlayer.hasEffect(ModEffects.AQUA_BUBBLE)) {
      costs.noCharge();
      return 0;
    }*/

    pPlayer.extinguishFire();
    SnapshotHelper.addLiving(pPlayer, ModSerializers.AQUA_BUBBLE.get(), new AquaBubbleSnapshot(pPlayer, duration, absorption, fire_reduction, lava_reduction));
    pPlayer.addEffect(new MobEffectInstance(ModEffects.AQUA_BUBBLE, duration, 0, false, false), pPlayer);
    pPlayer.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, absorption, false, false), pPlayer);
    PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new CastAquaBubbleFXPacket(pPlayer.getId()));
    return SpellCastResult.success(cooldown);
  }
}
