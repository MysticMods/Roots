package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.ritual.EntityRitualLight;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualLight extends RitualBase {
  public RitualLight(String name, int duration) {
    super(name, duration);
    addIngredients(
            new ItemStack(ModItems.pereskia), 
            new ItemStack(ModItems.pereskia_bulb), 
            new ItemStack(ModItems.bark_birch),
            new ItemStack(ModItems.bark_acacia), 
            new ItemStack(Items.GLOWSTONE_DUST)
    );

  }

  @Override
  public boolean canFire(World world, BlockPos pos, EntityPlayer player) {
    return world.getWorldTime() % 24000 < 13000;
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualLight.class);
  }
}