package mysticmods.roots.spell;

/*public class TemporalMorassSpell extends TwoRadiusSpell {
  private int duration, amplifier;

  public TemporalMorassSpell(ChatFormatting color, CostInstance costs) {
    super(SpellCastType.INSTANT, color, costs, ParentChargeType.INSTANCE, 0x404040, 0xc020ff);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.TEMPORAL_MORASS_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.TEMPORAL_MORASS_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.TEMPORAL_MORASS_RADIUS_ZX;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.duration = properties.get(ModSpells.TEMPORAL_MORASS_DURATION);
    this.amplifier = properties.get(ModSpells.TEMPORAL_MORASS_AMPLIFIER);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.TEMPORAL_MORASS_DURATION);
    properties.add(ModSpells.TEMPORAL_MORASS_AMPLIFIER);
  }

  @Override
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    TemporalMorassEntity timeStop = ModEntities.TEMPORAL_MORASS.get().create(pLevel);
    if (timeStop != null) {
      timeStop.setLifetime(duration);
      timeStop.setPos(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
      // Don't use the helper
      pLevel.addFreshEntity(timeStop);
      SnapshotHelper.addLiving(timeStop, ModSerializers.TEMPORAL_MORASS.get(), new TemporalMorassEntitySnapshot(timeStop.tickCount, -1, radiusZX, radiusY, duration, amplifier));
      return SpellCastResult.success(cooldown);
    } else {
      costs.noCharge();
      return SpellCastResult.nothing();
    }
  }
}*/
