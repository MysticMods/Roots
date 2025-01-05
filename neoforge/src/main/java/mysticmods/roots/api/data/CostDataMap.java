package mysticmods.roots.api.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger;
import net.neoforged.neoforge.registries.datamaps.DataMapValueRemover;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CostDataMap {
  public record CostData(List<Cost> costs) {
    public static Codec<CostData> CODEC = RecordCodecBuilder.create(instance -> instance.group(Cost.CODEC.listOf().fieldOf("costs").forGetter(CostData::costs)).apply(instance, CostData::new));
  }

  public interface CostRemover<R> extends DataMapValueRemover<R, CostData> {
    Herb herbCost();
    Cost.CostType type();
    float value();

    @Override
    default Optional<CostData> remove(CostData object, Registry<R> arg, Either<TagKey<R>, ResourceKey<R>> either, R object2) {
      List<Cost> newCosts = new ArrayList<>(object.costs());
      newCosts.removeIf(cost -> cost.getHerb().is(herbCost()) && (type() == null || cost.getType() == type()) && (value() == -1.0f || cost.getValue() == value()));
      return Optional.of(new CostData(newCosts));
    }
  }

  public record SpellCostRemover(Herb herbCost, Cost.CostType type,
                                 float value) implements CostRemover<Spell> {
    public static final Codec<SpellCostRemover> CODEC = RecordCodecBuilder.create(instance -> instance.group(RootsRegistries.HERBS.byNameCodec().fieldOf("herb").forGetter(o -> o.herbCost), Cost.CostType.CODEC.optionalFieldOf("type", null).forGetter(o -> o.type), Codec.FLOAT.optionalFieldOf("value", -1.0f).forGetter(o -> o.value)).apply(instance, SpellCostRemover::new));
  }

  public record SpellModifierCostRemover(Herb herbCost, Cost.CostType type,
                                         float value) implements CostRemover<SpellModifier> {
    public static final Codec<SpellModifierCostRemover> CODEC = RecordCodecBuilder.create(instance -> instance.group(RootsRegistries.HERBS.byNameCodec().fieldOf("herb").forGetter(o -> o.herbCost), Cost.CostType.CODEC.optionalFieldOf("type", null).forGetter(o -> o.type), Codec.FLOAT.optionalFieldOf("value", -1.0f).forGetter(o -> o.value)).apply(instance, SpellModifierCostRemover::new));
  }

  public static <R> DataMapValueMerger<R, CostData> listMerger() {
    return (registry, first, firstValue, second, secondValue) -> {
      final List<Cost> list = new ArrayList<>(firstValue.costs);
      list.addAll(secondValue.costs);
      return new CostData(list);
    };
  }

}
