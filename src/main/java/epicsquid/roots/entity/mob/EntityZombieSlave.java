package epicsquid.roots.entity.mob;

import epicsquid.roots.util.EntityUtil;
import epicsquid.roots.util.SlaveUtil;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class EntityZombieSlave extends EntityZombie {
	public EntityZombieSlave(World worldIn) {
		super(worldIn);
	}
	
	
	@Override
	public String getName() {
		if (hasCustomName()) {
			return super.getName();
		}
		
		return I18n.translateToLocal("entity.Zombie.name");
	}
	
	@Override
	protected void applyEntityAI() {
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityMob.class, 10, false, false, o -> EntityUtil.isHostile(o) && !SlaveUtil.isSlave(o)));
	}
}
