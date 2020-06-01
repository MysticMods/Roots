package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.config.SpellConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.modifiers.instance.ModifierInstanceList;
import epicsquid.roots.network.fx.MessageAcidCloudFX;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellAcidCloud extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(10);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("baffle_cap", 0.250));
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(5f).setDescription("damage dealt each time to living entities");
  public static Property<Integer> PROP_POISON_DURATION = new Property<>("poison_duration", 80).setDescription("duration in ticks of the poison effect applied on the enemies");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 5).setDescription("duration in seconds of the fire effect applied on the enemies");
  public static Property<Integer> PROP_POISON_AMPLIFICATION = new Property<>("poison_amplification", 0).setDescription("the level of the poison effect applied on the enemies (0 is the first level)");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_acid_cloud");
  public static SpellAcidCloud instance = new SpellAcidCloud(spellName);

  private float damage;
  private int poisonDuration;
  private int poisonAmplification;
  private int fireDuration;

  public SpellAcidCloud(ResourceLocation name) {
    super(name, TextFormatting.DARK_GREEN, 80f / 255f, 160f / 255f, 40f / 255f, 64f / 255f, 96f / 255f, 32f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DAMAGE, PROP_POISON_DURATION, PROP_FIRE_DURATION, PROP_POISON_AMPLIFICATION);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.SPIDER_EYE),
        new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom)),
        new OreIngredient("dyeLime"),
        new OreIngredient("blockCactus"),
        new ItemStack(Items.ROTTEN_FLESH)
    );
    //acceptsModifiers(ModuleRegistry.module_fire);
  }

  @Override
  public boolean cast(EntityPlayer player, ModifierInstanceList modifiers, int ticks, int amplifier) {
    if (!player.world.isRemote) {
      List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class,
          new AxisAlignedBB(player.posX - 4.0, player.posY - 1.0, player.posZ - 4.0, player.posX + 4.0, player.posY + 3.0, player.posZ + 4.0));
      for (EntityLivingBase e : entities) {
        if (!(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())
            && !e.getUniqueID().equals(player.getUniqueID())) {
          if (e.hurtTime <= 0 && !e.isDead) {
            e.attackEntityFrom(DamageSource.causeMobDamage(player), damage);
            if (SpellConfig.spellFeaturesCategory.acidCloudPoisoningEffect)
              e.addPotionEffect(new PotionEffect(MobEffects.POISON, poisonDuration, poisonAmplification));
            e.setRevengeTarget(player);
            e.setLastAttackedEntity(player);

/*            if (modules.contains(ModuleRegistry.module_fire)) {
              e.setFire(fireDuration);
            }*/
          }
        }
      }
      PacketHandler.sendToAllTracking(new MessageAcidCloudFX(player.posX, player.posY + player.getEyeHeight(), player.posZ), player);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.damage = properties.get(PROP_DAMAGE);
    this.poisonAmplification = properties.get(PROP_POISON_AMPLIFICATION);
    this.poisonDuration = properties.get(PROP_POISON_DURATION);
    this.fireDuration = properties.get(PROP_FIRE_DURATION);
  }
}
