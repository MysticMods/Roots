package epicsquid.mysticallib.item;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.CustomModelItem;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nullable;

public class ItemArmorBase extends ItemArmor implements IModeledObject, ICustomModeledObject {
  private boolean hasCustomModel;

  private String armor1;
  private String armor2;

  public ItemArmorBase(String name, ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn, ResourceLocation armor) {
    super(materialIn, 0, equipmentSlotIn);
    this.armor1 = armor.toString() + "_1.png";
    this.armor2 = armor.toString() + "_2.png";
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
  }

  @Nullable
  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
    if (slot == EntityEquipmentSlot.LEGS) {
      return armor2;
    }

    return armor1;
  }

  public ItemArmorBase setModelCustom(boolean custom) {
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
