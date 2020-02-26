package epicsquid.roots.spell;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellSaturate extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(500);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildewheet", 0.7));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("terra_moss", 0.5));

  public static String spellName = "spell_saturate";
  public static SpellSaturate instance = new SpellSaturate(spellName);

  public SpellSaturate(String name) {
    super(name, TextFormatting.GOLD, 235F / 255F, 183F / 255F, 52F / 255F, 156F / 255F, 100F / 255F, 16F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.MUSHROOM_STEW),
        new ItemStack(Items.MILK_BUCKET),
        new ItemStack(ModItems.cooked_aubergine),
        new ItemStack(Items.PUMPKIN_PIE),
        new ItemStack(epicsquid.roots.init.ModItems.wildewheet)
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
