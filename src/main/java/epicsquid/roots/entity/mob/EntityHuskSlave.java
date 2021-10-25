package epicsquid.roots.entity.mob;

import epicsquid.roots.util.EntityUtil;
import epicsquid.roots.util.SlaveUtil;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class EntityHuskSlave extends HuskEntity {
  public EntityHuskSlave(World worldIn) {
    super(worldIn);
  }


  @Override
  public String getName() {
    if (hasCustomName()) {
      return super.getName();
    }

    return I18n.translateToLocal("entity.Husk.name");
  }

  @Override
  protected void applyEntityAI() {
    this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, MonsterEntity.class, 10, false, false, o -> EntityUtil.isHostile(o) && !SlaveUtil.isSlave(o)));
  }
}
