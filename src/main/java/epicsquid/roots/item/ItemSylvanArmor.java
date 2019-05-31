package epicsquid.roots.item;

import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.roots.Roots;
import epicsquid.roots.model.ModelSylvanArmor;
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
    setMaxDamage(500);
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
    return new ModelSylvanArmor(armorSlot);
  }

  public static double sylvanBonus(EntityPlayer player) {
    for (ItemStack stack : player.getArmorInventoryList()) {
      if (!(stack.getItem() instanceof ItemSylvanArmor)) return 0.0;
    }

    return BONUS;
  }
}
