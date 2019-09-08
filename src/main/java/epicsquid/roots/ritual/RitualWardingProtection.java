package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualWardingProtection;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionStandingStones;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualWardingProtection extends RitualBase {

  public RitualWardingProtection(String name, int duration, boolean disabled) {
    super(name, duration, disabled);
    addCondition(new ConditionItems(
        new ItemStack(Items.SPECKLED_MELON),
        new ItemStack(ModItems.stalicripe),
        new ItemStack(ModItems.wildroot),
        new OreIngredient("rootsBark"),
        new ItemStack(Items.IRON_CHESTPLATE)
    ));
    addCondition(new ConditionStandingStones(3, 3));
    addCondition(new ConditionStandingStones(4, 3));
    setIcon(ModItems.ritual_warding_protection);
    setColor(TextFormatting.DARK_BLUE);
    setBold(true);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualWardingProtection.class);
  }
}