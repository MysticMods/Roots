package epicsquid.roots.spell;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellGeas extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(80);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildewheet", 0.5));
  public static Property<Integer> PROP_DURATION = new Property<>("geas_duration", 400);

  public static String spellName = "spell_geas";
  public static SpellGeas instance = new SpellGeas(spellName);

  private int duration;
  private PotionEffect geasEffect;

  public SpellGeas(String name) {
    super(name, TextFormatting.DARK_RED, 128f / 255f, 32f / 255f, 32f / 255f, 32f / 255f, 32f / 255f, 32f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DURATION);

    addIngredients(
        new ItemStack(Items.ROTTEN_FLESH),
        new ItemStack(ModItems.wildewheet),
        new ItemStack(ModItems.wildewheet_seed),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine_seed),
        new OreIngredient("enderpearl")
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    boolean foundTarget = false;
    for (int i = 0; i < 4 && !foundTarget; i++) {
      double x = player.posX + player.getLookVec().x * 3.0 * (float) i;
      double y = player.posY + player.getEyeHeight() + player.getLookVec().y * 3.0 * (float) i;
      double z = player.posZ + player.getLookVec().z * 3.0 * (float) i;
      List<EntityLivingBase> entities = player.world
          .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - 2.0, y - 2.0, z - 2.0, x + 2.0, y + 2.0, z + 2.0));
      for (EntityLivingBase e : entities) {
        if (e != player && !player.world.isRemote) {
          foundTarget = true;
          e.addPotionEffect(geasEffect);
          break;
        }
      }
    }
    return foundTarget;
  }

  @Override
  public void finalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);

    SpellCost cost = properties.getProperty(PROP_COST_1);
    addCost(cost.getHerb(), cost.getCost());

    this.duration = properties.getProperty(PROP_DURATION);
    this.geasEffect = new PotionEffect(ModPotions.geas, this.duration);
  }
}
