package epicsquid.roots.item.terrastone;

import epicsquid.mysticallib.item.ItemAxeBase;
import epicsquid.roots.item.ILivingRepair;
import epicsquid.roots.recipe.ingredient.RootsIngredients;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;

public class ItemTerrastoneAxe extends ItemAxeBase implements ILivingRepair {
  public ItemTerrastoneAxe(ToolMaterial material, String name) {
    super(material, name, 3, 565, () -> Ingredient.EMPTY);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected, 20);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && RootsIngredients.MOSSY_COBBLESTONE.test(repair);
  }

  // TODO: Update
  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    Material material = state.getMaterial();
    return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
  }
}
