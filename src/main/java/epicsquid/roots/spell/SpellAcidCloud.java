package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.config.SpellConfig;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageAcidCloudFX;
import epicsquid.roots.spell.modules.ModuleRegistry;
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

import java.util.List;

public class SpellAcidCloud extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(10);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("baffle_cap", 0.250));
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(5f);
  public static Property<Integer> PROP_POISON_DURATION = new Property<>("poison_duration", 80);
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 5);
  public static Property<Integer> PROP_POISON_AMPLIFICATION = new Property<>("poison_amplification", 0);

  public static String spellName = "spell_acid_cloud";
  public static SpellAcidCloud instance = new SpellAcidCloud(spellName);

  private float damage;
  private int poisonDuration;
  private int poisonAmplification;
  private int fireDuration;

  public SpellAcidCloud(String name) {
    super(name, TextFormatting.DARK_GREEN, 80f / 255f, 160f / 255f, 40f / 255f, 64f / 255f, 96f / 255f, 32f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DAMAGE, PROP_POISON_DURATION, PROP_FIRE_DURATION, PROP_POISON_AMPLIFICATION);
  }

  @Override
  public void init () {
    addIngredients(
        new ItemStack(Items.SPIDER_EYE),
        new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom)),
        new ItemStack(Items.SUGAR),
        new ItemStack(ModItems.terra_moss),
        new ItemStack(ModItems.terra_moss)
    );
    acceptModules(ModuleRegistry.module_fire);
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class,
          new AxisAlignedBB(player.posX - 4.0, player.posY - 1.0, player.posZ - 4.0, player.posX + 4.0, player.posY + 3.0, player.posZ + 4.0));
      for (EntityLivingBase e : entities) {
        if (!(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())
            && !e.getUniqueID().equals(player.getUniqueID())) {
          e.attackEntityFrom(DamageSource.causeMobDamage(player), damage);
          if (SpellConfig.spellFeaturesCategory.acidCloudPoisoningEffect)
            e.addPotionEffect(new PotionEffect(MobEffects.POISON, poisonDuration, poisonAmplification));
          e.setRevengeTarget(player);
          e.setLastAttackedEntity(player);

          if(modules.contains(ModuleRegistry.module_fire)){
            e.setFire(fireDuration);
          }
        }
      }
      PacketHandler.sendToAllTracking(new MessageAcidCloudFX(player.posX, player.posY + player.getEyeHeight(), player.posZ), player);
    }
    return true;
  }

  @Override
  public void finalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.damage = properties.getProperty(PROP_DAMAGE);
    this.poisonAmplification = properties.getProperty(PROP_POISON_AMPLIFICATION);
    this.poisonDuration = properties.getProperty(PROP_POISON_DURATION);
    this.fireDuration = properties.getProperty(PROP_FIRE_DURATION);
  }
}
