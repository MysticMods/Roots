package mysticmods.roots.spell;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.CastResult;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.DecayTargetFXPacket;
import mysticmods.roots.util.EntityUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DecaySpell extends TwoRadiusSpell {
  private int count;
  private double bossModifier, heartModifier;

  public DecaySpell(Properties properties) {
    super(properties);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.DECAY_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.DECAY_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.DECAY_RADIUS_ZX;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.DECAY_COUNT);
    properties.add(ModSpells.DECAY_COOLDOWN_BOSS_MODIFIER);
    properties.add(ModSpells.DECAY_COOLDOWN_HEART_MODIFIER);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var data = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.count = data.get(ModSpells.DECAY_COUNT);
    this.bossModifier = data.get(ModSpells.DECAY_COOLDOWN_BOSS_MODIFIER);
    this.heartModifier = data.get(ModSpells.DECAY_COOLDOWN_HEART_MODIFIER);
  }

  @Override
  public CastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    List<LivingEntity> entities = pLevel.getEntities(EntityTypeTest.forClass(LivingEntity.class), instance.getAABB().move(pPlayer.position()), EntityUtils.isHostileTo(pPlayer)
        .and(o -> o.getType().is(RootsTags.Entities.DECAYABLE)));
    int totalDecayed = 0;
    double heartsDecayed = 0;
    boolean bossFound = false;
    while (totalDecayed < count) {
      if (entities.isEmpty()) {
        break;
      }

      LivingEntity entity = entities.remove(pLevel.getRandom().nextInt(entities.size()));
      Result result = tryDecayEntity(pPlayer, entity);
      if (result != null) {
        if (!result.isEmpty()) {
          entity.spawnAtLocation(result.result());
        }
        // TODO: Visual
        totalDecayed++;
        heartsDecayed += result.healthDecayed;

        if (entity.getType().is(RootsTags.Entities.BOSS)) {
          bossFound = true;
        }

        PacketDistributor.sendToPlayersTrackingEntity(entity, new DecayTargetFXPacket(entity.getId()));
      }
    }

    if (totalDecayed == 0) {
      costs.noCharge();
      return CastResult.nothing();
    }

    costs.operations(totalDecayed);

    int newCooldown = cooldown + (int) Math.floor(heartModifier * Math.abs(heartsDecayed));

    if (bossFound) {
      newCooldown = (int) Math.floor(newCooldown * bossModifier);
    }


    return CastResult.success(totalDecayed, newCooldown);
  }

  @Nullable
  private static Result tryDecayEntity(LivingEntity attacker, LivingEntity entity) {
    var decayHealth = entity.getType().builtInRegistryHolder().getData(DataMaps.DECAYABLE_HEALTH_INFO);
    var decayDrop = entity.getType().builtInRegistryHolder().getData(DataMaps.DECAYABLE_DROP_INFO);
    if (decayHealth == null || decayDrop == null) {
      return null;
    }

    double removed = decayHealth.apply(attacker, entity);
    if (removed != 0) {
      return new Result(decayDrop.run(entity.getRandom()), removed);
    }

    return null;
  }

  record Result (ItemStack result, double healthDecayed) {
    boolean isEmpty () {
      return result.isEmpty();
    }
  }

  @Override
  public Component[] createExtendedDescriptionComponents() {
    return new Component[]{
        Component.literal(String.valueOf(count)),
        Component.literal(String.valueOf(radiusZX)),
        Component.literal(String.valueOf(radiusY))
    };
  }

  // TODO: When modifiers
  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    return new Component[]{};
  }
}
