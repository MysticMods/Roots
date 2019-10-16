package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageLightDrifterFX;
import epicsquid.roots.network.fx.MessageLightDrifterSync;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.Constants;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellLightDrifter extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(250);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("pereskia", 0.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("wildewheet", 0.25));
  public static Property<Integer> PROP_DURATION = new Property<>("duration", 200);

  public static String spellName = "spell_light_drifter";
  public static SpellLightDrifter instance = new SpellLightDrifter(spellName);

  private int duration;

  public SpellLightDrifter(String name) {
    super(name, TextFormatting.AQUA, 196f / 255f, 240f / 255f, 255f / 255f, 32f / 255f, 64f / 255f, 96f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DURATION);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("enderpearl"),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta()),
        new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()),
        new ItemStack(ModItems.wildewheet)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
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
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.duration = properties.getProperty(PROP_DURATION);
  }
}
