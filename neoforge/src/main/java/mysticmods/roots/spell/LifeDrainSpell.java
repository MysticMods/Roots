package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LifeDrainSpell extends Spell {
  private double vectorDistance, boundingBoxDistance;
  private float damage, heal;

  public LifeDrainSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0x902040, 0xffc4f0);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.LIFE_DRAIN_COOLDOWN;
  }

  @Override
  public void initialize() {
/*    this.vectorDistance = ModSpells.LIFE_DRAIN_DISTANCE.getValue();
    this.boundingBoxDistance = ModSpells.LIFE_DRAIN_BOUNDS.getValue();
    this.damage = ModSpells.LIFE_DRAIN_DAMAGE.getValue();
    this.heal = ModSpells.LIFE_DRAIN_HEAL.getValue();*/
  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    Vec3 look = pPlayer.getLookAngle();
    Vec3 position = pPlayer.position();
    float eyeHeight = pPlayer.getEyeHeight(pPlayer.getPose());
    boolean foundTarget = false;
    for (int i = 0; i < 4 && !foundTarget; i++) {
      double x = position.x + look.x * vectorDistance * (float) i;
      double y = position.y + eyeHeight + look.y * vectorDistance * (float) i;
      double z = position.z + look.z * vectorDistance * (float) i;
      List<LivingEntity> entities = pLevel.getEntities(EntityTypeTest.forClass(LivingEntity.class), new AABB(x - boundingBoxDistance, y - boundingBoxDistance, z - boundingBoxDistance, x + boundingBoxDistance, y + boundingBoxDistance, z + boundingBoxDistance), EntityUtils.isHostileTo(pPlayer));
      for (LivingEntity entity : entities) {
        foundTarget = true;
        // TODO:
/*        if (entity.hurt(DamageSource.playerAttack(pPlayer).bypassArmor(), damage)) {
          pPlayer.heal(heal);
        }*/
      }
    }
  }
}
