package mysticmods.roots.spell;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.mixin.accessor.AccessorMixinMob;
import mysticmods.roots.network.client.fx.DisarmFXPacket;
import mysticmods.roots.network.client.fx.LightningFXPacket;
import mysticmods.roots.util.EntityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

// Note: VISUALS DONE!
public class DisarmSpell extends TwoRadiusSpell {
  private float dropChance;
  private int glowDuration, count;

  public DisarmSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0x3a3a3a, 0x7a0000);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.DISARM_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.DISARM_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.DISARM_RADIUS_ZX;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> result) {
    super.buildProperties(result);
    result.add(ModSpells.DISARM_DROP_CHANCE);
    result.add(ModSpells.DISARM_GLOW_DURATION);
    result.add(ModSpells.DISARM_COUNT);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.dropChance = properties.get(ModSpells.DISARM_DROP_CHANCE);
    this.glowDuration = properties.get(ModSpells.DISARM_GLOW_DURATION);
    this.count = properties.get(ModSpells.DISARM_COUNT);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    List<EquipmentSlot> slots = List.of(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
    List<LivingEntity> entities = pLevel.getEntities(EntityTypeTest.forClass(LivingEntity.class), getAABB().move(pPlayer.position()), EntityUtils.isHostileTo(pPlayer)
        .and((o) -> !o.getType().is(RootsTags.Entities.DISABLE_DISARM)));

    DamageSources damage = pPlayer.damageSources();
    DamageSource source = damage.playerAttack(pPlayer);

    Vec3 start = pPlayer.position().add(0, 0.5, 0);

    int count = 0;

    for (LivingEntity entity : entities) {
      Mob mob = null;
      boolean didDrop = false;
      if (entity instanceof Mob mobEntity) {
        mob = mobEntity;
      }

      Vec3 stop = entity.position().add(0, entity.getBbHeight() / 2, 0);
      PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new LightningFXPacket(LightningFXPacket.LightningPreset.FANCY, 1, start, stop));

      for (EquipmentSlot slot : slots) {
        ItemStack stack = entity.getItemBySlot(slot);
        if (stack.isEmpty()) {
          continue;
        }

        float thisChance = dropChance;

        // We never dropped if the item is tagged as un-droppable
        if (stack.is(RootsTags.Items.DISABLE_DISARMING) || EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
          continue;
        }

        if (mob != null) {
          float defaultChance = ((AccessorMixinMob) mob).rootsInvokeGetEquipmentDropChance(slot);
          if (defaultChance == 0.0f) {
            continue;
          }
          if (defaultChance < dropChance) {
            defaultChance = dropChance;
          }
          thisChance = EnchantmentHelper.processEquipmentDropChance((ServerLevel) pLevel, entity, source, defaultChance);
        }

        count++;
        didDrop = true;

        if (pPlayer.getRandom().nextFloat() < thisChance) {
          entity.spawnAtLocation(stack);
        }

        entity.setItemSlot(slot, ItemStack.EMPTY);
      }

      if (didDrop) {
        entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, glowDuration, 0, false, false));
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new DisarmFXPacket(entity.getId()));
      }
    }

    if (count == 0) {
      costs.noCharge();
      return 0;
    } else {
      costs.operations(count);
    }

    return cooldown * count;
  }

  @Override
  public int getMaximumOperations() {
    return 100;
  }

  @Override
  public CostInstance.ChargeType getChargeType() {
    return CostInstance.ChargeType.OPERATION;
  }
}
