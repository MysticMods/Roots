package epicsquid.mysticallib.item;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.CustomModelItem;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBowBase extends ItemBow implements IModeledObject, ICustomModeledObject {
  private boolean hasCustomModel = false;
  private final int enchantability;

  public ItemBowBase(String name, int maxDamage, int enchantability) {
    super();
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
    setMaxDamage(maxDamage);
    this.enchantability = enchantability;
  }

  @Override
  public int getItemEnchantability() {
    return enchantability;
  }

  public ItemBowBase setModelCustom(boolean custom) {
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
}
