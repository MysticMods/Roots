package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.ritual.EntityRitualFireStorm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualFireStorm extends RitualBase {

  public RitualFireStorm(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredients(new ItemStack(ModItems.wildroot, 1), new ItemStack(ModItems.bark_acacia, 1), new ItemStack(Items.COAL, 1),
        new ItemStack(ModItems.bark_acacia, 1), new ItemStack(Items.BLAZE_POWDER, 1));
  }

  @Override
  public boolean canFire(World world, BlockPos pos, EntityPlayer player) {
    return this.getThreeHighStandingStones(world, pos) >= 3;
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualFireStorm.class);
  }
}