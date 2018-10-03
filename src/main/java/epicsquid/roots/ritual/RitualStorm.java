package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.EntityRitualStorm;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualStorm extends RitualBase {
  public RitualStorm(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredient(new ItemStack(Blocks.WATERLILY, 1));
    addIngredient(new ItemStack(ModItems.bark_oak, 1));
    addIngredient(new ItemStack(Blocks.VINE, 1));
    addIngredient(new ItemStack(ModItems.wildroot, 1));
    addIngredient(new ItemStack(Items.BEETROOT_SEEDS, 1));
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualStorm.class);
  }
}