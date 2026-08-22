package mysticmods.roots.api.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.registry.IDescribed;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class GroveAction implements Consumer<GroveContext>, GroveContextUser, IDescribed {
  private String descriptionId;
  private boolean skipped = false;

  public boolean shouldTest() {
    return !skipped;
  }

  @Override
  public void accept(GroveContext context) {
    if (skipped) {
      return;
    }
    validate(context);
    if (testAndLog(context)) {
      reward(context);
    }
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("grove_action", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }

  public GroveReputation modify(GroveContext context, GroveReputation reputation) {
    return reputation;
  }

  public abstract void log(GroveContext context);

  public abstract boolean test(GroveContext context);

  public boolean shouldLog() {
    return RootsAPI.getInstance().logGroveActions();
  }

  public boolean testAndLog(GroveContext context) {
    if (skipped) {
      return false;
    }
    if (shouldLog()) {
      log(context);
    }
    return test(context);
  }

  public void reward(GroveContext context) {
    if (skipped) {
      return;
    }
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

  public void validate(GroveContext context) {
    if (skipped) {
      return;
    }
    for (GroveContext.Parameter type : getUsedParameters()) {
      if (!GroveContext.hasParameter(context, type)) {
        throw new NoSuchElementException("Missing required parameter '" + type.name() + "' in context '" + context + "' for action '" + this + "'");
      }
    }
  }

  public List<GroveReputationEntry> getReputationEntries() {
    List<GroveReputationEntry> result = builtInRegistryHolder().getData(DataMaps.GROVE_ACTION_REPUTATIONS);
    if (result == null) {
      RootsAPI.LOG.error("Grove action " + this + " has no reputation entries");
      this.skipped = true;
      return Collections.emptyList();
    }

    return result;
  }

  public Holder<GroveAction> builtInRegistryHolder() {
    return RootsRegistries.GROVE_ACTIONS.wrapAsHolder(this);
  }

  @Deprecated
  public boolean is(Holder<GroveAction> holder) {
    return builtInRegistryHolder().is(holder);
  }

  @Deprecated
  public boolean is(GroveAction action) {
    return builtInRegistryHolder().is(action.builtInRegistryHolder());
  }

  public boolean is(ResourceLocation location) {
    return builtInRegistryHolder().is(location);
  }

  public boolean is(ResourceKey<GroveAction> resourceKey) {
    return builtInRegistryHolder().is(resourceKey);
  }

  public boolean is(Predicate<ResourceKey<GroveAction>> predicate) {
    return builtInRegistryHolder().is(predicate);
  }

  public boolean is(TagKey<GroveAction> tagKey) {
    return builtInRegistryHolder().is(tagKey);
  }
}