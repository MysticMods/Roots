package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualLight;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionWorldTime;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualLight extends RitualBase {
  public RitualLight(String name, int duration) {
    super(name, duration);
    addCondition(new ConditionItems(
            new ItemStack(ModItems.pereskia),
            new ItemStack(ModItems.cloud_berry),
            new ItemStack(ModItems.bark_birch),
            new ItemStack(ModItems.bark_oak),
            new ItemStack(Items.GLOWSTONE_DUST)
    ));
    addCondition(new ConditionWorldTime(0, 13000));
    setIcon(ModItems.ritual_light);
    setColor(TextFormatting.YELLOW);
    setBold(true);
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualLight.class);
  }
}