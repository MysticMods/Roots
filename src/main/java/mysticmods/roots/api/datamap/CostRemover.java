package mysticmods.roots.api.datamap;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.datamaps.DataMapValueRemover;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record CostRemover<T>(Herb herbCost, Cost.CostType type,
                             float value) implements DataMapValueRemover<T, CostInstance> {
  public static Codec<CostInstance> CODEC = CostInstance.CODEC;

  public static <T> Codec<CostRemover<T>> codec() {
    return RecordCodecBuilder.create(instance -> instance.group(RootsRegistries.HERBS.byNameCodec().fieldOf("herb")
            .forGetter(CostRemover::herbCost), Cost.CostType.CODEC.optionalFieldOf("type", null)
            .forGetter(CostRemover::type), Codec.FLOAT.optionalFieldOf("defaultValue", -1.0f).forGetter(CostRemover::value))
        .apply(instance, CostRemover::new));
  }

  @Override
  public Optional<CostInstance> remove(CostInstance object, Registry<T> arg, Either<TagKey<T>, ResourceKey<T>> either, T object2) {
    List<Cost> newCosts = new ArrayList<>(object.costs());
    newCosts.removeIf(cost -> cost.getHerb()
        .is(herbCost()) && (type() == null || cost.getType() == type()) && (value() == -1.0f || cost.getValue() == value()));
    return Optional.of(CostInstance.of(newCosts));
  }
}
