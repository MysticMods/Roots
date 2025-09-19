package mysticmods.roots.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Set;

public class CraftRecipeAction extends GroveAction {
  @Override
  public void log(GroveContext context) {
    RootsAPI.LOG.error("CraftRecipeAction fired by '{}' with recipe '{}'",
        context.player().getName().getString(), context.recipeId());
  }

  @Override
  public boolean test(GroveContext context) {
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, ResourceLocation recipeId, Recipe<?> recipe,
                        BlockEntity blockEntity) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.RECIPE_ID, GroveContext.RECIPE, GroveContext.BLOCK_ENTITY);

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.RECIPE) {
        return recipeId.equals(tag);
      }
      return false;
    }
  }
}
