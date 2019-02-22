package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.ritual.EntityRitualStorm;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualStorm extends RitualBase {
  public RitualStorm(String name, int duration) {
    super(name, duration);
    addCondition(new ConditionItems(
            new ItemStack(Blocks.WATERLILY),
            new ItemStack(ModItems.dewgonia),
            new ItemStack(Blocks.VINE),
            new ItemStack(ModItems.cloud_berry),
            new ItemStack(Items.BEETROOT_SEEDS)
    ));
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualStorm.class);
  }
}