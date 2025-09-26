package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.entity.other.LightDrifterEntity;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.ClientboundLightDrifterSyncPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class LightDrifterSpell extends Spell {
  private int duration;

  public LightDrifterSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0xf2ee96, 0x96dbf2);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.LIGHT_DRIFTER_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var map = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.duration = map.get(ModSpells.LIGHT_DRIFTER_DURATION);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.LIGHT_DRIFTER_DURATION);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    pPlayer.addEffect(new MobEffectInstance(ModEffects.LIGHT_DRIFTER, duration));
    LightDrifterEntity drifter = new LightDrifterEntity(ModEntities.LIGHT_DRIFTER.get(), pLevel, pPlayer);
    drifter.setPos(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
    drifter.setXRot(pPlayer.getXRot());
    drifter.setYRot(pPlayer.getYRot());
    pLevel.addFreshEntity(drifter);
    PacketDistributor.sendToPlayer((ServerPlayer) pPlayer, new ClientboundLightDrifterSyncPacket(drifter.getId()));
    return cooldown;
  }
}
