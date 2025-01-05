package mysticmods.roots.api.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger;
import net.neoforged.neoforge.registries.datamaps.DataMapValueRemover;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpellCostDataMap {
  public static final AdvancedDataMapType<Spell, SpellCostData, CostRemover> SPELL_COST_DATA = AdvancedDataMapType.builder(RootsAPI.rl("spell_cost_data"), RootsRegistries.Keys.SPELLS, SpellCostData.CODEC)
      .merger(new CostListMerger())
      .remover(CostRemover.CODEC)
      .build();

  public record SpellCostData(List<Cost> costs) {
    public static Codec<SpellCostData> CODEC = RecordCodecBuilder.create(instance -> instance.group(Cost.CODEC.listOf().fieldOf("costs").forGetter(SpellCostData::costs)).apply(instance, SpellCostData::new));
    public static StreamCodec<RegistryFriendlyByteBuf, SpellCostData> STREAM_CODEC = Cost.STREAM_CODEC.apply(ByteBufCodecs.list()).map(SpellCostData::new, SpellCostData::costs);
  }

  public record CostRemover(Herb herbCost, Cost.CostType type,
                            float value) implements DataMapValueRemover<Spell, SpellCostData> {
    public static final Codec<CostRemover> CODEC = RecordCodecBuilder.create(instance -> instance.group(RootsRegistries.HERBS.byNameCodec().fieldOf("herb").forGetter(o -> o.herbCost), Cost.CostType.CODEC.optionalFieldOf("type", null).forGetter(o -> o.type), Codec.FLOAT.optionalFieldOf("value", -1.0f).forGetter(o -> o.value)).apply(instance, CostRemover::new));

    @Override
    public Optional<SpellCostData> remove(SpellCostData object, Registry<Spell> arg, Either<TagKey<Spell>, ResourceKey<Spell>> either, Spell object2) {
      List<Cost> newCosts = new ArrayList<>(object.costs());
      newCosts.removeIf(cost -> cost.getHerb().is(herbCost) && (type == null || cost.getType() == type) && (value == -1.0f || cost.getValue() == value));
      return Optional.of(new SpellCostData(newCosts));
    }
  }

  public static final class CostListMerger implements DataMapValueMerger<Spell, SpellCostData> {
    @Override
    public SpellCostData merge(Registry<Spell> arg, Either<TagKey<Spell>, ResourceKey<Spell>> either, SpellCostData object, Either<TagKey<Spell>, ResourceKey<Spell>> either2, SpellCostData object2) {
      List<Cost> result = new ArrayList<>();
      result.addAll(object.costs());
      result.addAll(object2.costs());
      return new SpellCostData(result);
    }
  }

  public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
    event.register(SPELL_COST_DATA);
  }
}
