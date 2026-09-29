package mysticmods.roots.recipe.fake;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.grove.generator.BlockGenerator;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.Symmetry;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public record GroveBlockPowerRecipe(ValidatedTagKey tag, Grove groveTag, int power,
                                    Symmetry symmetry, int amount) {
  public static final Codec<GroveBlockPowerRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      ValidatedTagKey.CODEC.fieldOf("block_tag").forGetter(GroveBlockPowerRecipe::tag),
      RootsRegistries.GROVES.byNameCodec().fieldOf("grove_tag").forGetter(GroveBlockPowerRecipe::groveTag),
      Codec.INT.fieldOf("power").forGetter(GroveBlockPowerRecipe::power),
      Symmetry.CODEC.fieldOf("symmetry").forGetter(GroveBlockPowerRecipe::symmetry),
      Codec.INT.fieldOf("amount").forGetter(GroveBlockPowerRecipe::amount)
  ).apply(instance, GroveBlockPowerRecipe::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, GroveBlockPowerRecipe> STREAM_CODEC = StreamCodec.composite(ValidatedTagKey.STREAM_CODEC, GroveBlockPowerRecipe::tag, ByteBufCodecs.registry(RootsRegistries.Keys.GROVES), GroveBlockPowerRecipe::groveTag, ByteBufCodecs.VAR_INT, GroveBlockPowerRecipe::power, Symmetry.STREAM_CODEC, GroveBlockPowerRecipe::symmetry, ByteBufCodecs.VAR_INT, GroveBlockPowerRecipe::amount, GroveBlockPowerRecipe::new);

  public GroveBlockPowerRecipe(TagKey<Block> blockTag, Grove groveTag, int power, Symmetry symmetry, int amount) {
    this(ValidatedTagKey.create(blockTag), groveTag, power, symmetry, amount);
  }

  public TagKey<Item> itemTag() {
    return tag.itemTag();
  }

  public TagKey<Block> blockTag() {
    return tag.blockTag();
  }

  public Ingredient itemIngredient() {
    return tag.itemIngredient();
  }

  public static class ValidatedTagKey {
    private static final Interner<ValidatedTagKey> VALUES = Interners.newWeakInterner();

    public static final Codec<ValidatedTagKey> CODEC = TagKey.codec(Registries.BLOCK)
        .xmap(ValidatedTagKey::new, ValidatedTagKey::blockTag);
    public static final StreamCodec<ByteBuf, ValidatedTagKey> STREAM_CODEC = ExtraStreamCodecs.BLOCK_TAG_STREAM_CODEC.map(ValidatedTagKey::new, ValidatedTagKey::blockTag);

    private final TagKey<Block> blockTag;
    private final TagKey<Item> itemTag;
    private Ingredient itemIngredient = null;

    private boolean checked = false;

    protected ValidatedTagKey(TagKey<Block> tag) {
      this.blockTag = tag;
      this.itemTag = TagKey.create(Registries.ITEM, tag.location());
    }

    public TagKey<Block> blockTag() {
      return blockTag;
    }

    public TagKey<Item> itemTag() {
      if (!checked) {
        checked = true;
        var btag = BuiltInRegistries.BLOCK.getTag(blockTag).orElse(null);
        var itag = BuiltInRegistries.ITEM.getTag(itemTag).orElse(null);
        if (btag == null || itag == null) {
          throw new IllegalStateException("Block tag " + blockTag + " or item tag " + itemTag + " does not exist in GrovePowerRecipe. This should have been caught during validation.");
        }

        if (btag.size() != itag.size()) {
          RootsAPI.LOG.warn("Block tag {} and item tag {} have different sizes ({} vs {}) in GrovePowerRecipe, this may cause issues.", blockTag, itemTag, btag.size(), itag.size());
        }
      }

      return itemTag;
    }

    public Ingredient itemIngredient() {
      if (itemIngredient == null) {
        this.itemIngredient = Ingredient.of(itemTag());
      }
      return itemIngredient;
    }

    public static ValidatedTagKey create(TagKey<Block> blockTag) {
      return VALUES.intern(new ValidatedTagKey(blockTag));
    }
  }

  public static List<GroveBlockPowerRecipe> generate() {
    List<GroveBlockPowerRecipe> result = new ArrayList<>();

    RootsRegistries.GROVES.holders().forEach(o -> {
      var generators = o.getData(DataMaps.GROVE_BLOCK_GENERATION_ENTRIES);
      if (generators == null) {
        return;
      }
      for (var gen : generators) {
        var tag = BuiltInRegistries.BLOCK.getTag(gen.tag()).flatMap(p -> p.stream().findFirst()).orElse(null);
        if (tag == null) {
          continue;
        }

        var generator = tag.getData(DataMaps.GROVE_BLOCK_POWER_GENERATORS);
        if (generator == null) {
          continue;
        }

        int max = gen.maxCount();
        var symmetry = gen.symmetry();

        for (BlockGenerator g : generator) {
          result.add(new GroveBlockPowerRecipe(gen.tag(), o.value(), g.value(), symmetry, max));
        }
      }
    });

    return result;
  }
}
