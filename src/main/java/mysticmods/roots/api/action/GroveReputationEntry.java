package mysticmods.roots.api.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public record GroveReputationEntry(Grove grove, ResourceLocation name, GroveReputation reputation, boolean unique,
                                   List<SubEntry> entries) {
  public static final MapCodec<GroveReputationEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      RootsRegistries.GROVES.byNameCodec().fieldOf("grove").forGetter(GroveReputationEntry::grove),
      ResourceLocation.CODEC.fieldOf("name").forGetter(GroveReputationEntry::name),
      GroveReputation.CODEC.fieldOf("reputation").forGetter(GroveReputationEntry::reputation),
      Codec.BOOL.fieldOf("unique").forGetter(GroveReputationEntry::unique),
      SubEntry.LIST_CODEC.optionalFieldOf("entries", List.of()).forGetter(GroveReputationEntry::entries)
  ).apply(instance, GroveReputationEntry::new));
  public static final Codec<List<GroveReputationEntry>> LIST_CODEC = CODEC.codec().listOf();

  public GroveReputationEntry(Grove grove, ResourceLocation name, GroveReputation reputation, SubEntryType type, TagKey<?> tag) {
    this(grove, name, reputation, false, List.of(new SubEntry(type, tag.location())));
  }

  public GroveReputationEntry(Grove grove, ResourceLocation name, GroveReputation reputation) {
    this(grove, name, reputation, false, Collections.emptyList());
  }

  public enum SubEntryType implements StringRepresentable {
    BLOCK, // TagKey<Block> -> TagKey<Item>
    OLD_BLOCK, // TagKey<Block> -> TagKey<Item>
    ITEM, // TagKey<Item>
    EXACT_ITEM, // ItemStack
    OLD_ITEM, // TagKey<Item>
    TARGET_ENTITY, // EntityType or TagKey<EntityType<?>>
    SECONDARY_ENTITY, // EntityType or TagKey<EntityType<?>>
    TERTIARY_ENTITY, // EntityType or TagKey<EntityType<?>>
    RITUAL, // Ritual or TagKey<Ritual> -> TagKey<Item>
    EXACT_RITUAL, // Ritual or ItemStack
    RITUAL_MODIFIER, // NA
    SPELL, // TagKey<Spell> -> TagKey<Item>
    EXACT_SPELL, // Spell -> ItemStack
    SPELL_MODIFIER, // NA
    RECIPE, // ItemStack
    DAMAGE, // DamageType -> ItemStack map
    DIMENSION, // Dimension -> ItemStack map
    ALWAYS; // Symbol for always

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

    public SubEntry() {
      this(SubEntryType.ALWAYS, RootsAPI.rl("always"));
    }
  }
}
