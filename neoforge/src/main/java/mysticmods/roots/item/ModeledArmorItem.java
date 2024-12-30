package mysticmods.roots.item;

import mysticmods.roots.client.model.armor.ArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModeledArmorItem extends ModifiedArmorItem {
  public ModeledArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
    super(materialIn, slot, builder);
  }
}
