package mysticmods.roots.item;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.world.entity.EquipmentSlot;

public class BeetleArmorItem extends ModifiedArmorItem {

  public BeetleArmorItem(Properties builder, EquipmentSlot slot) {
    super(RootsAPI.CARAPACE_MATERIAL, slot, builder);
  }

  private static final String texture = RootsAPI.rl("textures/models/armor/beetle_armor.png").toString();
}
