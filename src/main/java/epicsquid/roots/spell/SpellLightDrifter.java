package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.config.SpellConfig;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageLightDrifterFX;
import epicsquid.roots.network.fx.MessageLightDrifterSync;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellLightDrifter extends SpellBase {
  public static String spellName = "spell_light_drifter";
  public static SpellLightDrifter instance = new SpellLightDrifter(spellName);

  public SpellLightDrifter(String name) {
    super(name, TextFormatting.AQUA, 196f / 255f, 240f / 255f, 255f / 255f, 32f / 255f, 64f / 255f, 96f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 200;

    addCost(HerbRegistry.getHerbByName("pereskia"), 0.5f);
    addCost(HerbRegistry.getHerbByName("moonglow_leaf"), 0.25f);
    addIngredients(
        new OreIngredient("enderpearl"),
        new ItemStack(ModItems.moonglow_leaf),
        new OreIngredient("string"),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.pereskia)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      player.capabilities.disableDamage = true;
      player.capabilities.allowFlying = true;
      player.noClip = true;
      player.getEntityData().setInteger(Constants.LIGHT_DRIFTER_TAG, SpellConfig.categoryLightDrifter.driftingDuration);
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

}
