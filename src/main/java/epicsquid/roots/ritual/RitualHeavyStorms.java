package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualHeavyStorms;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualHeavyStorms extends RitualBase {
  public RitualHeavyStorms(String name, int duration) {
    super(name, duration);
    addCondition(new ConditionItems(
            new ItemStack(Blocks.WATERLILY),
            new ItemStack(ModItems.dewgonia),
            new ItemStack(Blocks.VINE),
            new ItemStack(ModItems.cloud_berry),
            new ItemStack(Items.BEETROOT_SEEDS)
    ));
    setIcon(ModItems.ritual_heavy_storms);
    setColor(TextFormatting.DARK_AQUA);
    setBold(true);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualHeavyStorms.class);
  }
}