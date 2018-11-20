package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.ritual.EntityRitualWarden;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualWarden extends RitualBase {

  public RitualWarden(String name, int duration) {
    super(name, duration);
    addIngredients(new ItemStack(Items.DYE, 1, 15), new ItemStack(ModItems.pereskia_bulb, 1), new ItemStack(ModItems.spirit_herb, 1),
        new ItemStack(ModItems.bark_oak, 1), new ItemStack(Items.IRON_CHESTPLATE, 1));
  }

  @Override
  public boolean canFire(World world, BlockPos pos, EntityPlayer player) {
    return this.getStandingStones(world, pos, 3, null) >= 3 && this.getStandingStones(world, pos, 4, null) >= 3;
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualWarden.class);
  }
}