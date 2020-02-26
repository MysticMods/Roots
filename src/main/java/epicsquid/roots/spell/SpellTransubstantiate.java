package epicsquid.roots.spell;

import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellTransubstantiate extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(500);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("dewgonia", 0.45));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("stalicripe", 0.45));

  public static String spellName = "spell_transubstantiate";
  public static SpellTransubstantiate instance = new SpellTransubstantiate(spellName);

  public SpellTransubstantiate(String name) {
    super(name, TextFormatting.GOLD, 176F / 255F, 169F / 255F, 158F / 255F, 224F / 255F, 174F / 255F, 99F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("ingotIron"),
        new OreIngredient("ingotGold"),
        new ItemStack(epicsquid.roots.init.ModItems.pestle),
        new ItemStack(Items.MAGMA_CREAM),
        new OreIngredient("gemDiamond")
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
