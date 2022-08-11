package epicsquid.roots.entity.ritual;

import epicsquid.roots.block.BlockPyre;
import epicsquid.roots.entity.EntityLifetimeBase;
import net.minecraft.world.World;

public abstract class EntityRitualBase extends EntityLifetimeBase implements IRitualEntity {
	public EntityRitualBase(World worldIn) {
		super(worldIn);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if (!world.isRemote && !(world.getBlockState(getPosition()).getBlock() instanceof BlockPyre)) {
			setDead();
		}
	}
}
