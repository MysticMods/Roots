package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

public class BlockRunedObsidian extends BlockBase {
	public BlockRunedObsidian(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
		super(mat, type, hardness, name);
		setResistance(5000f);
	}
	
	public static boolean checkEntityBlocked(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
		if (entity instanceof net.minecraft.entity.boss.EntityDragon || (entity instanceof net.minecraft.entity.boss.EntityWither) || (entity instanceof net.minecraft.entity.projectile.EntityWitherSkull)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
		if (BlockRunedObsidian.checkEntityBlocked(state, world, pos, entity)) {
			return false;
		}
		
		return super.canEntityDestroy(state, world, pos, entity);
	}
}
