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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SanctuarySpell extends TwoRadiusSpell {
  private float velocity;

  public SanctuarySpell(ChatFormatting color, List<Cost> costs) {
    super(Type.CONTINUOUS, color, costs, 0xd01050, 0xe02090);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.SANCTUARY_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.SANCTUARY_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.SANCTUARY_RADIUS_XZ;
  }

  @Override
  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> result = super.getProperties();
    result.add(ModSpells.SANCTUARY_VELOCITY);
    return result;
  }

  @Override
  public void initialize() {
/*    this.velocity = ModSpells.SANCTUARY_VELOCITY.getValue();*/
  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    int r = 1 + radiusY + radiusZX;
    Vec3 playerPosition = pPlayer.position();
    List<Entity> entities = pLevel.getEntities(EntityTypeTest.forClass(Entity.class), getAABB().move(pPlayer.position()), EntityUtils.isProjectileOrHostile(pPlayer));
    int count = 0;
    for (Entity entity : entities) {
      if (entity.distanceToSqr(pPlayer) < r) {
        Vec3 entityPosition = entity.position();
        double x = velocity * (entityPosition.x - playerPosition.x);
        double y = velocity * (entityPosition.y - playerPosition.y);
        double z = velocity * (entityPosition.z - playerPosition.z);
        entity.setDeltaMovement(x, y, z);
        entity.hasImpulse = true;
        count++;
      }
    }

    if (count == 0) {
      costs.noCharge();
    }
  }
}
