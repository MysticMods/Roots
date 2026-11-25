package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.IntFunction;

public record PillarType(String name, TagKey<Block> pillar, PillarHeight height, TagKey<Item> itemTag) {
  public static final PillarType TALL_RUNESTONE = new PillarType("tall_runestone", RootsTags.Blocks.TALL_RUNESTONE_PILLARS, PillarHeight.TALL, RootsTags.Items.TALL_RUNESTONE_PILLARS);
  public static final PillarType SHORT_RUNESTONE = new PillarType("short_runestone", RootsTags.Blocks.SHORT_RUNESTONE_PILLARS, PillarHeight.SHORT, RootsTags.Items.SHORT_RUNESTONE_PILLARS);
  public static final PillarType TALL_RUNED_OBSIDIAN = new PillarType("tall_runed_obsidian", RootsTags.Blocks.TALL_RUNED_OBSIDIAN_PILLARS, PillarHeight.TALL, RootsTags.Items.TALL_RUNED_OBSIDIAN_PILLARS);
  public static final PillarType SHORT_RUNED_OBSIDIAN = new PillarType("short_runestone", RootsTags.Blocks.SHORT_RUNED_OBSIDIAN_PILLARS, PillarHeight.SHORT, RootsTags.Items.SHORT_RUNED_OBSIDIAN_PILLARS);
  public static final PillarType TALL = new PillarType("tall", RootsTags.Blocks.TALL_PILLARS, PillarHeight.TALL, RootsTags.Items.TALL_PILLARS);
  public static final PillarType SHORT = new PillarType("short", RootsTags.Blocks.SHORT_PILLARS, PillarHeight.SHORT, RootsTags.Items.SHORT_PILLARS);
  public static final PillarType ANY = new PillarType("any", RootsTags.Blocks.PILLARS, PillarHeight.ANY, RootsTags.Items.PILLARS);

  public static final MapCodec<PillarType> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.STRING.fieldOf("name").forGetter(PillarType::name),
      TagKey.codec(Registries.BLOCK).fieldOf("pillar").forGetter(PillarType::pillar),
      PillarHeight.CODEC.fieldOf("height").forGetter(PillarType::height),
      TagKey.codec(Registries.ITEM).fieldOf("itemTag").forGetter(PillarType::itemTag)
  ).apply(instance, PillarType::new));
  public static final Codec<PillarType> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, PillarType> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8, PillarType::name,
      ExtraStreamCodecs.BLOCK_TAG_STREAM_CODEC, PillarType::pillar,
      PillarHeight.STREAM_CODEC, PillarType::height,
      ExtraStreamCodecs.ITEM_TAG_STREAM_CODEC, PillarType::itemTag,
      PillarType::new
  );

  enum PillarHeight implements StringRepresentable {
    TALL(0, "tall"),
    SHORT(1, "short"),
    ANY(2, "any");

    private final int id;
    private final String name;
    public static final Codec<PillarHeight> CODEC = StringRepresentable.fromEnum(PillarHeight::values);
    public static final IntFunction<PillarHeight> BY_ID = ByIdMap.continuous(PillarHeight::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, PillarHeight> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, PillarHeight::getId);

    PillarHeight(int id, String name) {
      this.id = id;
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return name;
    }

    public int getId() {
      return id;
    }
  }
}
