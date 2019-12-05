package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.network.fx.MessageOvergrowthEffectFX;
import epicsquid.roots.recipe.TransmutationRecipe;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.ritual.RitualTransmutation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class EntityRitualTransmutation extends EntityRitualBase {
  private Set<TransmutationRecipe> recipes = new HashSet<>();
  private RitualTransmutation ritual;

  public EntityRitualTransmutation(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_transmutation.getDuration() + 20);
    ritual = (RitualTransmutation) RitualRegistry.ritual_transmutation;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (world.isRemote) return;

    if (this.ticksExisted % ritual.interval == 0) {
      List<BlockPos> eligiblePositions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, (pos) -> {
        if (world.isAirBlock(pos)) return false;
        BlockState state = world.getBlockState(pos);
        List<TransmutationRecipe> stateRecipes = ModRecipes.getTransmutationRecipes(state);
        if (stateRecipes == null) return false;
        boolean foundMatch = false;
        for (TransmutationRecipe r : stateRecipes) {
          if (r.matches(world, pos, state)) {
            foundMatch = true;
            break;
          }
        }
        if (foundMatch) {
          recipes.addAll(stateRecipes);
          return true;
        }
        return false;
      });
      if (eligiblePositions.isEmpty()) return;
      BlockPos pos = eligiblePositions.get(Util.rand.nextInt(eligiblePositions.size()));
      transmuteBlock(world, pos);
      PacketHandler.sendToAllTracking(new MessageOvergrowthEffectFX(pos.getX(), pos.getY(), pos.getZ()), this);
    }
  }

  private void transmuteBlock(World world, BlockPos pos) {
    BlockState state = world.getBlockState(pos);

    TransmutationRecipe recipe = null;
    for (TransmutationRecipe r : recipes) {
      if (r.matches(world, pos, state)) {
        recipe = r;
        break;
      }
    }
    if (recipe == null) return;

    if (recipe.itemOutput()) {
      world.setBlockState(pos, Blocks.AIR.getDefaultState());
      ItemUtil.spawnItem(world, pos, recipe.getEndStack().copy());
    } else {
      world.setBlockState(pos, recipe.getEndState());
    }
  }
}