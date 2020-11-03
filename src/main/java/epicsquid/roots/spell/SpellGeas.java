package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.RayCastUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageTargetedGeasFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.EntityUtil;
import epicsquid.roots.util.SlaveUtil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellGeas extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(80);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("baffle_cap", 0.5));
  public static Property<Integer> PROP_DURATION = new Property<>("geas_duration", 400).setDescription("duration in ticks of this spell effect on entities");
  public static Property<Double> PROP_DISTANCE = new Property<>("distance", 15d).setDescription("the farthest extent that entities will be looked for when using targeted mode");
  public static Property<Integer> PROP_EXTENSION = new Property<>("extension", 600).setDescription("additional duration in ticks to be added to the base duration");
  public static Property<Integer> PROP_WEAKNESS_DURATION = new Property<>("weakness_duration", 20 * 4).setDescription("how long the creature should be weakened for after geas ends");
  public static Property<Float> PROP_FIRE_DAMAGE = new Property<>("fire_damage", 2.5f).setDescription("the amount of fire damage that creatures should take after the geas ends");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 3).setDescription("how long creatures should be set on fire for after geas ends");
  public static Property<Float> PROP_PHYSICAL_DAMAGE = new Property<>("physical_damage", 2.5f).setDescription("the amount of physical damage creatures should take after the geas ends");
  public static Property<Float> PROP_WATER_DAMAGE = new Property<>("water_damage", 2.5f).setDescription("how much water damage creatures should take after the geas ends");
  public static Property<Integer> PROP_WEAKNESS_AMPLIFIER = new Property<>("weakness_amplifier", 0).setDescription("the amplifier to be applied to the weakness effect");


  public static Modifier DURATION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "extended_geas"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.45)));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "animal_servants"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier WEAKNESS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "weakened_response"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.275)));
  public static Modifier GUARDIANS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "unholy_command"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.675)));
  public static Modifier TRIO = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "triumvirate"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.675)));
  public static Modifier TARGET = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "targeted_geas"), ModifierCores.TERRA_MOSS, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.125)));
  public static Modifier DUO = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "duumvirate"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.675)));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "inferno"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.275)));
  public static Modifier PHYSICAL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "avalanche"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.275)));
  public static Modifier WATER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "waterfall"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.275)));

  static {
    // Conflicts
    TARGET.addConflicts(TRIO, DUO);
    PEACEFUL.addConflicts(FIRE, PHYSICAL, WATER);
    FIRE.addConflicts(PHYSICAL, WATER);
    PHYSICAL.addConflicts(WATER);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_geas");
  public static SpellGeas instance = new SpellGeas(spellName);

  public int weakness_duration, fire_duration, weakness_amplifier;
  public float fire_damage, physical_damage, water_damage;
  private int duration, extension;
  public double distance;

  public SpellGeas(ResourceLocation name) {
    super(name, TextFormatting.DARK_RED, 128f / 255f, 32f / 255f, 32f / 255f, 32f / 255f, 32f / 255f, 32f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DURATION, PROP_EXTENSION, PROP_WEAKNESS_DURATION, PROP_FIRE_DAMAGE, PROP_FIRE_DURATION, PROP_PHYSICAL_DAMAGE, PROP_WATER_DAMAGE, PROP_DISTANCE, PROP_WEAKNESS_AMPLIFIER);
    acceptsModifiers(DURATION, PEACEFUL, WEAKNESS, GUARDIANS, TRIO, TARGET, DUO, FIRE, PHYSICAL, WATER);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Item.getItemFromBlock(Blocks.WEB)),
        new ItemStack(Items.LEAD),
        new ItemStack(Items.CARROT_ON_A_STICK),
        new ItemStack(ModItems.terra_spores),
        new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom))
    );
  }

  private int affect(EntityLivingBase e, boolean peaceful, EntityPlayer player, StaffModifierInstanceList info, int dur) {
    if (e.getActivePotionEffect(ModPotions.geas) == null) {
      if (peaceful && EntityUtil.isFriendlyTo(e, player)) {
        if (!player.world.isRemote) {
          e.getEntityData().setIntArray(getCachedName(), info.snapshot());
          e.addPotionEffect(new PotionEffect(ModPotions.geas, dur, 0, false, false));
        }
        return 1;
      } else if (!peaceful && EntityUtil.isHostileTo(e, player)) {
        if (!player.world.isRemote) {
          if (info.has(GUARDIANS) && SlaveUtil.canBecomeSlave(e)) {
            EntityLivingBase slave = SlaveUtil.enslave(e);
            e.world.spawnEntity(slave);
            e.setDropItemsWhenDead(false);
            e.setDead();
            slave.setPositionAndUpdate(slave.posX, slave.posY, slave.posZ);
            slave.addPotionEffect(new PotionEffect(ModPotions.geas, dur, 0, false, false));
          } else {
            e.addPotionEffect(new PotionEffect(ModPotions.geas, dur, 0, false, false));
            if (e instanceof EntityLiving) {
              ((EntityLiving) e).setAttackTarget(null);
            }
          }
        }
        return 1;
      }
    }
    return 0;
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    int count = 1;
    int affected = 0;
    int dur = duration;
    if (info.has(DURATION)) {
      dur += extension;
    }
    dur = info.ampInt(dur);
    boolean peaceful = info.has(PEACEFUL);
    if (info.has(TARGET)) {
      List<EntityLivingBase> entities = RayCastUtil.rayTraceEntities(EntityLivingBase.class, player, distance);
      entities.removeIf(o -> o.getActivePotionEffect(ModPotions.geas) != null);
      if (!entities.isEmpty()) {
        EntityLivingBase target = entities.get(0);
        affected = affect(target, peaceful, player, info, dur);
        if (!player.world.isRemote) {
          MessageTargetedGeasFX packet = new MessageTargetedGeasFX(player, target);
          PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) player);
        }
      }
    } else {
      if (info.has(DUO)) {
        count = 2;
      }
      if (info.has(TRIO)) {
        count = 3;
      }
      for (int i = 0; i < 20; i++) {
        double x = player.posX + player.getLookVec().x * 3.0 * (float) i;
        double y = player.posY + player.getEyeHeight() + player.getLookVec().y * 3.0 * (float) i;
        double z = player.posZ + player.getLookVec().z * 3.0 * (float) i;
        List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - 4.0, y - 4.0, z - 4.0, x + 5.0, y + 5.0, z + 5.0));
        for (EntityLivingBase e : entities) {
          if (affected == count) {
            break;
          }
          if (e == player) {
            continue;
          }
          affected += affect(e, peaceful, player, info, dur);
        }
      }
    }
    return affected > 0;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.duration = properties.get(PROP_DURATION);
    this.extension = properties.get(PROP_EXTENSION);
    this.weakness_duration = properties.get(PROP_WEAKNESS_DURATION);
    this.weakness_amplifier = properties.get(PROP_WEAKNESS_AMPLIFIER);
    this.fire_duration = properties.get(PROP_FIRE_DURATION);
    this.fire_damage = properties.get(PROP_FIRE_DAMAGE);
    this.physical_damage = properties.get(PROP_PHYSICAL_DAMAGE);
    this.water_damage = properties.get(PROP_WATER_DAMAGE);
    this.distance = properties.get(PROP_DISTANCE);
  }
}
