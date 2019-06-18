package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualPurity;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualPurity extends RitualBase {

  public RitualPurity(String name, int duration) {
    super(name, duration);
    addCondition(new ConditionItems(
            new ItemStack(ModItems.terra_moss),
            new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
            new ItemStack(ModBlocks.baffle_cap_mushroom),
            new ItemStack(Items.MILK_BUCKET), 
            new ItemStack(Items.GLASS_BOTTLE)
    ));
    setIcon(ModItems.ritual_purity);
    setColor(TextFormatting.LIGHT_PURPLE);
    setBold(true);
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualPurity.class);
  }
}
