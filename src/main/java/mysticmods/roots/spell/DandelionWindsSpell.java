package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DandelionWindsSpell extends Spell {
  private float distance;
  private double r1, r2;

  public DandelionWindsSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0xffff20, 0xffb020);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.DANDELION_WINDS_COOLDOWN.get();
  }

  @Override
  public void initialize() {
    this.distance = ModSpells.DANDELION_WINDS_DISTANCE.get().getValue();
    this.r1 = ModSpells.DANDELION_WINDS_RANGE_1.get().getValue();
    this.r2 = ModSpells.DANDELION_WINDS_RANGE_2.get().getValue();
  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    Vec3 look = pPlayer.getLookAngle();
    float d = distance;
    float motion = d * d + d;
    Vec3 playVec = pPlayer.position();

    AABB box = new AABB(playVec.x + look.x * r1 - r1, playVec.y + look.y * r1 - r1, playVec.z + look.z * r1 - r2, playVec.x + look.x * r1 + r1, playVec.y + look.y * r1 + r1, playVec.z + look.z * r1 + r1);
    pLevel.getEntities(pPlayer, box, entity -> true /* TODO: better testing */).forEach(o -> {
      flingEntity(o, look, motion);
    });
  }

  private void flingEntity(Entity entity, Vec3 look, float motion) {
    Vec3 movement = entity.getDeltaMovement();
    entity.hasImpulse = true;
    entity.setDeltaMovement(movement.x + look.x, movement.y + (motion * 0.7), movement.z + look.z);
  }
}
