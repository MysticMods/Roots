package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.ritual.EntityRitualPurity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualPurity extends RitualBase {

  public RitualPurity(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredients(new ItemStack(ModItems.spirit_herb, 1), new ItemStack(ModItems.spirit_herb, 1), new ItemStack(ModItems.wildroot, 1),
        new ItemStack(Items.MILK_BUCKET, 1), new ItemStack(Items.GLASS_BOTTLE, 1));
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualPurity.class);
  }
}
