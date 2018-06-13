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
import teamroots.roots.entity.EntityRitualFireStorm;
import teamroots.roots.entity.EntityRitualLife;
import teamroots.roots.entity.EntityRitualLight;
import teamroots.roots.entity.EntityRitualRegrowth;

public class RitualRegrowth extends RitualBase {
	public RitualRegrowth(String name, int duration, boolean doUpdateValidity){
		super(name,duration,doUpdateValidity);
		addIngredient(new ItemStack(RegistryManager.terra_moss_ball,1));
		addIngredient(new ItemStack(RegistryManager.terra_moss_spore,1));
		addIngredient(new ItemStack(RegistryManager.bark_spruce,1));
		addIngredient(new ItemStack(Blocks.SAPLING,1,1));
		addIngredient(new ItemStack(Blocks.SAPLING,1));
	}
	
	@Override
	public void doEffect(World world, BlockPos pos){
		List<EntityRitualRegrowth> pastRituals = world.getEntitiesWithinAABB(EntityRitualRegrowth.class, new AxisAlignedBB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+100,pos.getZ()+1));
		if (pastRituals.size() == 0 && !world.isRemote){
			EntityRitualRegrowth ritual = new EntityRitualRegrowth(world);
			ritual.setPosition(pos.getX()+0.5, pos.getY()+6.5, pos.getZ()+0.5);
			world.spawnEntity(ritual);
		}
		else if (pastRituals.size() > 0){
			for (EntityRitualRegrowth ritual : pastRituals){
				ritual.getDataManager().set(EntityRitualRegrowth.lifetime, duration+20);
				ritual.getDataManager().setDirty(EntityRitualRegrowth.lifetime);
			}
		}
	}
}
