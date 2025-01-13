package mysticmods.roots.item;

/*
public abstract class ModifiedArmorItem extends ArmorItem implements IModifiable {
  protected Map<Attribute, AttributeModifier> modifiers = new HashMap<>();

  @Override
  public Map<Attribute, AttributeModifier> getModifiers() {
    return modifiers;
  }

  public ModifiedArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
    super(materialIn, slot, builder);
  }

  @Nullable
  @Override
  public EquipmentSlot getEquipmentSlot(ItemStack stack) {
    return getSlot();
  }

  @Override
  public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
    Multimap<Attribute, AttributeModifier> result = super.getDefaultAttributeModifiers(equipmentSlot);
    if (result.isEmpty()) {
      //noinspection UnstableApiUsage
      return MultimapBuilder.hashKeys().hashSetValues().build();
    } else {
      //noinspection UnstableApiUsage
      return MultimapBuilder.hashKeys().hashSetValues().build(result);
    }
  }
}
*/
