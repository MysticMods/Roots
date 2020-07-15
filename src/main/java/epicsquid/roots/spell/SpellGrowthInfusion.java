package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
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

import java.util.Random;

public class SpellGrowthInfusion extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("terra_moss", 0.08));
  public static Property<Integer> PROP_TICK_COUNT = new Property<>("tick_count", 1).setDescription("the number of times a random chance to grow the crop is applied every tick");

  public static Modifier PERESKIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "rampant_growth_i"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier WILDEWHEET = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "rampant_breeding"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier WILDROOT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "flower_spreading"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier MOONGLOW_LEAF = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "false_night"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier SPIRIT_HERB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "rampant_growth_ii"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier BAFFLE_CAP = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "embiggening"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier CLOUD_BERRY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "arms_of_air"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier INFERNAL_BULB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "incubation"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier STALICRIPE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "ore_infusion"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier DEWGONIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "hydration"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  static {
    // Conflicts
    WILDROOT.addConflicts(PERESKIA, SPIRIT_HERB, CLOUD_BERRY); // Targets specific flowers
    STALICRIPE.addConflicts(PERESKIA, SPIRIT_HERB, CLOUD_BERRY); // Can't AOE
    BAFFLE_CAP.addConflicts(PERESKIA, SPIRIT_HERB, CLOUD_BERRY); // Again can't aoe
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_growth_infusion");
  public static SpellGrowthInfusion instance = new SpellGrowthInfusion(spellName);

  private int tickCount;

  public SpellGrowthInfusion(ResourceLocation name) {
    super(name, TextFormatting.YELLOW, 48f / 255f, 255f / 255f, 48f / 255f, 192f / 255f, 255f / 255f, 192f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_TICK_COUNT);
    acceptsModifiers(PERESKIA, WILDEWHEET, WILDROOT, MOONGLOW_LEAF, SPIRIT_HERB, BAFFLE_CAP, CLOUD_BERRY, INFERNAL_BULB, STALICRIPE, DEWGONIA);
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
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
    RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().add(0, player.getEyeHeight(), 0), player.getLookVec().scale(8.0f).add(player.getPositionVector().add(0, player.getEyeHeight(), 0)));
    if (result != null) {
      if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
        BlockPos pos = result.getBlockPos();
        IBlockState state = player.world.getBlockState(pos);
        if (Growth.canGrow(player.world, pos, state)) {
          if (!player.world.isRemote) {
            for (int i = 0; i < tickCount + tickCount * amplifier; i++) {
              state.getBlock().randomTick(player.world, pos, state, new Random());
            }
            PacketHandler.sendToAllTracking(new MessageLifeInfusionFX(pos.getX(), pos.getY(), pos.getZ()), player);
            return true;
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
    this.tickCount = properties.get(PROP_TICK_COUNT);
  }
}
