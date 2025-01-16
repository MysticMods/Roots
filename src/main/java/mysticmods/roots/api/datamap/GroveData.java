package mysticmods.roots.api.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public record GroveData(TagKey<Herb> alignedHerbs,
                        TagKey<Grove> alignedGroves,
                        TagKey<Grove> opposedGroves,
                        TagKey<Spell> alignedSpells,
                        TagKey<Spell> opposedSpells,
                        TagKey<Ritual> alignedRituals,
                        TagKey<Ritual> opposedRituals) {
  public static MapCodec<GroveData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
          TagKey.codec(RootsRegistries.Keys.HERBS).fieldOf("alignedHerbs").forGetter(GroveData::alignedHerbs),
          TagKey.codec(RootsRegistries.Keys.GROVES).fieldOf("alignedGroves").forGetter(GroveData::alignedGroves),
          TagKey.codec(RootsRegistries.Keys.GROVES).fieldOf("opposedGroves").forGetter(GroveData::opposedGroves),
          TagKey.codec(RootsRegistries.Keys.SPELLS).fieldOf("alignedSpells").forGetter(GroveData::alignedSpells),
          TagKey.codec(RootsRegistries.Keys.SPELLS).fieldOf("opposedSpells").forGetter(GroveData::opposedSpells),
          TagKey.codec(RootsRegistries.Keys.RITUALS).fieldOf("alignedRituals").forGetter(GroveData::alignedRituals),
          TagKey.codec(RootsRegistries.Keys.RITUALS).fieldOf("opposedRituals").forGetter(GroveData::opposedRituals)
      ).apply(instance, GroveData::new)
  );
  public static Codec<GroveData> CODEC = MAP_CODEC.codec();
  public static StreamCodec<ByteBuf, GroveData> STREAM_CODEC = ExtraStreamCodecs.composite(
      ExtraStreamCodecs.HERB_TAG_STREAM_CODEC, GroveData::alignedHerbs,
      ExtraStreamCodecs.GROVE_TAG_STREAM_CODEC, GroveData::alignedGroves,
      ExtraStreamCodecs.GROVE_TAG_STREAM_CODEC, GroveData::opposedGroves,
      ExtraStreamCodecs.SPELL_TAG_STREAM_CODEC, GroveData::alignedSpells,
      ExtraStreamCodecs.SPELL_TAG_STREAM_CODEC, GroveData::opposedSpells,
      ExtraStreamCodecs.RITUAL_TAG_STREAM_CODEC, GroveData::alignedRituals,
      ExtraStreamCodecs.RITUAL_TAG_STREAM_CODEC, GroveData::opposedRituals,
      GroveData::new
  );

  public GroveData(TagKey<Herb> alignedHerbs, TagKey<Grove> alignedGroves, TagKey<Grove> opposedGroves, TagKey<Spell> alignedSpells, TagKey<Spell> opposedSpells, TagKey<Ritual> alignedRituals, TagKey<Ritual> opposedRituals) {
    this.alignedHerbs = alignedHerbs;
    this.alignedGroves = alignedGroves;
    this.opposedGroves = opposedGroves;
    this.alignedSpells = alignedSpells;
    this.opposedSpells = opposedSpells;
    this.alignedRituals = alignedRituals;
    this.opposedRituals = opposedRituals;
  }

  public GroveData(GroveInitRecord record) {
    this(record.alignedHerbs(), record.alignedGroves(), record.opposedGroves(), record.alignedSpells(), record.opposedSpells(), record.alignedRituals(), record.opposedRituals());
  }

  public record GroveInitRecord(ResourceKey<Grove> groveKey,
                                TagKey<Herb> alignedHerbs,
                                TagKey<Grove> alignedGroves,
                                TagKey<Grove> opposedGroves,
                                TagKey<Spell> alignedSpells,
                                TagKey<Spell> opposedSpells,
                                TagKey<Ritual> alignedRituals,
                                TagKey<Ritual> opposedRituals) {

  }
}
