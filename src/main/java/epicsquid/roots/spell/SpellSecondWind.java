package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.instance.ModifierInstanceList;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class SpellSecondWind extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(24);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("dewgonia", 0.125));

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_second_wind");
  public static SpellSecondWind instance = new SpellSecondWind(spellName);

  public SpellSecondWind(ResourceLocation name) {
    super(name, TextFormatting.BLUE, 64f / 255f, 64f / 255f, 64f / 255f, 192f / 255f, 32f / 255f, 255f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1);
  }

  @Override
  public void init () {
    addIngredients(
        new ItemStack(ModItems.dewgonia),
        new OreIngredient("sugarcane"),
        new ItemStack(Items.CLAY_BALL),
        new ItemStack(Items.GLASS_BOTTLE),
        new OreIngredient("ingotIron")
    );
  }

  @Override
  public boolean cast(EntityPlayer player, ModifierInstanceList modifiers, int ticks) {
    player.setAir(300);
    if (player.world.isRemote) {
      player.playSound(SoundEvents.ENTITY_BOAT_PADDLE_WATER, 1, 1);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
  }
}
