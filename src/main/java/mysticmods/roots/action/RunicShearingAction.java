package mysticmods.roots.action;

import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Set;

public class RunicShearingAction extends GroveAction {
  @Override
  public void log(GroveContext context) {
/*    RootsAPI.LOG.error("ArriveDimensionAction fired by '{}' in dimension '{}'", context.player().getName()
        .getString(), context.level().dimension().location());*/
  }

  @Override
  public boolean test(GroveContext context) {
    return false;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, ItemStack item, List<ItemStack> recipeOutputs,
                        RunicBlockRecipe recipe, ResourceLocation recipeId, BlockState oldBlockState, BlockState blockState, BlockPos position,
                        Entity targetEntity) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.ITEM, GroveContext.POSITION, GroveContext.OLD_BLOCK_STATE, BLOCK_STATE, GroveContext.TARGET_ENTITY, GroveContext.RECIPE_OUTPUTS);

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      return false;
    }
  }
}
