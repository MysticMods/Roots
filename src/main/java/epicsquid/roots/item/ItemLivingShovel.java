package epicsquid.roots.item;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.CustomModelItem;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.util.Util;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemLivingShovel extends ItemSpade implements IModeledObject, ICustomModeledObject {

  private boolean hasCustomModel = false;

  public ItemLivingShovel(ToolMaterial material, String name) {
    super(material);
    setUnlocalizedName(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
    setHarvestLevel("shovel", 3);
    setMaxDamage(192);
  }

  @Override
  public int getItemEnchantability() {
    return 22;
  }

  public ItemLivingShovel setModelCustom(boolean custom) {
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
      CustomModelLoader.itemmodels.put(getRegistryName(),
          new CustomModelItem(false, new ResourceLocation(getRegistryName().getResourceDomain() + ":items/" + getRegistryName().getResourcePath())));
    }
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    int chance = Util.rand.nextInt(80);
    if(chance == 0){
      stack.setItemDamage(stack.getItemDamage()-1);
    }
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }
}
