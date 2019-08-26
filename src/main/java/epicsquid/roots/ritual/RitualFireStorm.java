package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualFireStorm;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionStandingStones;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualFireStorm extends RitualBase {

  public RitualFireStorm(String name, int duration) {
    super(name, duration);
    addCondition(new ConditionItems(
        new ItemStack(ModItems.infernal_bulb),
        new ItemStack(ModItems.bark_acacia),
        new ItemStack(Items.COAL),
        new ItemStack(ModItems.bark_acacia),
        new ItemStack(Items.BLAZE_POWDER)
    ));
    addCondition(new ConditionStandingStones(3, 3));
    setIcon(ModItems.ritual_fire_storm);
    setColor(TextFormatting.RED);
    setBold(true);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualFireStorm.class);
  }
}