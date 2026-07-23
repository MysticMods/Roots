package mysticmods.roots.spell;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.spell.ParentChargeType;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModDamage;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.AcidCloudFXPacket;
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
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Predicate;

// Note: VISUALS DONE!
public class AcidCloudSpell extends TwoRadiusSpell {
  private float damage;
  private int count;

  public AcidCloudSpell(ChatFormatting color, CostInstance costs) {
    super(Type.CONTINUOUS, color, costs, ParentChargeType.INSTANCE, 0x50a028, 0x405f20);
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
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.damage = properties.get(ModSpells.ACID_CLOUD_DAMAGE);
    this.count = properties.get(ModSpells.ACID_CLOUD_COUNT);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    Predicate<Entity> entityTest = instance.hasModifier(RootsTags.SpellModifiers.PEACEFUL) ? EntityUtils.isHostileTo(pPlayer) : (p) -> true;

    List<LivingEntity> entities = pLevel.getEntities(EntityTypeTest.forClass(LivingEntity.class), getAABB().move(pPlayer.position()), entityTest);
    int totalDamaged = 0;
    for (int damaged = 0; damaged < count; damaged++) {
      if (entities.isEmpty()) {
        break;
      }

      LivingEntity entity = entities.get(pLevel.getRandom().nextInt(entities.size()));
      totalDamaged++;
      entity.hurt(ModDamage.acidCloud(pPlayer), damage);
    }

    if (ticks % 3 == 0) {
      PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new AcidCloudFXPacket(pPlayer.getId()));
    }

    if (totalDamaged == 0) {
      costs.noCharge();
      return -1;
    }

    return cooldown;
  }

}
