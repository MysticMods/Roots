package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.entity.spell.EntityFireJet;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class SpellWildfire extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(24);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("infernal_bulb", 0.125));
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(4.5f);
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 4);
  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_wild_fire");
  public static SpellWildfire instance = new SpellWildfire(spellName);

  public float damage;
  public int fire_duration;

  public SpellWildfire(ResourceLocation name) {
    super(name, TextFormatting.GOLD, 255f / 255f, 128f / 255f, 32f / 255f, 255f / 255f, 64f / 255f, 32f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DAMAGE, PROP_FIRE_DURATION);
  }

  @Override
  public void init () {
    addIngredients(
        new OreIngredient("dyeOrange"),
        new ItemStack(Items.COAL, 1, 1),
        new OreIngredient("gunpowder"),
        new ItemStack(ModItems.infernal_bulb),
        new ItemStack(Item.getItemFromBlock(Blocks.TNT))
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
    if (!player.world.isRemote) {
      EntityFireJet fireJet = new EntityFireJet(player.world);
      fireJet.setPlayer(player.getUniqueID());
      fireJet.setPosition(player.posX, player.posY, player.posZ);
      fireJet.setAmplifier(amplifier);
      fireJet.setSpeedy(speedy);
      player.world.spawnEntity(fireJet);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.damage = properties.get(PROP_DAMAGE);
    this.fire_duration = properties.get(PROP_FIRE_DURATION);
  }
}
