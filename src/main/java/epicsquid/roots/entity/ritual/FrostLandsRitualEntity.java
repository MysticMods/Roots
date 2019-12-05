package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.ritual.IColdRitual;
import epicsquid.roots.ritual.RitualFrostLands;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FrostLandsRitualEntity extends BaseRitualEntity implements IColdRitual {

  private RitualFrostLands ritual;

  public FrostLandsRitualEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
    this.ritual = (RitualFrostLands) RitualRegistry.ritual_frost_lands;
  }

  @Override
  protected void registerData() {
    this.getDataManager().register(lifetime, RitualRegistry.ritual_frost_lands.getDuration() + 20);
  }

  @Override
  public void tick() {
    super.tick();

    if (world.isRemote) return;
    List<BlockPos> affectedPositions = new ArrayList<>();

    if (this.ticksExisted % ritual.interval_heal == 0) {
      List<SnowGolemEntity> snowmen = Util.getEntitiesWithinRadius(world, SnowGolemEntity.class, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z);
      for (SnowGolemEntity snowman : snowmen) {
        snowman.heal(snowman.getMaxHealth() - snowman.getHealth());
        affectedPositions.add(snowman.getPosition());
      }

      // TODO: How do you place snow layers down?

      List<BlockPos> positions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, (BlockPos pos) -> world.isAirBlock(pos.up()) && !world.isAirBlock(pos) /*&& Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos)*/);
      int breakout = 0;
      /*while (!positions.isEmpty() && breakout < 20) {
        BlockPos choice = positions.get(rand.nextInt(positions.size()));
        if (world.getBlockState(choice).getBlock() == Blocks.SNOW_LAYER) {
          breakout++;
        } else if (Blocks.SNOW_LAYER.canPlaceBlockAt(world, choice.up())) {
          world.setBlockState(choice.up(), Blocks.SNOW_LAYER.getDefaultState());
          affectedPositions.add(choice.up());
          break;
        }
      }*/

      if (Util.rand.nextInt(ritual.interval_spawn) == 0) {
        SnowGolemEntity snowy = EntityType.SNOW_GOLEM.create(world);
        if (!positions.isEmpty() && snowy != null) {
          BlockPos chosen = positions.get(Util.rand.nextInt(positions.size()));
          snowy.setPosition(chosen.getX() + 0.5, chosen.getY() + 1, chosen.getZ());
          world.addEntity(snowy);
          affectedPositions.add(snowy.getPosition());
        }
      }

      positions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, (BlockPos pos) -> (world.getBlockState(pos).getBlock() == Blocks.WATER || world.getBlockState(pos).getBlock() == Blocks.LAVA) && world.isAirBlock(pos.up()));
      if (!positions.isEmpty()) {
        BlockPos choice = positions.get(rand.nextInt(positions.size()));
        BlockState state = world.getBlockState(choice);

        if (state.getBlock() == Blocks.WATER) {
          if (state.get(FlowingFluidBlock.LEVEL) == 0) {
            world.setBlockState(choice, Blocks.ICE.getDefaultState());
            affectedPositions.add(choice);
          }
        } else if (state.getBlock() == Blocks.LAVA) {
          if (state.get(FlowingFluidBlock.LEVEL) == 0) {
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
        // TODO: Is there no alternative to setBlockToAir?
        //world.setBlockToAir(pos);
        affectedPositions.add(pos);
      }

      positions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, Blocks.FARMLAND);
      for (BlockPos pos : positions) {
        BlockState state = world.getBlockState(pos);
        if (state.get(FarmlandBlock.MOISTURE) != 7) {
          world.setBlockState(pos, state.with(FarmlandBlock.MOISTURE, state.get(FarmlandBlock.MOISTURE) + 1));
          affectedPositions.add(pos);
        }
      }
    }

    if (!affectedPositions.isEmpty()) {
      // TODO: Reinstate this when we do packets
      /*MessageFrosLandsProgressFX progress = new MessageFrosLandsProgressFX(affectedPositions);
      PacketHandler.sendToAllTracking(progress, this);*/
    }
  }
}

