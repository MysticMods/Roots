package mysticmods.roots.integration.jei.ingredient.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public record RootsEntityType(EntityType<?> entity) {
  public static List<RootsEntityType> fromTag(TagKey<EntityType<?>> tag) {
    List<RootsEntityType> result = new ArrayList<>();
    BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(tag).forEach(o -> result.add(new RootsEntityType(o.value())));
    return result;
  }
}
