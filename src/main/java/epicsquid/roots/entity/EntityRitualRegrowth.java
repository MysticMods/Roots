package epicsquid.roots.entity;

import java.util.ArrayList;
import java.util.List;

import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityRitualRegrowth extends EntityRitualBase {
  public EntityRitualRegrowth(World worldIn) {
    super(worldIn);
    getDataManager().register(lifetime, RitualRegistry.ritual_regrowth.duration + 20);
  }

  @Override
  public void onUpdate() {
    ticksExisted++;
    float alpha = (float) Math.min(40, (RitualRegistry.ritual_regrowth.duration + 20) - getDataManager().get(lifetime)) / 40.0f;
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }
    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      //todo: fix particle when available | ParticleUtil.spawnParticleStar(world, (float)posX, (float)posY, (float)posZ, 0, 0, 0, 150, 255, 100, 0.5f*alpha, 20.0f, 40);
      if (rand.nextInt(5) == 0) {
        //todo: fix particle when available | ParticleUtil.spawnParticleSpark(world, (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.0625f*(rand.nextFloat()), 0.125f*(rand.nextFloat()-0.5f), 100, 255, 50, 1.0f*alpha, 1.0f+rand.nextFloat(), 160);
      }
      if (rand.nextInt(2) == 0) {
        for (float i = 0; i < 360; i += rand.nextFloat() * 120.0f) {
          float tx = (float) posX + 2.0f * (float) Math.sin(Math.toRadians(i));
          float ty = (float) posY;
          float tz = (float) posZ + 2.0f * (float) Math.cos(Math.toRadians(i));
          for (int j = 0; j < 4; j++) {
            //todo: fix particle when available | ParticleUtil.spawnParticleStar(world, tx, ty, tz, 0, rand.nextFloat()*0.125f, 0, 100, 255, 50, 0.5f*alpha, 5.0f+rand.nextFloat()*5.0f, 40);
          }
        }
      }
    }
    if (this.ticksExisted % 10 == 0) {
      BlockPos pos = world.getTopSolidOrLiquidBlock(getPosition().add(rand.nextInt(97) - 48, 0, rand.nextInt(97) - 48)).down();
      IBlockState state = world.getBlockState(pos);
      if (state.getBlock() instanceof BlockGrass) {
        if (rand.nextInt(5) == 0) {
          ((BlockGrass) state.getBlock()).grow(world, rand, pos, state);
          if (world.isRemote) {
            for (int i = 0; i < 360; i += rand.nextFloat() * 45.0f) {
              float tx = (float) (pos.getX() + 0.5f) + (rand.nextFloat() - 0.5f) * 0.25f;
              float ty = (float) (pos.getY() + 1.5f) + (rand.nextFloat() - 0.5f) * 0.25f;
              float tz = (float) (pos.getZ() + 0.5f) + (rand.nextFloat() - 0.5f) * 0.25f;
              float vx = 0.125f * (float) Math.sin(Math.toRadians(i));
              float vz = 0.125f * (float) Math.cos(Math.toRadians(i));
              //todo: fix particle when available | ParticleUtil.spawnParticleSpark(world, tx, ty, tz, vx, rand.nextFloat()*0.0625f+0.0625f, vz, 150, 255, 100, 0.5f*alpha, 6.0f+rand.nextFloat()*6.0f, 40);
            }
          }
        }
        if (rand.nextInt(1) == 0) {
          List<IBlockState> saplingTypes = new ArrayList<IBlockState>();
          for (int i = -8; i < 9; i++) {
            for (int j = 1; j < 7; j++) {
              for (int k = -8; k < 9; k++) {
                if (Math.abs(i) >= 4 && Math.abs(k) >= 4) {
                  BlockPos p = pos.add(i, j + 1, k);
                  IBlockState state2 = world.getBlockState(p);
                  if (state2.getBlock() instanceof BlockLeaves) {
                    List<ItemStack> items = ((BlockLeaves) state2.getBlock()).getDrops(world, p, state2, 0);
                    items.addAll(((BlockLeaves) state2.getBlock()).getDrops(world, p, state2, 0));
                    for (ItemStack s : items) {
                      if (Block.getBlockFromItem(s.getItem()) instanceof BlockSapling) {
                        Block b = Block.getBlockFromItem(s.getItem());
                        saplingTypes.add(b.getStateFromMeta(s.getItemDamage()));
                      }
                    }
                  }
                }
              }
            }
          }
          if (saplingTypes.size() > 0 && !world.isRemote) {
            IBlockState saplingState = saplingTypes.get(rand.nextInt(saplingTypes.size()));
            boolean canGenerate = true;
            for (int i = -2; i < 3; i++) {
              for (int j = 1; j < 7; j++) {
                for (int k = -2; k < 3; k++) {
                  if (!world.isAirBlock(pos.add(i, j + 1, k))) {
                    canGenerate = false;
                  }
                }
              }
            }
            if (canGenerate) {
              ((BlockSapling) saplingState.getBlock()).generateTree(world, pos.up(), saplingState, rand);
            }
          }
        }
      }
    }
  }
}