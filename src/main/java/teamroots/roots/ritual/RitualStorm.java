package teamroots.roots.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.roots.RegistryManager;
import teamroots.roots.entity.EntityRitualLife;
import teamroots.roots.entity.EntityRitualStorm;

public class RitualStorm extends RitualBase {
	public RitualStorm(String name, int duration, boolean doUpdateValidity){
		super(name,duration,doUpdateValidity);
		addIngredient(new ItemStack(Blocks.WATERLILY,1));
		addIngredient(new ItemStack(RegistryManager.bark_oak,1));
		addIngredient(new ItemStack(Blocks.VINE,1));
		addIngredient(new ItemStack(RegistryManager.wildroot_item,1));
		addIngredient(new ItemStack(Items.BEETROOT_SEEDS,1));
	}
	
	@Override
	public void doEffect(World world, BlockPos pos){
		List<EntityRitualStorm> pastRituals = world.getEntitiesWithinAABB(EntityRitualStorm.class, new AxisAlignedBB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+100,pos.getZ()+1));
		if (pastRituals.size() == 0 && !world.isRemote){
			EntityRitualStorm ritual = new EntityRitualStorm(world);
			ritual.setPosition(pos.getX()+0.5, pos.getY()+6.5, pos.getZ()+0.5);
			world.spawnEntity(ritual);
		}
		else if (pastRituals.size() > 0){
			for (EntityRitualStorm ritual : pastRituals){
				ritual.getDataManager().set(EntityRitualStorm.lifetime, duration+20);
				ritual.getDataManager().setDirty(EntityRitualStorm.lifetime);
			}
		}
	}
}
