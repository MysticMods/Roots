package epicsquid.mysticallib.item;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.CustomModelItem;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemDoor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ItemDoorBase extends ItemDoor implements ICustomModeledObject, IModeledObject {

  private boolean hasCustomModel;

  public ItemDoorBase(Block block, String name) {
    super(block);
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
  }

  public ItemDoorBase setModelCustom(boolean custom) {
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
}
