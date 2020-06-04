package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.modifiers.instance.base.BaseModifierInstanceList;
import epicsquid.roots.network.fx.MessagePetalShellBurstFX;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class SpellPetalShell extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(120);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("spirit_herb", 0.75));
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(120 * 20);
  public static Property<Integer> PROP_MAXIMUM = new Property<>("maximum_shells", 3).setDescription("maximum number of shells (attack blockers) a player can have");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_petal_shell");
  public static SpellPetalShell instance = new SpellPetalShell(spellName);

  private int maxShells;
  private int duration;

  public SpellPetalShell(ResourceLocation name) {
    super(name, TextFormatting.LIGHT_PURPLE, 255f / 255f, 192f / 255f, 240f / 255f, 255f / 255f, 255f / 255f, 255f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_MAXIMUM);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(ModItems.petals),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(ModItems.spirit_herb),
        new ItemStack(Items.SHIELD),
        new ItemStack(ModItems.pereskia)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, BaseModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
    if (!player.world.isRemote) {
      player.addPotionEffect(new PotionEffect(ModPotions.petal_shell, (int) (duration + duration * amplifier), (int) (maxShells + maxShells * amplifier), false, false));
      PacketHandler.sendToAllTracking(new MessagePetalShellBurstFX(player.posX, player.posY + 1.0f, player.posZ), player);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.maxShells = properties.get(PROP_MAXIMUM);
    this.duration = properties.get(PROP_DURATION);
  }
}
