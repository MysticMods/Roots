package epicsquid.mysticallib.item;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.CustomModelItem;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemArrowBase extends ItemArrow implements IModeledObject, ICustomModeledObject {

  private boolean hasCustomModel = false;

  public ItemArrowBase(String name) {
    super();
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
  }

  public ItemArrowBase setModelCustom(boolean custom) {
    this.hasCustomModel = custom;
    return this;
  }

  @Override
  public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
    EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, shooter);
    entitytippedarrow.setPotionEffect(stack);
    entitytippedarrow.setDamage(3.0D);
    return entitytippedarrow;
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
