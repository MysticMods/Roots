package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellCastResult;
import mysticmods.roots.init.ModDamage;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.CastLifeDrainFXPacket;
import mysticmods.roots.network.client.fx.DrainLifeFXPacket;
import mysticmods.roots.network.client.fx.HealFXPacket;
import mysticmods.roots.util.EntityUtils;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
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

// TODO: Overhaul targeting
public class LifeDrainSpell extends Spell {
  private double distance;
  private float damage, heal;
  private int angle, maxCount;

  public LifeDrainSpell(Properties properties) {
    super(properties);
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
    properties.add(ModSpells.LIFE_DRAIN_COUNT);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.distance = properties.get(ModSpells.LIFE_DRAIN_DISTANCE);
    // TODO: Handle when the angle is wrong
    this.angle = Math.clamp(properties.get(ModSpells.LIFE_DRAIN_ANGLE), 1, 180);
    this.damage = properties.get(ModSpells.LIFE_DRAIN_DAMAGE);
    this.heal = properties.get(ModSpells.LIFE_DRAIN_HEAL);
    this.maxCount = properties.get(ModSpells.LIFE_DRAIN_COUNT);
  }

  @Override
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    Vec3 eyePos = pPlayer.getEyePosition(1.0f);
    Vec3 look = pPlayer.getViewVector(1.0f);
    Vec3 endPos = eyePos.add(look.scale(distance));

    double reach = distance * Math.tan(Math.toRadians(angle / 2.0));

    AABB bounds = new AABB(eyePos, endPos).inflate(reach);

    int count = 0;

    List<LivingEntity> entities = getLifeDrainTargets(pLevel, pPlayer, eyePos, endPos, bounds, EntityUtils.isHostileTo(pPlayer));
    Util.shuffle(entities, pPlayer.getRandom());

    for (LivingEntity entity : entities) {
      if (count > maxCount) {
        break;
      }
      if (entity.hurt(ModDamage.lifeDrain(pPlayer), this.damage)) {
        count++;

        float amount = Math.min(heal, pPlayer.getMaxHealth() - pPlayer.getHealth());
        if (amount > 0) {
          pPlayer.heal(amount);
          PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new HealFXPacket(pPlayer.getId(), heal));
        }

        PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new DrainLifeFXPacket(entity.getId(), pPlayer.getId()));
      }
    }

    if (count == 0) {
      costs.noCharge();
      return SpellCastResult.nothing();
    } else {
      PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new CastLifeDrainFXPacket(pPlayer.getId(), distance, angle + (int) (angle * 0.3)));
      costs.operations(count);
      return SpellCastResult.success(count, cooldown);
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

      if (candidate.getEyePosition().subtract(startVec).normalize()
          .dot(look) >= radius && candidate.distanceToSqr(startVec) <= dist) {
        results.add(candidate);
      }
    }

    results.sort(Comparator.comparingDouble(e -> e.distanceToSqr(startVec)));

    return results;
  }

  @Override
  public Component[] createExtendedDescriptionComponents() {
    return new Component[]{
        Component.literal(String.valueOf(maxCount)),
        Component.literal(String.format("%.1f", distance)),
        Component.literal(String.valueOf(angle)),
        Component.literal(String.format("%.2f", damage / 2.0)),
        Component.literal(String.format("%.2f", heal / 2.0))
    };
  }

  // TODO: When modifiers
  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    return new Component[]{};
  }
}
