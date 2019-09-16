package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualAnimalHarvest;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionStandingStones;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualAnimalHarvest extends RitualBase {
  public RitualAnimalHarvest(String name, int duration, boolean disabled) {
    super(name, duration, disabled);
  }

  @Override
  public void init () {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.wildewheet),
        new OreIngredient("blockWool"),
        new ItemStack(Items.MELON),
        new ItemStack(Items.CARROT),
        new ItemStack(ModItems.wildroot)
    ));
    addCondition(new ConditionStandingStones(3, 3));
    setIcon(ModItems.ritual_animal_harvest);
    setColor(TextFormatting.GOLD);
    setBold(true);
  }


  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualAnimalHarvest.class);
  }
}
