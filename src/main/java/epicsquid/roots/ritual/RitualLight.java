package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.EntityRitualLight;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualLight extends RitualBase {
  public RitualLight(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredient(new ItemStack(ModItems.moonglow_leaf, 1));
    addIngredient(new ItemStack(ModItems.moonglow_seed, 1));
    addIngredient(new ItemStack(ModItems.bark_birch, 1));
    addIngredient(new ItemStack(ModItems.bark_acacia, 1));
    addIngredient(new ItemStack(Blocks.LOG, 1, 2));
  }

  @Override
  public boolean isValidForPos(World world, BlockPos pos) {
    return world.getWorldTime() % 24000 < 13000;
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualLight.class);
  }
}