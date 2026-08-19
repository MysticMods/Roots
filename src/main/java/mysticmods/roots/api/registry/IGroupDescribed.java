package mysticmods.roots.api.registry;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IGroupDescribed {
  default String getOrCreateGroupDescriptionId() {
    return null;
  }

  default String getGroupDescriptionId() {
    return getOrCreateGroupDescriptionId();
  }

  boolean canGroup();

  @NotNull
  default GroupId getGroupKey() {
    return GroupId.NONE;
  }

  default MutableComponent getGroupName(int count) {
    if (!canGroup() || count == 0 || getGroupKey().isEmpty()) {
      throw new IllegalStateException("Tried to get component for IGroupDescribed '" + this + "' where the object is not configured to be groupable.");
    }
    return Component.translatable(this.getGroupDescriptionId(), count);
  }

  default MutableComponent getGroupName(int count, Style style) {
    return getGroupName(count).setStyle(style);
  }

  record GroupId (String groupKey) {
    public static final GroupId NONE = new GroupId(null);

    public String createDescriptionId (String signifier, ResourceKey<?> key) {
      if (isEmpty()) {
        throw new IllegalStateException("Tried to generate a description id for invalid or empty GroupId: " + this);
      }

      return Util.makeDescriptionId(signifier, ResourceLocation.fromNamespaceAndPath(key.location().getNamespace(), groupKey()));
    }

    public boolean isEmpty () {
      return this == NONE || groupKey == null || groupKey.isEmpty();
    }
  }
}
