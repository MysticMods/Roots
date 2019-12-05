package epicsquid.roots.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.mechanics.Harvest;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

public class SpellHarvest extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(25);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildewheet", 0.55));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 6);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 6);

  public static String spellName = "spell_harvest";
  public static SpellHarvest instance = new SpellHarvest(spellName);

  private int radius_x, radius_y, radius_z;

  public SpellHarvest(String name) {
    super(name, TextFormatting.GREEN, 57f / 255f, 253f / 255f, 28f / 255f, 197f / 255f, 233f / 255f, 28f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.GOLDEN_HOE),
        /*        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine_seed),*/
        new ItemStack(ModItems.wildewheet),
        new ItemStack(Items.BEETROOT_SEEDS),
        new ItemStack(Items.WHEAT_SEEDS)
    );
  }

  private static List<Block> skipBlocks = Arrays.asList(Blocks.BEDROCK, Blocks.GRASS, Blocks.DIRT, Blocks.STONE, Blocks.WATER, Blocks.LAVA, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);

  @Override
  public boolean cast(PlayerEntity player, List<SpellModule> modules) {
    Harvest.prepare();
    List<BlockPos> affectedPositions = new ArrayList<>();
    List<BlockPos> pumpkinsAndMelons = new ArrayList<>();
    List<BlockPos> reedsAndCactus = new ArrayList<>();
    List<BlockPos> crops = Util.getBlocksWithinRadius(player.world, player.getPosition(),
        radius_x, radius_y, radius_z, (pos) -> {
          if (player.world.isAirBlock(pos)) return false;
          BlockState state = player.world.getBlockState(pos);
          Block block = state.getBlock();

          if (skipBlocks.contains(block)) return false;

          if (state.getBlock() == Blocks.PUMPKIN || state.getBlock() == Blocks.MELON) {
            pumpkinsAndMelons.add(pos);
            return false;
          }
          if (state.getBlock() == Blocks.SUGAR_CANE || state.getBlock() == Blocks.CACTUS) {
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
      BlockState state = player.world.getBlockState(pos);

      if (!player.world.isRemote) {
        Harvest.doHarvest(state, pos, player.world, player);
        affectedPositions.add(pos);
      }
      count++;
    }
    for (BlockPos pos : pumpkinsAndMelons) {
      count++;
      if (!player.world.isRemote) {
        BlockState state = player.world.getBlockState(pos);
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
      BlockState downState = player.world.getBlockState(down);
      while (downState.getBlock() == Blocks.CACTUS || downState.getBlock() == Blocks.SUGAR_CANE) {
        done.add(down);
        down = down.down();
        downState = player.world.getBlockState(down);
      }
      lowest.add(down.up());
      done.add(pos);
    }
    for (BlockPos pos : lowest) {
      BlockState state = player.world.getBlockState(pos.up());
      if (state.getBlock() == Blocks.CACTUS || state.getBlock() == Blocks.SUGAR_CANE) {
        count++;
        if (!player.world.isRemote) {
          state.getBlock().harvestBlock(player.world, player, pos.up(), state, null, player.getHeldItemMainhand());
          player.world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
          affectedPositions.add(pos);
        }
      }
    }

    if (!affectedPositions.isEmpty() && !player.world.isRemote) {
/*      MessageHarvestCompleteFX message = new MessageHarvestCompleteFX(affectedPositions);
      PacketHandler.sendToAllTracking(message, player);*/
    }

    return count != 0;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.radius_x = properties.getProperty(PROP_RADIUS_X);
    this.radius_y = properties.getProperty(PROP_RADIUS_Y);
    this.radius_z = properties.getProperty(PROP_RADIUS_Z);
  }
}
