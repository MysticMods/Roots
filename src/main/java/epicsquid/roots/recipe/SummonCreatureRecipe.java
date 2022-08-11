package epicsquid.roots.recipe;

import com.google.common.collect.Lists;
import epicsquid.roots.tileentity.TileEntityPyre;
import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SummonCreatureRecipe extends RegistryItem implements IRootsRecipe<TileEntityPyre> {
	private Class<? extends EntityLivingBase> clazz;
	private List<Ingredient> ingredients;
	
	public SummonCreatureRecipe(ResourceLocation resource, Class<? extends EntityLivingBase> clazz, Ingredient... ingredients) {
		this.setRegistryName(resource);
		this.clazz = clazz;
		this.ingredients = Lists.newArrayList(ingredients);
	}
	
	public SummonCreatureRecipe(ResourceLocation resource, Class<? extends EntityLivingBase> clazz, List<Ingredient> ingredients) {
		this.setRegistryName(resource);
		this.clazz = clazz;
		this.ingredients = ingredients;
	}
	
	public Class<? extends EntityLivingBase> getClazz() {
		return clazz;
	}
	
	@Override
	public List<Ingredient> getIngredients() {
		return ingredients;
	}
	
	@Nullable
	public EntityLivingBase getEntity(World world) {
		try {
			return clazz.getConstructor(World.class).newInstance(world);
		} catch (Exception e) {
			return null;
		}
	}
}
