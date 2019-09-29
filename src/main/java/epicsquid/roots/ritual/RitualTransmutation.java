package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualTransmutation;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualTransmutation extends RitualBase {

  public RitualTransmutation(String name, int duration, boolean disabled) {
    super(name, duration, disabled);
    addCondition(
        new ConditionItems(
            new ItemStack(Blocks.FURNACE),
            new ItemStack(Blocks.MOSSY_COBBLESTONE),
            new ItemStack(ModItems.cloud_berry),
            new OreIngredient("rootsBark"),
            new ItemStack(ModBlocks.chiseled_runestone)));
    addCondition(new ConditionStandingStones(3, 1));
    setIcon(ModItems.ritual_transmutation);
    setColor(TextFormatting.DARK_PURPLE);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualTransmutation.class);
  }

}
