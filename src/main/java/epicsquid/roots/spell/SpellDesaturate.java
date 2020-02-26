package epicsquid.roots.spell;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellDesaturate extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(500);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("spirit_herb", 0.7));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("terra_moss", 0.5));

  public static String spellName = "spell_desaturate";
  public static SpellDesaturate instance = new SpellDesaturate(spellName);

  public SpellDesaturate(String name) {
    super(name, TextFormatting.DARK_RED, 16F / 255F, 156F / 255F, 83F / 255F, 163F / 255F, 79F / 255F, 60F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.BOWL),
        new ItemStack(ModItems.fey_leather),
        new ItemStack(Items.ROTTEN_FLESH),
        new OreIngredient("bone"),
        new ItemStack(ModItems.spirit_herb)
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules, int ticks) {
    return false;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
  }

}
