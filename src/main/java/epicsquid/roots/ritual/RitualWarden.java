package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.EntityRitualWarden;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualWarden extends RitualBase {

  public RitualWarden(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredients(
        new ItemStack(Items.DYE, 1, 15),
        new ItemStack(ModItems.pereskia_bulb, 1),
        new ItemStack(ModItems.spirit_herb, 1),
        new ItemStack(ModItems.bark_oak, 1),
        new ItemStack(Items.IRON_CHESTPLATE, 1)
    );
  }

  @Override
  public boolean isValidForPos(World world, BlockPos pos) {
    return this.getThreeHighStandingStones(world, pos) >= 3 && this.getFourHighStandingStones(world, pos) >= 3;
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualWarden.class);
  }
}