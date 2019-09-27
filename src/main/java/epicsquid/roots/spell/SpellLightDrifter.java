package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageLightDrifterFX;
import epicsquid.roots.network.fx.MessageLightDrifterSync;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.Constants;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellLightDrifter extends SpellBase {
  public static String spellName = "spell_light_drifter";
  public static SpellLightDrifter instance = new SpellLightDrifter(spellName);

  public SpellLightDrifter(String name) {
    super(name, TextFormatting.AQUA, 196f / 255f, 240f / 255f, 255f / 255f, 32f / 255f, 64f / 255f, 96f / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 250;

    addCost(HerbRegistry.getHerbByName("pereskia"), 0.5f);
    addCost(HerbRegistry.getHerbByName("wildewheet"), 0.25f);
    addIngredients(
        new OreIngredient("enderpearl"),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta()),
        new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getMetadata()),
        new ItemStack(ModItems.wildewheet)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      player.capabilities.disableDamage = true;
      player.capabilities.allowFlying = true;
      player.noClip = true;
      player.getEntityData().setInteger(Constants.LIGHT_DRIFTER_TAG, 200);
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
