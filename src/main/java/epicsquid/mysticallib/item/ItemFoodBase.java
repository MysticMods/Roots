package epicsquid.mysticallib.item;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.CustomModelItem;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ItemFoodBase extends ItemFood implements IModeledObject, ICustomModeledObject {

  private boolean hasCustomModel;

  public ItemFoodBase(@Nonnull String name, int amount, float saturation, boolean isWolfFood) {
    super(amount, saturation, isWolfFood);
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
  }

  public ItemFoodBase(@Nonnull String name, int amount, boolean isWolfFood) {
    this(name, amount, 0.6F, isWolfFood);
  }

  public ItemFoodBase setModelCustom(boolean custom) {
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
      ResourceLocation texture = new ResourceLocation(getRegistryName().getNamespace() + ":items/" + getRegistryName().getPath());
      CustomModelItem item = new CustomModelItem(false, texture);
      item.addTexture("particle", texture);
      CustomModelLoader.itemmodels.put(getRegistryName(), item);
    }
  }
}
