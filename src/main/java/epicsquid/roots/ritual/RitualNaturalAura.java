package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualNaturalAura;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionStandingStones;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualNaturalAura extends RitualBase {

  public RitualNaturalAura(String name, int duration) {
    super(name, duration);
    addCondition(new ConditionItems(
            new ItemStack(ModItems.spirit_herb),
            new ItemStack(ModItems.wildroot), 
            new ItemStack(ModItems.aubergine),
            new ItemStack(Items.DYE, 1, 15),
            new ItemStack(Items.DYE, 1, 15)
    ));
    addCondition(new ConditionStandingStones(3, 3));
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualNaturalAura.class);
  }
}
