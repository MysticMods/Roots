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
import teamroots.roots.entity.EntityRitualWindwall;

public class RitualWindwall extends RitualBase {
	public RitualWindwall(String name, int duration, boolean doUpdateValidity){
		super(name,duration,doUpdateValidity);
		addIngredient(new ItemStack(RegistryManager.pereskia_blossom,1));
		addIngredient(new ItemStack(RegistryManager.pereskia_bulb,1));
		addIngredient(new ItemStack(RegistryManager.bark_spruce,1));
		addIngredient(new ItemStack(RegistryManager.bark_birch,1));
		addIngredient(new ItemStack(Items.FEATHER,1));
	}
	
	@Override
	public boolean isValidForPos(World world, BlockPos pos){
		BlockPos topPos = world.getTopSolidOrLiquidBlock(pos);
		return topPos.getY() == pos.getY() || topPos.getY() == pos.getY()+1;
	}
	
	@Override
	public void doEffect(World world, BlockPos pos){
		List<EntityRitualWindwall> pastRituals = world.getEntitiesWithinAABB(EntityRitualWindwall.class, new AxisAlignedBB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+100,pos.getZ()+1));
		if (pastRituals.size() == 0 && !world.isRemote){
			EntityRitualWindwall ritual = new EntityRitualWindwall(world);
			ritual.setPosition(pos.getX()+0.5, pos.getY()+6.5, pos.getZ()+0.5);
			world.spawnEntity(ritual);
		}
		else if (pastRituals.size() > 0){
			for (EntityRitualWindwall ritual : pastRituals){
				ritual.getDataManager().set(EntityRitualWindwall.lifetime, duration+20);
				ritual.getDataManager().setDirty(EntityRitualWindwall.lifetime);
			}
		}
	}
}
