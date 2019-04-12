package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualWarden;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionStandingStones;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualWarden extends RitualBase {

  public RitualWarden(String name, int duration) {
    super(name, duration);
    addCondition(new ConditionItems(
        new ItemStack(Items.SPECKLED_MELON),
        new ItemStack(ModItems.stalicripe),
        new ItemStack(ModItems.wildroot),
        new ItemStack(ModItems.bark_oak),
        new ItemStack(Items.IRON_CHESTPLATE)
    ));
    addCondition(new ConditionStandingStones(3, 3));
    addCondition(new ConditionStandingStones(4, 3));
    setIcon(ModItems.ritual_warden);
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualWarden.class);
  }
}