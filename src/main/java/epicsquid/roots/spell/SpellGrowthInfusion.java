package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.recipe.Ingredients;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.mechanics.Growth;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageLifeInfusionFX;
import epicsquid.roots.properties.Property;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;
import java.util.Random;

public class SpellGrowthInfusion extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("terra_moss", 0.08));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 7).setDescription("radius on the X axis of the area in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 7).setDescription("radius on the Y axis of the area in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 7).setDescription("radius on the Z axis of the area in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_BOOST = new Property<>("radius_boost", 8).setDescription("how much the radius of the spell is boosted by with each rampant growth modifier");
  public static Property<Integer> PROP_TICKS = new Property<>("ticks", 3).setDescription("the number of times a random chance to grow the crop is applied every tick");
  public static Property<Integer> PROP_COUNT = new Property<>("count", 2).setDescription("the number of crops selected to be grown each tick");
  public static Property<Integer> PROP_ADDITIONAL_COUNT = new Property<>("additional_count", 4).setDescription("an additional number of crops from zero to the specified value minus 1 added to the default count");

  public static Modifier RADIUS1 = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "rampant_growth_i"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier BREED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "rampant_breeding"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier FLOWERS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "flower_spreading"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier VILLAGERS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "false_night"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier RADIUS2 = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "rampant_growth_ii"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier MUSHROOM = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "embiggening"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier RADIUS3 = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "arms_of_air"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier ANIMAL_GROWTH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "incubation"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier ORE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "ore_infusion"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier HYDRATE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "hydration"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  static {
    // Conflicts
    FLOWERS.addConflicts(RADIUS1, RADIUS2, RADIUS3); // Targets specific flowers
    ORE.addConflicts(RADIUS1, RADIUS2, RADIUS3); // Can't AOE
    MUSHROOM.addConflicts(RADIUS1, RADIUS2, RADIUS3); // Again can't aoe
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_growth_infusion");
  public static SpellGrowthInfusion instance = new SpellGrowthInfusion(spellName);

  private int radius_x, radius_y, radius_z, ticks, additionalCount, count, radius_boost;

  public SpellGrowthInfusion(ResourceLocation name) {
    super(name, TextFormatting.YELLOW, 48f / 255f, 255f / 255f, 48f / 255f, 192f / 255f, 255f / 255f, 192f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_TICKS, PROP_COUNT, PROP_ADDITIONAL_COUNT, PROP_RADIUS_BOOST);
    acceptsModifiers(RADIUS1, BREED, FLOWERS, VILLAGERS, RADIUS2, MUSHROOM, RADIUS3, ANIMAL_GROWTH, ORE, HYDRATE);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("treeSapling"),
        new OreIngredient("treeSapling"),
        new ItemStack(ModItems.terra_moss),
        Ingredients.AUBERGINE,
        new OreIngredient("cropWheat")
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    boolean aoe = false;
    if (info.has(RADIUS1) || info.has(RADIUS2)) {
      aoe = true;
    }
    int boost = 0;
    if (aoe && (info.has(RADIUS1) || info.has(RADIUS2))) {
      boost = radius_boost;
    }
    if (aoe) {
      List<BlockPos> positions = Growth.collect(player.world, player.getPosition(), radius_x + boost, radius_y + boost, radius_z + boost);
      if (positions.isEmpty()) return false;
      if (!player.world.isRemote) {
        for (int i = 0; i < ampInt(count) + player.world.rand.nextInt((ampSubInt(additionalCount))); i++) {
          BlockPos pos = positions.get(player.world.rand.nextInt(positions.size()));
          IBlockState state = player.world.getBlockState(pos);
          for (int j = 0; j < ticks; j++) {
            state.getBlock().randomTick(player.world, pos, state, Util.rand);
          }
          // TODO: FINISH WRITING THIS
          // TODO: CENTRALISE EFFECT COLOURS
          if (player.world.rand.nextInt(3) == 0) {
            //PacketHandler.sendToAllTracking(new MessageRampantLifeInfusionFX(pos.getX(), pos.getY(), pos.getZ()), player);
          }
        }
      }
      return true;
    } else {
      RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().add(0, player.getEyeHeight(), 0), player.getLookVec().scale(8.0f).add(player.getPositionVector().add(0, player.getEyeHeight(), 0)));
      if (result != null) {
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
          BlockPos pos = result.getBlockPos();
          IBlockState state = player.world.getBlockState(pos);
          if (Growth.canGrow(player.world, pos, state)) {
            if (!player.world.isRemote) {
              for (int i = 0; i < ampInt(ticks); i++) {
                state.getBlock().randomTick(player.world, pos, state, new Random());
              }
              PacketHandler.sendToAllTracking(new MessageLifeInfusionFX(pos.getX(), pos.getY(), pos.getZ()), player);
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
    this.ticks = properties.get(PROP_TICKS);
    this.count = properties.get(PROP_COUNT);
    this.additionalCount = properties.get(PROP_ADDITIONAL_COUNT);
    this.radius_boost = properties.get(PROP_RADIUS_BOOST);
  }
}
