package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualWindwall;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualWindwall extends RitualBase {
  public RitualWindwall(String name, int duration, boolean disabled) {
    super(name, duration, disabled);
  }

  @Override
  public void init () {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.cloud_berry),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.bark_spruce),
        new ItemStack(ModItems.bark_birch),
        new ItemStack(Items.FEATHER)
    ));
    setIcon(ModItems.ritual_windwall);
    setColor(TextFormatting.DARK_AQUA);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualWindwall.class);
  }
}