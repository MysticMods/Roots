package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.network.fx.MessageOvergrowthEffectFX;
import epicsquid.roots.recipe.TransmutationRecipe;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.ritual.RitualTransmutation;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRitualTransmutation extends EntityRitualBase {
  private Map<BlockPos, TransmutationRecipe> recipes = new HashMap<>();
  private RitualTransmutation ritual;

  public EntityRitualTransmutation(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_transmutation.getDuration() + 20);
    ritual = (RitualTransmutation) RitualRegistry.ritual_transmutation;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (world.isRemote) {
      return;
    }

    if (this.ticksExisted % ritual.interval == 0) {
      recipes.clear();
      List<BlockPos> positions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, (pos) -> {
        List<TransmutationRecipe> incoming = ModRecipes.getTransmutationRecipesFor(world, pos);
        if (!incoming.isEmpty()) {
          if (incoming.size() > 1) {
            Roots.logger.error("Multiple (" + incoming.size() + ") recipes matching block state at " + pos.toString());
          }
          recipes.put(pos, incoming.get(0));
          return true;
        }
        return false;
      });
      if (positions.isEmpty()) {
        return;
      }
      BlockPos pos = positions.remove(Util.rand.nextInt(positions.size()));
      while (true) {
        if (transmuteBlock(world, pos) || positions.isEmpty()) {
          break;
        } else {
          pos = positions.remove(Util.rand.nextInt(positions.size()));
        }
      }
      // TODO: Better visual
      PacketHandler.sendToAllTracking(new MessageOvergrowthEffectFX(pos.getX(), pos.getY(), pos.getZ()), this);
    }
  }

  private boolean transmuteBlock(World world, BlockPos pos) {
    TransmutationRecipe recipe = recipes.get(pos);
    if (recipe == null) {
      return false;
    }

    if (recipe.getStack().isEmpty()) {
      recipe.getState().ifPresent((s) -> world.setBlockState(pos, s));
    } else {
      world.setBlockState(pos, Blocks.AIR.getDefaultState());
      ItemUtil.spawnItem(world, pos, recipe.getStack().copy());
    }
    return true;
  }
}