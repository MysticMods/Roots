package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.TransmutationRecipe;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.ritual.RitualTransmutation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransmutationRitualEntity extends BaseRitualEntity {
  private Set<TransmutationRecipe> recipes = new HashSet<>();
  private RitualTransmutation ritual;

  public TransmutationRitualEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
    ritual = (RitualTransmutation) RitualRegistry.ritual_transmutation;
  }

/*  public TransmutationRitualEntity(World worldIn) {
    super(worldIn);
  }*/

  @Override
  protected void registerData() {
    this.getDataManager().register(lifetime, RitualRegistry.ritual_transmutation.getDuration() + 20);
  }

  @Override
  public void tick() {
    super.tick();

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
      // TODO: When packets are in
      //PacketHandler.sendToAllTracking(new MessageOvergrowthEffectFX(pos.getX(), pos.getY(), pos.getZ()), this);
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
      InventoryHelper.spawnItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, recipe.getEndStack().copy());
    } else {
      world.setBlockState(pos, recipe.getEndState());
    }
  }
}