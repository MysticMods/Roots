package mysticmods.roots.api.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

// Actions:
// - Aging up a baby animal -> Unsure about this
// -> level, player, baby, spellinstance, itemstack
// - Completing a trial <- Unsure about this too
// -> level, player, List<Player> other players, block pos, block state, block entity, TrialSpawner instance


// - Brush a block
// -> level, player, blockstate, block entity, itemstack
// - Milking a cow
// -> level, player, entity, itemstack, spell instance
// - Successfully casting a geas
// -> level, player, entity, spell instance

public interface GroveAction extends Consumer<GroveContext>, GroveContextUser {
  @Override
  default void accept(GroveContext context) {
    validate(context);
    if (test(context)) {
      reward(context);
    }
  }

  default GroveReputation modify (GroveContext context, GroveReputation reputation) {
    return reputation;
  }

  boolean test (GroveContext context);

  default void reward (GroveContext context) {
    for (GroveReputationEntry entry : getReputationEntries()) {
      boolean doReward = true;
      for (GroveReputationEntry.SubEntry subEntry : entry.entries()) {
        if (subEntry.type() == GroveReputationEntry.SubEntryType.ALWAYS) {
          break;
        }
        if (!context.is(subEntry.type(), subEntry.name())) {
          doReward = false;
          break;
        }
      }
      if (doReward) {
        RootsAPI.getInstance()
            .grant(context.player(), entry.grove(), entry.name(), modify(context, entry.reputation()), entry.unique());
      }
    }
  }

  default void validate(GroveContext context) {
    for (GroveContext.Parameter type : getUsedParameters()) {
      if (!GroveContext.hasParameter(context, type)) {
        throw new NoSuchElementException("Missing required parameter '" + type.name() + "' in context");
      }
    }
  }

  default List<GroveReputationEntry> getReputationEntries() {
    List<GroveReputationEntry> result = builtinRegistryHolder().getData(DataMaps.GROVE_ACTION_REPUTATIONS);
    if (result == null) {
      RootsAPI.LOG.error("Grove action " + this + " has no reputation entries");
      return Collections.emptyList();
    }

    return result;
  }

  default Holder<GroveAction> builtinRegistryHolder() {
    return RootsRegistries.GROVE_ACTIONS.wrapAsHolder(this);
  }

  @Deprecated
  default boolean is (Holder<GroveAction> holder) {
    return builtinRegistryHolder().is(holder);
  }

  @Deprecated
  default boolean is (GroveAction action) {
    return builtinRegistryHolder().is(action.builtinRegistryHolder());
  }

  default boolean is(ResourceLocation location) {
    return builtinRegistryHolder().is(location);
  }

  default boolean is(ResourceKey<GroveAction> resourceKey) {
    return builtinRegistryHolder().is(resourceKey);
  }

  default boolean is(Predicate<ResourceKey<GroveAction>> predicate) {
    return builtinRegistryHolder().is(predicate);
  }

  default boolean is(TagKey<GroveAction> tagKey) {
    return builtinRegistryHolder().is(tagKey);
  }
}