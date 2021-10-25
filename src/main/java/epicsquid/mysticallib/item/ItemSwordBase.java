package epicsquid.mysticallib.item;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.CustomModelItem;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.types.OneTimeSupplier;
import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import java.util.function.Supplier;

public class ItemSwordBase extends ItemSword implements IModeledObject, ICustomModeledObject {

  private boolean hasCustomModel = false;
  protected Supplier<Ingredient> repairIngredient;

  public ItemSwordBase(ToolMaterial material, String name, int maxDamage, Supplier<Ingredient> repairIngredient) {
    super(material);
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
    setMaxDamage(maxDamage);
    this.repairIngredient = repairIngredient;
  }

  public ItemSwordBase setModelCustom(boolean custom) {
    this.hasCustomModel = custom;
    return this;
  }

  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "handlers"));
  }

  @Override
  public void initCustomModel() {
    if (this.hasCustomModel) {
      CustomModelLoader.itemmodels
          .put(getRegistryName(), new CustomModelItem(false, new ResourceLocation(getRegistryName().getNamespace() + ":items/" + getRegistryName().getPath())));
    }
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
    return !ItemUtil.equalWithoutDamage(oldStack, newStack);
  }

  @Override
  public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
    return !ItemUtil.equalWithoutDamage(oldStack, newStack);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return repairIngredient.get().test(repair);
  }
}
