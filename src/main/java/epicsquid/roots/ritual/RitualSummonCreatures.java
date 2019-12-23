package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualSummonCreatures;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.SummonCreatureRecipe;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.ritual.conditions.ConditionValidSummon;
import epicsquid.roots.tileentity.TileEntityOffertoryPlate;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class RitualSummonCreatures extends RitualBase {
  public RitualSummonCreatures(String name, int duration, boolean disabled) {
    super(name, duration, disabled);

    addCondition(new ConditionItems(
        new ItemStack(Items.WHEAT_SEEDS),
        new ItemStack(Items.WHEAT),
        new ItemStack(Items.EGG),
        new ItemStack(Items.ROTTEN_FLESH),
        new ItemStack(Items.WHEAT_SEEDS)
    ));
    addCondition(new ConditionValidSummon());

    setIcon(ModItems.ritual_summon_creatures);
    setColor(TextFormatting.DARK_PURPLE);
    setBold(true);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    EntityRitualSummonCreatures entity = (EntityRitualSummonCreatures) this.spawnEntity(world, pos, EntityRitualSummonCreatures.class);
    if (!world.isRemote) {
      List<TileEntityOffertoryPlate> plates = RitualUtil.getNearbyOfferingPlates(world, pos);
      List<ItemStack> plateItems = RitualUtil.getItemsFromNearbyPlates(plates);

      SummonCreatureRecipe recipe = ModRecipes.findSummonCreatureEntry(plateItems);
      if (recipe != null) {
        for (TileEntityOffertoryPlate plate : plates) {
          plate.removeItem();
        }
      }
      ItemStack essence = ItemStack.EMPTY;
      if (recipe == null) {
        for (TileEntityOffertoryPlate plate : plates) {
          ItemStack stack = plate.getHeldItem();
          if (stack.getItem() == ModItems.life_essence) {
            essence = stack;
            plate.removeItem();
            break;
          }
        }
      }

      if (entity != null) {
        entity.setEssence(essence);
        entity.setSummonRecipe(recipe);
      }
    }

    return entity;
  }
}
