package epicsquid.roots.spell;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.mechanics.Magnetize;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
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
    super(name, TextFormatting.DARK_GRAY, 255f / 255f, 130f / 255f, 130f / 255f, 130f / 255f, 130f / 255f, 255f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 60;

    addCost(HerbRegistry.getHerbByName("wildroot"), 0.195f);
    addIngredients(
        new OreIngredient("ingotIron"),
        new OreIngredient("dustRedstone"),
        new ItemStack(Items.MAP),
        new ItemStack(ModItems.wildroot),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    // TODO: Check to see what the potential standard is for "unmagnetising" things
    int count = 0;
    count += Magnetize.pull(EntityItem.class, player.world, player.getPosition(), 15, 15, 15);
    count += Magnetize.pull(EntityXPOrb.class, player.world, player.getPosition(), 15, 15, 15);

    return count != 0;
  }
}
