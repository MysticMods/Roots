package mysticmods.roots.item;

import mysticmods.roots.Roots;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class BeetleArmorItem extends ModeledArmorItem {

  public BeetleArmorItem(Properties builder, EquipmentSlot slot) {
    super(Roots.CARAPACE_MATERIAL, slot, builder);
  }

  private static final String texture = RootsAPI.rl("textures/models/armor/beetle_armor.png").toString();

  @Nullable
  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
    return texture;
  }
}
