package mysticmods.roots.item;

/*
public class AntlerHatItem extends ModifiedArmorItem {
  public AntlerHatItem(Properties builder) {
    super(Roots.ANTLER_MATERIAL, EquipmentSlot.HEAD, builder);
  }

  @Override
  public Map<Attribute, AttributeModifier> getModifiers() {
    return super.getModifiers();
  }

  @Override
  public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
    Multimap<Attribute, AttributeModifier> map = super.getDefaultAttributeModifiers(equipmentSlot);

    if (equipmentSlot == EquipmentSlot.HEAD && ConfigManager.HAT_CONFIG.getAntlerHealthBonus() != -1) {
      map.put(Attributes.MAX_HEALTH, this.getOrCreateModifier(Attributes.MAX_HEALTH, () -> new AttributeModifier(MaterialType.ARMOR_MODIFIERS[slot.getIndex()], "Antler Health Boost", ConfigManager.HAT_CONFIG.getAntlerHealthBonus(), AttributeModifier.Operation.ADDITION)));
    }

    return map;
  }

  @Override
  public void onArmorTick(ItemStack stack, Level world, Player player) {
    if (ConfigManager.HAT_CONFIG.getAntlerFrequency() == -1) {
      return;
    }

    if (!world.isClientSide && player.getHealth() < (ConfigManager.HAT_CONFIG.getAntlerThreshold() == -1 ? player.getMaxHealth() : player.getMaxHealth() - ConfigManager.HAT_CONFIG.getAntlerThreshold()) && world.random.nextInt(ConfigManager.HAT_CONFIG.getAntlerFrequency()) == 0) {
      if (player.getEffect(MobEffects.REGENERATION) != null) {
        return;
      }
      player.heal(ConfigManager.HAT_CONFIG.getAntlerHealing());
      player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, ConfigManager.HAT_CONFIG.getAntlerRegenDuration(), ConfigManager.HAT_CONFIG.getAntlerRegenAmplifier()));

      ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
      if (ConfigManager.HAT_CONFIG.getAntlerDamage() != -1) {
        head.hurtAndBreak(ConfigManager.HAT_CONFIG.getAntlerDamage(), player, (breaker) -> breaker.broadcastBreakEvent(EquipmentSlot.HEAD));
      }
    }
  }

  private static final String texture = RootsAPI.rl("textures/models/armor/antler_hat.png").toString();

  @Nullable
  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
    return texture;
  }
}
*/
