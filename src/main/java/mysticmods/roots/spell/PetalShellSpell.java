package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
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

public class PetalShellSpell extends Spell {
  private int duration, count;

  public PetalShellSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0xffc0f0, 0xffffff);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.PETAL_SHELL_COOLDOWN;
  }

  @Override
  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> properties = super.getProperties();
    properties.add(ModSpells.PETAL_SHELL_COUNT);
    properties.add(ModSpells.PETAL_SHELL_DURATION);
    return properties;
  }

  @Override
  public void initialize() {
/*    this.count = ModSpells.PETAL_SHELL_COUNT.getValue();
    this.duration = ModSpells.PETAL_SHELL_DURATION.getValue();*/
  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
/*    pPlayer.getCapability(Capabilities.SNAPSHOT_CAPABILITY).ifPresent(snapshot -> {
     pPlayer.addEffect(new MobEffectInstance(ModEffects.PETAL_SHELL, duration, count));
     snapshot.addSnapshot(pPlayer, ModSerializers.PETAL_SHELL, new PetalShellSnapshot(pPlayer, count));
    });*/
  }
}
