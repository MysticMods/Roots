package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualOvergrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualOvergrowth extends RitualBase {

  public RitualOvergrowth(String name, int duration, boolean disabled) {
    super(name, duration, disabled);
  }

  @Override
  public void init () {
    addCondition(
        new ConditionItems(
            new ItemStack(Items.REEDS),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(Blocks.TALLGRASS, 1, 1),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));
    setIcon(ModItems.ritual_overgrowth);
    setColor(TextFormatting.DARK_GREEN);
    setBold(true);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualOvergrowth.class);
  }

}
