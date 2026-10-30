package mysticmods.roots.api.state;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

public record StateCategory<V extends Enum<V> & StringRepresentable>(ResourceLocation id, V defaultValue) {
}
