package epicsquid.roots.item;

import com.google.common.collect.Lists;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.model.armor.SylvanArmorModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SylvanArmorItem extends ArmorItem {
  public static double BONUS = 0.05;

  public SylvanArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder) {
    super(materialIn, slot, builder);
/*    super(material, 0, slot);
    setTranslationKey(name);
    setRegistryName(new ResourceLocation(Roots.MODID, name));
    setMaxDamage(750);
    setCreativeTab(Roots.tab);*/
  }

  @Nullable
  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
    return Roots.MODID + ":textures/models/armor/sylvan_armor.png";
  }

  @Nullable
  @Override
  public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
    // TODO: This is probably unsafe
    return (A) SylvanArmorModel.getInstance(armorSlot);
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
