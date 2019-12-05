package epicsquid.roots.item;

import com.google.common.collect.Lists;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.model.armor.SylvanArmorModel;
import net.minecraft.client.model.BipedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class SylvanArmorItem extends ArmorItem implements IModeledObject {
  public static double BONUS = 0.05;

  public SylvanArmorItem(ArmorMaterial material, EquipmentSlotType slot, String name) {
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
  public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
    return Roots.MODID + ":textures/models/armor/sylvan_armor.png";
  }

  @Nullable
  @Override
  @OnlyIn(Dist.CLIENT)
  public BipedModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, BipedModel _default) {
    return SylvanArmorModel.getInstance(armorSlot);
  }

  public static double sylvanBonus(PlayerEntity player) {
    int count = (int) Lists.newArrayList(player.getArmorInventoryList()).stream().filter((o) -> o.getItem() instanceof SylvanArmorItem).count();

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
    return toRepair.getItem() == this && repair.getItem() == ModItems.fey_leather;
  }
}
