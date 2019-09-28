package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageLifeDrainAbsorbFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellLifeDrain extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("moonglow_leaf", 0.25));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("baffle_cap", 0.125));
  public static Property<Float> PROP_WITHER_DAMAGE = new Property<>("wither_damage", 3f);
  public static Property<Float> PROP_HEAL = new Property<>("heal", 1.5f);
  public static Property<Integer> PROP_WITHER_DURATION = new Property<>("wither_duration", 70);
  public static Property<Integer> PROP_WITHER_AMPLIFICATION = new Property<>("wither_amplification", 0);
  public static Property<Integer> PROP_WITHER_CHANCE = new Property<>("wither_chance", 4);

  public static String spellName = "spell_life_drain";
  public static SpellLifeDrain instance = new SpellLifeDrain(spellName);

  private float witherDamage;
  private float heal;
  private int witherDuration;
  private int witherAmplification;
  private int witherChance;

  public SpellLifeDrain(String name) {
    super(name, TextFormatting.DARK_GRAY, 144f / 255f, 32f / 255f, 64f / 255f, 255f / 255f, 196f / 255f, 240f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_WITHER_DAMAGE, PROP_HEAL, PROP_WITHER_DURATION, PROP_WITHER_AMPLIFICATION, PROP_WITHER_CHANCE);
  }

  @Override
  public void init () {
    addIngredients(
        new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom)),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.moonglow_seed),
        new ItemStack(Items.IRON_SWORD),
        new OreIngredient("blockCactus")
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      boolean foundTarget = false;
      PacketHandler.sendToAllTracking(new MessageLifeDrainAbsorbFX(player.getUniqueID(), player.posX, player.posY + player.getEyeHeight(), player.posZ), player);
      for (int i = 0; i < 4 && !foundTarget; i++) {
        double x = player.posX + player.getLookVec().x * 3.0 * (float) i;
        double y = player.posY + player.getEyeHeight() + player.getLookVec().y * 3.0 * (float) i;
        double z = player.posZ + player.getLookVec().z * 3.0 * (float) i;
        List<EntityLivingBase> entities = player.world
            .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - 2.0, y - 2.0, z - 2.0, x + 2.0, y + 2.0, z + 2.0));
        for (EntityLivingBase e : entities) {
          if (!(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())
              && e.getUniqueID().compareTo(player.getUniqueID()) != 0) {
            foundTarget = true;
            if (e.hurtTime <= 0 && !e.isDead) {
              e.attackEntityFrom(DamageSource.WITHER.causeMobDamage(player), witherDamage);
              if (e.rand.nextInt(witherChance) == 0) {
                e.addPotionEffect(new PotionEffect(MobEffects.WITHER, witherDuration, witherAmplification));
              }
              e.setRevengeTarget(player);
              e.setLastAttackedEntity(player);
              player.heal(heal);
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.witherDamage = properties.getProperty(PROP_WITHER_DAMAGE);
    this.heal = properties.getProperty(PROP_HEAL);
    this.witherDuration = properties.getProperty(PROP_WITHER_DURATION);
    this.witherAmplification = properties.getProperty(PROP_WITHER_AMPLIFICATION);
    this.witherChance = properties.getProperty(PROP_WITHER_CHANCE);
  }
}
