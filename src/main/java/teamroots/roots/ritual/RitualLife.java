package teamroots.roots.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.roots.RegistryManager;
import teamroots.roots.entity.EntityRitualLife;

public class RitualLife extends RitualBase {
	public RitualLife(String name, int duration, boolean doUpdateValidity){
		super(name,duration,doUpdateValidity);
		addIngredient(new ItemStack(RegistryManager.terra_moss_ball,1));
		addIngredient(new ItemStack(RegistryManager.bark_oak,1));
		addIngredient(new ItemStack(RegistryManager.bark_birch,1));
		addIngredient(new ItemStack(RegistryManager.bark_birch,1));
		addIngredient(new ItemStack(Blocks.SAPLING,1,2));
	}
	
	@Override
	public boolean isValidForPos(World world, BlockPos pos){
		int birchTreeCount = 0;
		for (int i = -9; i < 10; i ++){
			for (int j = -9; j < 10; j ++){
				IBlockState state = world.getBlockState(pos.add(i, 1, j));
				if (state.getBlock() instanceof BlockOldLog){
					if (state.getValue(BlockOldLog.VARIANT) == EnumType.BIRCH){
						birchTreeCount ++;
					}
				}
			}
		}
		return birchTreeCount >= 4;
	}
	
	@Override
	public void doEffect(World world, BlockPos pos){
		List<EntityRitualLife> pastRituals = world.getEntitiesWithinAABB(EntityRitualLife.class, new AxisAlignedBB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+100,pos.getZ()+1));
		if (pastRituals.size() == 0 && !world.isRemote){
			EntityRitualLife ritual = new EntityRitualLife(world);
			ritual.setPosition(pos.getX()+0.5, pos.getY()+6.5, pos.getZ()+0.5);
			world.spawnEntity(ritual);
		}
		else if (pastRituals.size() > 0){
			for (EntityRitualLife ritual : pastRituals){
				ritual.getDataManager().set(EntityRitualLife.lifetime, duration+20);
				ritual.getDataManager().setDirty(EntityRitualLife.lifetime);
			}
		}
	}
}
