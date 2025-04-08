package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DandelionWindsSpell extends Spell {
  private float distance, vertical;
  private double r1, r2;

  public DandelionWindsSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0xffff20, 0xffb020);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.DANDELION_WINDS_COOLDOWN;
  }

  @Override
  public void buildProperties (List<PropertyHolder<?>> result) {
    super.buildProperties(result);
    result.add(ModSpells.DANDELION_WINDS_DISTANCE);
    result.add(ModSpells.DANDELION_WINDS_RANGE_1);
    result.add(ModSpells.DANDELION_WINDS_RANGE_2);
    result.add(ModSpells.DANDELION_WINDS_VERTICAL);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.distance = properties.get(ModSpells.DANDELION_WINDS_DISTANCE);
    this.r1 = properties.get(ModSpells.DANDELION_WINDS_RANGE_1);
    this.r2 = properties.get(ModSpells.DANDELION_WINDS_RANGE_2);
    this.vertical = properties.get(ModSpells.DANDELION_WINDS_VERTICAL);
  }

  // TODO: Taggable entities for being moved/not being moved
  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    Vec3 look = pPlayer.getLookAngle();
    float d = distance;
    float motion = d * d + d;
    Vec3 playVec = pPlayer.position();

    AABB box = new AABB(playVec.x + look.x * r1 - r1, playVec.y + look.y * r1 - r1, playVec.z + look.z * r1 - r2, playVec.x + look.x * r1 + r1, playVec.y + look.y * r1 + r1, playVec.z + look.z * r1 + r1);
    int moved = 0;
    for (Entity entity : pLevel.getEntities(pPlayer, box, entity -> true /* TODO: better testing */)) {
      flingEntity(entity, look, motion);
      moved++;
    }
    if (moved == 0) {
      costs.noCharge();
      return 0;
    }
    return cooldown;
  }

  private void flingEntity(Entity entity, Vec3 look, float motion) {
    Vec3 movement = entity.getDeltaMovement();
    entity.hasImpulse = true;
    entity.setDeltaMovement(movement.x + look.x, movement.y + (motion * vertical), movement.z + look.z);
  }
}
