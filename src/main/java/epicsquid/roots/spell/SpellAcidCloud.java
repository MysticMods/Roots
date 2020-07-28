package epicsquid.roots.spell;

import com.google.common.collect.Lists;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.config.SpellConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageAcidCloudFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.EntityUtil;
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

  // TODO: Costs

  public static Modifier RADIUS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "radius_boost"), ModifierCores.PERESKIA, Lists.newArrayList(new ModifierCost(CostType.ADDITIONAL_COST, 0.2, ModifierCores.PERESKIA), new ModifierCost(CostType.ALL_COST_MULTIPLIER, 0.05))));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_cloud"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.1)));
  public static Modifier PARALYSIS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "rooting_cloud"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.3)));
  public static Modifier NIGHT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "moonfall"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.4)));
  public static Modifier UNDEAD = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "unholy_vanquisher"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.3)));
  public static Modifier HEALING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "healing_cloud"), ModifierCores.TERRA_MOSS, Lists.newArrayList(new ModifierCost(CostType.ADDITIONAL_COST, 0.3, ModifierCores.TERRA_MOSS), new ModifierCost(CostType.ALL_COST_MULTIPLIER, 0.05))));
  public static Modifier SPEED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "increased_speed"), ModifierCores.CLOUD_BERRY, Lists.newArrayList(new ModifierCost(CostType.ADDITIONAL_COST, 0.15, ModifierCores.CLOUD_BERRY), new ModifierCost(CostType.ALL_COST_MULTIPLIER, 0.15))));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "fire_cloud"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.25)));
  public static Modifier PHYSICAL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "cloud_of_rocks"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.25)));
  public static Modifier UNDERWATER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "underwater_increase"), ModifierCores.DEWGONIA, Lists.newArrayList(new ModifierCost(CostType.ADDITIONAL_COST, 0.2, ModifierCores.DEWGONIA), new ModifierCost(CostType.ALL_COST_MULTIPLIER, 0.1))));

  static {
    // Conflicts
    HEALING.addConflicts(PARALYSIS, UNDEAD, FIRE, PHYSICAL);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_acid_cloud");
  public static SpellAcidCloud instance = new SpellAcidCloud(spellName);

  private float damage;
  private int poisonDuration;
  private int poisonAmplification;
  private int fireDuration;


  public SpellAcidCloud(ResourceLocation name) {
    super(name, TextFormatting.DARK_GREEN, 80f / 255f, 160f / 255f, 40f / 255f, 64f / 255f, 96f / 255f, 32f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DAMAGE, PROP_POISON_DURATION, PROP_FIRE_DURATION, PROP_POISON_AMPLIFICATION);
    acceptsModifiers(RADIUS, PEACEFUL, PARALYSIS, NIGHT, UNDEAD, HEALING, SPEED, FIRE, PHYSICAL, UNDERWATER);
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
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks) {
    if (!player.world.isRemote) {
      List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class,
          new AxisAlignedBB(player.posX - 4.0, player.posY - 1.0, player.posZ - 4.0, player.posX + 4.0, player.posY + 3.0, player.posZ + 4.0));
      for (EntityLivingBase e : entities) {
        if (e != player && !(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
          if (e.hurtTime <= 0 && !e.isDead) {
            if (has(PEACEFUL) && EntityUtil.isFriendly(e)) {
              continue;
            }
            if (has(FIRE)) {
              e.attackEntityFrom(ModDamage.fireDamageFrom(player), ampFloat(damage)/2);
              e.attackEntityFrom(DamageSource.causeMobDamage(player), ampFloat(damage)/2);
              e.setFire(fireDuration);
            } else {
              e.attackEntityFrom(DamageSource.causeMobDamage(player), ampFloat(damage));
            }
            if (SpellConfig.spellFeaturesCategory.acidCloudPoisoningEffect) {
              e.addPotionEffect(new PotionEffect(MobEffects.POISON, ampInt(poisonDuration), poisonAmplification));
            }
            e.setRevengeTarget(player);
            e.setLastAttackedEntity(player);
          }
        }
      }
      PacketHandler.sendToAllTracking(new MessageAcidCloudFX(player.posX, player.posY + player.getEyeHeight(), player.posZ, modifiers), player);
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
