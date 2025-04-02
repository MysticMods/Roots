package mysticmods.roots.api.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.action.GroveReputation;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.List;

public record GroveReputationEntry(Grove grove, ResourceLocation name, GroveReputation reputation,
                                   ResourceLocation tag) {
  public static final MapCodec<GroveReputationEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      RootsRegistries.GROVES.byNameCodec().fieldOf("grove").forGetter(GroveReputationEntry::grove),
      ResourceLocation.CODEC.fieldOf("name").forGetter(GroveReputationEntry::name),
      GroveReputation.CODEC.fieldOf("reputation").forGetter(GroveReputationEntry::reputation),
      ResourceLocation.CODEC.fieldOf("tag").forGetter(GroveReputationEntry::tag)
  ).apply(instance, GroveReputationEntry::new));
  public static final Codec<List<GroveReputationEntry>> LIST_CODEC = CODEC.codec().listOf();

  public GroveReputationEntry(Grove grove, ResourceLocation name, GroveReputation reputation, TagKey<?> tag) {
    this(grove, name, reputation, tag.location());
  }

/*  public record GroveReputationRemover (Grove grove, ResourceLocation name, int gain1, int gain2, int gain3, int gain4) implements DataMapValueRemover<GroveAction, GroveReputationEntry> {

    @Override
    public Optional<GroveReputationEntry> remove(GroveReputationEntry value, Registry<GroveAction> registry, Either<TagKey<GroveAction>, ResourceKey<GroveAction>> source, GroveAction object) {
      return Optional.empty();
    }
  }*/
}
