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
import mysticmods.roots.api.spell.SpellCastResult;
import mysticmods.roots.init.ModDamage;
import mysticmods.roots.init.ModModifiers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.AcidCloudFXPacket;
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
    properties.add(ModSpells.ACID_CLOUD_DAMAGE);
    properties.add(ModSpells.ACID_CLOUD_COUNT);
    properties.add(ModSpells.ACID_CLOUD_FIRE_TICKS);
    properties.add(ModSpells.ACID_CLOUD_SLOW_DURATION);
    properties.add(ModSpells.ACID_CLOUD_SLOW_AMPLIFIER);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.damage = properties.get(ModSpells.ACID_CLOUD_DAMAGE);
    this.count = properties.get(ModSpells.ACID_CLOUD_COUNT);
    this.fireTicks = properties.get(ModSpells.ACID_CLOUD_FIRE_TICKS);
    this.slowDuration = properties.get(ModSpells.ACID_CLOUD_SLOW_DURATION);
    this.slowAmplifier = properties.get(ModSpells.ACID_CLOUD_SLOW_AMPLIFIER);
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
          Component.literal(String.valueOf(slowAmplifier))
      };
    }
    RootsAPI.LOG.error("Tried to create description components for modifiers not associated with {}: {}", this, spellModifier);
    return new Component[]{};
  }

  @Override
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    Predicate<Entity> entityTest = instance.has(RootsTags.SpellModifiers.PEACEFUL) ? EntityUtils.isHostileTo(pPlayer) : EntityUtils.allEntities(pPlayer, true);

    List<LivingEntity> entities = pLevel.getEntities(EntityTypeTest.forClass(LivingEntity.class), getAABB().move(pPlayer.position()), entityTest);
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
      if (instance.has(RootsTags.SpellModifiers.SETS_ON_FIRE)) {
        entity.igniteForTicks(fireTicks);
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

    if (totalDamaged == 0) {
      costs.noCharge();
      return SpellCastResult.nothing();
    }

    return SpellCastResult.success(totalDamaged, cooldown);
  }
}
