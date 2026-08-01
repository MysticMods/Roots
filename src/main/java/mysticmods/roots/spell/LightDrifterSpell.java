package mysticmods.roots.spell;

import mysticmods.roots.api.attachment.LightDrifterStorage;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.spell.ParentChargeType;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellCastType;
import mysticmods.roots.entity.other.LightDrifterEntity;
import mysticmods.roots.init.*;
import mysticmods.roots.network.client.light_drifter.ClientboundLightDrifterSyncPacket;
import mysticmods.roots.network.client.light_drifter.ClientboundStopPlayerMovementPacket;
import mysticmods.roots.snapshot.LightDrifterSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class LightDrifterSpell extends Spell {
  private int duration, maxDistance;

  public LightDrifterSpell(ChatFormatting color, CostInstance costs) {
    super(SpellCastType.INSTANT, color, costs, ParentChargeType.INSTANCE, 0xf2ee96, 0x96dbf2);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.LIGHT_DRIFTER_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var map = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.duration = map.get(ModSpells.LIGHT_DRIFTER_DURATION);
    this.maxDistance = map.get(ModSpells.LIGHT_DRIFTER_DISTANCE);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.LIGHT_DRIFTER_DURATION);
    properties.add(ModSpells.LIGHT_DRIFTER_DISTANCE);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    if (pPlayer.isFallFlying() || !pPlayer.onGround()) {
      pPlayer.displayClientMessage(Component.translatable("roots.spell.spell_light_drifter.on_ground"), true);
      costs.noCharge();
      return 0;
    }
    pPlayer.setDeltaMovement(0, 0, 0);
    pPlayer.hasImpulse = true;
    PacketDistributor.sendToPlayer((ServerPlayer) pPlayer, ClientboundStopPlayerMovementPacket.INSTANCE);
    LightDrifterEntity drifter = new LightDrifterEntity(ModEntities.LIGHT_DRIFTER.get(), pLevel, pPlayer);
    drifter.setPos(pPlayer.getX(), pPlayer.getY() + 1.8, pPlayer.getZ());
    drifter.setXRot(pPlayer.getXRot());
    drifter.setYRot(pPlayer.getYRot());
    pLevel.addFreshEntity(drifter);
    LightDrifterStorage storage = new LightDrifterStorage();
    storage.setId(drifter.getUUID());
    storage.setEntityId(drifter.getId());
    pPlayer.setData(ModAttachments.DRIFTER_SERVER_STORAGE, storage);
    LightDrifterSnapshot snapshot = new LightDrifterSnapshot(pPlayer, duration + 60, duration, maxDistance, pPlayer.getUUID());
    SnapshotHelper.addLiving(pPlayer, ModSerializers.LIGHT_DRIFTER.get(), snapshot);
    LightDrifterSnapshot snapshot2 = new LightDrifterSnapshot(drifter, duration + 60, duration, maxDistance, pPlayer.getUUID());
    SnapshotHelper.addLiving(drifter, ModSerializers.LIGHT_DRIFTER.get(), snapshot2);
    pPlayer.addEffect(new MobEffectInstance(ModEffects.LIGHT_DRIFTER, duration));
    PacketDistributor.sendToPlayer((ServerPlayer) pPlayer, new ClientboundLightDrifterSyncPacket(drifter.getId()));
    return cooldown;
  }
}
