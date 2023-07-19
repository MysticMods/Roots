package mysticmods.roots.item;

import mysticmods.roots.Roots;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class BeetleArmorItem extends ModeledArmorItem {

  public BeetleArmorItem(Properties builder, EquipmentSlot slot) {
    super(Roots.CARAPACE_MATERIAL, slot, builder);
  }

  @Nullable
  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
    return RootsAPI.MODID + ":textures/models/armor/beetle_armor.png";
  }
}
