package epicsquid.mysticallib.item;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.CustomModelItem;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import java.util.function.Supplier;

public class ItemShearsBase extends ShearsItem implements ICustomModeledObject, IModeledObject {

  private boolean hasCustomModel;
  protected Supplier<Ingredient> repairIngredient;

  public ItemShearsBase(String name, Supplier<Ingredient> repairIngredient) {
    super();
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
    setMaxStackSize(1);
    this.repairIngredient = repairIngredient;
  }

  public ItemShearsBase setModelCustom(boolean custom) {
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
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return repairIngredient.get().test(repair);
  }
}
