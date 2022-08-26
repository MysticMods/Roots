/*package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.client.SpectatorHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.MessageLightDrifterSync;
import epicsquid.roots.network.fx.MessageLightDrifterFX;
import epicsquid.roots.util.Constants;
import epicsquid.roots.properties.Property;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraftforge.oredict.OreIngredient;

public class SpellLightDrifter extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(250);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("pereskia", 0.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("wildewheet", 0.25));
  public static Property<Integer> PROP_DURATION = new Property<>("duration", 200).setDescription("the duration in ticks of the spell effect on the player");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_light_drifter");
  public static SpellLightDrifter instance = new SpellLightDrifter(spellName);

  private int duration;

  public SpellLightDrifter(ResourceLocation name) {
    super(name, TextFormatting.AQUA, 196f / 255f, 240f / 255f, 255f / 255f, 32f / 255f, 64f / 255f, 96f / 255f);
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
