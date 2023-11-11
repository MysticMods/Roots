package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessagePetalShellBurstFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class SpellPetalShell extends SpellBase {
	public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(120);
	public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
	public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("spirit_herb", 0.75));
	public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(120 * 20);
	public static Property<Integer> PROP_MAXIMUM = new Property<>("maximum_shells", 3).setDescription("maximum number of shells (attack blockers) a player can have");
	public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 5).setDescription("radius on the X axis of the area the spell has effect on");
	public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5).setDescription("radius on the Y axis of the area the spell has effect on");
	public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 5).setDescription("radius on the Z axis of the area the spell has effect on");
	public static Property<Integer> PROP_EXTRA = new Property<>("extra_shells", 2).setDescription("the number of additional extra shells that should be granted");
	public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 5).setDescription("the duration of time at which a person should be set on fire");
	public static Property<Float> PROP_FIRE_DAMAGE = new Property<>("fire_damage", 2.5f).setDescription("the amount of fire damage that should be done to a creature");
	public static Property<Integer> PROP_POISON_DURATION = new Property<>("poison_duration", 20 * 4).setDescription("the duration of the poison effect made by the reactant");
	public static Property<Integer> PROP_POISON_AMPLIFIER = new Property<>("poison_amplifier", 0).setDescription("the amplifier of the poison effect made by the reactant");
	public static Property<Integer> PROP_LEVITATE_DURATION = new Property<>("levitate_duration", 20 * 6).setDescription("the duration of the levitation effect applied to creatures");
	public static Property<Integer> PROP_WEAKNESS_DURATION = new Property<>("weakness_duration", 20 * 3).setDescription("how long creatures should be weakened for");
	public static Property<Integer> PROP_SLOW_DURATION = new Property<>("slow_duration", 8 * 20).setDescription("the duration of time that the slow effect should be applied for to creatures");
	public static Property<Integer> PROP_SLOW_AMPLIFIER = new Property<>("slow_amplifier", 0).setDescription("the amplifier applied to the slow effect");
	public static Property<Float> PROP_DAGGER_DAMAGE = new Property<>("dagger_damage", 2.5f).setDescription("the amount of slashing damage that should be done to creatures");
	public static Property<Integer> PROP_BLEED_DURATION = new Property<>("bleed_duration", 4 * 20).setDescription("the duration of the bleed effect created by the slashing damage");
	public static Property<Integer> PROP_BLEED_AMPLIFIER = new Property<>("bleed_amplifier", 0).setDescription("the amplifier applied to the bleed effect created by the slashing damage");
	public static Property<Float> PROP_RADIANT_DAMAGE = new Property<>("radiant_damage", 2.5f).setDescription("the amount of radiant damage applied to the attacking creature");
	public static Property<Integer> PROP_WEAKNESS_AMPLIFIER = new Property<>("weakness_amplifier", 0).setDescription("the amplifier to be applied to the weakness effect");
	
	
	public static Modifier RADIANT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "react_radiance"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.375)));
	public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_dance"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.45)));
	public static Modifier SLASHING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "react_daggers"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.375)));
	public static Modifier CHARGES = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "petal_overflow"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.75)));
	public static Modifier COLOUR = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "mossy_leaves"), ModifierCores.TERRA_MOSS, Cost.of(Cost.cost(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.125), Cost.cost(CostType.ALL_COST_MULTIPLIER, -0.15))));
	public static Modifier POISON = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "react_poison"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.375)));
	public static Modifier LEVITATE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "react_drift"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.375)));
	public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "react_fire"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.375)));
	public static Modifier WEAKNESS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "react_weakness"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.375)));
	public static Modifier SLOW = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "react_slow"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.375)));
	
	public static final float[] mossFirst = new float[]{138 / 255.0f, 154 / 255.0f, 91 / 255.0f, 0.5f};
	public static final float[] mossSecond = new float[]{79 / 255.0f, 93 / 255.0f, 35 / 255.0f, 0.5f};
	
	static {
		RADIANT.addConflicts(SLASHING, POISON, LEVITATE, WEAKNESS, SLOW);
		SLASHING.addConflicts(POISON, LEVITATE, WEAKNESS, SLOW);
		POISON.addConflicts(LEVITATE, WEAKNESS, SLOW);
		LEVITATE.addConflicts(WEAKNESS, SLOW);
		WEAKNESS.addConflict(SLOW);
	}
	
	public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_petal_shell");
	public static SpellPetalShell instance = new SpellPetalShell(spellName);
	
	public int duration, fire_duration, poison_duration, poison_amplifier, levitate_duration, weakness_duration, slow_duration, slow_amplifier, bleed_duration, bleed_amplifier, weakness_amplifier;
	public float fire_damage, dagger_damage, radiant_damage;
	public int maxShells, extraShells;
	private int radius_x, radius_y, radius_z;
	
	public SpellPetalShell(ResourceLocation name) {
		super(name, TextFormatting.LIGHT_PURPLE, 255f / 255f, 192f / 255f, 240f / 255f, 255f / 255f, 255f / 255f, 255f / 255f);
		properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DURATION, PROP_MAXIMUM, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_EXTRA, PROP_FIRE_DURATION, PROP_FIRE_DAMAGE, PROP_POISON_AMPLIFIER, PROP_POISON_DURATION, PROP_LEVITATE_DURATION, PROP_WEAKNESS_DURATION, PROP_SLOW_AMPLIFIER, PROP_SLOW_DURATION, PROP_DAGGER_DAMAGE, PROP_BLEED_AMPLIFIER, PROP_BLEED_DURATION, PROP_RADIANT_DAMAGE, PROP_WEAKNESS_AMPLIFIER);
		acceptModifiers(RADIANT, PEACEFUL, SLASHING, CHARGES, COLOUR, POISON, LEVITATE, FIRE, WEAKNESS, SLOW);
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
		setCastSound(ModSounds.Spells.PETAL_SHELL);
	}
	
	private AxisAlignedBB bb;
	
	@Override
	public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
		if (!player.world.isRemote) {
			int shells = maxShells;
			if (info.has(CHARGES)) {
				shells += extraShells;
			}
			if (info.has(PEACEFUL)) {
				World world = player.world;
				List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb.offset(player.getPosition()));
				entities.removeIf(o -> !EntityUtil.isFamiliar(player, o));
				if (!entities.isEmpty()) {
					EntityLivingBase entity = entities.get(Util.rand.nextInt(entities.size()));
					entity.getEntityData().setIntArray(getCachedName(), info.toArray());
					PotionEffect effect = entity.getActivePotionEffect(ModPotions.petal_shell);
					if (effect != null) {
						entity.addPotionEffect(new PotionEffect(ModPotions.petal_shell, duration, shells, false, false));
					} else {
						entity.addPotionEffect(new PotionEffect(ModPotions.petal_shell, duration, maxShells, false, false));
					}
					PacketHandler.sendToAllTracking(new MessagePetalShellBurstFX(entity.posX, entity.posY + 1.0f, entity.posZ, info), entity);
				}
			}
			player.getEntityData().setIntArray(getCachedName(), info.toArray());
			PotionEffect effect = player.getActivePotionEffect(ModPotions.petal_shell);
			if (effect != null) {
				player.addPotionEffect(new PotionEffect(ModPotions.petal_shell, duration, shells, false, false));
			} else {
				player.addPotionEffect(new PotionEffect(ModPotions.petal_shell, duration, maxShells, false, false));
			}
			PacketHandler.sendToAllTracking(new MessagePetalShellBurstFX(player.posX, player.posY + 1.0f, player.posZ, info), player);
		}
		return true;
	}
	
	@Override
	public void doFinalise() {
		this.castType = properties.get(PROP_CAST_TYPE);
		this.cooldown = properties.get(PROP_COOLDOWN);
		this.maxShells = properties.get(PROP_MAXIMUM);
		this.duration = properties.get(PROP_DURATION);
		this.radius_x = properties.get(PROP_RADIUS_X);
		this.radius_y = properties.get(PROP_RADIUS_Y);
		this.radius_z = properties.get(PROP_RADIUS_Z);
		this.bb = new AxisAlignedBB(-radius_x, -radius_y, -radius_z, 1 + radius_x, 1 + radius_y, 1 + radius_z);
		this.extraShells = properties.get(PROP_EXTRA);
		this.fire_duration = properties.get(PROP_FIRE_DURATION);
		this.poison_duration = properties.get(PROP_POISON_DURATION);
		this.poison_amplifier = properties.get(PROP_POISON_AMPLIFIER);
		this.levitate_duration = properties.get(PROP_LEVITATE_DURATION);
		this.weakness_duration = properties.get(PROP_WEAKNESS_DURATION);
		this.slow_duration = properties.get(PROP_SLOW_DURATION);
		this.slow_amplifier = properties.get(PROP_SLOW_AMPLIFIER);
		this.bleed_duration = properties.get(PROP_BLEED_DURATION);
		this.bleed_amplifier = properties.get(PROP_BLEED_AMPLIFIER);
		this.fire_damage = properties.get(PROP_FIRE_DAMAGE);
		this.dagger_damage = properties.get(PROP_DAGGER_DAMAGE);
		this.radiant_damage = properties.get(PROP_RADIANT_DAMAGE);
		this.weakness_amplifier = properties.get(PROP_WEAKNESS_AMPLIFIER);
	}
}
