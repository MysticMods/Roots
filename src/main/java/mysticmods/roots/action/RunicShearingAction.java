package mysticmods.roots.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class RunicShearingAction extends GroveAction {
  @Override
  public void log(GroveContext context) {
    RootsAPI.LOG.error("RunicShearingAction fired by '{}' in dimension '{}' at '{}'", context.player().getName()
        .getString(), context.level().dimension().location(), context.position());
  }

  @Override
  public boolean test(GroveContext context) {
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, ItemStack item, List<ItemStack> recipeOutputs,
                        @Nullable Recipe<?> recipe, ResourceLocation recipeId, @Nullable BlockState oldBlockState,
                        @Nullable BlockState blockState, @Nullable BlockPos position,
                        @Nullable Entity targetEntity) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.ITEM, GroveContext.POSITION);

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.BLOCK) {
        return blockState != null && blockState.is(TagKey.create(Registries.BLOCK, tag));
      } else if (type == GroveReputationEntry.SubEntryType.OLD_BLOCK) {
        return oldBlockState != null && oldBlockState.is(TagKey.create(Registries.BLOCK, tag));
      } else if (type == GroveReputationEntry.SubEntryType.RECIPE) {
        return recipeId.equals(tag);
      } else if (type == GroveReputationEntry.SubEntryType.TARGET_ENTITY) {
        return targetEntity != null && targetEntity.getType().is(TagKey.create(Registries.ENTITY_TYPE, tag));
      } else if (type == GroveReputationEntry.SubEntryType.TOOL) {
        return this.item().is(TagKey.create(Registries.ITEM, tag));
      } else if (type == GroveReputationEntry.SubEntryType.ITEM) {
        var itemTag = TagKey.create(Registries.ITEM, tag);
        for (ItemStack item : recipeOutputs) {
          if (item.is(itemTag)) {
            return true;
          }
        }

        return false;
      } else if (type == GroveReputationEntry.SubEntryType.EXACT_ITEM) {
        for (ItemStack item : recipeOutputs) {
          if (item.getItemHolder().is(tag)) {
            return true;
          }
        }
        return false;
      }
      return false;
    }
  }
}
