package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.CastAquaBubbleFXPacket;
import mysticmods.roots.snapshot.AquaBubbleSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

// Note: VISUALS TEMPORARY
public class AquaBubbleSpell extends Spell {
  private int duration;
  private float fire_reduction, lava_reduction;
  private int absorption;

  public AquaBubbleSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0xede658, 0x5dd1de);
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
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    if (pPlayer.hasEffect(ModEffects.AQUA_BUBBLE)) {
      costs.noCharge();
      return 0;
    }
    SnapshotHelper.addLiving(pPlayer, ModSerializers.AQUA_BUBBLE.get(), new AquaBubbleSnapshot(pPlayer, duration, absorption, fire_reduction, lava_reduction));
    pPlayer.addEffect(new MobEffectInstance(ModEffects.AQUA_BUBBLE, duration, 0, false, false));
    pPlayer.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, absorption, false, false));
    PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new CastAquaBubbleFXPacket(pPlayer.getId()));
    return cooldown;
  }
}
