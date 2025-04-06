package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.snapshot.PetalShellSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class PetalShellSpell extends Spell {
  private int duration, count;

  public PetalShellSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0xffc0f0, 0xffffff);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.PETAL_SHELL_COOLDOWN;
  }

  @Override
  public void buildProperties (List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.PETAL_SHELL_COUNT);
    properties.add(ModSpells.PETAL_SHELL_DURATION);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.count = properties.get(ModSpells.PETAL_SHELL_COUNT);
    this.duration = properties.get(ModSpells.PETAL_SHELL_DURATION);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    pPlayer.addEffect(new MobEffectInstance(ModEffects.PETAL_SHELL, duration, count));
    SnapshotHelper.addLiving(pPlayer, ModSerializers.PETAL_SHELL.get(), new PetalShellSnapshot(pPlayer, duration + 40, count));
    return cooldown;
  }
}
