package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualFrost;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class  RitualFrost extends RitualBase {

  public RitualFrost(String name, int duration) {
    super(name, duration);
    addCondition(new ConditionItems(
        new ItemStack(Items.SNOWBALL),
        new ItemStack(ModItems.dewgonia),
        new ItemStack(Blocks.SNOW),
        new ItemStack(ModItems.bark_spruce),
        new ItemStack(ModItems.bark_spruce)
    ));
    setIcon(ModItems.ritual_frost);
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualFrost.class);
  }

}
