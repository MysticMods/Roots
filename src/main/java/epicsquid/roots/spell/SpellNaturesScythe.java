package epicsquid.roots.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellNaturesScythe extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(160);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST = new Property.PropertyCost(0, new SpellCost("stalicripe", 0.1));
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 12).setDescription("horizontal radius of the area in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5).setDescription("radius on the Y axis of the area in which the spell takes effect");
  public static Property<Integer> PROP_INTERVAL = new Property<>("interval", 2).setDescription("interval in ticks between each harvested block");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_natures_scythe");
  public static SpellNaturesScythe instance = new SpellNaturesScythe(spellName);

  public static Modifier PERESKIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "dewebbing"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier WILDEWHEET = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "leaf_harvester"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier WILDROOT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "magnetic_harvester"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier MOONGLOW_LEAF = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "silken_touch"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier SPIRIT_HERB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "scythe_regrowth"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier TERRA_MOSS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "grass_harvester"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier BAFFLE_CAP = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "mushroom_harvester"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier CLOUD_BERRY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "flower_harvester"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier INFERNAL_BULB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "blazing_speed"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier DEWGONIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "watery_harvest"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  private int radius, radius_y, interval;

  public SpellNaturesScythe(ResourceLocation name) {
    super(name, TextFormatting.DARK_GREEN, 64 / 255F, 240 / 255F, 24 / 255F, 26 / 255F, 110 / 255F, 13 / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST, PROP_RADIUS, PROP_RADIUS_Y, PROP_INTERVAL);
    acceptsModifiers(PERESKIA, WILDEWHEET, WILDROOT, MOONGLOW_LEAF, SPIRIT_HERB, TERRA_MOSS, BAFFLE_CAP, CLOUD_BERRY, INFERNAL_BULB, DEWGONIA);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("wildroot"),
        new OreIngredient("wildroot"),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(Items.GOLDEN_SWORD),
        new OreIngredient("tallgrass")
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
    if (ticks % (interval - interval * speedy) != 0) {
      return false;
    }

    World world = caster.world;
    List<BlockPos> blocks = Util.getBlocksWithinRadius(caster.world, caster.getPosition(), radius, radius_y, radius, pos -> ifAffectedByNaturesScythe(world, pos));

    if (blocks.isEmpty()) {
      return false;
    }

    BlockPos pos = blocks.get(Util.rand.nextInt(blocks.size()));

    if (!world.isRemote) {
      world.destroyBlock(pos, true);
      world.playSound(null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.5f, 1f);
      //PacketHandler.sendToAllTracking(new MessageFallBladesFX(pos.getX(), pos.getY(), pos.getZ(), false), world, pos);
    }
    return true;
  }

  private boolean ifAffectedByNaturesScythe(World world, BlockPos pos) {
    return world.getBlockState(pos).getBlock() instanceof BlockFlower
        || world.getBlockState(pos).getBlock() == Blocks.TALLGRASS
        || world.getBlockState(pos).getBlock() == Blocks.DOUBLE_PLANT
        && (world.getBlockState(pos).getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.FERN
        || world.getBlockState(pos).getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.GRASS
    );
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius = properties.get(PROP_RADIUS);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.interval = properties.get(PROP_INTERVAL);
  }
}
