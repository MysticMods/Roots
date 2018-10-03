package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.EntityRitualRegrowth;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualRegrowth extends RitualBase {
  public RitualRegrowth(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredient(new ItemStack(ModItems.terra_moss, 1));
    addIngredient(new ItemStack(ModItems.terra_moss_seed, 1));
    addIngredient(new ItemStack(ModItems.bark_spruce, 1));
    addIngredient(new ItemStack(Blocks.SAPLING, 1, 1));
    addIngredient(new ItemStack(Blocks.SAPLING, 1));
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualRegrowth.class);
  }
}