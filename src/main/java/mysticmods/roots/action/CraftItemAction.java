package mysticmods.roots.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class CraftItemAction extends GroveAction {
  @Override
  public void log(GroveContext context) {
    RootsAPI.LOG.error("CraftItemAction fired by '{}' with item '{}'",
        context.player().getName().getString(), context.item().getDisplayName().getString());
  }

  @Override
  public boolean test(GroveContext context) {
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, ItemStack item) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.ITEM);

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.ITEM) {
        return this.item().is(TagKey.create(Registries.ITEM, tag));
      } else if (type == GroveReputationEntry.SubEntryType.EXACT_ITEM) {
        return this.item().getItemHolder().is(tag);
      }

      return false;
    }
  }
}
