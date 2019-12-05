package epicsquid.roots.spell;

import epicsquid.roots.init.ModPotions;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellPetalShell extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(120);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("spirit_herb", 0.75));
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(120 * 20);
  public static Property<Integer> PROP_MAXIMUM = new Property<>("maximum_shells", 3);

  public static String spellName = "spell_petal_shell";
  public static SpellPetalShell instance = new SpellPetalShell(spellName);

  private int maxShells;
  private int duration;

  public SpellPetalShell(String name) {
    super(name, TextFormatting.LIGHT_PURPLE, 255f / 255f, 192f / 255f, 240f / 255f, 255f / 255f, 255f / 255f, 255f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_MAXIMUM);
  }

  @Override
  public void init() {
    addIngredients(
/*        new ItemStack(ModItems.petals),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(ModItems.spirit_herb),
        new ItemStack(Items.SHIELD),
        new ItemStack(ModItems.pereskia)*/
    );
  }

  @Override
  public boolean cast(PlayerEntity player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      player.addPotionEffect(new EffectInstance(ModPotions.petal_shell, duration, maxShells, false, false));
      /*      PacketHandler.sendToAllTracking(new MessagePetalShellBurstFX(player.posX, player.posY + 1.0f, player.posZ), player);*/
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.maxShells = properties.getProperty(PROP_MAXIMUM);
    this.duration = properties.getProperty(PROP_DURATION);
  }
}
