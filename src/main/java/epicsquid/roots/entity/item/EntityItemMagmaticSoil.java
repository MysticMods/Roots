package epicsquid.roots.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityItemMagmaticSoil extends EntityItem {
	public EntityItemMagmaticSoil(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}
	
	public EntityItemMagmaticSoil(World worldIn, double x, double y, double z, ItemStack stack) {
		super(worldIn, x, y, z, stack);
	}
	
	public EntityItemMagmaticSoil(World worldIn) {
		super(worldIn);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.world.isRemote || this.isDead) {
			return false;
		}
		
		if (source.isFireDamage()) {
			return false;
		}
		
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	public boolean isEntityInvulnerable(DamageSource source) {
		return source.isFireDamage() || super.isEntityInvulnerable(source);
	}
}
