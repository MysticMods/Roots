package mysticmods.roots.spell;

// TODO: Charge interval
/*public class SanctuarySpell extends TwoRadiusSpell {
  private float velocity;

  public SanctuarySpell(ChatFormatting color, CostInstance costs) {
    super(Type.CONTINUOUS, color, costs, 0xd01050, 0xe02090);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.SANCTUARY_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.SANCTUARY_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.SANCTUARY_RADIUS_XZ;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> result) {
    super.buildProperties(result);
    result.add(ModSpells.SANCTUARY_VELOCITY);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.velocity = properties.get(ModSpells.SANCTUARY_VELOCITY);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    int r = 1 + radiusY + radiusZX;
    Vec3 playerPosition = pPlayer.position();
    List<Entity> entities = pLevel.getEntities(EntityTypeTest.forClass(Entity.class), getAABB().move(pPlayer.position()), EntityUtils.isProjectileOrHostile(pPlayer));
    int count = 0;
    for (Entity entity : entities) {
      if (entity.distanceToSqr(pPlayer) < r) {
        Vec3 entityPosition = entity.position();
        double x = velocity * (entityPosition.x - playerPosition.x);
        double y = velocity * (entityPosition.y - playerPosition.y);
        double z = velocity * (entityPosition.z - playerPosition.z);
        entity.setDeltaMovement(x, y, z);
        entity.hasImpulse = true;
        count++;
      }
    }

    PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new SanctuaryFXPacket(pPlayer.getId(), r));

    if (count == 0) {
      costs.noCharge();
      return 0;
    } else {
      costs.operations(count);
      return cooldown;
    }
  }

  @Override
  public int getMaximumOperations() {
    return 100;
  }

  @Override
  public CostInstance.ChargeType getChargeType() {
    return CostInstance.ChargeType.OPERATION;
  }
}*/
