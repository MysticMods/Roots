package epicsquid.roots.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.integration.botania.SolegnoliaHelper;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellMagnetism extends SpellBase {
  public static String spellName = "spell_magnetism";
  public static SpellMagnetism instance = new SpellMagnetism(spellName);

  public SpellMagnetism(String name) {
    super(name, TextFormatting.DARK_RED, 53f / 255f, 31f / 255f, 34f / 255f, 37f / 255f, 45f / 255f, 80f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 100;

    addCost(HerbRegistry.getHerbByName("wildroot"), 0.25f);
    addIngredients(
        new OreIngredient("ingotIron"),
        new OreIngredient("dustRedstone"),
        new ItemStack(Items.MAP),
        new ItemStack(ModItems.wildroot),
        new ItemStack(ModItems.aubergine)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    List<EntityItem> items = Util.getEntitiesWithinRadius(player.getEntityWorld(), EntityItem.class, player.getPosition(), 15, 15, 15);
    if (items.isEmpty()) return false;

    int i = 0;
    for (EntityItem item : items) {
      if (SolegnoliaHelper.hasBotania() && SolegnoliaHelper.hasSolegnoliaAround(item)) continue;

      item.setPickupDelay(0);
      // TODO: Check to see what the potential standard is for "unmagnetising" things
      item.moveToBlockPosAndAngles(player.getPosition(), 0f, 0f);
      i++;
    }

    return i != 0;
  }
}
