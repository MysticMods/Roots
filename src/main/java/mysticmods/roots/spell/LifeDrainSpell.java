package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModDamage;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.CastLifeDrainFXPacket;
import mysticmods.roots.network.client.fx.DrainLifeFXPacket;
import mysticmods.roots.network.client.fx.EntityBeamFXPacket;
import mysticmods.roots.util.EntityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class LifeDrainSpell extends Spell {
  private double distance;
  private float damage, heal;
  private int angle;

  public LifeDrainSpell(ChatFormatting color, CostInstance costs) {
    super(Type.CONTINUOUS, color, costs, 0x902040, 0xffc4f0);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.LIFE_DRAIN_COOLDOWN;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.LIFE_DRAIN_DISTANCE);
    properties.add(ModSpells.LIFE_DRAIN_ANGLE);
    properties.add(ModSpells.LIFE_DRAIN_DAMAGE);
    properties.add(ModSpells.LIFE_DRAIN_HEAL);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.distance = properties.get(ModSpells.LIFE_DRAIN_DISTANCE);
    // TODO: Handle when the angle is wrong
    this.angle = Math.clamp(properties.get(ModSpells.LIFE_DRAIN_ANGLE), 1, 180);
    this.damage = properties.get(ModSpells.LIFE_DRAIN_DAMAGE);
    this.heal = properties.get(ModSpells.LIFE_DRAIN_HEAL);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    Vec3 eyePos = pPlayer.getEyePosition(1.0f);
    Vec3 look = pPlayer.getViewVector(1.0f);
    Vec3 endPos = eyePos.add(look.scale(distance));

    double reach = distance * Math.tan(Math.toRadians(angle / 2.0));

    AABB bounds = new AABB(eyePos, endPos).inflate(reach);

    boolean foundTarget = false;
    int count = 0;


    List<LivingEntity> entities = getLifeDrainTargets(pLevel, pPlayer, eyePos, endPos, bounds, EntityUtils.isHostileTo(pPlayer));

    for (LivingEntity entity : entities) {
      foundTarget = true;
      if (entity.hurt(ModDamage.lifeDrain(pPlayer), this.damage)) {
        pPlayer.heal(heal);
        count++;

        PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new DrainLifeFXPacket(entity.getId(), pPlayer.getId()));
      }
    }

    if (ticks % 10 == 0) {
      PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new CastLifeDrainFXPacket(pPlayer.getId(), distance, angle + (int) (angle * 0.3)));
    }

    if (!foundTarget) {
      costs.noCharge();
      return 0;
    } else {
      costs.operations(count);
      return cooldown;
    }
  }

  public List<LivingEntity> getLifeDrainTargets(Level level, LivingEntity player, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter) {
    Vec3 look = player.getViewVector(1f).normalize();
    double radius = Math.cos(Math.toRadians(angle / 2.0));
    double dist = distance * distance;

    List<LivingEntity> results = new ArrayList<>();

    Entity vehicle = player.getRootVehicle();

    for (LivingEntity candidate : level.getEntities(EntityTypeTest.forClass(LivingEntity.class), boundingBox, filter)) {
      if (candidate.getRootVehicle().equals(vehicle) && !candidate.canRiderInteract()) {
        continue;
      }

      if (candidate.getEyePosition().subtract(startVec).normalize().dot(look) >= radius && candidate.distanceToSqr(startVec) <= dist) {
        results.add(candidate);
      }
    }

    results.sort(Comparator.comparingDouble(e -> e.distanceToSqr(startVec)));

    return results;
  }

  @Override
  public CostInstance.ChargeType getChargeType() {
    return CostInstance.ChargeType.OPERATION;
  }
}
