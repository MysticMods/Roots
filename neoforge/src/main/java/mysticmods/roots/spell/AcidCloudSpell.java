package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.util.EntityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;

import java.util.List;

public class AcidCloudSpell extends TwoRadiusSpell {
  private float damage;
  private int count;

  public AcidCloudSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.CONTINUOUS, color, costs, 0x50a028, 0x405f20);
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
  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> result = super.getProperties();
    result.add(ModSpells.ACID_CLOUD_DAMAGE);
    result.add(ModSpells.ACID_CLOUD_COUNT);
    return result;
  }

  @Override
  public void initialize() {
/*    this.damage = ModSpells.ACID_CLOUD_DAMAGE.getValue();
    this.count = ModSpells.ACID_CLOUD_COUNT.getValue();*/
  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    List<LivingEntity> entities = pLevel.getEntities(EntityTypeTest.forClass(LivingEntity.class), getAABB().move(pPlayer.position()), EntityUtils.isHostileTo(pPlayer));
    for (int damaged = 0; damaged < count; damaged++) {
      if (entities.isEmpty()) {
        break;
      }

      LivingEntity entity = entities.remove(pLevel.getRandom().nextInt(entities.size()));
      // TODO:
/*      entity.hurt(DamageSource.playerAttack(pPlayer), damage);*/
    }
  }

}
