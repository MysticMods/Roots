package mysticmods.roots.item.copper;

import com.google.common.collect.Multimap;
import mysticmods.roots.item.ModifiedArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorMaterial;
import noobanidus.libs.noobutil.material.MaterialType;

public class CopperArmorItem extends ModifiedArmorItem implements ICopperItem {
  public CopperArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
    super(materialIn, slot, builder);
  }

  @Override
  public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
    Multimap<Attribute, AttributeModifier> map = super.getDefaultAttributeModifiers(equipmentSlot);

    if (this.slot == equipmentSlot) {
      map.put(Attributes.MAX_HEALTH, getOrCreateModifier(Attributes.MAX_HEALTH, () -> new AttributeModifier(MaterialType.ARMOR_MODIFIERS[slot.getIndex()], "Healthiness", 1f, AttributeModifier.Operation.ADDITION)));
    }

    return map;
  }
}
