package mysticmods.roots.spell;

/*public class WildfireSpell extends Spell {
  private float damage, velocity;

  public WildfireSpell(ChatFormatting color, CostInstance costs) {
    super(SpellCastType.INSTANT, color, costs, ParentChargeType.INSTANCE, 0xff8020, 0xff4020);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.WILDFIRE_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.damage = properties.get(ModSpells.WILDFIRE_DAMAGE);
    this.velocity = properties.get(ModSpells.WILDFIRE_VELOCITY);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.WILDFIRE_DAMAGE);
    properties.add(ModSpells.WILDFIRE_VELOCITY);
  }

  @Override
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    WildfireEntity wildfire = new WildfireEntity(ModEntities.WILDFIRE.get(), pPlayer, pLevel);
    wildfire.shootFromRotation(pPlayer, pPlayer.getViewXRot(1), pPlayer.getViewYRot(1), 0, velocity, 0);
    pLevel.addFreshEntity(wildfire);
    SnapshotHelper.addLiving(wildfire, ModSerializers.WILDFIRE.get(), new WildfireEntitySnapshot(pPlayer, -1, damage));
    return SpellCastResult.success(cooldown);
  }
}*/
