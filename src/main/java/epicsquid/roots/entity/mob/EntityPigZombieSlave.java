package epicsquid.roots.entity.mob;

import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class EntityPigZombieSlave extends EntityPigZombie {
  public EntityPigZombieSlave(World worldIn) {
    super(worldIn);
  }

  @Override
  public String getName() {
    if (hasCustomName()) {
      return super.getName();
    }

    return I18n.translateToLocal("entity.PigZombie.name");
  }

  @Override
  protected void applyEntityAI() {
    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityMob.class, 10, false, false, EntityUtil::isHostile));
  }
}
