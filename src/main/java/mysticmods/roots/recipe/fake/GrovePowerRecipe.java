package mysticmods.roots.recipe.fake;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.GrovePowerGenerator;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public record GrovePowerRecipe(TagKey<Block> blockTag, TagKey<Grove> groveTag, int power,
                               GrovePowerGenerator.Symmetry symmetry, int amount) {
  public static final Codec<GrovePowerRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      TagKey.codec(Registries.BLOCK).fieldOf("block_tag").forGetter(GrovePowerRecipe::blockTag),
      TagKey.codec(RootsRegistries.Keys.GROVES).fieldOf("grove_tag").forGetter(GrovePowerRecipe::groveTag),
      Codec.INT.fieldOf("power").forGetter(GrovePowerRecipe::power),
      GrovePowerGenerator.Symmetry.CODEC.fieldOf("symmetry").forGetter(GrovePowerRecipe::symmetry),
      Codec.INT.fieldOf("amount").forGetter(GrovePowerRecipe::amount)
  ).apply(instance, GrovePowerRecipe::new));
  public static final StreamCodec<ByteBuf, GrovePowerRecipe> STREAM_CODEC = StreamCodec.composite(ExtraStreamCodecs.BLOCK_TAG_STREAM_CODEC, GrovePowerRecipe::blockTag, ExtraStreamCodecs.GROVE_TAG_STREAM_CODEC, GrovePowerRecipe::groveTag, ByteBufCodecs.VAR_INT, GrovePowerRecipe::power, GrovePowerGenerator.Symmetry.STREAM_CODEC, GrovePowerRecipe::symmetry, ByteBufCodecs.VAR_INT, GrovePowerRecipe::amount, GrovePowerRecipe::new);

  public static List<GrovePowerRecipe> generate() {
    List<GrovePowerRecipe> result = new ArrayList<>();

    RootsRegistries.GROVES.holders().forEach(o -> {
      var generators = o.getData(DataMaps.GROVE_GENERATION_ENTRIES);
      if (generators == null) {
        return;
      }
      for (var gen : generators) {
        var tag = BuiltInRegistries.BLOCK.getTag(gen.tag()).flatMap(p -> p.stream().findFirst()).orElse(null);
        if (tag == null) {
          continue;
        }

        var generator = tag.getData(DataMaps.GROVE_POWER_GENERATORS);
        if (generator == null) {
          continue;
        }

        int max = gen.maxCount();
        var symmetry = gen.symmetry();

        for (GrovePowerGenerator.Generator g : generator) {
          result.add(new GrovePowerRecipe(gen.tag(), g.tag(), g.value(), symmetry, max));
        }
      }
    });

    return result;
  }
}
