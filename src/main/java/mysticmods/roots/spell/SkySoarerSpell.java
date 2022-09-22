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
import mysticmods.roots.snapshot.SkySoarerSnapshot;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SkySoarerSpell extends Spell {
  private float amplifier, boosted_amplifier;
  private int duration, boosted_duration;

  public SkySoarerSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.SKY_SOARER_COOLDOWN.get();
  }

  @Override
  public void initialize() {
    this.amplifier = ModSpells.SKY_SOARER_AMPLIFIER.get().getValue();
    this.boosted_amplifier = ModSpells.SKY_SOARER_BOOSTED_AMPLIFIER.get().getValue();
    this.duration = ModSpells.SKY_SOARER_DURATION.get().getValue();
    this.boosted_duration = ModSpells.SKY_SOARER_BOOSTED_DURATION.get().getValue();
  }

  @Override
  public void cast(Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    pPlayer.getCapability(Capabilities.SNAPSHOT_CAPABILITY).ifPresent(snapshot -> {
     pPlayer.addEffect(new MobEffectInstance(ModEffects.SKY_SOARER.get(), duration, 0));
     snapshot.addSnapshot(pPlayer, ModSerializers.SKY_SOARER.get(), new SkySoarerSnapshot(pPlayer, pPlayer.getDeltaMovement(), amplifier));
    });
  }
}
