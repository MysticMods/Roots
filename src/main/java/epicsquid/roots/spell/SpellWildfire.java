package epicsquid.roots.spell;

import epicsquid.mysticallib.util.AABBUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.spell.EntityFireJet;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class SpellWildfire extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(24);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("infernal_bulb", 0.225));
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(4.5f);

  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 4).setDescription("how long entities should be set on fire for");
  public static Property<Integer> PROP_SLOW_DURATION = new Property<>("slow_duration", 6 * 20).setDescription("how long an entity should be slowed for");
  public static Property<Integer> PROP_SLOW_AMPLIFIER = new Property<>("slow_amplifier", 0).setDescription("the amplifier that should be applied to the slow effect");
  public static Property<Integer> PROP_POISON_DURATION = new Property<>("poison_duration", 6 * 20).setDescription("how long an entity should be poisoned for");
  public static Property<Integer> PROP_POISON_AMPLIFIER = new Property<>("poison_amplifier", 0).setDescription("the amplifier that should be applied to the poison effect");
  public static Property<Integer> PROP_LEVITATE_DURATION = new Property<>("levitate_duration", 6 * 20).setDescription("how long an entity should be levitated for");
  public static Property<Integer> PROP_FIRE_RADIUS = new Property<>("fire_radius", 2).setDescription("the radius that wildfire should be created");
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 5).setDescription("radius on the X axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5).setDescription("radius on the Y axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 5).setDescription("radius on the Z axis within which entities are affected by the spell");
  public static Property<Float> PROP_ICE_DAMAGE = new Property<>("icicle_damage", 1f).setDescription("how much damage icicles should deal when they strike a creature");
  public static Property<Integer> PROP_ICICLE_COUNT = new Property<>("icicle_count", 3).setDescription("how many icicles should spawn");
  public static Property<Integer> PROP_LIFETIME = new Property<>("lifetime", 12).setDescription("how long the fire projectile should exist for (which determines how far it will travel");
  public static Property<Integer> PROP_WEAKNESS_DURATION = new Property<>("weakness_duration", 4 * 20).setDescription("how long enemies should be weakened in place for");
  public static Property<Integer> PROP_WEAKNESS_AMPLIFIER = new Property<>("weakness_amplifier", 0).setDescription("the amplifier to be applied to the weakness effect");
  public static Property<Integer> PROP_GROWTH_TICKS = new Property<>("growth_ticks", 12).setDescription("the number of growth ticks that will be applied when an entity is killed by the fire ball");


  public static Modifier PURPLE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "purple_flame"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.05)));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_flame"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.125)));
  public static Modifier WEAKNESS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "weakening_thorns"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.35)));
  public static Modifier SLOW = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "slow_fire"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.325)));
  public static Modifier GREEN = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "green_flame"), ModifierCores.TERRA_MOSS, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.05)));
  public static Modifier DUALITY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "spiritual_duality"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.925)));
  public static Modifier POISON = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "poisoned_fire"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.325)));
  public static Modifier LEVITATE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "floating_cinders"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.6)));
  public static Modifier WILDFIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "pyroclastic_cloud"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.45)));
  public static Modifier ICICLES = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "fire_and_ice"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.375)));

  static {
    GREEN.addConflicts(PURPLE);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_wild_fire");
  public static SpellWildfire instance = new SpellWildfire(spellName);

  public int radius_x, radius_y, radius_z, growth_ticks;
  public float damage, ice_damage;
  public int fire_duration, fire_radius, slow_duration, slow_amplifier, poison_duration, poison_amplifier, levitate_duration, icicle_count, lifetime, weakness_amplifier, weakness_duration;

  public SpellWildfire(ResourceLocation name) {
    super(name, TextFormatting.GOLD, 255f / 255f, 128f / 255f, 32f / 255f, 255f / 255f, 64f / 255f, 32f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DAMAGE, PROP_FIRE_DURATION, PROP_SLOW_AMPLIFIER, PROP_SLOW_DURATION, PROP_POISON_AMPLIFIER, PROP_POISON_DURATION, PROP_LEVITATE_DURATION, PROP_FIRE_RADIUS, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_ICE_DAMAGE, PROP_ICICLE_COUNT, PROP_LIFETIME, PROP_WEAKNESS_AMPLIFIER, PROP_WEAKNESS_DURATION, PROP_GROWTH_TICKS);
    acceptModifiers(PURPLE, PEACEFUL, WEAKNESS, SLOW, GREEN, DUALITY, POISON, LEVITATE, WILDFIRE, ICICLES);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("dyeOrange"),
        new ItemStack(Items.COAL, 1, 1),
        new OreIngredient("gunpowder"),
        new ItemStack(ModItems.infernal_bulb),
        new OreIngredient("dustRedstone")
    );
    setCastSound(ModSounds.Spells.WILDFIRE);
  }

  private void createJet(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    EntityFireJet fireJet = new EntityFireJet(player.world);
    fireJet.setPlayer(player.getUniqueID());
    fireJet.setPosition(player.posX, player.posY, player.posZ);
    fireJet.setModifiers(info);
    player.world.spawnEntity(fireJet);
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    if (!player.world.isRemote) {
      createJet(player, info, ticks);
      if (info.has(DUALITY)) {
        createJet(player, info, ticks);
      }
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.damage = properties.get(PROP_DAMAGE);
    this.fire_duration = properties.get(PROP_FIRE_DURATION);
    this.slow_duration = properties.get(PROP_SLOW_DURATION);
    this.slow_amplifier = properties.get(PROP_SLOW_AMPLIFIER);
    this.poison_amplifier = properties.get(PROP_POISON_AMPLIFIER);
    this.poison_duration = properties.get(PROP_POISON_DURATION);
    this.levitate_duration = properties.get(PROP_LEVITATE_DURATION);
    this.fire_radius = properties.get(PROP_FIRE_RADIUS);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
    this.ice_damage = properties.get(PROP_ICE_DAMAGE);
    this.icicle_count = properties.get(PROP_ICICLE_COUNT);
    this.lifetime = properties.get(PROP_LIFETIME);
    this.weakness_duration = properties.get(PROP_WEAKNESS_DURATION);
    this.weakness_amplifier = properties.get(PROP_WEAKNESS_AMPLIFIER);
    this.growth_ticks = properties.get(PROP_GROWTH_TICKS);
  }
}
