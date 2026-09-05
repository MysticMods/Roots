package mysticmods.roots.spell;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.CastResult;
import mysticmods.roots.entity.other.TemporalMorassEntity;
import mysticmods.roots.init.*;
import mysticmods.roots.network.client.fx.AcidCloudFXPacket;
import mysticmods.roots.snapshot.SnapshotHelper;
import mysticmods.roots.snapshot.TemporalMorassEntitySnapshot;
import mysticmods.roots.util.EntityUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Predicate;

public class AcidCloudSpell extends TwoRadiusSpell {
  private float damage;
  private int count, fireTicks, slowDuration, slowAmplifier;

  private int tCooldown, tDuration, tRadiusY, tRadiusZX, tAmplifier;

  public AcidCloudSpell(Properties properties) {
    super(properties);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.ACID_CLOUD_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.ACID_CLOUD_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.ACID_CLOUD_RADIUS_ZX;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.addAll(List.of(ModSpells.ACID_CLOUD_DAMAGE, ModSpells.ACID_CLOUD_COUNT, ModSpells.ACID_CLOUD_FIRE_TICKS, ModSpells.ACID_CLOUD_SLOW_DURATION, ModSpells.ACID_CLOUD_SLOW_AMPLIFIER));
    properties.addAll(List.of(ModSpells.TEMPORAL_MORASS_AMPLIFIER, ModSpells.TEMPORAL_MORASS_COOLDOWN, ModSpells.TEMPORAL_MORASS_DURATION, ModSpells.TEMPORAL_MORASS_RADIUS_Y, ModSpells.TEMPORAL_MORASS_RADIUS_ZX));
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.damage = properties.get(ModSpells.ACID_CLOUD_DAMAGE);
    this.count = properties.get(ModSpells.ACID_CLOUD_COUNT);
    this.fireTicks = properties.get(ModSpells.ACID_CLOUD_FIRE_TICKS);
    this.slowDuration = properties.get(ModSpells.ACID_CLOUD_SLOW_DURATION);
    this.slowAmplifier = properties.get(ModSpells.ACID_CLOUD_SLOW_AMPLIFIER);
    this.tCooldown = properties.get(ModSpells.TEMPORAL_MORASS_COOLDOWN);
    this.tAmplifier = properties.get(ModSpells.TEMPORAL_MORASS_AMPLIFIER);
    this.tDuration = properties.get(ModSpells.TEMPORAL_MORASS_DURATION);
    this.tRadiusY = properties.get(ModSpells.TEMPORAL_MORASS_RADIUS_Y);
    this.tRadiusZX = properties.get(ModSpells.TEMPORAL_MORASS_RADIUS_ZX);
  }

  @Override
  public Component[] createExtendedDescriptionComponents() {
    float hearts = damage / 2.0f;
    return new Component[]{
        Component.literal(String.format("%.1f", hearts)),
        Component.literal(String.valueOf(count))
    };
  }

  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    if (spellModifier.is(ModModifiers.ACID_CLOUD_FIRE)) {
      return new Component[]{
          Component.literal(String.format("%.1f", fireTicks / 20.0f)),
          Component.literal(String.valueOf(fireTicks))
      };
    } else if (spellModifier.is(ModModifiers.ACID_CLOUD_PEACEFUL)) {
      return new Component[]{};
    } else if (spellModifier.is(ModModifiers.ACID_CLOUD_SLOWNESS)) {
      return new Component[]{
          Component.literal(String.format("%.1f", slowDuration / 20.0f)),
          Component.literal(String.valueOf(slowDuration)),
          Component.literal(String.valueOf(slowAmplifier + 1))
      };
    } else if (spellModifier.is(ModModifiers.ACID_CLOUD_TEMPORAL_MORASS)) {
      return new Component[]{
          Component.literal(String.valueOf(tRadiusZX)),
          Component.literal(String.valueOf(tRadiusY)),
          Component.literal(String.format("%.1f", tDuration / 20.0f)),
          Component.literal(String.valueOf(tDuration)),
          Component.literal(String.valueOf(tAmplifier + 1)),
          Component.literal(String.format("%.1f", tCooldown / 20.0f)),
          Component.literal(String.valueOf(tCooldown))
      };
    }
    RootsAPI.LOG.error("Tried to create description components for modifiers not associated with {}: {}", this, spellModifier);
    return new Component[]{};
  }

  @Override
  public CastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    Predicate<Entity> entityTest = instance.has(RootsTags.SpellModifiers.PEACEFUL) ? EntityUtils.isHostileTo(pPlayer) : EntityUtils.allEntities(pPlayer, true);

    List<LivingEntity> entities = pLevel.getEntities(EntityTypeTest.forClass(LivingEntity.class), getAABB(instance).move(pPlayer.position()), entityTest);
    int totalDamaged = 0;
    for (int damaged = 0; damaged < count; damaged++) {
      if (entities.isEmpty()) {
        break;
      }

      LivingEntity entity = entities.get(pLevel.getRandom().nextInt(entities.size()));
      totalDamaged++;
      // TODO: Look into modifying damage based on enchantments and attributes
      // TODO: Mace-like modifying damage based off of item
      // TODO: Critical hit modification?
      // DONE: Handle entity incoming damage is already done via `hurt`
      // DONE: Knockback prevention is done via damage type tag
      entity.hurt(ModDamage.acidCloud(pPlayer, instance.has(ModModifiers.ACID_CLOUD_KNOCKBACK)), damage);
      if (instance.has(RootsTags.SpellModifiers.SETS_ON_FIRE) && !entity.isOnFire()) {
        entity.igniteForTicks(fireTicks);
        costs.charge(ModModifiers.ACID_CLOUD_FIRE.get());
      }
      if (instance.has(RootsTags.SpellModifiers.SLOWS)) {
        if (!entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
          entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slowDuration, slowAmplifier));
          costs.charge(ModModifiers.ACID_CLOUD_SLOWNESS.value());
        }
      }
    }

    if (ticks % 3 == 0) {
      PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new AcidCloudFXPacket(ISpellInstance.snapshot(instance), pPlayer.getId()));
    }

    boolean morass = false;

    if (instance.has(ModModifiers.ACID_CLOUD_TEMPORAL_MORASS)) {
      if (!pPlayer.hasEffect(ModEffects.TEMPORAL_MORASS_COOLDOWN)) {
        TemporalMorassEntity timeStop = ModEntities.TEMPORAL_MORASS.get().create(pLevel);
        if (timeStop != null) {
          timeStop.setLifetime(tDuration);
          timeStop.setPos(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
          // Don't use the helper
          pLevel.addFreshEntity(timeStop);
          SnapshotHelper.addLiving(timeStop, ModSerializers.TEMPORAL_MORASS.get(), new TemporalMorassEntitySnapshot(timeStop.tickCount, -1, tRadiusZX, tRadiusY, tDuration, tAmplifier));
          pPlayer.addEffect(new MobEffectInstance(ModEffects.TEMPORAL_MORASS_COOLDOWN, tCooldown));
          morass = true;
          costs.charge(ModModifiers.ACID_CLOUD_TEMPORAL_MORASS.get());
        }
      }
    }

    if (totalDamaged == 0 && !morass) {
      costs.noCharge();
      return CastResult.nothing();
    }

    return CastResult.success(totalDamaged, cooldown);
  }
}
