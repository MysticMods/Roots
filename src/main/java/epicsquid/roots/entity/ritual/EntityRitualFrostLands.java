package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.network.fx.MessageFrostLandsProgressFX;
import epicsquid.roots.ritual.IColdRitual;
import epicsquid.roots.ritual.RitualFrostLands;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityRitualFrostLands extends EntityRitualBase implements IColdRitual {

  private RitualFrostLands ritual;

  public EntityRitualFrostLands(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_frost_lands.getDuration() + 20);
    this.ritual = (RitualFrostLands) RitualRegistry.ritual_frost_lands;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (world.isRemote) return;
    List<BlockPos> affectedPositions = new ArrayList<>();

    if (this.ticksExisted % ritual.interval_heal == 0) {
      List<EntitySnowman> snowmen = Util.getEntitiesWithinRadius(world, EntitySnowman.class, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z);
      for (EntitySnowman snowman : snowmen) {
        snowman.heal(snowman.getMaxHealth() - snowman.getHealth());
        affectedPositions.add(snowman.getPosition());
      }

      List<BlockPos> positions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, (BlockPos pos) -> world.isAirBlock(pos.up()) && !world.isAirBlock(pos) && Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos));
      int breakout = 0;
      while (!positions.isEmpty() && breakout < 50) {
        BlockPos choice = positions.get(rand.nextInt(positions.size()));
        IBlockState choiceState = world.getBlockState(choice);
        if (choiceState.getBlock() != Blocks.SNOW_LAYER) {
          if (Blocks.SNOW_LAYER.canPlaceBlockAt(world, choice.up())) {
            world.setBlockState(choice.up(), Blocks.SNOW_LAYER.getDefaultState());
            affectedPositions.add(choice.up());
            break;
          }
        } else if (choiceState.getValue(BlockSnow.LAYERS) < 8 && Util.rand.nextInt(5) == 0) {
          world.setBlockState(choice, choiceState.withProperty(BlockSnow.LAYERS, Math.min(choiceState.getValue(BlockSnow.LAYERS) + 1, 3)));
          affectedPositions.add(choice);
          break;
        }

        breakout++;
      }

      if (Util.rand.nextInt(ritual.interval_spawn) == 0) {
        EntitySnowman snowy = new EntitySnowman(world);
        if (!positions.isEmpty()) {
          BlockPos chosen = positions.get(Util.rand.nextInt(positions.size()));
          snowy.setPosition(chosen.getX() + 0.5, chosen.getY() + 1, chosen.getZ());
          world.spawnEntity(snowy);
          affectedPositions.add(snowy.getPosition());
        }
      }

      positions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, (BlockPos pos) -> (GeneralConfig.getWaterBlocks().contains(world.getBlockState(pos).getBlock()) || world.getBlockState(pos).getBlock() == Blocks.LAVA) && world.isAirBlock(pos.up()));
      if (!positions.isEmpty()) {
        BlockPos choice = positions.get(rand.nextInt(positions.size()));
        IBlockState state = world.getBlockState(choice);

        if (GeneralConfig.getWaterBlocks().contains(state.getBlock())) {
          if (state.getValue(BlockLiquid.LEVEL) == 0) {
            world.setBlockState(choice, Blocks.ICE.getDefaultState());
            affectedPositions.add(choice);
          }
        } else if (state.getBlock() == Blocks.LAVA) {
          if (state.getValue(BlockLiquid.LEVEL) == 0) {
            if (!world.isRemote) {
              world.setBlockState(choice, Blocks.OBSIDIAN.getDefaultState());
              //world.playSound(null, choice, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.3f, 1);
            }
          } else {
            if (!world.isRemote) {
              world.setBlockState(choice, Blocks.COBBLESTONE.getDefaultState());
              //world.playSound(null, choice, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.3f, 1);
            }
          }
          affectedPositions.add(choice);
        }
      }

      positions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, Blocks.FIRE);
      for (BlockPos pos : positions) {
        world.setBlockToAir(pos);
        affectedPositions.add(pos);
      }

      positions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, Blocks.FARMLAND);
      for (BlockPos pos : positions) {
        IBlockState state = world.getBlockState(pos);
        if (state.getValue(BlockFarmland.MOISTURE) != 7) {
          world.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, state.getValue(BlockFarmland.MOISTURE) + 1));
          affectedPositions.add(pos);
        }
      }
    }

    if (!affectedPositions.isEmpty()) {
      MessageFrostLandsProgressFX progress = new MessageFrostLandsProgressFX(affectedPositions);
      PacketHandler.sendToAllTracking(progress, this);
    }
  }
}

