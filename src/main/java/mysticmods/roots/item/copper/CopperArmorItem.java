package mysticmods.roots.item.copper;

/*public class CopperArmorItem extends ModifiedArmorItem implements ICopperItem {
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
}*/
