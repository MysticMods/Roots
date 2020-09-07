/*
public class SpellLightDrifter extends SpellBase {


  public SpellLightDrifter(ResourceLocation name) {
    super(name, TextFormatting.AQUA, );
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DURATION);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("enderpearl"),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta()),
        new OreIngredient("dyeYellow"),
        new ItemStack(ModItems.wildewheet)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
    if (!player.world.isRemote) {
      player.capabilities.disableDamage = true;
      player.capabilities.allowFlying = true;
      player.noClip = true;
      player.getEntityData().setInteger(Constants.LIGHT_DRIFTER_TAG, duration);
      player.getEntityData().setDouble(Constants.LIGHT_DRIFTER_X, player.posX);
      player.getEntityData().setDouble(Constants.LIGHT_DRIFTER_Y, player.posY);
      player.getEntityData().setDouble(Constants.LIGHT_DRIFTER_Z, player.posZ);
      if (player.capabilities.isCreativeMode) {
        player.getEntityData().setInteger(Constants.LIGHT_DRIFTER_MODE, GameType.CREATIVE.getID());
      } else {
        player.getEntityData().setInteger(Constants.LIGHT_DRIFTER_MODE, GameType.SURVIVAL.getID());
      }
      player.setGameType(GameType.SPECTATOR);
      PacketHandler.sendToAllTracking(new MessageLightDrifterSync(player.getUniqueID(), player.posX, player.posY, player.posZ, true, GameType.SPECTATOR.getID()), player);
      PacketHandler.sendToAllTracking(new MessageLightDrifterFX(player.posX, player.posY + 1.0f, player.posZ), player);
    } else {
      SpectatorHandler.setFake();
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.duration = properties.get(PROP_DURATION);
  }
}*/
