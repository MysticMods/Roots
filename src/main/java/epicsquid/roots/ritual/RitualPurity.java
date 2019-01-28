package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.ritual.EntityRitualPurity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualPurity extends RitualBase {

  public RitualPurity(String name, int duration) {
    super(name, duration);
    addIngredients(
            new ItemStack(ModItems.terra_moss), 
            new ItemStack(ModItems.aubergine), 
            new ItemStack(ModItems.wildroot),
            new ItemStack(Items.MILK_BUCKET), 
            new ItemStack(Items.GLASS_BOTTLE)
    );
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualPurity.class);
  }
}
