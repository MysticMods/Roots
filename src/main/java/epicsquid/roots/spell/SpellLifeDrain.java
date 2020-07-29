package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageLifeDrainAbsorbFX;
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

public class SpellLifeDrain extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("moonglow_leaf", 0.25));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("baffle_cap", 0.125));
  public static Property<Float> PROP_WITHER_DAMAGE = new Property<>("wither_damage", 3f).setDescription("wither damage dealt to the enemies (different from the damage dealt by the wither itself)");
  public static Property<Float> PROP_HEAL = new Property<>("heal", 1.5f).setDescription("health points restored to the player");
  public static Property<Integer> PROP_WITHER_DURATION = new Property<>("wither_duration", 70).setDescription("duration in ticks of the wither effect");
  public static Property<Integer> PROP_WITHER_AMPLIFICATION = new Property<>("wither_amplification", 0).setDescription("the level of the wither effect (0 is the first level)");
  public static Property<Integer> PROP_WITHER_CHANCE = new Property<>("wither_chance", 4).setDescription("chance for the enemies to be affected by a wither effect (the higher the number is the lower the chance is: 1/x) [default: 1/4]");

  public static Modifier PERESKIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "amplified_healing"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_drain"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier WILDROOT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "distributed_healing"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier SPIRIT_HERB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "spectral_drain"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier TERRA_MOSS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "targeted_drain"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier CLOUD_BERRY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "lightened_load"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "brimstone"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier STALICRIPE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "weakening_drain"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier DEWGONIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "underwater_breath"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  static {
    SPIRIT_HERB.addConflict(TERRA_MOSS); // You can't see the creature to target
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_life_drain");
  public static SpellLifeDrain instance = new SpellLifeDrain(spellName);

  private float witherDamage;
  private float heal;
  private int witherDuration;
  private int witherAmplification;
  private int witherChance;

  public SpellLifeDrain(ResourceLocation name) {
    super(name, TextFormatting.DARK_GRAY, 144f / 255f, 32f / 255f, 64f / 255f, 255f / 255f, 196f / 255f, 240f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_WITHER_DAMAGE, PROP_HEAL, PROP_WITHER_DURATION, PROP_WITHER_AMPLIFICATION, PROP_WITHER_CHANCE);
    acceptsModifiers(PERESKIA, PEACEFUL, WILDROOT, SPIRIT_HERB, TERRA_MOSS, CLOUD_BERRY, FIRE, STALICRIPE, DEWGONIA);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom)),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.moonglow_seed),
        new ItemStack(Items.IRON_SWORD),
        new OreIngredient("blockCactus")
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks) {
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
          if (e != player && !(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
            if (has(PEACEFUL) && EntityUtil.isFriendly(e)) {
              continue;
            }
            foundTarget = true;
            if (e.hurtTime <= 0 && !e.isDead) {
              e.attackEntityFrom(DamageSource.causeMobDamage(player), ampFloat(witherDamage));
              if (e.rand.nextInt(ampSubInt(witherChance)) == 0) {
                e.addPotionEffect(new PotionEffect(MobEffects.WITHER, ampInt(witherDuration)));
              }
              e.setRevengeTarget(player);
              e.setLastAttackedEntity(player);
              player.heal(ampFloat(heal));
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.witherDamage = properties.get(PROP_WITHER_DAMAGE);
    this.heal = properties.get(PROP_HEAL);
    this.witherDuration = properties.get(PROP_WITHER_DURATION);
    this.witherAmplification = properties.get(PROP_WITHER_AMPLIFICATION);
    this.witherChance = properties.get(PROP_WITHER_CHANCE);
  }
}
