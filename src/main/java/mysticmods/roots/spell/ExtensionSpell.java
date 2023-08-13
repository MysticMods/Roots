package mysticmods.roots.spell;

import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.snapshot.ExtensionSnapshot;
import mysticmods.roots.snapshot.SkySoarerSnapshot;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class ExtensionSpell extends TwoRadiusSpell {
  private int nightVisionDuration, senseDangerDuration;
  public ExtensionSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0xcde645, 0xb872b1);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.EXTENSION_COOLDOWN.get();
  }

  @Override
  public SpellProperty<Integer> getRadiusYProperty() {
    return ModSpells.EXTENSION_RADIUS_Y.get();
  }

  @Override
  public SpellProperty<Integer> getRadiusZXProperty() {
    return ModSpells.EXTENSION_RADIUS_ZX.get();
  }

  @Override
  public void initialize() {
    this.nightVisionDuration = ModSpells.EXTENSION_NIGHT_VISION_DURATION.get().getValue();
    this.senseDangerDuration = ModSpells.EXTENSION_SENSE_DANGER_DURATION.get().getValue();
  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    pPlayer.getCapability(Capabilities.SNAPSHOT_CAPABILITY).ifPresent(snapshot -> {
      pPlayer.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, nightVisionDuration, 0, false, false));
      pPlayer.addEffect(new MobEffectInstance(ModEffects.SENSE_DANGER.get(), senseDangerDuration, 0, false, false));
      snapshot.addSnapshot(pPlayer, ModSerializers.EXTENSION.get(), new ExtensionSnapshot(pPlayer, radiusZX, radiusY));
    });


  }
}
