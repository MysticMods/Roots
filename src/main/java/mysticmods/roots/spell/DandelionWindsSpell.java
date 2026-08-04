package mysticmods.roots.spell;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.ParentChargeType;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellCastType;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModModifiers;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.snapshot.DandelionWindsSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class DandelionWindsSpell extends Spell {
  private int duration, durationIncrease, vortexCooldown, vortexCooldownDecrease, gustsCooldown, gustsCooldownDecrease;
  private float deflectionChance, deflectionChanceIncrease;

  public DandelionWindsSpell(ChatFormatting color, CostInstance costs) {
    super(SpellCastType.INSTANT, color, costs, ParentChargeType.INSTANCE, 0xffff20, 0xffb020);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.DANDELION_WINDS_COOLDOWN;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> result) {
    super.buildProperties(result);
    result.add(ModSpells.DANDELION_WINDS_DURATION_INCREASE);
    result.add(ModSpells.DANDELION_WINDS_DURATION);
    result.add(ModSpells.DANDELION_WINDS_PROJECTILE_DEFLECTION_CHANCE);
    result.add(ModSpells.DANDELION_WINDS_PROJECTILE_DEFLECTION_INCREASE);
    result.add(ModSpells.DANDELION_WINDS_MAGNETIC_COOLDOWN_DECREASE);
    result.add(ModSpells.DANDELION_WINDS_MAGNETIC_COOLDOWN);
    result.add(ModSpells.DANDELION_WINDS_VORTEX_COOLDOWN_DECREASE);
    result.add(ModSpells.DANDELION_WINDS_VORTEX_COOLDOWN);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap data = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.deflectionChance = data.get(ModSpells.DANDELION_WINDS_PROJECTILE_DEFLECTION_CHANCE);
    this.deflectionChanceIncrease = data.get(ModSpells.DANDELION_WINDS_PROJECTILE_DEFLECTION_INCREASE);
    this.duration = data.get(ModSpells.DANDELION_WINDS_DURATION);
    this.durationIncrease = data.get(ModSpells.DANDELION_WINDS_DURATION_INCREASE);
    this.vortexCooldown = data.get(ModSpells.DANDELION_WINDS_VORTEX_COOLDOWN);
    this.vortexCooldownDecrease = data.get(ModSpells.DANDELION_WINDS_VORTEX_COOLDOWN_DECREASE);
    this.gustsCooldown = data.get(ModSpells.DANDELION_WINDS_MAGNETIC_COOLDOWN);
    this.gustsCooldownDecrease = data.get(ModSpells.DANDELION_WINDS_MAGNETIC_COOLDOWN_DECREASE);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
/*    if (pPlayer.hasEffect(ModEffects.DANDELION_WINDS)) {
      costs.noCharge();
      return 0;
    }*/

    int curDuration = duration + (instance.count(RootsTags.SpellModifiers.DANDELION_WINDS_INCREASES_DURATION) * durationIncrease);
    float curChance = deflectionChance + (instance.count(RootsTags.SpellModifiers.DANDELION_WINDS_INCREASES_CHANCE) * deflectionChanceIncrease);
    int curGusts = gustsCooldown + (instance.count(RootsTags.SpellModifiers.DANDELION_WINDS_GUSTS_COOLDOWN_DECREASE) * gustsCooldownDecrease);
    int curVortex = vortexCooldown + (instance.count(RootsTags.SpellModifiers.DANDELION_WINDS_VORTEX_COOLDOWN_DECREASE) * vortexCooldownDecrease);

    SnapshotHelper.addLiving(pPlayer, ModSerializers.DANDELION_WINDS.get(), new DandelionWindsSnapshot(pPlayer, curDuration, curChance, instance.has(ModModifiers.DANDELION_WINDS_VORTEX), curVortex, instance.has(ModModifiers.DANDELION_WINDS_GUSTS), curGusts));
    pPlayer.addEffect(new MobEffectInstance(ModEffects.DANDELION_WINDS, curDuration, 0, false, false));
    return cooldown;
  }

  @SuppressWarnings("removal")
  @Override
  public Component[] createExtendedDescriptionComponents() {
    return new Component[]{
        Component.literal(String.format("%s", duration / 20)),
        Component.literal(String.format("%s", duration)),
        Component.literal(String.format("%.1f", deflectionChance))
    };
  }

  @SuppressWarnings("removal")
  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    if (spellModifier.is(RootsTags.SpellModifiers.DANDELION_WINDS_INCREASES_DURATION)) {
      int count;
      if (spellModifier.is(ModModifiers.DANDELION_WINDS_DURATION_1)) {
        count = 1;
      } else if (spellModifier.is(ModModifiers.DANDELION_WINDS_DURATION_2)) {
        count = 2;
      } else if (spellModifier.is(ModModifiers.DANDELION_WINDS_DURATION_3)) {
        count = 3;
      } else if (spellModifier.is(ModModifiers.DANDELION_WINDS_DURATION_4)) {
        count = 4;
      } else if (spellModifier.is(ModModifiers.DANDELION_WINDS_DURATION_5)) {
        count = 5;
      } else {
        RootsAPI.LOG.error("Spell modifier {} increases dandelion winds duration but isn't duration 1, 2, 3, 4 or 5!", spellModifier);
        count = 1;
      }
      int totalDur = duration + (durationIncrease * count);
      return new Component[]{
          Component.literal(String.format("%s", durationIncrease / 20)),
          Component.literal(String.format("%s", durationIncrease)),
          Component.literal(String.format("%s", totalDur / 20)),
          Component.literal(String.format("%s", totalDur))
      };
    } else if (spellModifier.is(RootsTags.SpellModifiers.DANDELION_WINDS_INCREASES_CHANCE)) {
      int count;
      if (spellModifier.is(ModModifiers.DANDELION_WINDS_CHANCE_1)) {
        count = 1;
      } else if (spellModifier.is(ModModifiers.DANDELION_WINDS_CHANCE_2)) {
        count = 2;
      } else if (spellModifier.is(ModModifiers.DANDELION_WINDS_CHANCE_3)) {
        count = 3;
      } else if (spellModifier.is(ModModifiers.DANDELION_WINDS_CHANCE_4)) {
        count = 4;
      } else {
        RootsAPI.LOG.error("Spell modifier {} increases dandelion winds chance but isn't chance 1, 2, 3 or 4!", spellModifier);
        count = 1;
      }

      float totalChance = deflectionChance + (deflectionChanceIncrease * count);

      return new Component[]{
          Component.literal(String.format("%s", (int) (deflectionChance * 100))),
          Component.literal(String.format("%s", (int) (totalChance * 100)))
      };
    }
    RootsAPI.LOG.error("Tried to create description components for modifiers not associated with {}: {}", this, spellModifier);
    return new Component[]{};
  }
}
