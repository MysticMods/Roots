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
import mysticmods.roots.snapshot.PetalShellSnapshot;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class PetalShellSpell extends Spell {
  private int duration, count;

  public PetalShellSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.PETAL_SHELL_COOLDOWN.get();
  }

  @Override
  public void initialize() {
    this.count = ModSpells.PETAL_SHELL_COUNT.get().getValue();
    this.duration = ModSpells.PETAL_SHELL_DURATION.get().getValue();
  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    pPlayer.getCapability(Capabilities.SNAPSHOT_CAPABILITY).ifPresent(snapshot -> {
     pPlayer.addEffect(new MobEffectInstance(ModEffects.PETAL_SHELL.get(), duration, count));
     snapshot.addSnapshot(pPlayer, ModSerializers.PETAL_SHELL.get(), new PetalShellSnapshot(pPlayer, count));
    });
  }
}
