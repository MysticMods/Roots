package mysticmods.roots.spell;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.spell.ParentChargeType;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.util.EntityUtils;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DecaySpell extends TwoRadiusSpell {
  private int count;

  public DecaySpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, ParentChargeType.INSTANCE, 0x2d8115, 0xc92b5f);
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
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var data = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.count = data.get(ModSpells.DECAY_COUNT);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    List<LivingEntity> entities = pLevel.getEntities(EntityTypeTest.forClass(LivingEntity.class), getAABB().move(pPlayer.position()), EntityUtils.isHostileTo(pPlayer).and(o -> o.getType().is(RootsTags.Entities.DECAYABLE)));
    int totalDecayed = 0;
    while (totalDecayed < count) {
      if (entities.isEmpty()) {
        break;
      }

      LivingEntity entity = entities.remove(pLevel.getRandom().nextInt(entities.size()));
      ItemStack result = tryDecayEntity(pPlayer, entity);
      if (result != null) {
        if (!result.isEmpty()) {
          entity.spawnAtLocation(result);
        }
        // TODO: Visual
        totalDecayed++;
      }
    }

    if (totalDecayed == 0) {
      costs.noCharge();
      return -1;
    }

    return cooldown;
  }

  @Nullable
  private static ItemStack tryDecayEntity (LivingEntity attacker, LivingEntity entity) {
    var decayHealth = entity.getType().builtInRegistryHolder().getData(DataMaps.DECAYABLE_HEALTH_INFO);
    var decayDrop = entity.getType().builtInRegistryHolder().getData(DataMaps.DECAYABLE_DROP_INFO);
    if (decayHealth == null || decayDrop == null) {
      return null;
    }

    if (decayHealth.apply(attacker, entity)) {
      return decayDrop.run(entity.getRandom());
    }

    return null;
  }
}
