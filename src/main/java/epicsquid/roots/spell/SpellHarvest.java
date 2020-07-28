package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.mechanics.Harvest;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageHarvestCompleteFX;
import epicsquid.roots.properties.Property;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

public class SpellHarvest extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(25);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildewheet", 0.55));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 6).setDescription("radius on the X axis of the area the spell has effect on");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5).setDescription("radius on the Y axis of the area the spell has effect on");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 6).setDescription("radius on the Z axis of the area the spell has effect on");

  public static Modifier PERESKIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "extended_harvest"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier WILDROOT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "magnetic_harvest"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier MOONGLOW_LEAF = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "midnight_harvest"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier SPIRIT_HERB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "gifts_of_undeath"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier TERRA_MOSS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "targeted_harvest"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier BAFFLE_CAP = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "poisoned_harvest"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier CLOUD_BERRY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "regrowth"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier INFERNAL_BULB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "fiery_harvest"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier STALICRIPE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "yield"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier DEWGONIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "block_harvest"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  static {
    // Conflcits
    PERESKIA.addConflict(TERRA_MOSS); // Can't increase/decrease at the same time
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_harvest");
  public static SpellHarvest instance = new SpellHarvest(spellName);

  private int radius_x, radius_y, radius_z;

  public SpellHarvest(ResourceLocation name) {
    super(name, TextFormatting.GREEN, 57f / 255f, 253f / 255f, 28f / 255f, 197f / 255f, 233f / 255f, 28f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z);
    acceptsModifiers(PERESKIA, WILDROOT, MOONGLOW_LEAF, SPIRIT_HERB, TERRA_MOSS, BAFFLE_CAP, CLOUD_BERRY, INFERNAL_BULB, STALICRIPE, DEWGONIA);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.GOLDEN_HOE),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine_seed),
        new ItemStack(ModItems.wildewheet),
        new ItemStack(Items.BEETROOT_SEEDS),
        new ItemStack(Items.WHEAT_SEEDS)
    );
  }

  private static List<Block> skipBlocks = Arrays.asList(Blocks.BEDROCK, Blocks.GRASS, Blocks.DIRT, Blocks.STONE, Blocks.TALLGRASS, Blocks.WATER, Blocks.LAVA, Blocks.DOUBLE_PLANT, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks) {
    Harvest.prepare();
    List<BlockPos> affectedPositions = new ArrayList<>();
    List<BlockPos> pumpkinsAndMelons = new ArrayList<>();
    List<BlockPos> reedsAndCactus = new ArrayList<>();
    List<BlockPos> crops = Util.getBlocksWithinRadius(player.world, player.getPosition(),
        radius_x, radius_y, radius_z, (pos) -> {
          if (player.world.isAirBlock(pos)) return false;
          IBlockState state = player.world.getBlockState(pos);
          Block block = state.getBlock();

          if (skipBlocks.contains(block)) return false;

          if (state.getBlock() == Blocks.PUMPKIN || state.getBlock() == Blocks.MELON_BLOCK) {
            pumpkinsAndMelons.add(pos);
            return false;
          }
          if (state.getBlock() == Blocks.REEDS || state.getBlock() == Blocks.CACTUS) {
            reedsAndCactus.add(pos);
            return false;
          }
          IProperty<?> prop = Harvest.resolveStates(state);
          if (prop != null) {
            return Harvest.isGrown(state);
          }
          return false;
        });

    int count = 0;

    for (BlockPos pos : crops) {
      IBlockState state = player.world.getBlockState(pos);

      if (!player.world.isRemote) {
        Harvest.doHarvest(state, pos, player.world, player);
        affectedPositions.add(pos);
      }
      count++;
    }
    for (BlockPos pos : pumpkinsAndMelons) {
      count++;
      if (!player.world.isRemote) {
        IBlockState state = player.world.getBlockState(pos);
        player.world.setBlockState(pos, Blocks.AIR.getDefaultState());
        state.getBlock().harvestBlock(player.world, player, pos, state, null, player.getHeldItemMainhand());
        affectedPositions.add(pos);
      }
    }
    Set<BlockPos> done = new HashSet<>();
    List<BlockPos> lowest = new ArrayList<>();
    for (BlockPos pos : reedsAndCactus) {
      if (done.contains(pos)) continue;

      BlockPos down = pos.down();
      IBlockState downState = player.world.getBlockState(down);
      while (downState.getBlock() == Blocks.CACTUS || downState.getBlock() == Blocks.REEDS) {
        done.add(down);
        down = down.down();
        downState = player.world.getBlockState(down);
      }
      lowest.add(down.up());
      done.add(pos);
    }
    for (BlockPos pos : lowest) {
      IBlockState state = player.world.getBlockState(pos.up());
      if (state.getBlock() == Blocks.CACTUS || state.getBlock() == Blocks.REEDS) {
        count++;
        if (!player.world.isRemote) {
          state.getBlock().harvestBlock(player.world, player, pos.up(), state, null, player.getHeldItemMainhand());
          player.world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
          affectedPositions.add(pos);
        }
      }
    }

    if (!affectedPositions.isEmpty() && !player.world.isRemote) {
      MessageHarvestCompleteFX message = new MessageHarvestCompleteFX(affectedPositions);
      PacketHandler.sendToAllTracking(message, player);
    }

    return count != 0;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
  }
}
