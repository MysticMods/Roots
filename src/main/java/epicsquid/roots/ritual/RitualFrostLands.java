package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualFrostLands;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualFrostLands extends RitualBase implements IColdRitual {
  public RitualFrostLands(String name, int duration, boolean disabled) {
    super(name, duration, disabled);
    addCondition(new ConditionItems(
        new ItemStack(Items.SNOWBALL),
        new ItemStack(ModItems.dewgonia),
        new ItemStack(Blocks.SNOW),
        new ItemStack(ModItems.bark_spruce),
        new ItemStack(ModItems.bark_spruce)
    ));
    setIcon(ModItems.ritual_frost_lands);
    setColor(TextFormatting.AQUA);
    setBold(true);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualFrostLands.class);
  }

}
