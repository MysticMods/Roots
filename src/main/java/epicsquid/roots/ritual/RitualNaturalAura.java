package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.ritual.EntityRitualNaturalAura;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualNaturalAura extends RitualBase {

  public RitualNaturalAura(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredients(new ItemStack(ModItems.wildroot, 1), new ItemStack(ModItems.wildroot, 1), new ItemStack(ModItems.spirit_herb, 1),
        new ItemStack(Items.BONE, 1), new ItemStack(Items.BONE, 1));
  }

  @Override
  public boolean canFire(World world, BlockPos pos, EntityPlayer player) {
    return this.getThreeHighStandingStones(world, pos) >= 3;
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualNaturalAura.class);
  }
}
