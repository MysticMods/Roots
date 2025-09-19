package mysticmods.roots.spell;

import mysticmods.roots.action.GeasAction;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.util.EntityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GeasSpell extends Spell {
  private int count, duration, maxCooldown;
  private double maxHealth;

  public GeasSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0x802020, 0x202020);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.GEAS_COOLDOWN;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> result) {
    super.buildProperties(result);
    result.add(ModSpells.GEAS_COUNT);
    result.add(ModSpells.GEAS_DURATION);
    result.add(ModSpells.GEAS_MAX_COOLDOWN);
    result.add(ModSpells.GEAS_MAX_HEALTH);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var dataMap = holder.getData(DataMaps.SPELL_PROPERTY_DATA);

    this.count = dataMap.get(ModSpells.GEAS_COUNT);
    this.duration = dataMap.get(ModSpells.GEAS_DURATION);
    this.maxCooldown = dataMap.get(ModSpells.GEAS_MAX_COOLDOWN);
    this.maxHealth = dataMap.get(ModSpells.GEAS_MAX_HEALTH);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    int affected = 0;

    Vec3 look = pPlayer.getLookAngle();

    float hpAffected = 0;

    // TODO: Improve this dramatically
    for (int i = 0; i < 20; i++) {
      double x = pPlayer.getX() + look.x * 3.0 * (float) i;
      double y = pPlayer.getY() + pPlayer.getEyeHeight() + look.y * 3.0 * (float) i;
      double z = pPlayer.getZ() + look.z * 3.0 * (float) i;
      // TODO: Make geas predicate "static" (excluding player) and obey maxHealth.
      List<LivingEntity> entities = pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(x - 4.0, y - 4.0, z - 4.0, x + 5.0, y + 5.0, z + 5.0), EntitySelector.NO_SPECTATORS.and(Entity::isAlive)
          .and(EntityUtils.isHostileTo(pPlayer).and(o -> !o.getType().is(RootsTags.Entities.GEAS_EXCLUDE))));
      for (Entity entity : entities) {
        if (!(entity instanceof LivingEntity living)) {
          continue;
        }

        if (living.getMaxHealth() > this.maxHealth) {
          continue;
        }

        if (affected == this.count) {
          break;
        }

        affected += affect(pPlayer, living);
        hpAffected += living.getMaxHealth();
      }
    }

    if (affected == 0) {
      costs.noCharge();
      return 0;
    }

    double scaleFactor = Math.sqrt(hpAffected) * 1.5;
    return (int) Math.min(cooldown * scaleFactor, maxCooldown);
  }

  private int affect(Player player, LivingEntity entity) {
    if (entity.hasEffect(ModEffects.GEAS)) {
      return 0;
    }

    entity.addEffect(new MobEffectInstance(ModEffects.GEAS, this.duration, 0, false, false));
    ServerPlayer serverPlayer = (ServerPlayer) player;
    GeasAction.Context context = new GeasAction.Context(serverPlayer.serverLevel(), serverPlayer, entity);
    ModActions.GEAS.get().accept(context);
    return 1;
  }

}
