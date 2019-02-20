package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.ritual.EntityRitualWarden;
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
        new ItemStack(ModItems.pereskia_bulb),
        new ItemStack(ModItems.pereskia),
        new ItemStack(ModItems.bark_oak),
        new ItemStack(Items.IRON_CHESTPLATE)
    ));
    addCondition(new ConditionStandingStones(3, 3));
    addCondition(new ConditionStandingStones(4, 3));
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualWarden.class);
  }
}