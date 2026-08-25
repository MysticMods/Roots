package mysticmods.roots.api.registry;

import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

// TODO: Group descriptions
public record GroupId(String groupKey, boolean useGroupDescription) {
  public static final GroupId NONE = new GroupId(null, false);

  public GroupId(String groupKey) {
    this(groupKey, false);
  }

  public String createDescriptionId(String signifier, String namespace) {
    return Util.makeDescriptionId(signifier, ResourceLocation.fromNamespaceAndPath(namespace, groupKey()));
  }

  public String createDescriptionId(String signifier, ResourceKey<?> key) {
    if (isEmpty()) {
      throw new IllegalStateException("Tried to generate a description id for invalid or empty GroupId: " + this);
    }

    return createDescriptionId(signifier, key.location().getNamespace());
  }

  public boolean isEmpty() {
    return this == NONE || groupKey == null || groupKey.isEmpty();
  }
}
