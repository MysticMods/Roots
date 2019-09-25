package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.network.fx.MessagePetalShellBurstFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellPetalShell extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(120);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("spirit_herb", 0.75));
  public static Property<Integer> PROP_MAXIMUM = new Property<>("maximum_shells", 3);

  public static String spellName = "spell_petal_shell";
  public static SpellPetalShell instance = new SpellPetalShell(spellName);

  private int maxShells;

  public SpellPetalShell(String name) {
    super(name, TextFormatting.LIGHT_PURPLE, 255f / 255f, 192f / 255f, 240f / 255f, 255f / 255f, 255f / 255f, 255f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_MAXIMUM);
  }

  @Override
  public void init () {
    addIngredients(
        new ItemStack(Items.MELON),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(ModItems.spirit_herb),
        new ItemStack(Items.SHIELD),
        new ItemStack(ModItems.pereskia_bulb)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    PotionEffect shell = player.getActivePotionEffect(ModPotions.petal_shell);
    int amp = shell == null ? 0 : shell.getAmplifier() + 1;
    if (amp < 3) {
      if (!player.world.isRemote) {
        player.addPotionEffect(new PotionEffect(ModPotions.petal_shell, 60 * 20, amp, false, false));
        PacketHandler.sendToAllTracking(new MessagePetalShellBurstFX(player.posX, player.posY + 1.0f, player.posZ), player);
      }
      return true;
    }
    return false;
  }

  @Override
  public void finalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.maxShells = properties.getProperty(PROP_MAXIMUM);
  }
}
