package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualAnimalHarvest;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionStandingStones;
import epicsquid.roots.ritual.RitualBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualAnimalHarvest extends RitualBase {
  public RitualAnimalHarvest(String name, int duration) {
    super(name, duration);

    addCondition(new ConditionItems(
            new ItemStack(ModItems.wildewheet),
            new OreIngredient("blockWool"),
            new ItemStack(Items.MELON),
            new ItemStack(Items.CARROT),
            new ItemStack(ModItems.wildroot)
    ));
    addCondition(new ConditionStandingStones(3, 3));
  }


  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualAnimalHarvest.class);
  }
}
