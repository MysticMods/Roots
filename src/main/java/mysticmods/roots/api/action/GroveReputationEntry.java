package mysticmods.roots.api.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Locale;

public record GroveReputationEntry(Grove grove, ResourceLocation name, GroveReputation reputation,
                                   List<SubEntry> entries) {
  public static final MapCodec<GroveReputationEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      RootsRegistries.GROVES.byNameCodec().fieldOf("grove").forGetter(GroveReputationEntry::grove),
      ResourceLocation.CODEC.fieldOf("name").forGetter(GroveReputationEntry::name),
      GroveReputation.CODEC.fieldOf("reputation").forGetter(GroveReputationEntry::reputation),
      SubEntry.LIST_CODEC.optionalFieldOf("entries", List.of()).forGetter(GroveReputationEntry::entries)
  ).apply(instance, GroveReputationEntry::new));
  public static final Codec<List<GroveReputationEntry>> LIST_CODEC = CODEC.codec().listOf();

  public GroveReputationEntry(Grove grove, ResourceLocation name, GroveReputation reputation, SubEntryType type, TagKey<?> tag) {
    this(grove, name, reputation, List.of(new SubEntry(type, tag.location())));
  }

  public enum SubEntryType implements StringRepresentable {
    BLOCK,
    OLD_BLOCK,
    ITEM,
    TARGET_ENTITY,
    SECONDARY_ENTITY,
    TERTIARY_ENTITY,
    RITUAL,
    SPELL,
    RECIPE;

    public static final Codec<SubEntryType> CODEC = StringRepresentable.fromEnum(SubEntryType::values);

    @Override
    public String getSerializedName() {
      return name().toLowerCase(Locale.ROOT);
    }
  }

  public record SubEntry(SubEntryType type, ResourceLocation name) {
    public static Codec<SubEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SubEntryType.CODEC.fieldOf("type").forGetter(SubEntry::type),
        ResourceLocation.CODEC.fieldOf("name").forGetter(SubEntry::name)
    ).apply(instance, SubEntry::new));
    public static Codec<List<SubEntry>> LIST_CODEC = CODEC.listOf();
  }
}
