package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.config.SpellConfig;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageSanctuaryBurstFX;
import epicsquid.roots.network.fx.MessageSanctuaryRingFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellSanctuary extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("pereskia", 0.225));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(new SpellCost("wildroot", 0.225));
  public static Property<Float> PROP_VELOCITY = new Property<>("push_velocity", 0.125f).setDescription("the knockback speed at which thaumcraft.entities are pushed away by the spell");
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 4).setDescription("the radius on the X axis of the area the spell in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5).setDescription("the radius on the Y axis of the area the spell in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 4).setDescription("the radius on the Z axis of the area the spell in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_BOOST = new Property<>("radius_boost", 3).setDescription("how much is added to the size of the circle");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 4).setDescription("the duration that creatures knocked back are set on fire for");
  public static Property<Integer> PROP_LEVITATION_DURATION = new Property<>("levitate_duration", 6 * 20).setDescription("how long the levitation effect should be applied for");
  public static Property<Integer> PROP_WITHER_DURATION = new Property<>("wither_duration", 4 * 20).setDescription("how long the wither duration should be applied for");
  public static Property<Integer> PROP_WITHER_AMPLIFIER = new Property<>("wither_amplifier", 0).setDescription("the amplifier for the wither effect");
  public static Property<Float> PROP_SPIDER_DAMAGE = new Property<>("spider_damage", 4.0f).setDescription("how much damage is dealt to arachnids");
  public static Property<Float> PROP_UNDEAD_DAMAGE = new Property<>("undead_damage", 3f).setDescription("how much damage is dealt to undead");

  public static Modifier UNPEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "reject_nature"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.125)));
  public static Modifier WITHER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "withering"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.375)));
  public static Modifier UNDEAD = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "undead_rejection"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.375)));
  public static Modifier KNOCKBACK1 = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "repulsive_bubble"), ModifierCores.TERRA_MOSS, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.375)));
  public static Modifier SPIDER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "spider_unsuffrage"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.375)));
  public static Modifier LEVITATE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "levitating_bubble"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.125)));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "inflammatory_bubble"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.125)));
  public static Modifier RADIUS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "earthen_radius"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.275)));
  public static Modifier KNOCKBACK2 = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "liquid_repulsion"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.125)));

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_sanctuary");
  public static SpellSanctuary instance = new SpellSanctuary(spellName);

  private float velocity, spider_damage, undead_damage;
  public int radius_x, radius_y, radius_z, radius_boost;
  private int fire_duration, levitation_duration, wither_duration, wither_amplifier;

  public SpellSanctuary(ResourceLocation name) {
    super(name, TextFormatting.DARK_PURPLE, 208f / 255f, 16f / 255f, 80f / 255f, 224f / 255f, 32f / 255f, 144f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_VELOCITY, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_RADIUS_BOOST, PROP_FIRE_DURATION, PROP_LEVITATION_DURATION, PROP_WITHER_AMPLIFIER, PROP_WITHER_DURATION, PROP_SPIDER_DAMAGE, PROP_UNDEAD_DAMAGE);
    acceptModifiers(UNPEACEFUL, WITHER, UNDEAD, KNOCKBACK1, SPIDER, LEVITATE, FIRE, RADIUS, KNOCKBACK2);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.ARMOR_STAND),
        new ItemStack(ModItems.pereskia),
        new ItemStack(Items.BOWL),
        new ItemStack(ModItems.bark_spruce),
        new OreIngredient("wildroot")
    );
    //setCastSound(ModSounds.Spells.SANCTUARY);
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    int x = radius_x;
    int y = radius_y;
    int z = radius_z;
    if (info.has(RADIUS)) {
      x += radius_boost;
      y += radius_boost;
      z += radius_boost;
    }
    int r = 1 + x + z;
    float v = velocity;
    if (info.has(KNOCKBACK1)) {
      v *= 2;
    }
    if (info.has(KNOCKBACK2)) {
      v *= 2;
    }

    List<Entity> entities = Util.getEntitiesWithinRadius(player.world, Entity.class, player.getPosition(), x, y, z);

    for (Entity e : entities) {
      if (e != player) {
        boolean reject = false;
        if (e instanceof IProjectile) {
          reject = true;
        } else if (EntityUtil.isHostile(e, SpellSanctuary.instance)) {
          reject = true;
        } else if (info.has(UNPEACEFUL) && EntityUtil.isFriendly(e, SpellSanctuary.instance)) {
          reject = true;
        } else if (SpellConfig.spellFeaturesCategory.getSanctuaryBlacklist().contains(EntityList.getKey(e))) {
          reject = true;
        }
        if (reject) {
          if (e.getDistanceSq(player) < r) {
            e.motionX = v * (e.posX - player.posX);
            e.motionY = v * (e.posY - player.posY);
            e.motionZ = v * (e.posZ - player.posZ);
            e.velocityChanged = true;
            if (!e.isInvisible()) {
              PacketHandler.sendToAllTracking(new MessageSanctuaryBurstFX(e.posX, e.posY + 0.6f * e.getEyeHeight(), e.posZ), e);
            }
            if (e instanceof EntityLivingBase) {
              if (info.has(SPIDER) && ((EntityLivingBase) e).getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
                e.attackEntityFrom(DamageSource.causeMobDamage(player), spider_damage);
              }
              if (info.has(WITHER)) {
                ((EntityLivingBase) e).addPotionEffect(new PotionEffect(MobEffects.WITHER, wither_duration, wither_amplifier));
              }
              if (info.has(LEVITATE)) {
                ((EntityLivingBase) e).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, levitation_duration, 0));
              }
              if (info.has(UNDEAD) && ((EntityLivingBase) e).isEntityUndead()) {
                e.attackEntityFrom(DamageSource.causeMobDamage(player), undead_damage);
              }
            }
            if (info.has(FIRE)) {
              e.setFire(fire_duration);
            }
          }
        }
      }
    }
    if (player.ticksExisted % 2 == 0) {
      PacketHandler.sendToAllTracking(new MessageSanctuaryRingFX(player.posX, player.posY + 0.875f, player.posZ, info), player);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
    this.velocity = properties.get(PROP_VELOCITY);
    this.radius_boost = properties.get(PROP_RADIUS_BOOST);
    this.spider_damage = properties.get(PROP_SPIDER_DAMAGE);
    this.fire_duration = properties.get(PROP_FIRE_DURATION);
    this.levitation_duration = properties.get(PROP_LEVITATION_DURATION);
    this.wither_amplifier = properties.get(PROP_WITHER_AMPLIFIER);
    this.wither_duration = properties.get(PROP_WITHER_DURATION);
    this.undead_damage = properties.get(PROP_UNDEAD_DAMAGE);
  }
}

