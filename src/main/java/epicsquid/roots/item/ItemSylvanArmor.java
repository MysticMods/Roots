package epicsquid.roots.item;

import com.google.common.collect.Lists;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.roots.Roots;
import epicsquid.roots.model.armor.ModelSylvanArmor;
import epicsquid.roots.recipe.ingredient.RootsIngredients;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemSylvanArmor extends ItemArmor implements IModeledObject {
  public static double BONUS = 0.05;

  public ItemSylvanArmor(ArmorMaterial material, EntityEquipmentSlot slot, String name) {
    super(material, 0, slot);
    setTranslationKey(name);
    setRegistryName(new ResourceLocation(Roots.MODID, name));
    setMaxDamage(750);
    setCreativeTab(Roots.tab);
  }

  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "handler"));
  }

  @Nullable
  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
    return Roots.MODID + ":textures/models/armor/sylvan_armor.png";
  }

  @Nullable
  @Override
  @SideOnly(Side.CLIENT)
  public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
    return ModelSylvanArmor.getInstance(armorSlot);
  }

  public static double sylvanBonus(EntityPlayer player) {
    int count = (int) Lists.newArrayList(player.getArmorInventoryList()).stream().filter((o) -> o.getItem() instanceof ItemSylvanArmor).count();

    // TODO: Make configurable?
    switch (count) {
      case 1:
        return 0.02;
      case 2:
        return 0.04;
      case 3:
        return 0.06;
      case 4:
        return 0.08;
      default:
        return 0.0;
    }
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && RootsIngredients.FEY_LEATHER.test(repair);
  }
}
