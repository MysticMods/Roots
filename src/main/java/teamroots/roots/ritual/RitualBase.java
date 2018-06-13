package teamroots.roots.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualBase {
	public List<ItemStack> ingredients = new ArrayList<ItemStack>();
	public String name = "";
	public int duration = 0;
	public boolean updateValidity = false;
	public RitualBase(String name, int duration, boolean doUpdateValidity){
		this.name = name;
		this.duration = duration;
		this.updateValidity = doUpdateValidity;
	}
	public RitualBase addIngredient(ItemStack stack){
		ingredients.add(stack);
		return this;
	}
	
	public boolean isValidForPos(World world, BlockPos pos){
		return true;
	}
	
	public void doEffect(World world, BlockPos pos){
		
	}
}
