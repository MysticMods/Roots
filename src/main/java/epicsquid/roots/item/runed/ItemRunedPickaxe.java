package epicsquid.roots.item.runed;

import epicsquid.mysticallib.item.tool.ItemHammerBase;
import epicsquid.roots.config.ToolConfig;
import epicsquid.roots.item.ILivingRepair;
import epicsquid.roots.recipe.ingredient.RootsIngredients;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;

import java.util.Set;

public class ItemRunedPickaxe extends ItemHammerBase implements ILivingRepair {
  public ItemRunedPickaxe(ToolMaterial material, String name) {
    super(name, 1992, material, () -> Ingredient.EMPTY); // TODO: VALUES
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected, 90);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && RootsIngredients.RUNED_OBSIDIAN.test(repair);
  }

  @Override
  public Set<Block> getBlockBlacklist() {
    return ToolConfig.getRunicBlockBlacklist();
  }
}
