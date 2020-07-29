package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageDandelionCastFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellDandelionWinds extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(20);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("cloud_berry", 0.125));
  public static Property<Float> PROP_DISTANCE = new Property<>("distance", 0.75f).setDescription("the vertical component of the vector that determines the direction of the entity");

  public static Modifier PERESKIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "strong_gusts"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_winds"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier WILDROOT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "grounded_wind"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier MOONGLOW_LEAF = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "slow_falling"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier SPIRIT_HERB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "circle_of_winds"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier TERRA_MOSS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "item_updraft"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier POISON = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "poisonous_breeze"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "flaming_wind"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier STALICRIPE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "suction"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier DEWGONIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "billowing_sails"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  // Conflicts
  static {
    // Grounded Wind <-> Slow Falling
    WILDROOT.addConflict(MOONGLOW_LEAF);
    // Circle of Winds <-> Billowing Sails
    SPIRIT_HERB.addConflict(DEWGONIA);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_dandelion_winds");
  public static SpellDandelionWinds instance = new SpellDandelionWinds(spellName);

  private float distance;

  public SpellDandelionWinds(ResourceLocation name) {
    super(name, TextFormatting.YELLOW, 255f / 255f, 255f / 255f, 32f / 255f, 255f / 255f, 176f / 255f, 32f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DISTANCE);
    acceptsModifiers(PERESKIA, PEACEFUL, WILDROOT, MOONGLOW_LEAF, SPIRIT_HERB, TERRA_MOSS, POISON, FIRE, STALICRIPE, DEWGONIA);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Blocks.YELLOW_FLOWER),
        new OreIngredient("treeLeaves"),
        new ItemStack(ModItems.runic_dust),
        new ItemStack(ModItems.cloud_berry),
        new ItemStack(ModItems.terra_spores)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks) {
    PacketHandler.sendToAllTracking(new MessageDandelionCastFX(player.getUniqueID(), player.posX, player.posY + player.getEyeHeight(), player.posZ), player);
    List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class,
        new AxisAlignedBB(player.posX + player.getLookVec().x * 6.0 - 6.0, player.posY + player.getLookVec().y * 6.0 - 6.0,
            player.posZ + player.getLookVec().z * 6.0 - 4.0, player.posX + player.getLookVec().x * 6.0 + 6.0, player.posY + player.getLookVec().y * 6.0 + 6.0,
            player.posZ + player.getLookVec().z * 6.0 + 6.0));
    if (entities.size() > 0) {
      for (EntityLivingBase e : entities) {
        if (e != player) {
          if (has(PEACEFUL) && EntityUtil.isFriendly(e)) {
            continue;
          }
          e.motionX += (player.getLookVec().x);
          e.motionY += (distance + distance * distance);
          e.motionZ += (player.getLookVec().z);
          e.velocityChanged = true;
        }
      }
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.distance = properties.get(PROP_DISTANCE);
  }
}
