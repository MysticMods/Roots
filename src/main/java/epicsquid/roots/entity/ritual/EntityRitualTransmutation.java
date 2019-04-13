package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.network.fx.MessageOvergrowthEffectFX;
import epicsquid.roots.recipe.TransmutationRecipe;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.ItemSpawnUtil;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class EntityRitualTransmutation extends EntityRitualBase {

  protected static Random random = new Random();
  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualTransmutation.class, DataSerializers.VARINT);
  private Set<TransmutationRecipe> recipes = new HashSet<>();

  public EntityRitualTransmutation(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_transmutation.getDuration() + 20);
  }

  @Override
  public void onUpdate() {
    int curLifetime = getDataManager().get(lifetime);
    getDataManager().set(lifetime, curLifetime - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }
    if (world.isRemote) return;

    if (this.ticksExisted % 100 == 0) {
      List<BlockPos> eligiblePositions = Util.getBlocksWithinRadius(world, getPosition(), 16, 16, 8, (pos) -> {
        if (world.isAirBlock(pos)) return false;
        IBlockState state = world.getBlockState(pos);
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
      BlockPos pos = eligiblePositions.get(random.nextInt(eligiblePositions.size()));
      transmuteBlock(world, pos);
      PacketHandler.sendToAllTracking(new MessageOvergrowthEffectFX(pos.getX(), pos.getY(), pos.getZ()), this);
    }
  }

  @Override
  public DataParameter<Integer> getLifetime() {
    return lifetime;
  }

  private void transmuteBlock(World world, BlockPos pos) {
    IBlockState state = world.getBlockState(pos);

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
      ItemSpawnUtil.spawnItem(world, pos, recipe.getEndStack().copy());
    } else {
      world.setBlockState(pos, recipe.getEndState());
    }
  }
}