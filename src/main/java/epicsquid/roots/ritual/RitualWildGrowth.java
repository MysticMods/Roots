package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualWildGrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualWildGrowth extends RitualBase {

  public RitualWildGrowth(String name, int duration, boolean disabled) {
    super(name, duration, disabled);

    addCondition(new ConditionItems(
        new ItemStack(ModItems.wildroot),
        new OreIngredient("rootsBark"),
        new OreIngredient("rootsBark"),
        new ItemStack(ModItems.bark_dark_oak),
        new ItemStack(ModItems.spirit_herb))
    );
    setIcon(ModItems.ritual_wild_growth);
    setColor(TextFormatting.DARK_GRAY);
    setBold(true);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualWildGrowth.class);
  }
}
